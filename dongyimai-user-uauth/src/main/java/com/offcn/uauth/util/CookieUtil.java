package com.offcn.uauth.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class CookieUtil {

    /**
     * ����cookie
     *
     * @param response
     * @param name     cookie����
     * @param value    cookieֵ
     * @param maxAge   cookie�������� ����Ϊ��λ
     */
    public static void addCookie(HttpServletResponse response, String domain, String path, String name,
                                 String value, int maxAge, boolean httpOnly) {
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(httpOnly);
        response.addCookie(cookie);
    }

    /**
     * ����cookie���ƶ�ȡcookie
     * @param request
     * @return map<cookieName,cookieValue>
     */

    public static Map<String,String> readCookie(HttpServletRequest request, String ... cookieNames) {
        Map<String,String> cookieMap = new HashMap<String,String>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String cookieName = cookie.getName();
                String cookieValue = cookie.getValue();
                for(int i=0;i<cookieNames.length;i++){
                    if(cookieNames[i].equals(cookieName)){
                        cookieMap.put(cookieName,cookieValue);
                    }
                }
            }
        }
        return cookieMap;

    }
}