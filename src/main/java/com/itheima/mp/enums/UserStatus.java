package com.itheima.mp.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum UserStatus {
    NOMAL(1,"正常"),
    FREEZE(2,"冻结"),
    ;
    @EnumValue//必写，对应上面的value
    private final int value;
    @JsonValue//返回值，放到value就返回1/2，放到desc就返回正常/冻结。不写就返回NOML/FREEZE
    private final String desc;

    UserStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
