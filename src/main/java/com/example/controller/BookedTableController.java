package com.example.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.example.model.BookedTable;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/booked")
public class BookedTableController {
	private RestTemplate rest=new RestTemplate();
	@GetMapping
	private String get() {
		return "listUnpaidBooking";
	}
	@PostMapping("/searchBookedTableByName")
	private String getListUnpaid(@RequestParam(name="key") String key, HttpSession session){
		List<BookedTable>list=new ArrayList<>();
		list=Arrays.asList(rest.getForObject("https://kttkserver.azurewebsites.net/booked/getListBookedTableUnpaid?key="+key, BookedTable[].class));
		session.setAttribute("listUnpaid", list);
		return "listUnpaidBooking";
	}
}
