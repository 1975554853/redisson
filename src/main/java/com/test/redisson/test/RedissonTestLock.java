package com.test.redisson.test;

import com.test.redisson.lockutil.LockUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @version 1.0.0
 * @description
 * @date 2018/05/04 17:02
 **/
public class RedissonTestLock {




    public static void main(String[] args) {

        //模拟2个线程
        for (int i = 1; i <= 2; i++) {

            //可以开2个IDE，分别测试以下三个方法
            //打开2个IDE同时执行时，这里可以分别取不同名，区分
            new Thread("IDE-ONE-"+i) {
                @Override
                public void run() {

                    /**
                     * 测试testLock结果，每个IDE中线程，依次排队等待获取锁。然后执行任务
                     */
//                    testLock("redissonlocktest_testkey");

                    /**
                     * 测试testTryLock结果，每个IDE中线程，在TryLock的等待时间范围内，若获取到锁，返回true,则执行任务;若获取不到，则返回false，直接返回return;
                     */
//                    testTryLock("redissonlocktest_testkey");

                    /**
                     * 测试testSyncro结果，IDE之间的线程互不影响，同一个IDE中的线程排队值执行，不同IDE之间的互补影响，可同时执行
                     */
                    testSyncro("redissonlocktest_testkey");
                }
            }.start();
        }

    }


    //测试lock，拿不到lock就不罢休，不然线程就一直block。
    public static void testLock(String preKey) {
        try {
            System.out.println(getDate()+Thread.currentThread().getName() + "准备开始任务。。。。");
            LockUtil.lock(preKey);
            System.out.println(getDate()+Thread.currentThread().getName() + "模拟正在执行任务。。。。");
            Thread.sleep(5000);//等待5秒，后面的所有线程都“依次”等待5秒，等待获取锁，执行任务

        } catch (Exception e) {
            System.out.println(getDate()+"线程锁 :" + Thread.currentThread().getId() + " exception :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            e.printStackTrace();
        } finally {

            LockUtil.unlock(preKey);
            System.out.println(getDate()+Thread.currentThread().getName() + "释放。。。。");
        }
    }

    //带时间限制的tryLock()，拿不到lock，就等一段时间，超时返回false。
    public static void testTryLock(String preKey) {

        try {
            System.out.println(getDate()+Thread.currentThread().getName() + "准备开始任务。。。。");

            boolean falg = LockUtil.tryLock(preKey);
            //这里若获取不到锁，就直接返回了
            if(!falg){
                System.out.println(getDate()+Thread.currentThread().getName() + "--没有获取到锁直接返回--" + falg);
                return;
            }
            System.out.println(getDate()+Thread.currentThread().getName() + "--获取锁--" + falg);
            System.out.println(getDate()+Thread.currentThread().getName() + "模拟正在执行任务。。。。");

           //由于在LockUtil.tryLock设置的等待时间是5s，所以这里如果休眠的小于5秒，这第二个线程能获取到锁，
            // 如果设置的大于5秒，则剩下的线程都不能获取锁。可以分别试试2s,和6s的情况
            Thread.sleep(8000);//等待6秒

        } catch (Exception e) {
            System.out.println(getDate()+"线程锁 :" + Thread.currentThread().getId() + " exception :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            e.printStackTrace();
        } finally {
            try {
                LockUtil.unlock(preKey);
                System.out.println(getDate()+Thread.currentThread().getName() + "释放。。。。");
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }


    //synchronized 这种锁只能锁住同一台机器的线程,若部署多台机器，则不能锁住
    public static void testSyncro(String preKey) {
        synchronized (preKey.intern()){

            try {
                System.out.println(getDate()+Thread.currentThread().getName() + "准备开始任务。。。。");

                System.out.println(getDate()+Thread.currentThread().getName() + "--获取锁--" );

                System.out.println(getDate()+Thread.currentThread().getName() + "模拟正在执行任务。。。。");
                Thread.sleep(6000);//执行2秒

            } catch (Exception e) {
                System.out.println(getDate()+"线程锁 :" + Thread.currentThread().getId() + " exception :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                e.printStackTrace();
            } finally {
                try {
                    System.out.println(getDate()+Thread.currentThread().getName() + "释放。。。。");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }


    public static String getDate(){

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"   ";

    }
}
