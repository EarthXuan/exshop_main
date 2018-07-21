package com.example.exshop_main.service;


import com.example.exshop_main.common.ServerResponse;
import com.example.exshop_main.entity.User;

public interface IUserService{
    ServerResponse<User> login(String username, String password);
    ServerResponse<String> register(User user);
    ServerResponse<String> checkValid(String str, String type);
    ServerResponse<String> selectQuestion(String username);
    ServerResponse<String> checkAnswer(String username, String question, String answer);
    ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken);
    ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user);
    ServerResponse<User> updateInformation(User user);
    ServerResponse<User> getInformation(Integer userId);
    /**校验手机号是否存在*/
    User findByUserNameOrPhone(String phone);
}
