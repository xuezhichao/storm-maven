package com.byxf.storm.dao;

import com.byxf.storm.entity.Demo;
import com.byxf.storm.hbase.HBaseDaoUtil;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

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
}
