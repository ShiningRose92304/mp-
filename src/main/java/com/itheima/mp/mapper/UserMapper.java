package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.mp.domain.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;


@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Update("update tb_user set balance = balance - #{balance} where id = #{id}")
    void deductionMoneyById(@Param("id") Long id,@Param("balance") Integer balance);
}
