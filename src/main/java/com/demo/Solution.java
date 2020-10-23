package com.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xzc.storm.util.MD5Util;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author ：xzc
 * @date ：Created in 2020/6/19 10:36
 * @description：
 * @modified By：
 * @version: $
 */
class Solution {
    public static void main(String[] args) {

        System.out.println(MD5Util.MD5("15100000016"));
    }
}