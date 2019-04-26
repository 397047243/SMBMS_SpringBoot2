package cn.xrz.service.impl;


import cn.xrz.dao.UserMapper;
import cn.xrz.pojo.User;
import cn.xrz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional //启用SpringBoot事务管理 该类下面的所有方法都会支持事务 （也可作用在方法上）
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    
    @Override
    public boolean add(User user) {
            return this.userMapper.add(user) > 0;
    }
    @Override
    public User login(String userCode, String userPassword){
        User user = this.userMapper.getLoginUser(userCode);
        if(null != user){
            if(!user.getUserPassword().equals(userPassword))
                user = null;
        }
        return user;
    }
    @Override
    public List<User> getUserList(String queryUserName,Integer queryUserRole){
        return this.userMapper.getUserList(queryUserName,queryUserRole);
    }
    @Override
    public User selectUserCodeExist(String userCode){
        return this.userMapper.getLoginUser(userCode);
    }
    @Override
    public boolean deleteUserById(Integer delId){
        return this.userMapper.deleteUserById(delId) > 0;
    }
    @Override
    public User getUserById(String id){
        return this.userMapper.getUserById(id);
    }
    @Override
    public boolean modify(User user){
        return  this.userMapper.modify(user) > 0;
    }
    @Override
    public boolean updatePwd(Integer id, String pwd){
        return  this.userMapper.updatePwd(id,pwd) > 0;
    }
    @Override
    public int getUserCount(String queryUserName, Integer queryUserRole){
        return this.userMapper.getUserCount(queryUserName,queryUserRole);
    }

}
