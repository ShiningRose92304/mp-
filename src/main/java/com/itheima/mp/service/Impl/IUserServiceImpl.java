package com.itheima.mp.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.dto.PageDTO;
import com.itheima.mp.domain.po.Address;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.AddressVO;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.enums.UserStatus;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        if (user.getStatus() == UserStatus.FREEZE) {
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
                .set(user.getBalance()-balance==0,User::getStatus,UserStatus.FREEZE)
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

    //根据id查询用户和对应的地址信息
    public UserVO queryUserAndAddressById(Long id) {
        //根据id查询用户
        User user = getById(id);
        //根据用户id查询地址
        if(user==null || user.getStatus()==UserStatus.FREEZE){
            throw new RuntimeException("用户不存在或已被冻结");
        }
        //根据用户id查询地址,可以注入mapper@autowired，也可以用静态方法
        List<Address> addresses = Db.lambdaQuery(Address.class).eq(Address::getUserId, id).list();
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        if(CollUtil.isNotEmpty(addresses)){//hutool工具也是一个很强大的工具
            userVO.setAddresses(BeanUtil.copyToList(addresses, AddressVO.class));
        }
        return userVO;
    }

    //根据id批量查询用户和对应的地址信息
    public List<UserVO> queryUsersAndAddressByIds(List<Long> ids) {
        List<User> users = listByIds(ids);
        if(users==null || users.isEmpty()){
            throw new RuntimeException("用户不存在");
        }
        ArrayList<UserVO> list = new ArrayList<>();
        for (User user : users) {
            UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
            List<Address> addresses = Db.lambdaQuery(Address.class).eq(Address::getUserId, user.getId()).list();
            if(CollUtil.isNotEmpty(addresses)){
                userVO.setAddresses(BeanUtil.copyToList(addresses, AddressVO.class));
            }
            list.add(userVO);
        }
        return list;
    }

    //分页查询用户列表
    public PageDTO<UserVO> pageQuery(UserQuery userQuery) {
        //1.构造分页条件
        Page<User> page = Page.of(userQuery.getPageNo(), userQuery.getPageSize());
        //2.排序条件
        //page.addOrder(new OrderItem(userQuery.getSortBy(), userQuery.getIsAsc()));
        //2.构造查询条件
        String name = userQuery.getName();
        UserStatus status = userQuery.getStatus();
        Integer minBalance = userQuery.getMinBalance();
        Integer maxBalance = userQuery.getMaxBalance();
        Page<User> userPage = lambdaQuery()
                .like(name != null, User::getUsername, name)
                .eq(status != null, User::getStatus, status)
                .gt(minBalance != null, User::getBalance, minBalance)
                .lt(maxBalance != null, User::getBalance, maxBalance)
                .page(page);
        //3.返回分页结果
        PageDTO<UserVO> pageDTO = new PageDTO<>();
        pageDTO.setTotal((int) userPage.getTotal());
        pageDTO.setPages((int)userPage.getPages());
        List<User> records = userPage.getRecords();
        BeanUtil.copyProperties(records,pageDTO.getList());
        return pageDTO;
    }
}
