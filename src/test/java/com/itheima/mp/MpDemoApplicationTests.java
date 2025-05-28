package com.itheima.mp;

import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.po.UserInfo;
import com.itheima.mp.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;

@SpringBootTest
class MpDemoApplicationTests {
    @Autowired
    private IUserService userService;

    private User buildUser(int i){
        User user = new User();
        user.setUsername("user_"+i);
        user.setPassword("123");
        user.setPhone(""+18688190000L+i);
        user.setBalance(2000);
        if(i%2==0){
            user.setInfo(UserInfo.of(19,"rapper老师","male"));

        }else {
            user.setInfo(UserInfo.of(20,"r&b老师","female"));
        }
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        return user;
    }
    @Test
    void contextLoads() {

    }

    @Test
    //非批处理方式，插入10w条数据耗时3min..
    void testSaveOneByOne(){
        long b = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            userService.save(buildUser(i));
        }
        long e = System.currentTimeMillis();
        System.out.println("耗时："+(e-b));
    }

    @Test
    //批处理方式，插入10w条数据耗时1.25s
    //因为每次往数据库提交都是一次网络请求，提交10w次和批处理提交10次肯定效率不一样
    //还有一种方式，动态sql，foreach批量插入也是批处理，但是不想手写sql，所以有个方法————在配置文件用参数rewriteBatchedStatements=true(重写批处理的语句)
    void testSaveBatch(){
        ArrayList<User> list = new ArrayList<>(1000);
        long b = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            list.add(buildUser(i));
            //每1000条批量插入一次
            if(i%1000==0){
                userService.saveBatch(list);
            }
            list.clear();
        }
        long e = System.currentTimeMillis();
        System.out.println("耗时："+(e-b));
    }
}
