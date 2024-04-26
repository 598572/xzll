package com.xzll.admin.api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

	private Long id;


	private String username;

	private String password;

	private String fullname;

	private Integer sex;



	public static void main(String[] args) {

		LocalDateTime today_start = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN);//当天零点
		LocalDateTime end = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MAX);//当天零点

		System.out.println(today_start);
		System.out.println(end);

	}
}
