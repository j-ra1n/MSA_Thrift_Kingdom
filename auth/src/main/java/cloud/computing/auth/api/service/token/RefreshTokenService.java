package cloud.computing.auth.api.service.token;

import cloud.computing.auth.api.service.jwt.JwtService;
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


    private String[] extractFromSubject(String subject) {
        // "_"로 문자열을 나누고 id와 type을 추출
        // 이미 검증된 토큰이므로 따로 예외처리 필요 없음
        return subject.split("_");
    }





}
