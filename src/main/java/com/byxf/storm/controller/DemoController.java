package com.byxf.storm.controller;

import com.byxf.storm.entity.Demo;
import com.byxf.storm.hbase.HconnectionFactory;
import com.byxf.storm.service.DemoService;
import org.apache.commons.lang.time.StopWatch;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;
import org.apache.hadoop.hbase.client.coprocessor.LongColumnInterpreter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private DemoService demoService;

    @GetMapping("/test")
    public String test(){
        return "test";
    }

    @PostMapping("/save")
    public void save(@RequestBody Demo demo){
        demoService.save(demo);
    }

    @GetMapping("/get/{rowkeyPrefix}")
    public void getBean(@PathVariable String rowkeyPrefix){
        List<Demo> apples = demoService.getById(new Demo(), rowkeyPrefix);
    }

    @GetMapping("/getColumn/{columnPrefix}")
    public void getBeanByColumnPrefix(@PathVariable String columnPrefix) throws Exception {
        ResultScanner apples = demoService.getByColumnPrefix(new Demo(), columnPrefix);
        for(Result result:apples){
            showCell(result);
        }
    }


    @GetMapping("/queryScanAndColumn/{columnPrefix}")
    public void queryScanAndColumn(@PathVariable String columnPrefix) throws Exception {
        ResultScanner apples = demoService.queryScanAndColumn(new Demo(), columnPrefix);
        for(Result result:apples){
            showCell(result);
        }
    }

    @GetMapping("/statistics/{columnPrefix}")
    public void statistics(@PathVariable String columnPrefix) throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ResultScanner apples = demoService.statistics(new Demo(),columnPrefix);
        Integer count=0;
        for(Result result:apples){
            count++;
        }
        stopWatch.stop();
        System.out.println(stopWatch.getTime()+"-----"+count);
    }

    @GetMapping("/rowCountByCoprocessor")
    public void rowCountByCoprocessor(){
        System.out.println(demoService.rowCountByCoprocessor("t_demo"));
    }

    public static void showCell(Result result){
        Cell[] cells = result.rawCells();
        for(Cell cell:cells){
            System.out.println("RowName(行键):"+new String(CellUtil.cloneRow(cell))+" ");
            System.out.println("Timetamp(时间戳):"+cell.getTimestamp()+" ");
            System.out.println("column Family（列簇）:"+new String(CellUtil.cloneFamily(cell))+" ");
            System.out.println("column Name（列名）:"+new String(CellUtil.cloneQualifier(cell))+" ");
            System.out.println("value:（值）"+new String(CellUtil.cloneValue(cell))+" ");
            System.out.println();
        }

    }
}
