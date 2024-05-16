package cloud.computing.auth.api.service.oauth.adapter.google;

import cloud.computing.auth.api.service.oauth.adapter.OAuthAdapter;
import cloud.computing.auth.api.service.oauth.response.OAuthResponse;
import cloud.computing.auth.common.exception.ExceptionMessage;
import cloud.computing.auth.common.exception.oauth.OAuthException;
import cloud.computing.auth.external.clients.oauth.google.GoogleProfileClients;
import cloud.computing.auth.external.clients.oauth.google.response.GoogleProfileResponse;
import cloud.computing.auth.external.clients.oauth.google.response.GoogleTokenClients;
import cloud.computing.auth.external.clients.oauth.google.response.GoogleTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;

import static cloud.computing.auth.domain.define.account.user.constant.UserPlatformType.GOOGLE;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleAdapter implements OAuthAdapter {

    private final GoogleTokenClients googleTokenClients;
    private final GoogleProfileClients googleProfileClients;

    @Override
    public String getToken(String tokenURL) {
        try {
            GoogleTokenResponse token = googleTokenClients.getToken(URI.create(tokenURL));
            // URL로 액세스 토큰을 요청

            // 만약 token이 null일 경우 예외처리
            if (token.getAccess_token() == null) {
                throw new OAuthException(ExceptionMessage.OAUTH_INVALID_TOKEN_URL);
            }
            return token.getAccess_token();
        } catch (RuntimeException e) {
            log.error(">>>> [ Google Oauth 인증 에러 발생: {}", ExceptionMessage.OAUTH_INVALID_TOKEN_URL.getText());
            throw new OAuthException(ExceptionMessage.OAUTH_INVALID_TOKEN_URL);
        }
    }

    @Override
    public OAuthResponse getProfile(String accessToken) {
        try {
            GoogleProfileResponse profile = googleProfileClients.getProfile("Bearer " + accessToken);

            // 액세스 토큰을 사용하여 프로필 정보 요청
            return OAuthResponse.builder()
                    .platformId(profile.getSub())
                    .platformType(GOOGLE)
                    .name(profile.getName())
                    .profileImageUrl(profile.getPicture())
                    .build();
        } catch (RuntimeException e) {
            log.error(">>>> [ Google Oauth 인증 에러 발생: {}", ExceptionMessage.OAUTH_INVALID_ACCESS_TOKEN.getText());
            throw new OAuthException(ExceptionMessage.OAUTH_INVALID_ACCESS_TOKEN);
        }
    }
}

