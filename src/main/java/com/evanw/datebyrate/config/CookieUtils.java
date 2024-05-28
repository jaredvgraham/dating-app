package com.evanw.datebyrate.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtils {

    public static void addCookieWithSameSite(HttpServletResponse response,
                                             Cookie cookie,
                                             String sameSiteOption
    ) {
        StringBuilder cookieValue = new StringBuilder()
                .append(cookie.getName()).append("=").append(cookie.getValue())
                .append("; Path=").append(cookie.getPath())
                .append("; Max-Age=").append(cookie.getMaxAge())
                .append("; HttpOnly");

        if (cookie.getSecure()) {
            cookieValue.append("; Secure");
        }
        cookie.setDomain("http://localhost:5173/");

        cookieValue.append("; SameSite=").append(sameSiteOption);

        response.addHeader("Set-Cookie", cookieValue.toString());

    }
}
