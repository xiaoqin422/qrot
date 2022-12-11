package cn.stuxx.service;

import cn.stuxx.model.eum.PERMISSION;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Test
    public void userServiceTest_userHasPermission(){
        System.out.println(userService.userHasPermission(PERMISSION.ROOT.getCODE(),1L));
        System.out.println(userService.selectHasPermissionsUser(PERMISSION.ROOT.getCODE()));

    }
}
