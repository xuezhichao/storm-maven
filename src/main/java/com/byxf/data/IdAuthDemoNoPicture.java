package com.byxf.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 简项身份核验
 * 
 * @version 2018年8月31日上午9:33:24
 * @author wuliu
 */
public class IdAuthDemoNoPicture {
    
    private static String url = "https://op.credlink.com/xlzxop/certificate/idAuthNoPicture.do";
    
    public static void main(String[] args) throws Throwable {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("trueName", "xx");// 姓名
        params.put("certNo", "xx");  // 身份证号码
        params.put("version", "1.0");
        params.put("transDate", new SimpleDateFormat("yyyyMMdd").format(new Date()));
        params.put("transTime", new SimpleDateFormat("HHmmss").format(new Date()));
        params.put("serialNo", new SimpleDateFormat("yyyyMMdd").format(new Date()) + new Random().nextInt(1000));
        params.put("certType", "01");
        RequestUtil.request(params, url);
    }
    
}
