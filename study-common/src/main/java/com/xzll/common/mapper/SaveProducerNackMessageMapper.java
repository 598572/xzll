package com.xzll.common.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xzll.common.rabbitmq.eneity.NackProducerMessageDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SaveProducerNackMessageMapper extends BaseMapper<NackProducerMessageDO> {

}
