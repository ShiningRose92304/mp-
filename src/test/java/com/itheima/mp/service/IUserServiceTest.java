package com.itheima.mp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.mp.domain.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
/*
* 创建测试类方法，shift+ctrl+T
* */
class IUserServiceTest {
    @Autowired
    private IUserService userService;
    @Test
    void testPageQuery(){
        Page<User> page = Page.of(1, 10);
        Page<User> userPage = userService.page(page);
        long total = userPage.getTotal();
        System.out.println("总条数："+total);
        long pages = userPage.getPages();
        System.out.println("总页数："+pages);
        List<User> records = userPage.getRecords();
        records.forEach(System.out::println);
    }
}