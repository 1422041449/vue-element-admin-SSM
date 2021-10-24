package cn.jlw.firelearning.model;

import cn.jlw.firelearning.model.enums.RetCodeEnum;
import lombok.Data;

/**
 * 公共返回
 */
@Data
public class LeResponse<T> {

    private Integer code;

    private String msg;

    private T data;

    /**
     * 默认成功返回
     *
     * @return 1
     */
    public static <T> LeResponse<T> succ() {
        return restResult(RetCodeEnum.SUCC.getKey(), RetCodeEnum.SUCC.getDesc(), null);
    }

    /**
     * 默认成功返回，加返回数据
     *
     * @param returnData 1
     * @return 1
     */
    public static <T> LeResponse<T> succ(T returnData) {
        return restResult(RetCodeEnum.SUCC.getKey(), RetCodeEnum.SUCC.getDesc(), returnData);
    }

    /**
     * 默认成功返回，加返回数据,及返回原因
     *
     * @param returnData 1
     * @return 1
     */
    public static <T> LeResponse<T> succ(String msg, T returnData) {
        return restResult(RetCodeEnum.SUCC.getKey(), msg, returnData);
    }

    public static <T> LeResponse<T> succ(RetCodeEnum retCodeEnum) {
        return restResult(retCodeEnum.getKey(), retCodeEnum.getDesc(), null);
    }

    public static <T> LeResponse<T> succ(Integer code, String msg) {
        return restResult(code, msg, null);
    }

    /**
     * 默认失败返回
     *
     * @return 1
     */
    public static <T> LeResponse<T> fail() {
        return restResult(RetCodeEnum.FAIL.getKey(), RetCodeEnum.FAIL.getDesc(), null);
    }

    /**
     * 默认失败返回，加失败原因
     *
     * @return 1
     */
    public static <T> LeResponse<T> fail(String errorMsg) {
        return restResult(RetCodeEnum.FAIL.getKey(), errorMsg, null);
    }

    /**
     * 添加失败返回码，加失败原因
     *
     * @return 1
     */
    public static <T> LeResponse<T> fail(RetCodeEnum retCodeEnum, String errorMsg) {
        return restResult(retCodeEnum.getKey(), errorMsg, null);
    }

    public static <T> LeResponse<T> fail(Integer code, String errorMsg) {
        return restResult(code, errorMsg, null);
    }

    /**
     * 添加失败返回码，加返回对象
     *
     * @return 1
     */
    public static <T> LeResponse<T> fail(RetCodeEnum retCodeEnum, T returnData) {
        return restResult(retCodeEnum.getKey(), retCodeEnum.getDesc(), returnData);
    }

    public static <T> LeResponse<T> fail(Integer code, T returnData) {
        return restResult(code, null, returnData);
    }

    /**
     * 添加失败返回码，加失败原因，返回对象
     *
     * @return 1
     */
    public static <T> LeResponse<T> fail(RetCodeEnum retCodeEnum, String errorMsg, T returnData) {
        return restResult(retCodeEnum.getKey(), errorMsg, returnData);
    }

    public static <T> LeResponse<T> fail(Integer code, String errorMsg, T returnData) {
        return restResult(code, errorMsg, returnData);
    }

    /**
     * 返回参数处理
     *
     * @param code       1
     * @param msg        1
     * @param returnData 1
     * @return 1
     */
    private static <T> LeResponse<T> restResult(Integer code, String msg, T returnData) {
        LeResponse<T> apiResult = new LeResponse<>();
        apiResult.setCode(code);
        apiResult.setMsg(msg);
        apiResult.setData(returnData);
        return apiResult;
    }

}
