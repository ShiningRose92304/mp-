package com.itheima.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.mp.domain.dto.UserFormDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;

import java.util.List;

public interface IUserService extends IService<User> {
    //根据id扣减余额
    void deductionMoneyById(Long id, Integer balance);

    //根据UserQuery查询用户列表
    List<User> listByUserQuery(UserQuery userQuery);


}
