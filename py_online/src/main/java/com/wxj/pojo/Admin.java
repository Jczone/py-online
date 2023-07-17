package com.wxj.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    // 主键
    @TableId
    private String adminId;
    // 密码
    private String password;
    // 联系电话
    private String telephone;
}
