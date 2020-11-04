package com.byxf.storm.dao;

import com.byxf.storm.entity.Demo;
import com.byxf.storm.hbase.HBaseDaoUtil;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("demoDao")
public class DemoDao {

    @Autowired
    private HBaseDaoUtil hBaseDaoUtil;

    public void save(Demo demo) {
        hBaseDaoUtil.save(demo);
    }

    public List<Demo> scanRowkeyPre(Demo demo, String id) {
        return hBaseDaoUtil.scanRowkeyPre(demo, id);
    }

    public List<Map<String,Object>> scanColPre(Demo demo, String id) throws Exception {
        return hBaseDaoUtil.scanColPre(demo, id);
    }

    public List<Map<String,Object>> scanValueAndColPre(Demo demo,Map param, String columnPrefix) throws Exception {
        return hBaseDaoUtil.scanValueAndColPre(demo,param, columnPrefix);
    }

    public List<Map<String,Object>> firstKeyOnlyFilter(Demo demo) throws Exception {
        return hBaseDaoUtil.firstKeyOnlyFilter(demo);
    }

    public Long rowCountByCoprocessor(String tableName){
        return hBaseDaoUtil.rowCountByCoprocessor(tableName);
    }
}
