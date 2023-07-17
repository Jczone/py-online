package com.czj.pojo.view;
import com.czj.pojo.Experiment;
import com.czj.pojo.Section;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class SectionSuperSet extends Section{

    // 章节对应的实验列表
    private List<Experiment> experiments;

    public SectionSuperSet(Integer sectionId, Integer classId, String title, Integer expNum, Date createTime){
        super(sectionId,classId,title,expNum,createTime);
    }
}
