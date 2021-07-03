package com.crm.service;

import com.bjpowernode.crm.settings.domain.TblUser;
import com.bjpowernode.crm.settings.mapper.UserMapper;
import com.bjpowernode.crm.settings.service.UserService;
import com.crm.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 王琦
 * 2021/6/4
 */
public class UserServiceTest  extends BaseTest {

    @Autowired
    private UserService userService;



    @Test
    public void testSelectUserById(){
        TblUser user =  userService.selectUserById("06f5fc056eac41558a964f96daa7f27c");
        System.out.println(user.getName());

    }

}
