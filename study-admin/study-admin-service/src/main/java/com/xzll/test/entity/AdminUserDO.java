package com.xzll.test.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xzll.common.base.BaseBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/8/7 17:13
 * @Description:
 */
@Data
@TableName("u_admin_user")
public class AdminUserDO extends BaseBean {

	private String username;
	private String password;
	private String fullname;
	private Integer sex;
}
