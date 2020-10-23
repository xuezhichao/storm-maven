package com.xzc.zk;

import org.apache.zookeeper.*;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author ：xzc
 * @date ：Created in 2020/8/7 17:24
 * @description：
 * @modified By：
 * @version: $
 */
public class CreateGroup implements Watcher {

    private static final int SESSION_TIMEOUT = 5000;

    private ZooKeeper zk;
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public void connect(String hosts) throws InterruptedException, IOException {
        zk = new ZooKeeper(hosts,SESSION_TIMEOUT,this);
        countDownLatch.await();
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if(watchedEvent.getState() == Event.KeeperState.SyncConnected){
            countDownLatch.countDown();
        }
    }

    public void create(String groupName) throws KeeperException, InterruptedException {
        String path = "/"+groupName;
        String createPath = zk.create(path,null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("created " +createPath);
    }


    public void close() throws InterruptedException {
        zk.close();
    }

    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        CreateGroup createGroup = new CreateGroup();
        createGroup.connect("");
        createGroup.create("");
        createGroup.close();
    }
}
