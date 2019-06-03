package com.lilingyan.shiro.subject;

import com.lilingyan.shiro.SecurityUtils;
import com.lilingyan.shiro.authc.AuthenticationException;
import com.lilingyan.shiro.authc.AuthenticationToken;
import com.lilingyan.shiro.mgt.SecurityManager;
import com.lilingyan.shiro.session.Session;
import com.lilingyan.shiro.subject.support.DefaultSubjectContext;
import com.lilingyan.shiro.util.CollectionUtils;

import java.io.Serializable;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 11:27
 */
public interface Subject {

    Session getSession();

    Session getSession(boolean create);

    PrincipalCollection getPrincipals();

    boolean isAuthenticated();

    void login(AuthenticationToken token) throws AuthenticationException;

    public static class Builder {

        private final SubjectContext subjectContext;

        private final SecurityManager securityManager;

        public Builder() {
            this(SecurityUtils.getSecurityManager());
        }

        public Builder(SecurityManager securityManager) {
            if (securityManager == null) {
                throw new NullPointerException("SecurityManager method argument cannot be null.");
            }
            this.securityManager = securityManager;
            this.subjectContext = newSubjectContextInstance();
            if (this.subjectContext == null) {
                throw new IllegalStateException("Subject instance returned from 'newSubjectContextInstance' " +
                        "cannot be null.");
            }
            this.subjectContext.setSecurityManager(securityManager);
        }

        protected SubjectContext newSubjectContextInstance() {
            return new DefaultSubjectContext();
        }

        protected SubjectContext getSubjectContext() {
            return this.subjectContext;
        }

        public Builder sessionId(Serializable sessionId) {
            if (sessionId != null) {
                this.subjectContext.setSessionId(sessionId);
            }
            return this;
        }

        public Builder session(Session session) {
            if (session != null) {
                this.subjectContext.setSession(session);
            }
            return this;
        }

        public Builder principals(PrincipalCollection principals) {
            if (!CollectionUtils.isEmpty(principals)) {
                this.subjectContext.setPrincipals(principals);
            }
            return this;
        }


        public Builder sessionCreationEnabled(boolean enabled) {
            this.subjectContext.setSessionCreationEnabled(enabled);
            return this;
        }

        public Builder authenticated(boolean authenticated) {
            this.subjectContext.setAuthenticated(authenticated);
            return this;
        }

        public Builder contextAttribute(String attributeKey, Object attributeValue) {
            if (attributeKey == null) {
                String msg = "Subject context map key cannot be null.";
                throw new IllegalArgumentException(msg);
            }
            if (attributeValue == null) {
                this.subjectContext.remove(attributeKey);
            } else {
                this.subjectContext.put(attributeKey, attributeValue);
            }
            return this;
        }

        public Subject buildSubject() {
            /**
             * subjectContext就是构造函数里面创建出来的DefaultSubjectContext
             */
            return this.securityManager.createSubject(this.subjectContext);
        }
    }

}
