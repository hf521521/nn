package com.lilingyan.shiro.subject;

import java.io.Serializable;

/**
 * 一个登陆者
 * 可以拥有多个登陆凭证
 * 因为shiro支持多个realm认证
 * @Author: lilingyan
 * @Date 2019/6/3 11:19
 */
public interface PrincipalCollection extends Iterable, Serializable {

    Object getPrimaryPrincipal();

    boolean isEmpty();

}
