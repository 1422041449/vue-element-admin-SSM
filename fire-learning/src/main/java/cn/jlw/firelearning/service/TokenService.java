package cn.jlw.firelearning.service;

import cn.jlw.firelearning.entity.UserInfo;

public interface TokenService {

    /**
     * 获取token
     * @param userInfo
     * @return
     */
    public String getToken(UserInfo userInfo);
}
