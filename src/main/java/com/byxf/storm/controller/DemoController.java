package com.byxf.storm.controller;

import com.alibaba.fastjson.JSON;
import com.byxf.storm.entity.Demo;
import com.byxf.storm.service.DemoService;
import org.apache.commons.lang.time.StopWatch;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private DemoService demoService;

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @PostMapping("/save")
    public void save(@RequestBody Demo demo) {
        demoService.save(demo);
    }

    @GetMapping("/get/{rowkeyPrefix}")
    public void getBean(@PathVariable String rowkeyPrefix) {
        List<Demo> apples = demoService.scanRowkeyPre(new Demo(), rowkeyPrefix);
    }

    @GetMapping("/getColumn/{columnPrefix}")
    public void getBeanByColumnPrefix(@PathVariable String columnPrefix) throws Exception {
        List<Map<String,Object>> apples = demoService.scanColPre(new Demo(), columnPrefix);
        for (Map result : apples) {
            showMap(result);
        }
    }


    @GetMapping("/queryScanAndColumn/{columnPrefix}")
    public void queryScanAndColumn(@PathVariable String columnPrefix,@RequestBody Demo demo) throws Exception {
        Map map = JSON.parseObject(JSON.toJSONString(demo), Map.class);
//        Map map = new HashMap();
//        map.put("avg", "123");
//        map.put("content", "123");
        List<Map<String,Object>> apples = demoService.scanValueAndColPre(new Demo(), map, columnPrefix);
        for (Map result : apples) {
            showMap(result);
        }
    }

    @GetMapping("/firstKeyOnlyFilter")
    public void firstKeyOnlyFilter() throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<Map<String,Object>> apples = demoService.firstKeyOnlyFilter(new Demo());
        Integer count = 0;
        for (Map result : apples) {
            showMap(result);
            count++;
        }
        stopWatch.stop();
        System.out.println(stopWatch.getTime() + "-----" + count);
    }

    @GetMapping("/rowCountByCoprocessor")
    public void rowCountByCoprocessor() {
        System.out.println(demoService.rowCountByCoprocessor("t_demo"));
    }

    public static void showCell(Result result) {
        Cell[] cells = result.rawCells();
        for (Cell cell : cells) {
            System.out.println("RowName(行键):" + new String(CellUtil.cloneRow(cell)) + " ");
            System.out.println("Timetamp(时间戳):" + cell.getTimestamp() + " ");
            System.out.println("column Family（列簇）:" + new String(CellUtil.cloneFamily(cell)) + " ");
            System.out.println("column Name（列名）:" + new String(CellUtil.cloneQualifier(cell)) + " ");
            System.out.println("value:（值）" + new String(CellUtil.cloneValue(cell)) + " ");
            System.out.println();
        }

    }


    public static void showMap(Map m){
        for(Object s:m.keySet()){
            System.out.println(s.toString()+"-----"+m.get(s));
        }
    }
}
