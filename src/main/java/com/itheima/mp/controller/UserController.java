package com.itheima.mp.controller;

import cn.hutool.core.bean.BeanUtil;
import com.itheima.mp.domain.dto.PageDTO;
import com.itheima.mp.domain.dto.UserFormDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.enums.UserStatus;
import com.itheima.mp.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Api(tags = "微服务mp接口")
@RequestMapping("/users")
/*
springboot不推荐@autowired注入，推荐构造函数，但构造函数写起来麻烦，改用注解，
但是不是controller下所有参数都要无参构造，
我们对userService变成final常量类，只对特殊的成员变量初始化
*/
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    @ApiOperation("新增用户")
    @PostMapping
    public void saveUser(@RequestBody UserFormDTO userFormDTO){
        log.info("新增用户：{}",userFormDTO);
        //User user = new User();
        //BeanUtils.copyProperties(userFormDTO,User.class);
        User user = BeanUtil.copyProperties(userFormDTO, User.class);
        userService.save(user);
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        log.info("删除用户：{}",id);
        userService.removeById(id);
    }

    @ApiOperation("根据id查询用户和对应的地址信息")
    @GetMapping("/{id}")
    public UserVO queryUserById(@PathVariable Long id){
        log.info("根据id查询用户和对应的地址信息：{}",id);
        return userService.queryUserAndAddressById(id);
    }

    @ApiOperation("根据id批量查询用户和对应的地址信息")
    @GetMapping
    public List<UserVO> queryUserByIds(@RequestParam List<Long> ids){
        log.info("根据id批量查询用户和对应的地址信息：{}",ids);
        return userService.queryUsersAndAddressByIds(ids);
    }

    @ApiOperation("扣减用户余额")
    @PutMapping("/{id}/deduction/{balance}")
    public void deductionMoneyById(@PathVariable("id") Long id, @PathVariable("balance") Integer balance){
        log.info("扣减用户余额：{},{}",id,balance);
        userService.deductionMoneyById(id,balance);
    }

    @ApiOperation("根据userQuery查询用户集")
    @GetMapping("/list")
    public List<UserVO> queryUsers(@Param("name") String name,
                                   @Param("status") UserStatus status,
                                   @Param("minBalance") Integer minBalance,
                                   @Param("maxBalance") Integer maxBalance){
        log.info("根据userQuery查询用户集：{}",name,status,minBalance,maxBalance);
        UserQuery userQuery = new UserQuery();
        userQuery.setName(name);
        userQuery.setStatus(status);
        List<User> users=userService.listByUserQuery(userQuery);
        return BeanUtil.copyToList(users,UserVO.class);
    }

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public PageDTO<UserVO> PageQuery( UserQuery userQuery){
        log.info("分页查询：{}",userQuery);
        return userService.pageQuery(userQuery);
    }
}
