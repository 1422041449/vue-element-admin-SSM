package cn.jlw.firelearning.intercept;


import cn.jlw.firelearning.exception.LeException;
import cn.jlw.firelearning.model.constants.LeConstants;
import cn.jlw.firelearning.model.enums.LeRequestFiltURLHandle;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

@Slf4j
@Component
public class LeRequestFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("请求入参过滤");
        //获取请求信息
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        MyRequestWrapper req = new MyRequestWrapper(httpServletRequest);
        String path = req.getRequestURI();
        // 对于某些请求地址可以，无需验证路径，则直接向下执行
        if (LeRequestFiltURLHandle.skipAuth(path)) {
            chain.doFilter(req, response);
            return;
        }
        String method = req.getMethod();
        if ("GET".equals(method)) {
            // 获取所有参数
            Enumeration<String> enums = req.getParameterNames(); // 获取所有参数名的集合
            Map<String, String> map = new HashMap<>();
            while (enums.hasMoreElements()) {
                String paramName = enums.nextElement();             // 获取参数名
                String attribute = req.getParameter(paramName);     //获取参数值
                map.put(paramName, attribute);
            }
            String sign = map.get("sign");
            String version = map.get("version");
            String nonceStr = map.get("nonceStr");
            String timestamp = map.get("timestamp");
            String signType = map.get("signType");
            String content = map.get("content");
            boolean result = checkSign(sign, content, nonceStr, signType, timestamp, version);
            if (result) {
                chain.doFilter(req, response);
                return;
            }
        } else {
            String bodyString = req.getBody();
            LinkedHashMap<String, Object> bodyParams = JSON.parseObject(bodyString, LinkedHashMap.class, Feature.OrderedField);
            String sign = (String) bodyParams.get("sign");
            String version = (String) bodyParams.get("version");
            String nonceStr = (String) bodyParams.get("nonceStr");
            String timestamp = String.valueOf(bodyParams.get("timestamp"));
            String signType = (String) bodyParams.get("signType");
            String content = JSON.toJSONString(bodyParams.get("content"), SerializerFeature.WriteMapNullValue);
            boolean result = checkSign(sign, content, nonceStr, signType, timestamp, version);
            if (result) {
                chain.doFilter(req, response);
                return;
            }
        }
        throw new LeException("签名验证失败!");
    }

    /**
     * 验证签名
     *
     * @param sign      签名
     * @param content   参数
     * @param nonceStr  随机字符串
     * @param signType  加密类型
     * @param timestamp 时间戳
     * @param version   版本号
     * @return 成功失败
     */
    private static boolean checkSign(String sign, String content, String nonceStr,
                                     String signType, String timestamp, String version) {
        Map<String, String> paramMap = new TreeMap<>();
        paramMap.put("content", content);
        paramMap.put("nonceStr", nonceStr);
        paramMap.put("signType", signType);
        paramMap.put("timestamp", timestamp);
        paramMap.put("version", version);
        if (sign == null || sign.isEmpty()) {
            return false;
        }
        StringBuilder paramText = new StringBuilder();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            paramText.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        paramText = new StringBuilder(paramText.substring(0, paramText.length() - 1));
        paramText.append("&key=").append(LeConstants.md5Key);
        log.info("***加密前的拼接参数结果paramText : {}***************", paramText);
        String md5 = getMd5(paramText.toString()).toUpperCase();
        log.info("***请求参数加密之后的结果md5 : {}*******************", md5);
        return sign.equals(md5);
    }

    public static String getMd5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes(StandardCharsets.UTF_8));
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            String md5 = new BigInteger(1, md.digest()).toString(16);
            //BigInteger会把0省略掉，需补全至32位
            return fillMd5(md5);
        } catch (Exception e) {
            throw new RuntimeException("MD5加密错误:" + e.getMessage(), e);
        }
    }

    private static String fillMd5(String md5) {
        return md5.length() == 32 ? md5 : fillMd5("0" + md5);
    }
}
