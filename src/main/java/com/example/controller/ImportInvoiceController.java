package com.example.controller;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.example.model.DetailInvoice;
import com.example.model.ImportInvoice;
import com.example.model.Ingredient;
import com.example.model.Supplier;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/importInvoice")
public class ImportInvoiceController {
	private RestTemplate rest=new RestTemplate();
	@GetMapping
	private String get(@RequestParam(name="idncc")int id, HttpSession session) {
		List<Supplier>list=(List<Supplier>) session.getAttribute("listSupplier");
		Supplier sup=new Supplier();
		for(Supplier s:list) {
			if(s.getId()==id)
			{
				sup=s;
				session.setAttribute("supplier", s);
				break;
			}
		}
		ImportInvoice invoice=new ImportInvoice();
		invoice.setSupplier(sup);
		long millis=System.currentTimeMillis();
		Date date=new Date(millis);
		invoice.setDateImport(date);
		session.setAttribute("invoice", invoice);
		return "importInvoice";
	}
	@GetMapping("/addDetail")
	private String addDetail(@RequestParam(name="chon")int chon, @RequestParam(name="unitPrice")float price, @RequestParam(name="quantity")float quantity, HttpSession session) {
		ImportInvoice invoice=(ImportInvoice) session.getAttribute("invoice");
		int mark=0;
		for(int i=0;i<invoice.getListDetailInvoice().size();i++)
		{
			if(invoice.getListDetailInvoice().get(i).getIngredient().getId()==chon) {
				mark=1;
				DetailInvoice d=invoice.getListDetailInvoice().get(i);
				d.setQuantity(d.getQuantity()+quantity);
				d.setUnitPrice(price);
				d.setTotalMoney(d.getQuantity()*d.getUnitPrice());
				invoice.getListDetailInvoice().set(i, d);
				break;
			}
		}
		if(mark==0)
		{
			DetailInvoice detail=new DetailInvoice();
			List<Ingredient>listIngre=(List<Ingredient>) session.getAttribute("listIngre");
			Ingredient ingre=new Ingredient();
			for(Ingredient i:listIngre) {
				if(i.getId()==chon)
				{
					ingre=i;
					break;
				}
			}
			detail.setIngredient(ingre);
			detail.setQuantity(quantity);
			detail.setUnitPrice(price);
			detail.setTotalMoney(quantity*price);
			invoice.getListDetailInvoice().add(detail);
		}
		float totalMoney=0, totalQuantity=0;
		for(DetailInvoice d:invoice.getListDetailInvoice())
		{
			totalMoney=totalMoney+d.getTotalMoney();
			totalQuantity=totalQuantity+d.getQuantity();
		}
		invoice.setTotalMoney(totalMoney);
		invoice.setTotalQuantity(totalQuantity);
		session.setAttribute("invoice", invoice);
		return "importInvoice";
	}
	@GetMapping("/save")
	private String save(HttpSession session, Model model) {
		ImportInvoice invoice=(ImportInvoice) session.getAttribute("invoice");
		invoice=rest.postForObject("https://kttkserver.azurewebsites.net/invoice/save", invoice, ImportInvoice.class);
		if(invoice==null)
			model.addAttribute("result", "fail");
		else
			model.addAttribute("result", "success");
		return "importInvoice";
	}
	@GetMapping("/getListStat")
	private String getListStat(@RequestParam(name="idSup")int id,HttpSession session) {
		Date dateStart=(Date)session.getAttribute("dateStart");
		Date dateEnd=(Date)session.getAttribute("dateEnd");
		List<ImportInvoice>list=Arrays.asList(rest.getForObject("https://kttkserver.azurewebsites.net/invoice/getStat?dateStart="+dateStart+"&dateEnd="+dateEnd+"&idSup="+id, ImportInvoice[].class));
		session.setAttribute("listInvoice", list);
		return "listInvoice";
	}
	@GetMapping("/getInvoice")
	private String getInvoice(@RequestParam(name="id")int id, HttpSession session) {
		List<ImportInvoice>list=(List<ImportInvoice>) session.getAttribute("listInvoice");
		for(ImportInvoice invoice:list) {
			if(invoice.getId()==id)
			{
				session.setAttribute("invoice", invoice);
				break;
			}
		}
		return "invoice";
	}
}
