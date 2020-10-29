package com.byxf.storm.dao;

import com.byxf.storm.entity.Demo;
import com.byxf.storm.hbase.HBaseDaoUtil;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("demoDao")
public class DemoDao {

    @Autowired
    private HBaseDaoUtil hBaseDaoUtil;


    public void save(Demo demo) {
        hBaseDaoUtil.save(demo);
    }

    public List<Demo> getById(Demo demo, String id) {
        return hBaseDaoUtil.queryScanRowkey(demo, id);
    }

    public ResultScanner getByColumnPrefix(Demo demo, String id) throws Exception {
        return hBaseDaoUtil.queryScanColumnName(demo, id);
    }

    public ResultScanner queryScanAndColumn(Demo demo, String columnPrefix) throws Exception {
        Map map = new HashMap();
        map.put("avg","123");
        map.put("content","123");
        return hBaseDaoUtil.queryScanAndColumn(demo,map, columnPrefix);
    }

    public ResultScanner statistics(Demo demo, String columnPrefix) throws Exception {
        Map map = new HashMap();
        map.put("avg","123");
        map.put("content","123");
        return hBaseDaoUtil.statistics(demo,map, columnPrefix);
    }


    public Long rowCountByCoprocessor(String tableName){
        return hBaseDaoUtil.rowCountByCoprocessor(tableName);
    }
}
