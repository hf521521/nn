package com.lilingyan.shiro.authc;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 11:17
 */
public interface Authenticator {

    public AuthenticationInfo authenticate(AuthenticationToken authenticationToken)
            throws AuthenticationException;

}
