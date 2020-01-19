package com.citrix.lvda.controller;

import com.alibaba.fastjson.JSONObject;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.request.*;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/oauth")
public class RestAuthController {

    @RequestMapping("/render/{source}")
    public void renderAuth(@PathVariable("source") final String source, final HttpServletResponse response)
            throws IOException {
        System.out.println("进入render：" + source);
        final AuthRequest authRequest = getAuthRequest(source);

        System.out.println("1---" + authRequest);

        final String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        System.out.println("2---" + authorizeUrl);
        // 请求获取授权码
        response.sendRedirect(authorizeUrl);
    }

    /**
     * oauth平台中配置的授权回调地址，以本项目为例，在创建github授权应用时的回调地址应为：http://127.0.0.1:8443/oauth/callback/github
     */
    @RequestMapping("/callback/{source}")
    public void login(HttpServletResponse httpResponse, @PathVariable("source") final String source,
            final AuthCallback callback) throws IOException {
        System.out.println("进入callback：" + source + " callback params：" + JSONObject.toJSONString(callback));
        final AuthRequest authRequest = getAuthRequest(source);
        final AuthResponse response = authRequest.login(callback);

        System.out.println("3---" + response);

        System.out.println(JSONObject.toJSONString(response));

        httpResponse.sendRedirect("/greet");
    }

    @RequestMapping("/revoke/{source}/{token}")
    public Object revokeAuth(@PathVariable("source") final String source, @PathVariable("token") final String token)
            throws IOException {
        final AuthRequest authRequest = getAuthRequest(source);
        return authRequest.revoke(AuthToken.builder().accessToken(token).build());
    }

    @RequestMapping("/refresh/{source}")
    public Object refreshAuth(@PathVariable("source") final String source, final String token) {
        final AuthRequest authRequest = getAuthRequest(source);
        return authRequest.refresh(AuthToken.builder().refreshToken(token).build());
    }

    /**
     * 根据具体的授权来源，获取授权请求工具类
     *
     * @param source
     * @return
     */
    private AuthRequest getAuthRequest(final String source) {
        AuthRequest authRequest = null;
        switch (source) {
        case "github": {
            authRequest = new AuthGithubRequest(AuthConfig.builder().clientId("3eb8073cc3b40720d62f")
                    .clientSecret("c35ec9789059abb385fecb8f176b2039a1162171")
                    .redirectUri("http://127.0.0.1:8443/oauth/callback/github").build());
            break;
        }
        default: {
            break;
        }
        }
        if (null == authRequest) {
            throw new AuthException("未获取到有效的Auth配置");
        }
        return authRequest;
    }
}