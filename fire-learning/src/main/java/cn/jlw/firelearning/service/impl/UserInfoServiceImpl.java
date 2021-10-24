package cn.jlw.firelearning.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.jlw.firelearning.entity.UserInfo;
import cn.jlw.firelearning.exception.LeException;
import cn.jlw.firelearning.mapper.UserInfoMapper;
import cn.jlw.firelearning.model.LeResponse;
import cn.jlw.firelearning.model.dto.UserInfoLoginDTO;
import cn.jlw.firelearning.service.TokenService;
import cn.jlw.firelearning.service.UserInfoService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author jlw
 * @since 2021-10-23
 */
@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    private final TokenService tokenService;

    @Override
    public LeResponse<?> login(UserInfoLoginDTO content) {
        UserInfo userInfo = baseMapper.selectOne(Wrappers.lambdaQuery(UserInfo.class)
                .eq(UserInfo::getUsername, content.getUsername())
                .eq(UserInfo::getPassword, content.getPassword()));
        if (ObjectUtil.isEmpty(userInfo)) {
            return LeResponse.fail("用户名或密码错误!");
        }
        String token = tokenService.getToken(userInfo);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token);
        return LeResponse.succ(jsonObject);
    }

    @Override
    public LeResponse<UserInfo> getUserInfo(String token) {
        //获取token中的id
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new LeException("token获取用户失败!");
        }
        UserInfo userInfo = baseMapper.selectOne(Wrappers.lambdaQuery(UserInfo.class).eq(UserInfo::getId, userId));
        if (ObjectUtil.isEmpty(userInfo)) {
            return LeResponse.fail("用户不存在!");
        }
        return LeResponse.succ(userInfo);
    }
}
