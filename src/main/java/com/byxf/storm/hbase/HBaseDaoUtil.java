package com.byxf.storm.hbase;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;
import org.apache.hadoop.hbase.client.coprocessor.LongColumnInterpreter;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @Descriptions: HBaseDao操作公共类
 * @Date: Created in 2018/3/22
 */
@Component("hBaseDaoUtil")
public class HBaseDaoUtil {

    protected final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    // 关闭连接
    public static void close() {
        if (HconnectionFactory.connection != null) {
            try {
                HconnectionFactory.connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @Descripton: 创建表
     * @param tableName
     * @param familyColumn
     * @Date: 2018/3/22
     */
    public void createTable(String tableName, Set<String> familyColumn) {
        TableName tn = TableName.valueOf(tableName);
        try (Admin admin = HconnectionFactory.connection.getAdmin();) {
            HTableDescriptor htd = new HTableDescriptor(tn);
            for (String fc : familyColumn) {
                HColumnDescriptor hcd = new HColumnDescriptor(fc);
                htd.addFamily(hcd);
            }
            admin.createTable(htd);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("创建"+tableName+"表失败！", e);
        }
    }

    /**
     * @Descripton: 删除表
     * @param tableName
     * @Date: 2018/3/22
     */
    public void dropTable(String tableName) {
        TableName tn = TableName.valueOf(tableName);
        try (Admin admin = HconnectionFactory.connection.getAdmin();){
            admin.disableTable(tn);
            admin.deleteTable(tn);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("删除"+tableName+"表失败！");
        }
    }

    /**
     * @Descripton: 根据rowkey查询
     * @param obj
     * @param rowkeys
     * @Date: 2018/3/22
     */
    public <T> List<T> get(T obj, String ... rowkeys) {
        List<T> objs = new ArrayList<T>();
        String tableName = getORMTable(obj);
        if (StringUtils.isBlank(tableName)) {
            return objs;
        }
        try (Table table = HconnectionFactory.connection.getTable(TableName.valueOf(tableName)); Admin admin = HconnectionFactory.connection.getAdmin();){
            if(!admin.isTableAvailable(TableName.valueOf(tableName))){
                return objs;
            }
            List<Result> results = getResults(tableName, rowkeys);
            if (results.isEmpty()) {
                return objs;
            }
            for (int i = 0; i < results.size(); i++) {
                T bean = null;
                Result result = results.get(i);
                if (result == null || result.isEmpty()) {
                    continue;
                }
                try {
                    bean = HBaseBeanUtil.resultToBean(result, obj);
                    objs.add(bean);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("查询异常！", e);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return objs;
    }


    /**
     * @Descripton: 保存实体对象
     * @param objs
     * @Date: 2018/3/22
     */
    public <T> boolean save(T ... objs) {
        List<Put> puts = new ArrayList<Put>();
        String tableName = "";
        try (Admin admin = HconnectionFactory.connection.getAdmin();){
            for (Object obj : objs) {
                if (obj == null) {
                    continue;
                }
                tableName = getORMTable(obj);
                // 表不存在，先获取family创建表
                if(!admin.isTableAvailable(TableName.valueOf(tableName))){
                    // 获取family, 创建表
                    Class<?> clazz = obj.getClass();
                    Field[] fields = clazz.getDeclaredFields();
                    Set<String> set = new HashSet<>(10);
                    for(int i=0;i<fields.length;i++){
                        if (!fields[i].isAnnotationPresent(HbaseColumn.class)) {
                            continue;
                        }
                        fields[i].setAccessible(true);
                        HbaseColumn orm = fields[i].getAnnotation(HbaseColumn.class);
                        String family = orm.family();
                        if ("rowkey".equalsIgnoreCase(family)) {
                            continue;
                        }
                        set.add(family);
                    }
                    // 创建表
                    createTable(tableName, set);
                }
                Put put = HBaseBeanUtil.beanToPut(obj);
                puts.add(put);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("保存Hbase异常！");
        }
        return savePut(puts, tableName);
    }

    /**
     * @Descripton: 根据tableName保存
     * @param tableName
     * @param objs
     * @Date: 2018/3/22
     */
    public <T> void save(String tableName, T ... objs){
        List<Put> puts = new ArrayList<Put>();
        for (Object obj : objs) {
            if (obj == null) {
                continue;
            }
            try {
                Put put = HBaseBeanUtil.beanToPut(obj);
                puts.add(put);
            } catch (Exception e) {
                logger.warn("", e);
            }
        }
        savePut(puts, tableName);
    }

    /**
     * @Descripton: 删除
     * @param obj
     * @param rowkeys
     * @Date: 2018/3/22
     */
    public <T> void delete(T obj, String... rowkeys) {
        String tableName = "";
        tableName = getORMTable(obj);
        if (StringUtils.isBlank(tableName)) {
            return;
        }
        List<Delete> deletes = new ArrayList<Delete>();
        for (String rowkey : rowkeys) {
            if (StringUtils.isBlank(rowkey)) {
                continue;
            }
            deletes.add(new Delete(Bytes.toBytes(rowkey)));
        }
        delete(deletes, tableName);
    }


    /**
     * @Descripton: 批量删除
     * @param deletes
     * @param tableName
     * @Date: 2018/3/22
     */
    private void delete(List<Delete> deletes, String tableName) {
        try (Table table = HconnectionFactory.connection.getTable(TableName.valueOf(tableName));) {
            if (StringUtils.isBlank(tableName)) {
                logger.info("tableName为空！");
                return;
            }
            table.delete(deletes);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("删除失败！",e);
        }
    }

    /**
     * @Descripton: 根据tableName获取列簇名称
     * @param tableName
     * @Date: 2018/3/22
     */
    public List<String> familys(String tableName) {
        try (Table table = HconnectionFactory.connection.getTable(TableName.valueOf(tableName));){
            List<String> columns = new ArrayList<String>();
            if (table==null) {
                return columns;
            }
            HTableDescriptor tableDescriptor = table.getTableDescriptor();
            HColumnDescriptor[] columnDescriptors = tableDescriptor.getColumnFamilies();
            for (HColumnDescriptor columnDescriptor :columnDescriptors) {
                String columnName = columnDescriptor.getNameAsString();
                columns.add(columnName);
            }
            return columns;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询列簇名称失败！" ,e);
        }
        return new ArrayList<String>();
    }

    // 保存方法
    private boolean savePut(List<Put> puts, String tableName){
        if (StringUtils.isBlank(tableName)) {
            return false;
        }
        try (Table table = HconnectionFactory.connection.getTable(TableName.valueOf(tableName)); Admin admin = HconnectionFactory.connection.getAdmin();){
            table.put(puts);
            return true;
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 获取tableName
    public String getORMTable(Object obj) {
        HbaseTable table = obj.getClass().getAnnotation(HbaseTable.class);
        return table.tableName();
    }

    // 获取查询结果
    private List<Result> getResults(String tableName, String... rowkeys) {
        List<Result> resultList = new ArrayList<Result>();
        List<Get> gets = new ArrayList<Get>();
        for (String rowkey : rowkeys) {
            if (StringUtils.isBlank(rowkey)) {
                continue;
            }
            Get get = new Get(Bytes.toBytes(rowkey));
            gets.add(get);
        }
        try (Table table = HconnectionFactory.connection.getTable(TableName.valueOf(tableName));) {
            Result[] results = table.get(gets);
            Collections.addAll(resultList, results);
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
            return resultList;
        }
    }
    
    /**
     * @Descripton: 根据条件过滤查询（大于等于）
     * @param obj
     * @param param
     * @Date: 2018/3/26
     */
    public <T> List<T> queryScanGreater(T obj, Map<String, String> param)throws Exception{
        List<T> objs = new ArrayList<T>();
        String tableName = getORMTable(obj);
        if (StringUtils.isBlank(tableName)) {
            return null;
        }
        try (Table table = HconnectionFactory.connection.getTable(TableName.valueOf(tableName)); Admin admin = HconnectionFactory.connection.getAdmin();){
            if(!admin.isTableAvailable(TableName.valueOf(tableName))){
                return objs;
            }
            Scan scan = new Scan();
            for (Map.Entry<String, String> entry : param.entrySet()){
                Class<?> clazz = obj.getClass();
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (!field.isAnnotationPresent(HbaseColumn.class)) {
                        continue;
                    }
                    field.setAccessible(true);
                    HbaseColumn orm = field.getAnnotation(HbaseColumn.class);
                    String family = orm.family();
                    String qualifier = orm.qualifier();
                    if(qualifier.equals(entry.getKey())){
                        Filter filter = new SingleColumnValueFilter(Bytes.toBytes(family), Bytes.toBytes(entry.getKey()), CompareFilter.CompareOp.GREATER_OR_EQUAL, Bytes.toBytes(entry.getValue()));
                        scan.setFilter(filter);
                    }
                }
            }
            ResultScanner scanner = table.getScanner(scan);
            for (Result result : scanner) {
                T beanClone = (T) BeanUtils.cloneBean(HBaseBeanUtil.resultToBean(result, obj));
                objs.add(beanClone);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询失败！");
            throw new Exception(e);
        }
        return objs;
    }


    public <T> List<T> queryScanColumnPrefix(T obj, Map<String, String> param)throws Exception{
        List<T> objs = new ArrayList<T>();
        String tableName = getORMTable(obj);
        if (StringUtils.isBlank(tableName)) {
            return null;
        }
        try (Table table = HconnectionFactory.connection.getTable(TableName.valueOf(tableName)); Admin admin = HconnectionFactory.connection.getAdmin();){
            if(!admin.isTableAvailable(TableName.valueOf(tableName))){
                return objs;
            }
            Scan scan = new Scan();
            for (Map.Entry<String, String> entry : param.entrySet()){
                Class<?> clazz = obj.getClass();
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (!field.isAnnotationPresent(HbaseColumn.class)) {
                        continue;
                    }
                    field.setAccessible(true);
                    HbaseColumn orm = field.getAnnotation(HbaseColumn.class);
                    String family = orm.family();
                    String qualifier = orm.qualifier();
                    if(qualifier.equals(entry.getKey())){
//                        Filter filter = new ColumnPrefixFilter(Bytes.toBytes(entry.getKey()));
                        Filter filter = new SingleColumnValueFilter(Bytes.toBytes(family), Bytes.toBytes(entry.getKey()), CompareFilter.CompareOp.GREATER_OR_EQUAL, Bytes.toBytes(entry.getValue()));
                        scan.setFilter(filter);
                    }
                }
            }
            ResultScanner scanner = table.getScanner(scan);
            for (Result result : scanner) {
                T beanClone = (T) BeanUtils.cloneBean(HBaseBeanUtil.resultToBean(result, obj));
                objs.add(beanClone);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询失败！");
            throw new Exception(e);
        }
        return objs;
    }

    /**
     * @Descripton: 根据条件过滤查询
     * @param obj
     * @param param
     * @Date: 2018/3/26
     */
    public <T> List<T> queryScan(T obj, Map<String, String> param)throws Exception{
        List<T> objs = new ArrayList<T>();
        String tableName = getORMTable(obj);
        if (StringUtils.isBlank(tableName)) {
            return null;
        }
        try (Table table = HconnectionFactory.connection.getTable(TableName.valueOf(tableName)); Admin admin = HconnectionFactory.connection.getAdmin();){
            if(!admin.isTableAvailable(TableName.valueOf(tableName))){
                return objs;
            }
            Scan scan = new Scan();
            for (Map.Entry<String, String> entry : param.entrySet()){
                Class<?> clazz = obj.getClass();
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (!field.isAnnotationPresent(HbaseColumn.class)) {
                        continue;
                    }
                    field.setAccessible(true);
                    HbaseColumn orm = field.getAnnotation(HbaseColumn.class);
                    String family = orm.family();
                    String qualifier = orm.qualifier();
                    if(qualifier.equals(entry.getKey())){
                        Filter filter = new SingleColumnValueFilter(Bytes.toBytes(family), Bytes.toBytes(entry.getKey()), CompareFilter.CompareOp.EQUAL, Bytes.toBytes(entry.getValue()));
                        scan.setFilter(filter);
                    }
                }
            }
            ResultScanner scanner = table.getScanner(scan);
            for (Result result : scanner) {
                T beanClone = (T) BeanUtils.cloneBean(HBaseBeanUtil.resultToBean(result, obj));
                objs.add(beanClone);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询失败！");
            throw new Exception(e);
        }
        return objs;
    }

    /**
     * 根据rowkey查询记录
     * @param obj
     * @param rowkey
     * @param <T>
     * @return
     */
    public <T> List<T> scanRowkeyPre(T obj, String rowkey){
        List<T> objs = new ArrayList<T>();
        String tableName = getORMTable(obj);
        if (StringUtils.isBlank(tableName)) {
            return null;
        }
        ResultScanner scanner = null;
        try (Table table = HconnectionFactory.connection.getTable(TableName.valueOf(tableName)); Admin admin = HconnectionFactory.connection.getAdmin()){
            Scan scan = new Scan();
            scan.setRowPrefixFilter(Bytes.toBytes(rowkey));
            scanner = table.getScanner(scan);
            for (Result result : scanner) {
                T beanClone = (T) BeanUtils.cloneBean(HBaseBeanUtil.resultToBean(result, obj));
                objs.add(beanClone);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("queryScanRowkey:查询失败！", e);
        }finally {
            if(scanner!=null){
                try {
                    scanner.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("queryScan:关闭流异常！", e);
                }
            }
        }
        return objs;
    }


    /**
     * description: 列名前缀模糊查询
     * create time: 2020/10/29 16:20
     * * @Param: null
     *
     * @return
     */
    public <T> List<Map<String,Object>> scanColPre(T obj, String columnPrefix)throws Exception{
        List<T> objs = new ArrayList<T>();
        String tableName = getORMTable(obj);
        if (StringUtils.isBlank(tableName)) {
            return null;
        }
        try (Table table = HconnectionFactory.connection.getTable(TableName.valueOf(tableName)); Admin admin = HconnectionFactory.connection.getAdmin();){
            if(!admin.isTableAvailable(TableName.valueOf(tableName))){
                return null;
            }
            Scan scan = new Scan();
            Filter filter = new ColumnPrefixFilter(Bytes.toBytes(columnPrefix));
            scan.setFilter(filter);
            ResultScanner scanner = table.getScanner(scan);
            return this.readScanner(obj,scanner);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询失败！");
            throw new Exception(e);
        }
    }

    /**
     * description: 读取ResultScanner返回信息，返回map
     * create time: 2020/11/4 10:18
     */
    public <T> List<Map<String,Object>> readScanner(T obj,ResultScanner scanner) throws Exception {
        List ls = new ArrayList();
        try {
            for (Result result : scanner) {
                Map<String,Object> map = new HashMap();
                map = HBaseBeanUtil.resultToMap(result, obj);
                ls.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            if(scanner!=null){
                try {
                    scanner.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("queryScan:关闭流异常！", e);
                }
            }
        }
        return ls;
    }

    /**
     * 根据rowkey钱难追和列前缀模糊查询记录
     * @param obj
     * @param rowkey
     * @param <T>
     * @return
     */
    public <T> List<T> scanRowkeyPreAndColPre(T obj, String rowkey, String columnPrefix){
        List<T> objs = new ArrayList<T>();
        String tableName = getORMTable(obj);
        if (StringUtils.isBlank(tableName)) {
            return null;
        }
        ResultScanner scanner = null;
        try (Table table = HconnectionFactory.connection.getTable(TableName.valueOf(tableName)); Admin admin = HconnectionFactory.connection.getAdmin()){
            Scan scan = new Scan();
            //rowkey前缀模糊查询
            scan.setRowPrefixFilter(Bytes.toBytes(rowkey));
            scanner = table.getScanner(scan);
            //列名前缀模糊查询
            Filter filter = new ColumnPrefixFilter(Bytes.toBytes(columnPrefix));
            scan.setFilter(filter);
            for (Result result : scanner) {
                T beanClone = (T) BeanUtils.cloneBean(HBaseBeanUtil.resultToBean(result, obj));
                objs.add(beanClone);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("queryScanRowkey:查询失败！", e);
        }finally {
            if(scanner!=null){
                try {
                    scanner.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("queryScan:关闭流异常！", e);
                }
            }
        }
        return objs;
    }


    /**
     * @Descripton: 根据列值和列名称前缀模糊查询
     * @param obj
     * @param param
     * @Date: 2018/3/26
     */
    public <T> List<Map<String,Object>> scanValueAndColPre(T obj, Map<String, String> param, String columnPrefix)throws Exception{
        String tableName = getORMTable(obj);
        if (StringUtils.isBlank(tableName)) {
            return null;
        }
        try (Table table = HconnectionFactory.connection.getTable(TableName.valueOf(tableName)); Admin admin = HconnectionFactory.connection.getAdmin();){
            if(!admin.isTableAvailable(TableName.valueOf(tableName))){
                return null;
            }
            Scan scan = new Scan();
            FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
            for (Map.Entry<String, String> entry : param.entrySet()){
                Class<?> clazz = obj.getClass();
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (!field.isAnnotationPresent(HbaseColumn.class)) {
                        continue;
                    }
                    field.setAccessible(true);
                    HbaseColumn orm = field.getAnnotation(HbaseColumn.class);
                    String family = orm.family();
                    String qualifier = orm.qualifier();
                    if(qualifier.equals(entry.getKey())){
                        List<Filter> filters = new ArrayList<>();
                        //列值模糊查询
                        Filter filter = new SingleColumnValueFilter(Bytes.toBytes(family), Bytes.toBytes(entry.getKey()), CompareFilter.CompareOp.EQUAL, Bytes.toBytes(entry.getValue()));
                        //列名称前缀模糊查询
                        Filter columnFilter = new ColumnPrefixFilter(Bytes.toBytes(columnPrefix));
                        filterList.addFilter(filter);
                        filterList.addFilter(columnFilter);
                    }
                }
            }
            scan.setFilter(filterList);
            ResultScanner scanner = table.getScanner(scan);
           return this.readScanner(obj,scanner);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询失败！");
            throw new Exception(e);
        }
    }


    /**
     * @Descripton: 统计符合条件的数据，每行返回第一列数据
     * @param obj
     * @param param
     * @Date: 2018/3/26
     */
    public <T> List<Map<String,Object>> firstKeyOnlyFilter(T obj)throws Exception{
        String tableName = getORMTable(obj);
        if (StringUtils.isBlank(tableName)) {
            return null;
        }
        try(Table table = HconnectionFactory.connection.getTable(TableName.valueOf(tableName)); Admin admin = HconnectionFactory.connection.getAdmin();){
            if(!admin.isTableAvailable(TableName.valueOf(tableName))){
                return null;
            }
            Scan scan = new Scan();
            FilterList filterList = new FilterList();
            Filter firstFilter = new FirstKeyOnlyFilter();
            filterList.addFilter(firstFilter);
            scan.setFilter(filterList);
            ResultScanner scanner = table.getScanner(scan);
            return this.readScanner(obj,scanner);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询失败！");
            throw new Exception(e);
        }
    }

    /**
     * description: 统计数据长度
     * create time: 2020/10/29 17:50
     * @return
     */
    public Long rowCountByCoprocessor(String tablename){
        try (Admin admin = HconnectionFactory.connection.getAdmin();){
            TableName name=TableName.valueOf(tablename);
            if(!admin.isTableAvailable(name)){
                return null;
            }
                //提前创建connection和conf
            //先disable表，添加协处理器后再enable表
            HTableDescriptor descriptor = admin.getTableDescriptor(name);
            String coprocessorClass = "org.apache.hadoop.hbase.coprocessor.AggregateImplementation";
            if (! descriptor.hasCoprocessor(coprocessorClass)) {
                admin.disableTable(name);
                descriptor.addCoprocessor(coprocessorClass);
                admin.modifyTable(name, descriptor);
                admin.enableTable(name);
            }
            //计时
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            Scan scan = new Scan();
            AggregationClient aggregationClient = new AggregationClient(HconnectionFactory.conf);
            Long totles = aggregationClient.rowCount(name, new LongColumnInterpreter(), scan);
            stopWatch.stop();
            System.out.println("统计耗时：" +stopWatch.getTime());
            return totles;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

}
