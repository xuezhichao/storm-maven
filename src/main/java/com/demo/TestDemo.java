package com.demo;

import com.xzc.storm.util.BqsParseVerifyEventEnum;
import com.xzc.storm.util.DateUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hdfs.web.JsonUtil;
import org.apache.storm.Config;
import org.apache.storm.hbase.common.ColumnList;
import org.apache.storm.hbase.common.HBaseClient;
import org.apache.storm.hbase.common.Utils;

import java.nio.ByteBuffer;
import java.sql.Array;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ：xzc
 * @date ：Created in 2020/6/5 10:06
 * @description：
 * @modified By：
 * @version: $
 */
public class TestDemo {

    public static void main(String[] args) {
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");

        System.out.println(strings.stream().filter(s->!s.isEmpty()).count());


        Random random = new Random();
        random.ints().limit(10).sorted().forEach(System.out::println);


        System.out.println(BqsParseVerifyEventEnum.black_list_17184.name() + "----");
    }
}
