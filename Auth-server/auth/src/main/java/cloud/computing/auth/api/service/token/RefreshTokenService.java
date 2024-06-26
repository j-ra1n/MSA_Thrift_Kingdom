package cloud.computing.auth.api.service.token;

import cloud.computing.auth.api.service.jwt.JwtService;
import cloud.computing.auth.common.exception.ExceptionMessage;
import cloud.computing.auth.common.exception.jwt.JwtException;
import cloud.computing.auth.domain.define.account.user.User;
import cloud.computing.auth.domain.define.account.user.constant.UserPlatformType;
import cloud.computing.auth.domain.define.account.user.constant.UserRole;
import cloud.computing.auth.domain.define.account.user.repository.UserRepository;
import cloud.computing.auth.domain.define.refreshToken.RefreshToken;
import cloud.computing.auth.domain.define.refreshToken.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    final RefreshTokenRepository refreshTokenRepository;
    final JwtService jwtService;
    final UserRepository userRepository;

    // 리프레시 토큰 저장 메서드
    public void saveRefreshToken(RefreshToken refreshToken) {
        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);
        log.info(">>>> Refresh Token register : {}", savedToken.getRefreshToken());
    }

    public String reissue(Claims claims, String refreshToken) {

        refreshTokenRepository.findById(refreshToken).orElseThrow(() -> {
            log.warn(">>>> Token Not Exist : {}", ExceptionMessage.JWT_NOT_EXIST_RTK.getText());
            throw new JwtException(ExceptionMessage.JWT_NOT_EXIST_RTK);
        });

        String role = claims.get("role", String.class);

        String[] platformIdAndPlatformType = extractFromSubject(jwtService.extractSubject(refreshToken));
        String platformId = platformIdAndPlatformType[0];
        String platformType = platformIdAndPlatformType[1];


        UserDetails userDetails = User.builder()
                .role(UserRole.valueOf(role))
                .platformId(platformId)
                .platformType(UserPlatformType.valueOf(platformType))
                .build();

        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            map.put(entry.getKey(), entry.getValue().toString());
        }

        return jwtService.generateAccessToken(map, userDetails);
    }

    private String[] extractFromSubject(String subject) {
        // "_"로 문자열을 나누고 id와 type을 추출
        // 이미 검증된 토큰이므로 따로 예외처리 필요 없음
        return subject.split("_");
    }

    // Logout 시 Redis에 저장된 RefreshToken 삭제
    public void logout(String refreshToken) {

        String sub = jwtService.extractAllClaims(refreshToken).getSubject();

        RefreshToken rtk = refreshTokenRepository.findById(refreshToken).orElseThrow(() -> {
            log.warn(">>>> Token Not Exist : {}", ExceptionMessage.REFRESHTOKEN_NOT_EXIST.getText());
            throw new JwtException(ExceptionMessage.REFRESHTOKEN_NOT_EXIST);
        });

        // refreshToken 유효성 검사
        if (!jwtService.isTokenValid(refreshToken, rtk.getRefreshToken())) {
            log.warn(">>>> Token Validation Fail : {}", ExceptionMessage.REFRESHTOKEN_INVALID.getText());
            throw new JwtException(ExceptionMessage.REFRESHTOKEN_INVALID);
        }

        refreshTokenRepository.delete(rtk);
        log.info(">>>> {}'s RefreshToken id deleted.", sub);
    }

}
