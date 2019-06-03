package com.lilingyan.shiro.authc;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 11:35
 */
@Slf4j
public abstract class AbstractAuthenticator implements Authenticator{

    public final AuthenticationInfo authenticate(AuthenticationToken token) throws AuthenticationException {

        if (token == null) {
            throw new IllegalArgumentException("Method argument (authentication token) cannot be null.");
        }

        log.trace("Authentication attempt received for token [{}]"+token);

        AuthenticationInfo info;
        try {
            info = doAuthenticate(token);
            if (info == null) {
                String msg = "No account information found for authentication token [" + token + "] by this " +
                        "Authenticator instance.  Please check that it is configured correctly.";
                throw new AuthenticationException(msg);
            }
        } catch (Throwable t) {
            AuthenticationException ae = null;
            if (t instanceof AuthenticationException) {
                ae = (AuthenticationException) t;
            }
            if (ae == null) {
                //Exception thrown was not an expected AuthenticationException.  Therefore it is probably a little more
                //severe or unexpected.  So, wrap in an AuthenticationException, log to warn, and propagate:
                String msg = "Authentication failed for token submission [" + token + "].  Possible unexpected " +
                        "error? (Typical or expected login exceptions should extend from AuthenticationException).";
                ae = new AuthenticationException(msg, t);
                if (log.isWarnEnabled())
                    log.warn(msg, t);
            }
            try {
//                notifyFailure(token, ae);
            } catch (Throwable t2) {
                if (log.isWarnEnabled()) {
                    String msg = "Unable to send notification for failed authentication attempt - listener error?.  " +
                            "Please check your AuthenticationListener implementation(s).  Logging sending exception " +
                            "and propagating original AuthenticationException instead...";
                    log.warn(msg, t2);
                }
            }


            throw ae;
        }

        log.debug("Authentication successful for token [{}].  Returned account [{}]", token, info);

//        notifySuccess(token, info);

        return info;
    }

    protected abstract AuthenticationInfo doAuthenticate(AuthenticationToken token)
            throws AuthenticationException;

}
