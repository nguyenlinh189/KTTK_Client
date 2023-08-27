package com.example.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.example.model.SupplierStat;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/stat")
public class SupplierStatController {
	private RestTemplate rest=new RestTemplate();
	@GetMapping
	private String getStat() {
		return "supplierStat";
	}
	@PostMapping
	private String stat(@RequestParam(name="dateStart")Date dateStart, @RequestParam(name="dateEnd") Date dateEnd, HttpSession session) {
		List<SupplierStat>list=new ArrayList<>();
		list=Arrays.asList(rest.getForObject("https://kttkserver.azurewebsites.net/stat?dateStart="+dateStart+"&dateEnd="+dateEnd, SupplierStat[].class));
		session.setAttribute("listStat", list);
		session.setAttribute("dateStart", dateStart);
		session.setAttribute("dateEnd", dateEnd);
		return "supplierStat";
	}
}
