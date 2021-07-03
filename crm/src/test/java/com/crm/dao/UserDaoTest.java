package com.crm.dao;

import com.bjpowernode.crm.settings.domain.TblUser;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.mapper.TblUserMapper;
import com.bjpowernode.crm.settings.mapper.UserMapper;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.mapper.ActivityMapper;
import com.crm.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 王琦
 * 2021/6/4
 */
public class UserDaoTest extends BaseTest {

    @Autowired
    private TblUserMapper tblUserMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelectUserById(){
       TblUser tblUser = tblUserMapper.selectByPrimaryKey("06f5fc056eac41558a964f96daa7f27c");
        System.out.println(tblUser.getName());
    }


    @Test
    public void selectUserByLoginActtAndPwd(){
        HashMap map = new HashMap();
        map.put("loginAct", "ls");
        map.put("loginPwd", "44ba5ca65651b4f36f1927576dd35436");
        User user = userMapper.selectUserByLoginActtAndPwd(map);
        System.out.println(user.getName());

    }

    @Test
    public void selectAllUsers(){
        List<User> userList= userMapper.selectAllUsers();
        System.out.println(userList.size());


    }
    @Test
    public void abcde(){
        Map map = new HashMap();

        map.put("beginNo",0);
        map.put("pageSize",10);
        activityMapper.selectActivityForPageByCondition(map);
    }

    @Test
    public void testSearchActivityNoBoundById(){
        HashMap map = new HashMap();
        map.put("clueId", "b41229673949f7a8196215332aaa70");
        map.put("activityName", "1");

        List<Activity> activityList = activityMapper.searchActivityNoBoundById(map);
        System.out.println(activityList.size());
    }
}
