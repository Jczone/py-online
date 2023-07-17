package com.wxj.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.wxj.util.TimeFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Experiment {

    // 实验id
    @TableId
    private Integer experimentId;
    // 所属章节ID
    private Integer sectionId;
    // 标题
    private String title;
    // 实验内容
    private String description;
    // 运行结果文件
    private String result;
    // 分值
    private Integer score;
    // 创建时间
    private Date createTime;
    // 格式化时间
    public String getFormatDate(){
        return TimeFormat.formatTimeToYMDHMS(createTime);
    }
    public String getShortDate(){
        return TimeFormat.formatTimeToYMD(createTime);
    }
}
