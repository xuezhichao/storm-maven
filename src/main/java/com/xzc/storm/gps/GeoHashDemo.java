package com.xzc.storm.gps;

import com.github.wenhao.geohash.GeoHash;
import com.github.wenhao.geohash.GeoSearch;
import com.github.wenhao.geohash.domain.GeoRange;
import com.xzc.storm.util.DateUtil;
import com.xzc.storm.util.MD5Util;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @author ：xzc
 * @date ：Created in 2020/6/11 19:06
 * @description：
 * @modified By：
 * @version: $
 */
public class GeoHashDemo {

    public static void main(String[] args) throws ParseException {
        String aa = "1597388472000";
        System.out.println(DateUtil.timestampToDateStr(Long.parseLong(aa))
        );
//        String longitude="116.425355",latitude="39.987415";
//        Double lat = Double.parseDouble("39.987415");
//        Double lng = Double.parseDouble("116.425355");
//
//        GeoHash geoHash = GeoHash.fromCoordinate(lat ,lng);
//
//        String longValue = geoHash.toLong() + "";
//        System.out.println(longValue);
//
//        //500m范围的经纬度
//        List<GeoRange> list =  GeoSearch.range(lat, lng, Double.parseDouble("500"));
//        for(GeoRange g:list){
//            System.out.println(g.getMin()+ "---"+g.getMax());
//        }
//        DecimalFormat df = new DecimalFormat("#.###");
//        System.out.println(df.format(new BigDecimal("116.42").doubleValue()));
//
//        Double lng= Double.parseDouble("116.425");
//        Double lat = Double.parseDouble("39.987");
//
//
//        GeoHash geoHash = GeoHash.fromCoordinate(lat ,lng);
//
//        String longValue = geoHash.toLong() + "";
//
//        System.out.println(MD5Util.MD5(longValue).charAt(0)+"_"+longValue);
//
//            int j = 0;
//            for (int i = 0; i < 10; i++) {
//                j = (++j);
//            }
//            System.out.println(j);


//        String GPS = "";
//        if(StringUtils.isNotBlank(longitude) && StringUtils.isNotBlank(latitude)){
//            longitude = df.format(new BigDecimal(longitude).doubleValue());
//            latitude = df.format(new BigDecimal(latitude).doubleValue());
//            GeoHash geoHash = GeoHash.fromCoordinate(Double.parseDouble(latitude) ,Double.parseDouble(longitude));
//            GPS = Long.toString(geoHash.toLong());
//        }
//        StringBuffer rowkey = new StringBuffer();
//        rowkey.append(MD5Util.MD5(GPS).charAt(0)).append("_").append(GPS);
//        System.out.println(rowkey.toString());
    }
}
