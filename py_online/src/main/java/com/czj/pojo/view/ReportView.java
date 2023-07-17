package com.czj.pojo.view;

import com.czj.pojo.Report;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReportView extends Report {

    // 实验标题
    private String title;
    // 报告人姓名
    private String username;
    // 头像路径
    private String avatarUrl;

}
