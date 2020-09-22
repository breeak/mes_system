package com.mscode.common.exception.user;

import com.mscode.common.exception.BaseException;

/**
 * 用户信息异常类
 * 
 * @author mscode  官方网址：www.mscode.vip
 */
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
