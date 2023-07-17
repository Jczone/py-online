package com.czj.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.czj.util.TimeFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassGroup {
    // 主键
    @TableId
    private Integer classId;
    // 课程名称
    private String className;
    // 创建用户id
    private String userId;
    // 是否开启
    private Integer isStart;
    // 是否公开
    private Integer isPublic;
    // 班级描述
    private String describes;
    // 学生数
    private Integer stuNum;
    // 创建时间
    private Date createTime;
    // 格式化时间
    public String getFormatDate(){
        if(null == createTime) createTime = new Date();
        return TimeFormat.formatTimeToYMD(createTime);
    }
    // 格式化时间
    public String getFormatDateShort(){
        if(null == createTime) createTime = new Date();
        return TimeFormat.formatTimeToYMD(createTime);
    }
    // 是否公开（Boolean）
    public Boolean getIsPublicBool(){
        return isPublic == 1;
    }
    // 是否开启（Boolean）
    public Boolean getIsStartBool(){
        return isStart == 1;
    }
    // 是否公开（String）
    public String getIsPublicStr(){
        return isPublic == 1? "公开" : "私有";
    }
    // 是否开启（String）
    public String getIsStartStr(){
        return isStart == 1? "开启" : "关闭";
    }


}
