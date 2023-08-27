package com.example.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.example.model.Ingredient;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/ingredient")
public class IngredientController {
	private RestTemplate rest=new RestTemplate();
	
	@GetMapping("/search")
	private String getSearch() {
		return "searchIngre";
	}
	@PostMapping("/search")
	private String search(@RequestParam(name="key")String key,HttpSession session) {
		List<Ingredient>list=new ArrayList<>();
		list=Arrays.asList(rest.getForObject("https://kttkserver.azurewebsites.net/ingredient/search?key="+key, Ingredient[].class));
		session.setAttribute("listIngre", list);
		return "importInvoice";
	}
	@GetMapping("/add")
	private String getAdd(Model model) {
		model.addAttribute("ingredient", new Ingredient());
		return "addIngre";
	}
	@PostMapping("/add")
	private String add(Ingredient ingredient, HttpSession session) {
		ingredient=rest.postForObject("https://kttkserver.azurewebsites.net/ingredient/add", ingredient, Ingredient.class);
		List<Ingredient>listIngre=new ArrayList<>();
		listIngre.add(ingredient);
		session.setAttribute("listIngre", listIngre);
		return "importInvoice";
	}
}
