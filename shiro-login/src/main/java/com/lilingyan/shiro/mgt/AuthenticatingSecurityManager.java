package com.lilingyan.shiro.mgt;

import com.lilingyan.shiro.authc.AuthenticationException;
import com.lilingyan.shiro.authc.AuthenticationInfo;
import com.lilingyan.shiro.authc.AuthenticationToken;
import com.lilingyan.shiro.authc.Authenticator;
import com.lilingyan.shiro.authc.pam.ModularRealmAuthenticator;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 11:33
 */
public abstract class AuthenticatingSecurityManager extends RealmSecurityManager{

    private Authenticator authenticator;

    public AuthenticatingSecurityManager() {
        super();
        this.authenticator = new ModularRealmAuthenticator();
    }

    public AuthenticationInfo authenticate(AuthenticationToken token) throws AuthenticationException {
        /**
         * 使用登陆器去登陆
         * 默认使用ModularRealmAuthenticator
         */
        return this.authenticator.authenticate(token);
    }

}
