package cn.jlw.firelearning.exception;

import cn.jlw.firelearning.model.enums.RetCodeEnum;

/**
 * 自定义异常类
 */
public class LeException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    /**
     * 异常错误码, 默认码值,过度
     */
    private Integer errorCode = 40004;

    /**
     * 自定义返回码以及返回信息
     *
     * @param message
     */
    public LeException(String message) {
        super(message);
    }

    public LeException(RetCodeEnum retCodeEnum, String message) {
        super(message);
        this.errorCode = retCodeEnum.getKey();
    }

    public LeException(Integer code, String message) {
        super(message);
        this.errorCode = code;
    }

    /**
     * 2
     */
    public LeException(Throwable cause) {
        super(cause);
    }

    public LeException(RetCodeEnum retCodeEnum, Throwable cause) {
        super(cause);
        this.errorCode = retCodeEnum.getKey();
    }

    /**
     * 3
     */
    public LeException(String message, Throwable cause) {
        super(message, cause);
    }

    public LeException(RetCodeEnum retCodeEnum, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = retCodeEnum.getKey();
    }

    public Integer getErrorCode() {
        return errorCode;
    }
}
