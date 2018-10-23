package com.ldf.zk.lock;


import com.ldf.zk.publish_subscribe.DBConfig;
import com.ldf.zk.publish_subscribe.ZKConstants;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by ldf on 2018/10/20.
 */
public class LockTest {
    private static String LOCK_PATH = "/lock";
    public static void main(String[] args) throws Exception {
        //创建zookeeper的客户端
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(ZKConstants.zkAddress)
                .sessionTimeoutMs(ZKConstants.sessionTimeout)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.start();
        //创建分布式锁, 锁空间的根节点路径为/curator/lock
        InterProcessMutex mutex = new InterProcessMutex(client, LOCK_PATH);///data01/zk/zookeeper-3.4.10/lock
        mutex.acquire();
        //获得了锁, 进行业务流程
        System.out.println("Enter mutex");
        //完成业务流程, 释放锁
        mutex.release();
        //关闭客户端
        client.close();
    }
}
