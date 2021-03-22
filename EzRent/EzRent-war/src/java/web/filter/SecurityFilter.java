package web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "SecurityFilter", urlPatterns = {"/*"})

public class SecurityFilter implements Filter {

    FilterConfig filterConfig;

    private static final String CONTEXT_ROOT = "/EzRent-war";

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpSession httpSession = httpServletRequest.getSession(true);
        String requestServletPath = httpServletRequest.getServletPath();

        if (httpSession.getAttribute("isLogin") == null) {
            httpSession.setAttribute("isLogin", false);
        }

        Boolean isLogin = (Boolean) httpSession.getAttribute("isLogin");

        if (!excludeLoginCheck(requestServletPath)) {
            if (isLogin == true) {
                chain.doFilter(request, response);
            } else {
                httpServletResponse.sendRedirect(CONTEXT_ROOT + "/index.xhtml");
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    public void destroy() {

    }

    private Boolean excludeLoginCheck(String path) {
        if (path.equals("/index.xhtml")
                || path.equals("/accessRightError.xhtml")
                || path.equals("/profileAdmin/loginPage.xhtml")
                || path.equals("/profileAdmin/registerPage.xhtml")
                || path.startsWith("/javax.faces.resource")) {
            return true;
        } else {
            return false;
        }
    }
}

//package web.filter;
//
//import java.io.IOException;
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//@WebFilter(filterName = "SecurityFilter", urlPatterns = {"/*"})
//
//public class SecurityFilter implements Filter {
//
//    FilterConfig filterConfig;
//
//    private static final String CONTEXT_ROOT = "/EzRent-war";
//
//    public void init(FilterConfig filterConfig) throws ServletException {
//        this.filterConfig = filterConfig;
//    }
//
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//        HttpSession httpSession = httpServletRequest.getSession(true);
//        String requestServletPath = httpServletRequest.getServletPath();
//
//        if (httpSession.getAttribute("isLogin") == null) {
//            httpSession.setAttribute("isLogin", false);
//        }
//
//        Boolean isLogin = (Boolean) httpSession.getAttribute("isLogin");
//
//        if (!excludeLoginCheck(requestServletPath)) {
//            if (isLogin == true) {
//                chain.doFilter(request, response);
//            } else {
//                httpServletResponse.sendRedirect(CONTEXT_ROOT + "/index.xhtml");
//            }
//        } else {
//            chain.doFilter(request, response);
//        }
//    }
//
//    public void destroy() {
//
//    }
//
//    private Boolean excludeLoginCheck(String path) {
//        if (path.equals("/index.xhtml")
//                || path.equals("/accessRightError.xhtml")
//                || path.equals("/profileAdmin/loginPage.xhtml")
//                || path.equals("/profileAdmin/registerPage.xhtml")
//                || path.startsWith("/javax.faces.resource")) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//}
