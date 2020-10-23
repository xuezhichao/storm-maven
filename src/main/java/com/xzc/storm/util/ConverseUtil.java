package com.xzc.storm.util;

import com.github.wenhao.geohash.GeoHash;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.util.Bytes;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author ：xzc
 * @date ：Created in 2020/6/11 18:57
 * @description：
 * @modified By：
 * @version: $
 */
public class ConverseUtil {

    /**
     * create by: xzc
     * description: String类型的byte转long
     * create time: 2020/6/11 18:58
     * * @Param: null
     *
     * @return
     */
    public static long byteStringToLong(String stringByte){
        return Bytes.toLong(Bytes.toBytesBinary(stringByte));
    }

    public static double stringToLong(String ll){
        DecimalFormat df = new DecimalFormat("#.###");
        return new BigDecimal(ll).doubleValue();
    }

    public static void main(String[] args) {
        String GPS = "";
        String longitude = "116.633";
        String latitude = "39.872";
        if(StringUtils.isNotBlank(longitude) && StringUtils.isNotBlank(latitude)){
            GeoHash geoHash = GeoHash.fromCoordinate(doubleFormat(new BigDecimal(latitude).doubleValue()),doubleFormat(new BigDecimal(longitude).doubleValue()));
            GPS = Long.toString(geoHash.toLong());
        }
        if(StringUtils.isBlank(GPS)){
            return;
        }
        StringBuffer rowkey = new StringBuffer();
        rowkey.append(MD5Util.MD5(GPS).charAt(0)).append("_").append(GPS);

        System.out.println(rowkey);

    }

    private static double doubleFormat(double d){
        DecimalFormat df = new DecimalFormat("0.000");
        return Math.floor(d * 1000)/1000;
    }


//    longiTude= 116.633,latiTude=39.873




}
