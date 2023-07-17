package com.czj.pojo.view;

import com.czj.pojo.Notice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NoticeView extends Notice {
    // 发布教师姓名
    private String username;

}
