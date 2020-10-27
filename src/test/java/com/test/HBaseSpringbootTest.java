package com.test;

import com.byxf.storm.StormMavenApplication;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author ：xzc
 * @date ：Created in 2020/10/23 16:47
 * @description：
 * @modified By：
 * @version: $
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StormMavenApplication.class)
public class HBaseSpringbootTest {

    @Test
    public void putTest() {
    }


}
