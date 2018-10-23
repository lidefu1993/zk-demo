package com.ldf.zk.publish_subscribe;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by ldf on 2018/10/19.
 */
public class PublishServer {

    private static DBConfig dbConfig;
    private static CuratorFramework client;
    private static String CURRENT_PATH = System.getProperty("user.dir")+"\\";
    public static void main(String[] args) {
        System.out.println(CURRENT_PATH);
        init();
        readConfig();
        publishInfo();
    }

    public static void init() {
        client = CuratorFrameworkFactory.builder()
                .connectString(ZKConstants.zkAddress)
                .sessionTimeoutMs(ZKConstants.sessionTimeout)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.start();
        try {
            if (client.checkExists().forPath(ZKConstants.parentPath) == null) {
                client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(ZKConstants.parentPath);
            }
            if (client.checkExists().forPath(ZKConstants.configPath) == null) {
                client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(ZKConstants.configPath,"".getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readConfig() {
        BufferedReader reader = null;//加载文件流
        try {
            reader = new BufferedReader(new FileReader(CURRENT_PATH + "dbconfig.properties"));
            Properties prop = new Properties();//创建属性操作对象
            prop.load(reader);//加载流
//            dbConfig = new DBConfig(prop.getProperty("url"),prop.getProperty("driver"),prop.getProperty("username"),prop.getProperty("password"));
            dbConfig = new DBConfig("1", "1", "1", "1");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void publishInfo() {
        try {
            Kryo kryo = new Kryo();
            Output output = new Output(1,1024);
            kryo.writeObject(output, dbConfig);
            output.close();
            client.setData().forPath(ZKConstants.configPath, output.getBuffer());//添加到节点中
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
