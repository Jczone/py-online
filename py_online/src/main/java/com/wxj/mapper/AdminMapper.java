package com.wxj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wxj.pojo.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper // 定义Bean
@Repository
public interface AdminMapper extends BaseMapper<Admin> {

}