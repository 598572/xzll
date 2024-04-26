package com.xzll.test.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xzll.agent.config.advice.DecryptTransaction;
import com.xzll.agent.config.advice.EncryptTransaction;
import com.xzll.agent.config.advice.SensitiveData;
import com.xzll.common.base.BaseBean;
import lombok.Data;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/8/7 17:13
 * @Description:
 */
@Data
@TableName("u_admin_user")
@SensitiveData
public class AdminUserDO extends BaseBean {


	private String username;

	@EncryptTransaction
	@DecryptTransaction
	private String password;


	private String fullname;

	private Integer sex;
}
