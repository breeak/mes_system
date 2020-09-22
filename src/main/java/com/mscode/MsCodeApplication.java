package com.mscode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author mscode  官方网址：www.mscode.vip
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class MsCodeApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(MsCodeApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  纺织管理系统后台 启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
