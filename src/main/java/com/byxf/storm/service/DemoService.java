package com.byxf.storm.service;

import com.byxf.storm.dao.DemoDao;
import com.byxf.storm.entity.Demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * @Descriptions:
 * @Date: Created in 2018/3/21
 */
@Service("demoService")
public class DemoService {

    @Autowired
    private DemoDao demoDao;

    /**
     * @Descripton:    保存数据
     * @Date: 2018/3/22
     */
    public void save(Demo demo) {
        demoDao.save(demo);
    }

    /**
     * @Descripton: 根据rowkey前缀模糊查询
     * @Date: 2018/3/22
     */
    public List<Demo> scanRowkeyPre(Demo demo, String id) {
        return demoDao.scanRowkeyPre(demo, id);
    }

    /**
     * description: 根据列名前缀模糊查询
     * create time: 2020/10/30 11:51
     * @return
     */
    public List<Map<String,Object>> scanColPre(Demo demo, String id) throws Exception {
        return demoDao.scanColPre(demo, id);
    }

    /**
     * description: 根据列值、列名前缀模糊匹配
     * create time: 2020/10/30 11:57
     */
    public List<Map<String,Object>> scanValueAndColPre(Demo demo, Map param,String columnPrefix) throws Exception {
        return demoDao.scanValueAndColPre(demo,param, columnPrefix);
    }

    /**
     * description: 返回每条数据的第一个kv，用于count，计算总数，速度快
     * create time: 2020/10/30 11:59
     */
    public List<Map<String,Object>> firstKeyOnlyFilter(Demo demo) throws Exception {
        return demoDao.firstKeyOnlyFilter(demo);
    }

    /**
     * description: 统计筛选出的数据长度
     * create time: 2020/10/30 15:41
     */
    public Long rowCountByCoprocessor(String tableName){
        return demoDao.rowCountByCoprocessor(tableName);
    }

}
