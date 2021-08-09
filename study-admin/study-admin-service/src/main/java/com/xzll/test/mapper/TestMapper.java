package com.xzll.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xzll.test.entity.TestEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestMapper extends BaseMapper<TestEntity> {

}
