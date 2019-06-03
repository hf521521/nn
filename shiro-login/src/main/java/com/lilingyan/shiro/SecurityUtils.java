package com.lilingyan.shiro;

import com.lilingyan.shiro.mgt.SecurityManager;
import com.lilingyan.shiro.subject.Subject;
import com.lilingyan.shiro.util.ThreadContext;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 14:05
 */
public class SecurityUtils {

    private static SecurityManager securityManager;

    public static Subject getSubject() {
        //从线程中获取Subject
        Subject subject = ThreadContext.getSubject();
        if (subject == null) {
            //没有就创建一个
            subject = (new Subject.Builder()).buildSubject();
            //然后绑定到当前线程
            ThreadContext.bind(subject);
        }
        return subject;
    }

    public static SecurityManager getSecurityManager() throws UnavailableSecurityManagerException {
        SecurityManager securityManager = ThreadContext.getSecurityManager();
        if (securityManager == null) {
            securityManager = SecurityUtils.securityManager;
        }
        if (securityManager == null) {
            String msg = "No SecurityManager accessible to the calling code, either bound to the " +
                    ThreadContext.class.getName() + " or as a vm static singleton.  This is an invalid application " +
                    "configuration.";
            throw new UnavailableSecurityManagerException(msg);
        }
        return securityManager;
    }

}
