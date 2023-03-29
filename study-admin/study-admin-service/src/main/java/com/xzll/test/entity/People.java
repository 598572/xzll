package com.xzll.test.entity;

import lombok.ToString;

/**
 * @Author: hzz
 * @Date: 2021/12/12 16:38:42
 * @Description:
 */
//@Setter
//@Getter
@ToString
public class People {

	private int age;
	private int sex;
	private String name;
	private String address;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
