package com.lilingyan.shiro.authc;

import com.lilingyan.shiro.subject.PrincipalCollection;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 11:18
 */
public interface AuthenticationInfo {

    PrincipalCollection getPrincipals();

    Object getCredentials();

}
