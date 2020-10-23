package com.xzc.storm.check;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.storm.Config;
import org.apache.storm.hbase.bolt.mapper.HBaseProjectionCriteria;
import org.apache.storm.hbase.common.ColumnList;
import org.apache.storm.hbase.common.HBaseClient;
import org.apache.storm.hbase.common.Utils;

import java.util.*;

/**
 * @author ：xzc
 * @date ：Created in 2020/6/16 15:30
 * @description：
 * @modified By：
 * @version: $
 */
public class UatGetTest {
    public static void main(String[] args) {
        try{
            Configuration hbConfig = HBaseConfiguration.create();
            Map<String, Object> conf = new HashMap<String, Object>();
            String hbaseRootdir = "hdfs://clusteruat/apps/hbase/data";
            String hbaseZkQrm = "t1-24-14:2181,t1-24-14:2181,t1-24-14:2181";
            String hbaseZkZnode = "/hbase-unsecure";

            conf.put("hbase.rootdir", hbaseRootdir);
            conf.put("hbase.zookeeper.quorum", hbaseZkQrm);
            conf.put("zookeeper.znode.parent", hbaseZkZnode);


            Config stormConf = new Config();
            stormConf.setNumWorkers(2);
            stormConf.setMaxSpoutPending(1);
            stormConf.setMessageTimeoutSecs(60);
            stormConf.setNumAckers(1);
            stormConf.put("hbase.conf",conf);

            Iterator hbaseConfMap = conf.keySet().iterator();

            while(hbaseConfMap.hasNext()) {
                String key = (String)hbaseConfMap.next();
                hbConfig.set(key, String.valueOf(conf.get(key)));
            }

            HashMap hbaseConfMap1 = new HashMap(conf);
            hbaseConfMap1.put("topology.auto-credentials", stormConf.get("topology.auto-credentials"));
            HBaseClient hBaseClient = new HBaseClient(hbaseConfMap1, hbConfig, "crs_uat_ns:crs_date");

            List<Get> gets = new ArrayList<>();
            HBaseProjectionCriteria hpc = new HBaseProjectionCriteria();
            hpc.addColumn(new HBaseProjectionCriteria.ColumnMetaData("info" ,"dayApplyFinal"));
            hpc.addColumn(new HBaseProjectionCriteria.ColumnMetaData("info" ,"dayApplyNo"));

            Get get = hBaseClient.constructGetRequests(Bytes.toBytes("1_20200611") ,hpc);
            gets.add(get);
            Result[] r = hBaseClient.batchGet(gets);

            for (Result result : r){
                for(Cell cell : result.rawCells()){
                    System.out.println(
                            "rowkey: " + new String(CellUtil.cloneRow(cell)) +
                                    "\tfamilyColumn: " + new String(CellUtil.cloneFamily(cell)) +
                                    "\tcolumn: " + new String(CellUtil.cloneQualifier(cell)) +
                                    "\tvalue: " + Bytes.toLong(CellUtil.cloneValue(cell)) +
                                    "\ttimestamp: " + cell.getTimestamp()
                    );
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
