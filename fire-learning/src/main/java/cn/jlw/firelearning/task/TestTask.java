package cn.jlw.firelearning.task;

import cn.jlw.firelearning.entity.UserInfo;
import cn.jlw.firelearning.mapper.UserInfoMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 测试定时任务
 */
@Component
@RequiredArgsConstructor
public class TestTask {
    private final UserInfoMapper userInfoMapper;

//    @Scheduled(cron = "0/1 * * * * ?")
    public void test(){
        List<UserInfo> userInfos = userInfoMapper.selectList(Wrappers.lambdaQuery(UserInfo.class));
        System.out.println(userInfos);
    }
}
