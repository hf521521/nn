package com.lilingyan.shiro.realm;

import com.lilingyan.shiro.authc.AuthenticationException;
import com.lilingyan.shiro.authc.AuthenticationInfo;
import com.lilingyan.shiro.authc.AuthenticationToken;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 11:31
 */
public interface Realm {

    String getName();

    boolean supports(AuthenticationToken token);

    AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException;

}
