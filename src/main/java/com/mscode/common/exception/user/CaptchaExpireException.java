package com.mscode.common.exception.user;

/**
 * 验证码失效异常类
 * 
 * @author mscode  官方网址：www.mscode.vip
 */
public class CaptchaExpireException extends UserException
{
    private static final long serialVersionUID = 1L;

    public CaptchaExpireException()
    {
        super("user.jcaptcha.expire", null);
    }
}
