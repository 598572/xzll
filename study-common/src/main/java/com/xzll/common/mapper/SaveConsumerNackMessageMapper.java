package com.xzll.common.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xzll.common.rabbitmq.eneity.NackConsumerMessageDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SaveConsumerNackMessageMapper extends BaseMapper<NackConsumerMessageDO> {

}
