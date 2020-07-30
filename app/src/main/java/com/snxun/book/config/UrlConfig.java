package com.snxun.book.config;

import androidx.annotation.Keep;

/**
 * @author zhouL
 * @date 2020/7/30
 */
public class UrlConfig {

    private UrlConfig() {}

    @Keep
    private static class BaseUrls{
        @Keep
        private static final String Release = "http://www.baidu.com/"; // 正式地址
        @Keep
        private static final String Test = "http://www.baidu.com/"; // 测试地址
    }

    /** 正式环境 BASE_URL设置为静态可修改*/
    public static final String BASE_URL = BaseUrls.Release;
}
