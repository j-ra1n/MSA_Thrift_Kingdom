package cloud.computing.auth.api.controller;


import cloud.computing.auth.api.controller.request.AuthRegisterRequest;
import cloud.computing.auth.api.controller.response.AuthLoginPageResponse;
import cloud.computing.auth.api.controller.response.AuthLoginResponse;
import cloud.computing.auth.api.service.auth.AuthService;
import cloud.computing.auth.api.service.auth.request.AuthServiceRegisterRequest;
import cloud.computing.auth.api.service.oauth.OAuthService;
import cloud.computing.auth.api.service.state.LoginStateService;
import cloud.computing.auth.common.exception.ExceptionMessage;
import cloud.computing.auth.common.exception.jwt.JwtException;
import cloud.computing.auth.common.exception.oauth.OAuthException;
import cloud.computing.auth.common.response.JsonResult;
import cloud.computing.auth.domain.define.account.user.User;
import cloud.computing.auth.domain.define.account.user.constant.UserPlatformType;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final static int REFRESH_TOKEN_INDEX = 2;

    private final LoginStateService loginStateService;

    private final OAuthService oAuthService;

    private final AuthService authService;


    @GetMapping("/lP")
    public JsonResult<List<AuthLoginPageResponse>> loginPage() {

        String loginState = loginStateService.generateLoginState();

        // OAuth 사용하여 각 플랫폼의 로그인 페이지 URL을 가져와서 state 주입
        List<AuthLoginPageResponse> loginPages = oAuthService.loginPage(loginState);


        return JsonResult.successOf(loginPages);
    }

    @GetMapping("/loginPage")
    public JsonResult<String> loginPage(@RequestParam("platform") String platform) {
        String loginState = loginStateService.generateLoginState();
        String loginUrl = oAuthService.getLoginUrl(platform, loginState);
        return JsonResult.successOf(loginUrl);
    }


    @GetMapping("/{platformType}/login")
    public void login(
            @PathVariable("platformType") UserPlatformType platformType,
            @RequestParam("code") String code,
            @RequestParam("state") String loginState,
            HttpServletResponse response) throws IOException {

        // state 값이 유효한지 검증
        if (!loginStateService.isValidLoginState(loginState)) {
            throw new OAuthException(ExceptionMessage.LOGINSTATE_INVALID_VALUE);
        }

        AuthLoginResponse loginResponse = authService.login(platformType, code, loginState);

        // 로그인 처리가 완료되면 메인 페이지로 리디렉션
        String redirectUrl = "http://localhost:3000/doors?loggedIn=true";
        response.sendRedirect(redirectUrl);
    }

    @PostMapping("/register")
    public JsonResult<?> register(@AuthenticationPrincipal User user,
                                  @Valid @RequestBody AuthRegisterRequest request) throws JwtException {

        AuthLoginResponse response = authService.register(AuthServiceRegisterRequest.of(request), user);

        return JsonResult.successOf(response);
    }

    @PostMapping("/logout")
    public JsonResult<?> logout(@RequestHeader(name = "Authorization") String token) {
        List<String> tokens = Arrays.asList(token.split(" "));


        if (tokens.size() == 3) {
            authService.logout(tokens.get(REFRESH_TOKEN_INDEX));

            return JsonResult.successOf("로그아웃 되었습니다.");
        } else {
            log.warn(">>>> Invalid Header Access : {}", ExceptionMessage.JWT_INVALID_HEADER.getText());
            return JsonResult.failOf(ExceptionMessage.JWT_INVALID_HEADER.getText());
        }

    }
}
