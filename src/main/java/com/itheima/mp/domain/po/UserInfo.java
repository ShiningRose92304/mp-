package com.itheima.mp.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")//注解表示可以用静态方法UserInfo.of(age,intro,gender)创建对象
public class UserInfo {
    private Integer age;
    private String intro;
    private String gender;
}
