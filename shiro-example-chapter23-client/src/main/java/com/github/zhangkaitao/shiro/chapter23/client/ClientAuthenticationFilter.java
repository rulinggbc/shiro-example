package com.github.zhangkaitao.shiro.chapter23.client;

import com.github.zhangkaitao.shiro.chapter23.core.ClientSavedRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-3-14
 * <p>Version: 1.0
 */
public class ClientAuthenticationFilter extends AuthenticationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request, response);
        return subject.isAuthenticated();
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        String successUrl = request.getParameter("backurl");
        if(StringUtils.isEmpty(successUrl)) {
            successUrl = getSuccessUrl();
        }
        saveRequest(request, successUrl);
        redirectToLogin(request, response);
        return false;
    }

    protected void saveRequest(ServletRequest request, String successUrl) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        SavedRequest savedRequest = new ClientSavedRequest(httpRequest, successUrl);
        session.setAttribute(WebUtils.SAVED_REQUEST_KEY, savedRequest);
    }
}
