package cloud.computing.auth.api.service.auth;

import cloud.computing.auth.api.controller.response.AuthLoginResponse;
import cloud.computing.auth.api.service.auth.request.AuthServiceRegisterRequest;
import cloud.computing.auth.api.service.jwt.JwtService;
import cloud.computing.auth.api.service.jwt.JwtToken;
import cloud.computing.auth.api.service.oauth.OAuthService;
import cloud.computing.auth.api.service.oauth.response.OAuthResponse;
import cloud.computing.auth.api.service.token.RefreshTokenService;
import cloud.computing.auth.common.exception.ExceptionMessage;
import cloud.computing.auth.common.exception.jwt.JwtException;
import cloud.computing.auth.domain.define.account.user.User;
import cloud.computing.auth.domain.define.account.user.constant.UserPlatformType;
import cloud.computing.auth.domain.define.account.user.constant.UserRole;
import cloud.computing.auth.domain.define.account.user.repository.UserRepository;
import cloud.computing.auth.domain.define.refreshToken.RefreshToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    private static final String PLATFORM_ID_CLAIM = "platformId";
    private static final String PLATFORM_TYPE_CLAIM = "platformType";
    private static final String ROLE_CLAIM = "role";

    private final UserRepository userRepository;

    private final OAuthService oAuthService;

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    @Transactional
    public AuthLoginResponse login(UserPlatformType platformType, String code, String state) {
        OAuthResponse loginResponse = oAuthService.login(platformType, code, state);
        String name = loginResponse.getName();
        String platformId = loginResponse.getPlatformId();
        String profileImageUrl = loginResponse.getProfileImageUrl();

        log.info(">>>> [ {}님이 로그인하셨습니다 ] <<<<", name);

        /*
         * OAuth 로그인 인증을 마쳤으니 우리 애플리케이션의 DB에도 존재하는 사용자인지 확인한다.
         * 회원이 아닐 경우, 즉 회원가입이 필요한 신규 사용자의 경우 OAuthResponse를 바탕으로 DB에 등록해준다.
         */
        User findUser = userRepository.findByPlatformIdAndPlatformType(platformId, platformType)
                .orElseGet(() -> {
                    User saveUser = User.builder()
                            .platformId(platformId)
                            .platformType(loginResponse.getPlatformType())
                            .role(UserRole.UNAUTH)
                            .profileImageUrl(profileImageUrl)
                            .nickname(name)
                            .build();

                    log.info(">>>> [ UNAUTH 권한으로 사용자를 DB에 등록합니다. 이후 회원가입이 필요합니다 ] <<<<");
                    return userRepository.save(saveUser);
                });

        /*
            DB에 저장된 사용자 정보를 기반으로 JWT 토큰을 발급
            * JWT 토큰을 요청시에 담아 보내면 JWT 토큰 인증 필터에서 Security Context에 인증된 사용자로 등록
            TODO : JWT 재발급을 위한 Refresh 토큰은 Redis에서 관리할 예정입니다.
         */
        JwtToken jwtToken = generateJwtToken(findUser);

        // JWT 토큰과 권한 정보를 담아 반환
        return AuthLoginResponse.builder()
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .role(findUser.getRole())
                .nickname(findUser.getNickname())
                .build();
    }

    private JwtToken generateJwtToken(User user) {
        // JWT 토큰 생성을 위한 claims 생성
        HashMap<String, String> claims = new HashMap<>();
        claims.put(ROLE_CLAIM, user.getRole().name());
        claims.put(PLATFORM_ID_CLAIM, user.getPlatformId());
        claims.put(PLATFORM_TYPE_CLAIM, String.valueOf(user.getPlatformType()));


        // Access Token 생성
        final String jwtAccessToken = jwtService.generateAccessToken(claims, user);

        // Refresh Token 생성
        final String jwtRefreshToken = jwtService.generateRefreshToken(claims, user);
        log.info(">>>> [ 사용자 {}님의 JWT 토큰이 발급되었습니다 ] <<<<", user.getNickname());
        log.info(">>>> [ 사용자 {}님의 refresh 토큰이 발급되었습니다 ] <<<<", user.getNickname());

        // Refresh Token을 레디스에 저장
        RefreshToken refreshToken= RefreshToken.builder().refreshToken(jwtRefreshToken).subject(user.getUsername()).build();
        refreshTokenService.saveRefreshToken(refreshToken);
        return JwtToken.builder()
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .build();
    }


    @Transactional
    public AuthLoginResponse register(AuthServiceRegisterRequest request, User user) {
        User findUser = userRepository.findByPlatformIdAndPlatformType(user.getPlatformId(), user.getPlatformType()).orElseThrow(() -> {
            // UNAUTH인 토큰을 받고 회원 탈퇴 후 그 토큰으로 회원가입 요청시 예외 처리
            log.warn(">>>> User Not Exist : {}", ExceptionMessage.AUTH_INVALID_REGISTER.getText());
            throw new JwtException(ExceptionMessage.AUTH_INVALID_REGISTER);
        });

        // UNAUTH 토큰으로 회원가입을 요청했지만 이미 update되어 UNAUTH가 아닌 사용자 예외 처리
        if (findUser.getRole() != UserRole.UNAUTH) {
            log.warn(">>>> Not UNAUTH User : {}", ExceptionMessage.AUTH_DUPLICATE_UNAUTH_REGISTER.getText());
            throw new JwtException(ExceptionMessage.AUTH_DUPLICATE_UNAUTH_REGISTER);
        }

        // 회원가입 정보 DB 반영
        findUser.updateRegister(request.getName());

        // JWT Access Token, Refresh Token 재발급
        JwtToken tokens = createJwtToken(findUser);

        return AuthLoginResponse.builder()
                .accessToken(tokens.getAccessToken())
                .refreshToken(tokens.getRefreshToken())
                .role(findUser.getRole())
                .build();
    }

    private JwtToken createJwtToken(User user) {
        // JWT 토큰 생성을 위한 claims 생성
        HashMap<String, String> claims = new HashMap<>();
        claims.put(ROLE_CLAIM, user.getRole().name());
        claims.put(PLATFORM_ID_CLAIM, user.getPlatformId());
        claims.put(PLATFORM_TYPE_CLAIM, String.valueOf(user.getPlatformType()));

        // Access Token 생성
        final String accessToken = jwtService.generateAccessToken(claims, user);
        // Refresh Token 생성
        final String refreshToken = jwtService.generateRefreshToken(claims, user);

        log.info(">>>> {} generate Tokens", user.getNickname());

        // Refresh Token 저장 - REDIS
        RefreshToken rt = RefreshToken.builder()
                .refreshToken(refreshToken)
                .subject(user.getUsername())
                .build();
        refreshTokenService.saveRefreshToken(rt);


        return JwtToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void logout(String refreshToken) {
        refreshTokenService.logout(refreshToken);
    }

}