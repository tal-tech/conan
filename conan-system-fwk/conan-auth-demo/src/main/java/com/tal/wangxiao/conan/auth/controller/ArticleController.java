package com.tal.wangxiao.conan.auth.controller;

import com.tal.wangxiao.conan.auth.service.JwtUserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("article")
public class ArticleController {
	@Resource
	JwtUserService jwtUserService;
	
	@GetMapping("{id}")
	public String load(@PathVariable Long id) {
		return "This is my first blog";
	}
	
	@PostMapping("add")
	public void create(@AuthenticationPrincipal UserDetails user) {
		
	}

	@GetMapping("passwordEncoder")
	public void getToken(@AuthenticationPrincipal UserDetails user) {
		jwtUserService.getUserLoginInfo("");
	}

	@GetMapping("")
	public String load() {
		return "This is my first blog";
	}

}
