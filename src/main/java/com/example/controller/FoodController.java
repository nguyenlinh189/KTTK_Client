package com.example.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.example.model.Food;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/food")
public class FoodController {
	private RestTemplate rest=new RestTemplate();
	@GetMapping("/search")
	private String getSearch() {
		return "searchFood";
	}
	@PostMapping("/search")
	private String searchFood(@RequestParam(name="key")String key, HttpSession session,Model model) {
		List<Food>list=Arrays.asList(rest.getForObject("https://kttkserver.azurewebsites.net/food/search?key="+key, Food[].class));
		session.setAttribute("listFood", list);
		if(list.isEmpty())
			model.addAttribute("result", "notfound");
		return "searchFood";
	}
}
