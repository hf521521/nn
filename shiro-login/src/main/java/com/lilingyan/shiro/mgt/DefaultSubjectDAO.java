package com.lilingyan.shiro.mgt;

import com.lilingyan.shiro.session.Session;
import com.lilingyan.shiro.subject.PrincipalCollection;
import com.lilingyan.shiro.subject.Subject;
import com.lilingyan.shiro.subject.support.DefaultSubjectContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 13:55
 */
@Slf4j
public class DefaultSubjectDAO  implements SubjectDAO {

    private SessionStorageEvaluator sessionStorageEvaluator;

    public DefaultSubjectDAO() {
        //default implementation allows enabling/disabling session usages at a global level for all subjects:
        this.sessionStorageEvaluator = new DefaultSessionStorageEvaluator();
    }

    /**
     * 默认的实现方式是把subject保存到session中
     * @param subject
     * @return
     */
    public Subject save(Subject subject) {
        if (isSessionStorageEnabled(subject)) {
            saveToSession(subject);
        } else {
            log.trace("Session storage of subject state for Subject [{}] has been disabled: identity and " +
                    "authentication state are expected to be initialized on every request or invocation.", subject);
        }

        return subject;
    }

    protected boolean isSessionStorageEnabled(Subject subject) {
        return getSessionStorageEvaluator().isSessionStorageEnabled(subject);
    }

    public SessionStorageEvaluator getSessionStorageEvaluator() {
        return sessionStorageEvaluator;
    }


    /**
     * 保存登陆的信息和登陆的状态
     * @param subject
     */
    protected void saveToSession(Subject subject) {
        mergePrincipals(subject);
        mergeAuthenticationState(subject);
    }

    public void delete(Subject subject) {
        removeFromSession(subject);
    }

    protected void removeFromSession(Subject subject) {
    }

    protected void mergePrincipals(Subject subject) {
        PrincipalCollection currentPrincipals = null;
        if (currentPrincipals == null || currentPrincipals.isEmpty()) {
            currentPrincipals = subject.getPrincipals();
        }

        Session session = subject.getSession(false);

        /**
         * 如果有Session
         * 则把变动的PrincipalCollection保存进去
         */
        if (session == null) {
            if (!isEmpty(currentPrincipals)) {
                session = subject.getSession();
                session.setAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY, currentPrincipals);
            }
        } else {
            PrincipalCollection existingPrincipals =
                    (PrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);

            if (isEmpty(currentPrincipals)) {
                if (!isEmpty(existingPrincipals)) {
                    session.removeAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                }
            } else {
                //只有修改过了的，才会被保存
                if (!currentPrincipals.equals(existingPrincipals)) {
                    session.setAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY, currentPrincipals);
                }
            }
        }
    }

    /**
     * 刷新session中的登陆标记
     * @param subject
     */
    protected void mergeAuthenticationState(Subject subject) {

        Session session = subject.getSession(false);

        if (session == null) {
            if (subject.isAuthenticated()) {
                /**
                 * 如果登陆成功，并且第一次访问Session
                 * 则会去创建也给新的
                 */
                session = subject.getSession();
                session.setAttribute(DefaultSubjectContext.AUTHENTICATED_SESSION_KEY, Boolean.TRUE);
            }
        } else {
            /**
             * 如果有session
             * 则更新标记
             */
            Boolean existingAuthc = (Boolean) session.getAttribute(DefaultSubjectContext.AUTHENTICATED_SESSION_KEY);

            if (subject.isAuthenticated()) {
                if (existingAuthc == null || !existingAuthc) {
                    session.setAttribute(DefaultSubjectContext.AUTHENTICATED_SESSION_KEY, Boolean.TRUE);
                }
            } else {
                if (existingAuthc != null) {
                    //existing doesn't match the current state - remove it:
                    session.removeAttribute(DefaultSubjectContext.AUTHENTICATED_SESSION_KEY);
                }
            }
        }
    }

    private static boolean isEmpty(PrincipalCollection pc) {
        return pc == null || pc.isEmpty();
    }

}
