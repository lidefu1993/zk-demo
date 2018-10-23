package com.ldf.zk.publish_subscribe;

/**
 * Created by ldf on 2018/10/19.
 */
public class ZKConstants {
    public static final String zkAddress = "172.16.36.204:2181";
    public static final int sessionTimeout = 2000;
    public static String parentPath = "/Pub-Sub";//父节点
    public static String configPath = parentPath + "/DBConfig";//存放配置信息的节点
}
