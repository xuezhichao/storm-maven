package com.xzc.storm.check;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.storm.Config;
import org.apache.storm.hbase.common.ColumnList;
import org.apache.storm.hbase.common.HBaseClient;
import org.apache.storm.hbase.common.Utils;

import java.util.*;

/**
 * @author ：xzc
 * @date ：Created in 2020/6/11 18:56
 * @description： 读取hbase表信息并打印
 * @modified By：
 * @version: $
 */
public class UatTest {
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
            //        StormConfig.put("logTimeOffset" ,logTimeOffset);
            //        StormConfig.put("calculateType" ,calculateType);
            //        StormConfig.put("paramPrefix" ,ConfUtil.getParamPrefix(calculateType));
            stormConf.put("hbase.conf",conf);





            Iterator hbaseConfMap = conf.keySet().iterator();

            while(hbaseConfMap.hasNext()) {
                String key = (String)hbaseConfMap.next();
                hbConfig.set(key, String.valueOf(conf.get(key)));
            }

            HashMap hbaseConfMap1 = new HashMap(conf);
            hbaseConfMap1.put("topology.auto-credentials", stormConf.get("topology.auto-credentials"));
            HBaseClient hBaseClient = new HBaseClient(hbaseConfMap1, hbConfig, "crs_uat_ns:crs_date");

            List<Mutation> batchMutations = new ArrayList<Mutation>();
            StringBuffer rowkey = new StringBuffer("123");
            Increment increment = new Increment(Bytes.toBytes(rowkey.toString()));
            increment.addColumn(Bytes.toBytes("info"), Bytes.toBytes("test"), 1L);
            Put put = new Put(Bytes.toBytes("456"));
            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("ppp"), Bytes.toBytes("hhhh"));
            batchMutations.add(put);
            batchMutations.add(increment);
            ColumnList cols = new ColumnList();
            cols.addCounter(Utils.toBytes("info"),Utils.toBytes("dayApplyNo"),Utils.toLong(1L));
            cols.addColumn(Utils.toBytes("info"),Utils.toBytes(1),Long.MAX_VALUE,Utils.toBytes(1L));
            batchMutations.addAll(hBaseClient.constructMutationReq(Utils.toBytes("220200610_20200610"), cols,
                     Durability.SYNC_WAL));

            hBaseClient.batchMutate(batchMutations);
            List<Get> gets = new ArrayList<>();
            Get g = new Get(Bytes.toBytes("1_20200611"));
            g.addFamily(Bytes.toBytes("info"));
            gets.add(g);
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
