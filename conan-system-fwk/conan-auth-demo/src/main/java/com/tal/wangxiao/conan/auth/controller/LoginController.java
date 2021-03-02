package com.tal.wangxiao.conan.auth.controller;

import com.tal.wangxiao.conan.auth.domain.User;
import com.tal.wangxiao.conan.auth.service.JwtUserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/login")
public class LoginController {
	@Resource
	JwtUserService jwtUserService;
	

	@PostMapping("")
	public Object create(@RequestBody  User user) {
		return "xxxx";

	}



}
