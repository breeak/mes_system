package com.mscode.common.exception.user;

/**
 * 验证码错误异常类
 * 
 * @author mscode  官方网址：www.mscode.vip
 */
public class CaptchaException extends UserException
{
    private static final long serialVersionUID = 1L;

    public CaptchaException()
    {
        super("user.jcaptcha.error", null);
    }
}
