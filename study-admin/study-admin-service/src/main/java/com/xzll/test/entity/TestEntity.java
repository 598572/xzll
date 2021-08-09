package com.xzll.test.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xzll.common.base.BaseBean;
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
@TableName("test")
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TestEntity extends BaseBean {

	private String name;
	private Integer sex;

}
