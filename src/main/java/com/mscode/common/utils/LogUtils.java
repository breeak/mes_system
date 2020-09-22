package com.mscode.common.utils;

/**
 * 处理并记录日志文件
 * 
 * @author mscode  官方网址：www.mscode.vip
 */
public class LogUtils
{
    public static String getBlock(Object msg)
    {
        if (msg == null)
        {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }
}
