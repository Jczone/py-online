package com.wxj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wxj.pojo.ClassGroup;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper // 定义Bean
@Repository
public interface ClassGroupMapper extends BaseMapper<ClassGroup> {

}
