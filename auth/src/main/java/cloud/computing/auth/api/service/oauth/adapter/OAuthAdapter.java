package cloud.computing.auth.api.service.oauth.adapter;

import cloud.computing.auth.api.service.oauth.response.OAuthResponse;

public interface OAuthAdapter {

    // OAuth Access Token 요청 메서드
    String getToken(String tokenURL);

    // OAuth 인증이 완료된 사용자의 프로필 요청 메서드
    OAuthResponse getProfile(String accessToken);
}

