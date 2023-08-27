package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.example.model.User;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping
public class UserController {
	private RestTemplate rest=new RestTemplate();
	@GetMapping("/")
	private String getLogin(HttpSession session) {
		User user=(User)session.getAttribute("user");
		if(user==null)
			return "login";
		session.setAttribute("listSupplier", null);
		session.setAttribute("invoice", null);
		session.setAttribute("listIngre", null);
		session.setAttribute("listStat", null);
		return "manageStaff";
	}
	@PostMapping("/checklogin")
	private String checkLogin(@RequestParam(name="username")String username, @RequestParam(name="password")String password,HttpSession session) {
		User user=new User(username, password);
		user=rest.postForObject("https://kttkserver.azurewebsites.net/user/checklogin", user, User.class);
		if(user==null)
		{
			session.setAttribute("err", "sai thong tin dang nhap");
			return "login";
		}
		session.setAttribute("user", user);
		if(user.getRole().equalsIgnoreCase("staff")) {
			session.setAttribute("listSupplier", null);
			session.setAttribute("invoice", null);
			return "manageStaff";
		}
		return "manageCustomer";
	}
	@GetMapping("/logout")
	private String logout(HttpSession session) {
		session.setAttribute("user", null);
		return "login";
	}
}
