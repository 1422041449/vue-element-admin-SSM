package cn.jlw.firelearning.service.impl;

import cn.jlw.firelearning.entity.UserInfo;
import cn.jlw.firelearning.service.TokenService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {
    @Override
    public String getToken(UserInfo userInfo) {
        String token = "";
        token = JWT.create().withAudience(String.valueOf(userInfo.getId())).sign(Algorithm.HMAC256(userInfo.getPassword()));
        return token;
    }
}
