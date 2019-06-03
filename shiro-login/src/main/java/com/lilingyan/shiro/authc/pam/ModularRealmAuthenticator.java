package com.lilingyan.shiro.authc.pam;

import com.lilingyan.shiro.authc.*;
import com.lilingyan.shiro.realm.Realm;
import com.lilingyan.shiro.util.CollectionUtils;

import java.util.Collection;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 11:34
 */
public class ModularRealmAuthenticator extends AbstractAuthenticator {

    private Collection<Realm> realms;

    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
        assertRealmsConfigured();
        Collection<Realm> realms = getRealms();
        if (realms.size() == 1) {
            return doSingleRealmAuthentication(realms.iterator().next(), authenticationToken);
        } else {
            //只演示单realm的情况
//            return doMultiRealmAuthentication(realms, authenticationToken);
            return null;
        }
    }

    protected Collection<Realm> getRealms() {
        return this.realms;
    }

    /**
     * 只有一个realm的情况下使用
     * @param realm
     * @param token
     * @return
     */
    protected AuthenticationInfo doSingleRealmAuthentication(Realm realm, AuthenticationToken token) {
        /**
         * 先判断这个realm是否可以校验这个token
         * 这个方法需要我们在实现自己的realm时重写
         */
        if (!realm.supports(token)) {
            String msg = "Realm [" + realm + "] does not support authentication token [" +
                    token + "].  Please ensure that the appropriate Realm implementation is " +
                    "configured correctly or that the realm accepts AuthenticationTokens of this type.";
            throw new UnsupportedTokenException(msg);
        }
        /**
         * 从我们自己的realm中获取校验后的登陆信息
         */
        AuthenticationInfo info = realm.getAuthenticationInfo(token);
        if (info == null) {
            String msg = "Realm [" + realm + "] was unable to find account data for the " +
                    "submitted AuthenticationToken [" + token + "].";
            throw new UnknownAccountException(msg);
        }
        return info;
    }

    protected void assertRealmsConfigured() throws IllegalStateException {
        Collection<Realm> realms = getRealms();
        if (CollectionUtils.isEmpty(realms)) {
            String msg = "Configuration error:  No realms have been configured!  One or more realms must be " +
                    "present to execute an authentication attempt.";
            throw new IllegalStateException(msg);
        }
    }

}
