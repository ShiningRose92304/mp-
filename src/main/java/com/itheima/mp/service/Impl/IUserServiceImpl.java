package com.itheima.mp.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService{

    // 通过id扣减余额
    public void deductionMoneyById(Long id, Integer balance) {
        //查询用户
        User user = getById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        //判断用户状态是否正常
        if (user.getStatus() != 1) {
            throw new RuntimeException("用户已被冻结");
        }
        baseMapper.deductionMoneyById(id, balance);
        //判断用户余额是否足够
        if(user.getBalance() < balance) {
            throw new RuntimeException("余额不足");
        }
        //扣减余额,当用户余额为0时，将用户冻结
        lambdaUpdate()
                .set(User::getBalance, user.getBalance() - balance)
                .set(user.getBalance()-balance==0,User::getStatus,2)
                .eq(User::getId, id)
                .update();
    }

    //根据UserQuery查询用户列表
    public List<User> listByUserQuery(UserQuery userQuery) {
        //lambda条件查询表达式，用函数取代写死的sql
        List<User> users=lambdaQuery()
                .like(userQuery.getName()!=null,User::getUsername, userQuery.getName())
                .eq(userQuery.getStatus()!=null,User::getStatus, userQuery.getStatus())
                .gt(userQuery.getMinBalance()!=null,User::getBalance, userQuery.getMinBalance())
                .lt(userQuery.getMaxBalance()!=null,User::getBalance, userQuery.getMaxBalance())
                .list();

        return users;
    }
}
