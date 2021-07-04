package com.xzll.resourceserver.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 示例 Controller 进行角色校验 没细分权限
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @GetMapping("/admin-list")
    @PreAuthorize("hasRole('ADMIN')") // 要求管理员 ROLE_ADMIN 角色 很熟悉吧 之前用过 记得不 2019年左右
    public String adminList() {
        return "管理员列表";
    }

    @GetMapping("/user-list")
    @PreAuthorize("hasRole('USER')") // 要求普通用户 ROLE_USER 角色 很熟悉吧 之前用过 记得不 2019年左右 哈哈
    public String userList() {
        return "用户列表";
    }

}
