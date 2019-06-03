package com.lilingyan.shiro.mgt;

import com.lilingyan.shiro.session.Session;
import com.lilingyan.shiro.subject.PrincipalCollection;
import com.lilingyan.shiro.subject.Subject;
import com.lilingyan.shiro.subject.SubjectContext;
import com.lilingyan.shiro.subject.support.DelegatingSubject;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 13:48
 */
public class DefaultSubjectFactory implements SubjectFactory {

    public Subject createSubject(SubjectContext context) {
        SecurityManager securityManager = context.resolveSecurityManager();
        //解析session
        Session session = context.resolveSession();
        /**
         * session是否自动创建标记
         * ，没开启 会报错 @DelegatingSubject$getSession(boolean create)
         */
        boolean sessionCreationEnabled = context.isSessionCreationEnabled();
        //realm中返回的用户凭证信息
        PrincipalCollection principals = context.resolvePrincipals();
        boolean authenticated = context.resolveAuthenticated();

        //创建
        return new DelegatingSubject(principals, authenticated, session, sessionCreationEnabled, securityManager);
    }

}
