package com.czj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    //用户ID
    private String userId;
    //用户名称
    private String username;
    //密码
    private String password;
    //自定义问题1
    private String question1;
    //自定义问题2
    private String question2;
    //答案1
    private String answer1;
    //答案2
    private String answer2;
    //头像路径
    private String avatarUrl;
    //身份标识
    private Integer identity;
    // 插入数据构造
    public User(String id, String psw, String question1,String question2,String answer1,String answer2,Integer identity){
        userId = id;
        password = psw;
        this.question1 = question1;
        this.question2 = question2;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.identity = identity;
    }
    /**
     * 获取iderntiy对应布尔类型 0为老师 1为学生
     * @return bool类型
     */
    public boolean getIdentityBool(){
        return identity == 1;
    }
}
