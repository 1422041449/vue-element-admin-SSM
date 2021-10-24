package cn.jlw.firelearning.aop;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
@Aspect
public class LogAop {

    /**
     * 配置切入点,该方法无方法体,主要为方便同类中其他方法使用此处配置的切入点
     */
    @Pointcut("execution(* cn.jlw.firelearning.controller.*.*(..))")
    public void aspect() {

    }

    /**
     * 前置增强，在方法执行前会输出该方法的所在类名、方法名、请求参数
     *
     * @param joinPoint
     */
    @Before(value = "aspect()")
    public void before(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String className = joinPoint.getTarget().getClass().getName();
        String method = joinPoint.getSignature().getName() + "()";
        String decode = request.getQueryString();
        if (null == decode) {
            try {
                decode = JSON.toJSONString(joinPoint.getArgs());
            } catch (Exception e) {
                e.printStackTrace();
                log.info(e.getMessage());
                log.info("输入参数不能转换为json字符串!");
            }
        }
        log.info("类 : {}, 方法 : {}, 入参 : {}", className, method, decode);
    }

    /**
     * 返回增强，在方法返回时执行，输出方法的响应报文
     *
     * @param joinPoint
     * @param output
     */
    @AfterReturning(returning = "output", value = "aspect()")
    public void after(JoinPoint joinPoint, Object output) {
        if (ObjectUtil.isNotEmpty(output)) {
            log.info("返回参数 : {}", output.toString());
        }
    }
}
