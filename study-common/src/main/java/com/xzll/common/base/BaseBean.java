package com.xzll.common.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;


import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/8/7 17:23
 * @Description: 基础bean 所有表均需要有该类中的字段 (强制)
 */
public class BaseBean implements Serializable {

	private static final long serialVersionUID = 9009420433780268361L;

	@ApiModelProperty("id")
	@TableId(
			type = IdType.AUTO
	)
	private Long id;
	@ApiModelProperty(
			value = "创建时间",
			hidden = true
	)
	@JsonFormat(
			pattern = "yyyy-MM-dd HH:mm:ss",
			timezone = "GMT+8"
	)
	private Date ctime = null;
	@ApiModelProperty("创建人id")
	private Integer cuserId;

	@ApiModelProperty(
			value = "修改时间名称",
			hidden = true
	)
	@JsonFormat(
			pattern = "yyyy-MM-dd HH:mm:ss",
			timezone = "GMT+8"
	)
	private Date utime = null;
	@ApiModelProperty("修改人id")
	private Integer uuserId;

	@ApiModelProperty(
			value = "1 有效  0 无效",
			example = "1"
	)
	@TableLogic(
			value = "1",
			delval = "0"
	)
	private Integer enabled = 1;

	public BaseBean() {
	}

	@Override
	public String toString() {
		return "BaseBean{" +
				"id=" + id +
				", ctime=" + ctime +
				", cuserId=" + cuserId +
				", utime=" + utime +
				", uuserId=" + uuserId +
				", enabled=" + enabled +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BaseBean baseBean = (BaseBean) o;
		return Objects.equals(id, baseBean.id) && Objects.equals(ctime, baseBean.ctime) && Objects.equals(cuserId, baseBean.cuserId) && Objects.equals(utime, baseBean.utime) && Objects.equals(uuserId, baseBean.uuserId) && Objects.equals(enabled, baseBean.enabled);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, ctime, cuserId, utime, uuserId, enabled);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Integer getCuserId() {
		return cuserId;
	}

	public void setCuserId(Integer cuserId) {
		this.cuserId = cuserId;
	}

	public Date getUtime() {
		return utime;
	}

	public void setUtime(Date utime) {
		this.utime = utime;
	}

	public Integer getUuserId() {
		return uuserId;
	}

	public void setUuserId(Integer uuserId) {
		this.uuserId = uuserId;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public BaseBean(Long id, Date ctime, Integer cuserId, Date utime, Integer uuserId, Integer enabled) {
		this.id = id;
		this.ctime = ctime;
		this.cuserId = cuserId;
		this.utime = utime;
		this.uuserId = uuserId;
		this.enabled = enabled;
	}
}
