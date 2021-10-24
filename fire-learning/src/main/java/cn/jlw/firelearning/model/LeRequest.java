package cn.jlw.firelearning.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 公共请求
 *
 * @param <T>
 */
@Data
public class LeRequest<T> implements Serializable {
    /**
     * 版本控制
     */
    private String version;
    /**
     * 随机字符串
     */
    private String nonceStr;
    /**
     * 时间戳
     */
    private String timestamp;
    /**
     * 签名类型
     */
    private String signType;
    /**
     * 请求参数
     */
    private T content;
    /**
     * 签名
     */
    private String sign;
}
