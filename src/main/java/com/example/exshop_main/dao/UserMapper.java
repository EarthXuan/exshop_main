package com.example.exshop_main.dao;

import com.example.exshop_main.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(String username);

    int checkEmail(String email);

    int checkEmailByUserId(@Param("userid")Integer userid, @Param("email")String email);

    int checkAnswer(@Param("username") String username,@Param("question") String question,@Param("answer") String answer);

    User selectLoginUser(@Param("username")String username);

    String selectQuestionByUsername(String username);


    int updatePasswordByUsername(@Param("username") String username,@Param("passwordNew") String passwordNew);

    int checkPassword(@Param("password") String password,@Param("userid") Integer userid);
    /**校验手机号是否存在*/
    User findByUserNameOrPhone(String phone);
}