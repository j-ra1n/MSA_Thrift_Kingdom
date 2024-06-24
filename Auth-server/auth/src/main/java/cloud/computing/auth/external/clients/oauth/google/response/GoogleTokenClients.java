package cloud.computing.auth.external.clients.oauth.google.response;

import cloud.computing.auth.external.annotation.ExternalClients;
import org.springframework.http.MediaType;
import org.springframework.web.service.annotation.PostExchange;

import java.net.URI;

/*
    AccessToken을 얻기 위해 설정한 token URI로 POST 요청을 보낸다.
 */
@ExternalClients(baseUrl = "oauth2.provider.google.token-uri")
public interface GoogleTokenClients {

    // 요청의 content Type: FORM_URLENCODED 형식 ex) key1=value1&key2=value2
    @PostExchange(contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public GoogleTokenResponse getToken(URI uri);
}
