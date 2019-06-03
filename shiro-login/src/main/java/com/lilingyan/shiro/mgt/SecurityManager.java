package com.lilingyan.shiro.mgt;

import com.lilingyan.shiro.authc.AuthenticationException;
import com.lilingyan.shiro.authc.AuthenticationToken;
import com.lilingyan.shiro.authc.Authenticator;
import com.lilingyan.shiro.session.mgt.SessionManager;
import com.lilingyan.shiro.subject.Subject;
import com.lilingyan.shiro.subject.SubjectContext;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 11:17
 */
public interface SecurityManager extends Authenticator, SessionManager {

    Subject login(Subject subject, AuthenticationToken authenticationToken) throws AuthenticationException;

    Subject createSubject(SubjectContext context);
}
