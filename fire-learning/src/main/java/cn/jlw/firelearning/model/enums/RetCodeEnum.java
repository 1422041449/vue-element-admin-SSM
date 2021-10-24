package cn.jlw.firelearning.model.enums;

import lombok.Getter;

/**
 * 返回状态码枚举
 */
public enum RetCodeEnum {

    SUCC(10000, "成功"),
    FAIL(40004, "失败");


    @Getter
    private final Integer key;
    @Getter
    private final String desc;

    RetCodeEnum(Integer key, String desc) {
        this.key = key;
        this.desc = desc;
    }
}
