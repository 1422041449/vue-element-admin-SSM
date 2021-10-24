package cn.jlw.firelearning.model.enums;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * <b>功能描述: </b> <br>
 * 拦截器过滤地址
 *
 * @author shihao.li
 * @create 2020/8/3 15:09
 * @since 1.0.0
 */
public class LeRequestFiltURLHandle {

    private static String[] skipUrlArr;

    static {
        List<String> skipUrlList = Lists.newArrayList();
        // 登出接口
        skipUrlList.add("/userinfo/logout");

        skipUrlArr = skipUrlList.toArray(new String[skipUrlList.size()]);
    }

    /**
     * <b>功能描述: </b> <br>
     * 跳过权限验证
     *
     * @param path 请求路径
     * @return boolean
     * @author shihao.li
     * @date 15:58 2020/8/3
     * @since 1.0.0
     **/
    public static boolean skipAuth(String path) {
        return StrUtil.containsAnyIgnoreCase(path, skipUrlArr);
    }
}
