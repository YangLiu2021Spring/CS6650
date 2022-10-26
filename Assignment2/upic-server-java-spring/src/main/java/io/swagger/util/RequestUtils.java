package io.swagger.util;

import javax.servlet.http.HttpServletRequest;

public final class RequestUtils {
    public static String getHeaderAccept(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader("Accept");
    }

    public static boolean isAbleToAcceptMediaTypeJson(String accept) {
        return accept != null && (accept.contains("application/json") || accept.contains("*/*"));
    }

    private RequestUtils() {
        throw new UnsupportedOperationException();
    }
}
