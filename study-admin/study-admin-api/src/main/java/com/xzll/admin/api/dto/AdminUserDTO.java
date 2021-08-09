package com.xzll.admin.api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/8/7 17:13
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class AdminUserDTO {

	private String username;
	private String password;
	private String fullname;
	private Integer sex;
}
