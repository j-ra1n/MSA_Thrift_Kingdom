package cloud.computing.auth.api.service.oauth.adapter.kakao;

import cloud.computing.auth.api.service.oauth.adapter.OAuthAdapter;
import cloud.computing.auth.api.service.oauth.response.OAuthResponse;
import cloud.computing.auth.common.exception.ExceptionMessage;
import cloud.computing.auth.common.exception.oauth.OAuthException;
import cloud.computing.auth.external.clients.oauth.kakao.KakaoProfileClients;
import cloud.computing.auth.external.clients.oauth.kakao.KakaoTokenClients;
import cloud.computing.auth.external.clients.oauth.kakao.response.KakaoProfileResponse;
import cloud.computing.auth.external.clients.oauth.kakao.response.KakaoTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;

import static cloud.computing.auth.domain.define.account.user.constant.UserPlatformType.KAKAO;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoAdapter implements OAuthAdapter {

    private final KakaoTokenClients kakaoTokenClients;
    private final KakaoProfileClients kakaoProfileClients;

    @Override
    public String getToken(String tokenURL) {
        try {
            KakaoTokenResponse token = kakaoTokenClients.getToken(URI.create(tokenURL));

            // 받아온 token이 null일 경우 예외 발생
            if (token.getAccess_token() == null) {
                throw new OAuthException(ExceptionMessage.OAUTH_INVALID_TOKEN_URL);
            }

            return token.getAccess_token();
        } catch (RuntimeException e) {
            log.error(">>>> [ Kakao Oauth 인증 에러 발생: {}", ExceptionMessage.OAUTH_INVALID_TOKEN_URL.getText());
            throw new OAuthException(ExceptionMessage.OAUTH_INVALID_TOKEN_URL);
        }
    }

    @Override
    public OAuthResponse getProfile(String accessToken) {
        try {
            KakaoProfileResponse profile = kakaoProfileClients.getProfile("Bearer " + accessToken);

            return OAuthResponse.builder()
                    .platformId(profile.getId().toString())
                    .platformType(KAKAO)
                    .name(profile.getProperties().getNickname())
                    .profileImageUrl(profile.getProperties().getProfile_image())
                    .build();
        } catch (RuntimeException e) {
            log.error(">>>> [ Kakao Oauth 인증 에러 발생: {}", ExceptionMessage.OAUTH_INVALID_ACCESS_TOKEN.getText());
            throw new OAuthException(ExceptionMessage.OAUTH_INVALID_ACCESS_TOKEN);
        }
    }
}

