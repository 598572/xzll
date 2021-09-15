package com.xzll.test.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xzll.common.base.BaseBean;
import lombok.Data;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/8/7 17:13
 * @Description:
 */
@Data
@TableName("table_a")
public class TableADO extends BaseBean {

	private String aDescription;
}
