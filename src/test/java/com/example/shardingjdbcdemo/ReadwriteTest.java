package com.example.shardingjdbcdemo;

import com.example.shardingjdbcdemo.entity.User;
import com.example.shardingjdbcdemo.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ReadwriteTest {

    @Autowired
    private UserMapper userMapper;

    /**
     * 写入数据的测试
     */
    @Test
    public void testInsert(){

        User user = new User();
        user.setUname("张三丰");
        userMapper.insert(user);
    }

    /**
     * 这个案例 展示的就是不添加@Transactional：insert对主库操作，select对从库操作，所以users就查询不出数据铁锤2
     */
    @Test
    public void testTrans(){
        User user = new User();
        user.setUname("铁锤2");
        userMapper.insert(user);

        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }

    /**
     * 读数据测试
     */
    @Test
    public void testSelectAll(){
        //第一次会使用读写分离的负载均衡算法，第一次会从slave1读取
        List<User> users = userMapper.selectList(null);
        //第二次会使用读写分离的负载均衡算法，第二次会从slave2读取
        users = userMapper.selectList(null);
        users = userMapper.selectList(null);
        users = userMapper.selectList(null);
        users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }

}