package com.wxj.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassUser {
    // ID主键
    @TableId
    private Integer id;
    // 对应班级ID
    private Integer classId;
    // 对应学生ID
    private String userId;
    // 实验提交数
    private Integer expSubmitNum;
    // 创建时间
    private Date createTime;
    // 格式化时间
    public String getFormatDate(){
        if(null == createTime) createTime = new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(createTime);
    }
    // 格式化时间
    public String getFormatDateShort(){
        if(null == createTime) createTime = new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(createTime);
    }
}
