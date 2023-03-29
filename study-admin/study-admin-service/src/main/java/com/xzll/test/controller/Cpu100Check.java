package com.xzll.test.controller;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: hzz
 * @Date: 2021/9/29 17:52:46
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/cpu")
public class Cpu100Check {

	@GetMapping("/check")
	public List<Long> findByUserName(@RequestParam(value = "username", required = true) String username) {
		List<Long> add = Lists.newArrayList();
		long i=0;
		while (true){
			i=i++;
			add.add(i);
			if (i>=Long.MAX_VALUE-1){
				break;
			}
		}

		return add;
	}

	public static void main(String[] args) {
		int [] i={6000,2400,500,500,1000,500,2000,1000,2000,2000};


	}

}
