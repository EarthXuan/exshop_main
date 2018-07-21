package com.example.exshop_main.service.impl;


import com.example.exshop_main.common.Constant;
import com.example.exshop_main.common.ServerResponse;
import com.example.exshop_main.common.TokenCache;
import com.example.exshop_main.dao.UserMapper;
import com.example.exshop_main.entity.User;
import com.example.exshop_main.service.IUserService;
import com.example.exshop_main.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("iUserService")
public class UserServiceImpl implements IUserService {

   @Autowired
    private UserMapper userMapper;

    public ServerResponse<User> login(String username, String password) {
        Boolean verify=false;
        int resultCount=userMapper.checkUsername(username);
        if(resultCount==0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        User user=userMapper.selectLoginUser(username);
        if(user == null){
            return ServerResponse.createByErrorMessage("账号为空");
        }
        try{
            verify= MD5Util.verify(password,user.getPassword());
        }catch (Exception e){
            return ServerResponse.createByErrorMessage("密码错误");
        }
        if(verify==false){
            return ServerResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccessCodeMessage("登陆成功",user);
    }

    public ServerResponse<String> register(User user){
        //调用校验接口
        ServerResponse<String> validResponse=this.checkValid(user.getUsername(),Constant.USERNAME);
        if(!validResponse.isSuccess()) {
            return validResponse;
        }
        validResponse=this.checkValid(user.getEmail(), Constant.EMAIL);
        if(!validResponse.isSuccess()) {
            return validResponse;
        }
        user.setRole(Constant.Role.ROLE_CUSTOMER);
        //MD5加盐加密
        user.setPassword(MD5Util.generate(user.getPassword()));
        //存入数据库
        int resultCount=userMapper.insert(user);
        if(resultCount==0){
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    public ServerResponse<String> checkValid(String str, String type){
        if(StringUtils.isNotBlank(type)) {
            if(Constant.USERNAME.equals(type)){
                int resultCount=userMapper.checkUsername(str);
                if(resultCount>0) {
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }else if(Constant.EMAIL.equals(type)){
                int resultCount=userMapper.checkEmail(str);
                if(resultCount>0){
                    return ServerResponse.createByErrorMessage("邮件已存在");
                }
            }
        }else{
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    public ServerResponse<String> selectQuestion(String username){
        ServerResponse<String> validResponse=this.checkValid(username,Constant.USERNAME);
        if(validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question=userMapper.selectQuestionByUsername(username);
        if(StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题是空的");
    }

    public ServerResponse<String> checkAnswer(String username, String question, String answer){
        int resultCount=userMapper.checkAnswer(username,question,answer);
        if(resultCount>0) {
            String forgetToken= UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题答案错误");
    }


    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken){
        if(StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessage("参数错误，token需要传递");
        }
        ServerResponse<String> validResponse=this.checkValid(username,Constant.USERNAME);
        if(validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String token=TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token无效或过期");
        }
        if(StringUtils.equals(token,forgetToken)){
            if(StringUtils.isBlank(passwordNew)){
                return ServerResponse.createByErrorMessage("密码不能为空");
            }
            String md5Password=MD5Util.generate(passwordNew);
            int rowCount=userMapper.updatePasswordByUsername(username,md5Password);
            if(rowCount>0)
            {
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }else{
                return ServerResponse.createByErrorMessage("修改密码失败");
            }
        }else{
            return ServerResponse.createByErrorMessage("token错误，请重新获取重置密码的token");
        }
    }

    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user){
       int resultCount=userMapper.checkPassword(MD5Util.generate(passwordOld),user.getId());
        if(resultCount==0){
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(passwordNew);
        int updateCount=userMapper.updateByPrimaryKeySelective(user);
        if(updateCount>0){
            return ServerResponse.createBySuccessMessage("更新密码成功");
        }
        return ServerResponse.createByErrorMessage("更新密码失败");
    }

    public ServerResponse<User> updateInformation(User user){
        int resultCount=userMapper.checkEmailByUserId(user.getId(),user.getEmail());
        if(resultCount>0){
            return ServerResponse.createByErrorMessage("该email已被使用");
        }
        User updateUser=new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        int updateResult=userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateResult>0)
        {
            return ServerResponse.createBySuccessCodeMessage("更新个人信息成功",updateUser);
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败");
    }

    public ServerResponse<User> getInformation(Integer userid){
        User user=userMapper.selectByPrimaryKey(userid);
        if(user==null){
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword("");
        return ServerResponse.createBySuccess(user);
    }

    @Override
    public User findByUserNameOrPhone(String phone) {
        return userMapper.findByUserNameOrPhone(phone);
    }

}
