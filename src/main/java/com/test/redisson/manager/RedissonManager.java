package com.test.redisson.manager;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * @author yanming
 * @version 1.0.0
 * @description
 * @date 2018/05/04 16:54
 **/
public class RedissonManager {

    private static RedissonClient redissonClient;
    private static Config config = new Config();

    /**
     * 初始化Redisson，使用哨兵模式
     */
    public static void init(){
        try {
            //创建配置
            Config config = new Config();
//指定使用集群部署方式
            config.useClusterServers()
                    // 集群状态扫描间隔时间，单位是毫秒
                    .setScanInterval(2000)
                    //cluster方式至少6个节点(3主3从，3主做sharding，3从用来保证主宕机后可以高可用)
                    .addNodeAddress("192.168.0.193:7001" )
                    .addNodeAddress("192.168.0.193:7002")
                    .addNodeAddress("192.168.0.193:7003")
                    .addNodeAddress("192.168.0.193:7004")
                    .addNodeAddress("192.168.0.193:7005")
                    .addNodeAddress("192.168.0.193:7006");

//            config.useSingleServer().setAddress("127.0.0.1:6379")
//                    .setMasterName("cache")
//                    .addSentinelAddress("10.0.21.58:26379","10.0.21.59:26379", "10.0.21.60:26379")
                    //同任何节点建立连接时的等待超时。时间单位是毫秒。默认：10000
//                    .setConnectTimeout(30000)
//                    //当与某个节点的连接断开时，等待与其重新建立连接的时间间隔。时间单位是毫秒。默认:3000
//                    .setReconnectionTimeout(10000)
//                    //等待节点回复命令的时间。该时间从命令发送成功时开始计时。默认:3000
//                    .setTimeout(10000)
//                    //如果尝试达到 retryAttempts（命令失败重试次数） 仍然不能将命令发送至某个指定的节点时，将抛出错误。如果尝试在此限制之内发送成功，则开始启用 timeout（命令等待超时） 计时。默认值：3
//                    .setRetryAttempts(5)
//                    //在一条命令发送失败以后，等待重试发送的时间间隔。时间单位是毫秒。     默认值：1500
//                    .setRetryInterval(3000)
            ;
            redissonClient = Redisson.create(config);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 获取Redisson的实例对象
     * @return
     */
    public static Redisson getRedisson(){
        init();
        return (Redisson) redissonClient;
    }

    /**
     * 测试哨兵模式的Redisson是否正常
     * @param args
     */
    public static void main(String[] args) {
        Redisson redisson = RedissonManager.getRedisson();
        System.out.println("redisson = " + redisson);

    }
}
