/*
 * Copyright (C) 2017  ShenZhen xinLianZhengXin Co.,Ltd All Rights Reserved.
 * δ������˾��ʽ����ͬ�⣬�����κθ��ˡ����岻��ʹ�á����ơ��޸Ļ򷢲������.
 * ��Ȩ���������������������޹�˾ http://www.credlink.com/
 */
package com.byxf.data;


import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Request
 * 
 * @version 2018年8月31日上午8:15:01
 * @author wuliu
 */
public class RequestUtil {
    
    public static String insId = "INS81200730000001";// 机构标识
    public static String operId = "byxj_test";// 操作员
//    public static String insId = "INS150915000001";// 机构标识
//    public static String operId = "123@163.com";// 操作员
    
    @SuppressWarnings({ "rawtypes"})
    public static String request(Map<String,Object> params, String url) throws Throwable{
        String body = null;
        Gson gson = new Gson();
        RSAHelper cipher = new RSAHelper();// 初始化自己的私钥,对方的公钥以及密钥长度.
        cipher.initKey();
        String jsonStr = gson.toJson(params);
        byte[] signBytes = cipher.signRSA(jsonStr.getBytes("UTF-8"), false,
                "UTF-8");
        byte[] cryptedBytes = cipher.encryptRSA(jsonStr.getBytes("UTF-8"),
                false, "UTF-8");
        HttpClient httpClient = new DefaultHttpClient();
        String content = "";
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        formParams.add(new BasicNameValuePair("insId", insId));
        formParams.add(new BasicNameValuePair("operId", operId));
        String sign = Base64.encodeBase64String(signBytes);// Base64编码
        String encrypt = Base64.encodeBase64String(cryptedBytes);// Base64编码
        formParams.add(new BasicNameValuePair("sign", sign));
        formParams.add(new BasicNameValuePair("encrypt", encrypt));
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    formParams, "UTF-8");
            httpPost.setEntity(urlEncodedFormEntity);
            System.out.println("execurting request: " + httpPost.getURI());
            HttpResponse httpResponse = null;
            httpResponse = httpClient.execute(httpPost);
            HttpEntity resEntity = httpResponse.getEntity();
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            System.out.println("响应码:" + statusCode);
            if (statusCode == HttpStatus.SC_OK) {
                if (resEntity != null) {
                    content = EntityUtils.toString(resEntity, "UTF-8");
                    System.out.println("Response content:" + content);
                }
                EntityUtils.consume(resEntity);
            }else{
                System.out.println("响应码不为200,响应失败");
                
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.getConnectionManager().shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //返回内容处理
        if(StringUtils.isBlank(content)){
            System.out.println("请求失败,无返回内容");
        }else{
            Map map=gson.fromJson(content, HashMap.class);
            //获取签名
            signBytes=  Base64.decodeBase64(map.get("sign").toString());
            //获取加密串
            cryptedBytes=Base64.decodeBase64(map.get("encrypt").toString());
            // 获取文件
            String file= map.get("file")!=null ? map.get("file").toString() : "";
            // 解密返回信息
            byte[] decryptedBytes = cipher.decryptRSA(cryptedBytes, false, "UTF-8");
            body = new String(decryptedBytes,"UTF-8");
            System.out.println("返回报文: "+new String(decryptedBytes,"UTF-8"));
            if(StringUtils.isNotEmpty(file)) {
                System.out.println("文件流: "+file);
            }
            boolean isValid = cipher.verifyRSA(decryptedBytes,signBytes, false, "UTF-8");
            System.out.println("验签结果:"+(isValid?"成功！":"失败"));
        }
        return body;
    }
}
