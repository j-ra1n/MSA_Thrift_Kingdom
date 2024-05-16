package cloud.computing.auth.api.service.auth.request;

import cloud.computing.auth.api.controller.request.AuthRegisterRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthServiceRegisterRequest {
    private String name;


    public static AuthServiceRegisterRequest of(AuthRegisterRequest request) {
        return AuthServiceRegisterRequest.builder()
                .name(request.getName())
                .build();
    }
}
