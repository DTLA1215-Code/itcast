package com.itheima.health.main;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.health.utils.SMSUtils;

public class SMSMain {

    public static void main(String[] args) throws ClientException {
        //短信测试
        SMSUtils.sendShortMessage("SMS_190277399","18899853036","123456");
    }
}
