package cn.jlw.firelearning.intercept;

import cn.hutool.core.util.ObjectUtil;
import cn.jlw.firelearning.entity.UserInfo;
import cn.jlw.firelearning.exception.LeException;
import cn.jlw.firelearning.mapper.UserInfoMapper;
import cn.jlw.firelearning.model.interfaces.PassToken;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Data
@AllArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    UserInfoMapper userInfoMapper;

    public AuthenticationInterceptor() {

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        //如果不是映射到方法，直接跳过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //检查是否有passToken注解,有则跳过
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //其他接口都要经过token验证
        if (token == null) {
            throw new LeException("无token,请重新登录!");
        }
        //获取token中的id
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new LeException("401,token验证失败!");
        }
        UserInfo userInfo = userInfoMapper.selectOne(Wrappers.lambdaQuery(UserInfo.class).eq(UserInfo::getId, userId));
        if (ObjectUtil.isEmpty(userInfo)) {
            throw new LeException("用户不存在，请重新登录!");
        }
        //验证token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(userInfo.getPassword())).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new LeException("401,token验证失败!");
        }
        return true;
    }
}
