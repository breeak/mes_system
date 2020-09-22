package com.mscode.common.exception.file;

import com.mscode.common.exception.BaseException;

/**
 * 文件信息异常类
 * 
 * @author mscode  官方网址：www.mscode.vip
 */
public class FileException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args)
    {
        super("file", code, args, null);
    }

}
