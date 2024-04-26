package com.xzll.test.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xzll.test.entity.AdminUserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminUserMapper extends BaseMapper<AdminUserDO> {

	List<AdminUserDO> selectByUserName(@Param("username") String username);

	void batchInsertUser(@Param("users") List<AdminUserDO> users);

	void batchUpdateUser(@Param("users") List<AdminUserDO> users);

}
