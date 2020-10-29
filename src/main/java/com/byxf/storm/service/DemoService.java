package com.byxf.storm.service;

import com.byxf.storm.dao.DemoDao;
import com.byxf.storm.entity.Demo;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Descriptions:
 * @Date: Created in 2018/3/21
 */
@Service("demoService")
public class DemoService {

    @Autowired
    private DemoDao demoDao;

    /**
     * @Descripton:
     * @param demo
     * @Date: 2018/3/22
     */
    public void save(Demo demo) {
        demoDao.save(demo);
    }

    /**
     * @Descripton:
     * @param demo
     * @param id
     * @Date: 2018/3/22
     */
    public List<Demo> getById(Demo demo, String id) {
        return demoDao.getById(demo, id);
    }

    public ResultScanner getByColumnPrefix(Demo demo, String id) throws Exception {
        return demoDao.getByColumnPrefix(demo, id);
    }

    public ResultScanner queryScanAndColumn(Demo demo, String columnPrefix) throws Exception {
        return demoDao.queryScanAndColumn(demo, columnPrefix);
    }

}
