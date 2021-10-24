package cn.jlw.firelearning.controller;


import cn.jlw.firelearning.entity.UserInfo;
import cn.jlw.firelearning.model.LeRequest;
import cn.jlw.firelearning.model.LeResponse;
import cn.jlw.firelearning.model.dto.UserInfoLoginDTO;
import cn.jlw.firelearning.model.interfaces.PassToken;
import cn.jlw.firelearning.service.UserInfoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author jlw
 * @since 2021-10-23
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/userinfo")
public class UserInfoController {
    private final UserInfoService userInfoService;

    /**
     * 登录接口
     *
     * @param leRequest
     * @return
     */
    @PassToken
    @PostMapping("/login")
    public LeResponse<?> login(@Valid @RequestBody LeRequest<UserInfoLoginDTO> leRequest) {
        log.info("登录请求入参:{}", leRequest);
        UserInfoLoginDTO content = leRequest.getContent();
        return userInfoService.login(content);
    }

    /**
     * 根据token获取当前登录用户信息
     */
    @GetMapping("/getInfo")
    public LeResponse<UserInfo> getUserInfo(@RequestHeader String token, LeRequest<?> leRequest) {
        log.info("请求头token值：{}", token);
        return userInfoService.getUserInfo(token);
    }

    /**
     * 退出登录
     */
    @GetMapping("/logout")
    public LeResponse<?> logout() {
        return LeResponse.succ();
    }

    /**
     * 验证token测试
     */
    @PostMapping("/test/token")
    public LeResponse<?> testToken() {
        log.info("测试携带token登录");
        return LeResponse.succ();
    }

}
