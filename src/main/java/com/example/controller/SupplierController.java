package com.example.controller;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.example.model.ImportInvoice;
import com.example.model.Supplier;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/supplier")
public class SupplierController {
	private RestTemplate rest=new RestTemplate();
	@GetMapping("/searchSupplier")
	private String get(@RequestParam(name="action")String action,HttpSession session) {
		session.setAttribute("action", action);
		return "searchSupplier";
	}
	@PostMapping("/searchSupplier")
	private String search(@RequestParam(name="key")String key,HttpSession session) {
		List<Supplier>list=Arrays.asList(rest.getForObject("https://kttkserver.azurewebsites.net/supplier/search?key="+key, Supplier[].class));
		session.setAttribute("listSupplier", list);
		return "searchSupplier";
	}
	@GetMapping("/addSupplier")
	private String getAdd(Model model) {
		model.addAttribute("supplier", new Supplier());
		return "addSupplier";
	}
	@PostMapping("/addSupplier")
	private String addSupplier(Supplier supplier,HttpSession session, Model model) {
		supplier=rest.postForObject("https://kttkserver.azurewebsites.net/supplier/add", supplier, Supplier.class);
		session.setAttribute("supplier", supplier);
		String action=(String) session.getAttribute("action");
		if(action.equals("quanli"))
		{
			if(supplier==null)
				model.addAttribute("result","fail");
			else
				model.addAttribute("result", "success");
			return "addSupplier";
		}
		ImportInvoice invoice=new ImportInvoice();
		invoice.setSupplier(supplier);
		long millis=System.currentTimeMillis();
		Date date=new Date(millis);
		invoice.setDateImport(date);
		session.setAttribute("invoice", invoice);
		return "importInvoice";
	}
	@GetMapping("/editSupplier")
	private String getEdit(@RequestParam(name="idncc")int id,HttpSession session,Model model)
	{
		List<Supplier>list=(List<Supplier>) session.getAttribute("listSupplier");
		for(Supplier s:list)
			if(s.getId()==id)
			{
				model.addAttribute("supplier", s);
				break;
			}
		model.addAttribute("result", "");
		return "editSupplier";
	}
	@PostMapping("/editSupplier")
	private String editSupplier(Supplier supplier,Model model,HttpSession session) {
		supplier=rest.postForObject("https://kttkserver.azurewebsites.net/supplier/add", supplier, Supplier.class);
		if(supplier==null)
			model.addAttribute("result","fail");
		else
			model.addAttribute("result", "success");
		return "editSupplier";
	}
	@GetMapping("/deleteSupplier")
	private String deleteSupplier(@RequestParam(name="idncc")int id, Model model, HttpSession session) {
		rest.delete("https://kttkserver.azurewebsites.net/supplier/delete?id="+id);
		model.addAttribute("result", "success");
		return "searchSupplier";
	}
}
