package com.example.controller;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;

import com.example.model.Bill;
import com.example.model.BookedTable;
import com.example.model.Food;
import com.example.model.UsedFood;
import com.example.model.User;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/bill")
public class BillController {
	private RestTemplate rest=new RestTemplate();
	@GetMapping
	private String getBill(@RequestParam(name="id")int id, HttpSession session) {
		List<BookedTable>list=(List<BookedTable>) session.getAttribute("listUnpaid");
		BookedTable booked=new BookedTable();
		for(BookedTable b: list) {
			if(b.getId()==id) {
				booked=b;
			}
		}
		Bill bill=new Bill();
		bill.setBookedTable(booked);
		long millis=System.currentTimeMillis();
		Date date=new Date(millis);
		Time time=new Time(millis);
		bill.setPaymentDate(date+" "+time);
		User staff=(User) session.getAttribute("user");
		if(staff==null)
			return "login";
		bill.setStaff(staff);
		bill.setTotalMoney(booked.getTotalMoney());
		session.setAttribute("bill", bill);
		session.setAttribute("typepayment", '1');
		System.out.println(bill);
		return "bill";
	}
	@GetMapping("/updateUsedFood")
	private String updateUsedFood(@RequestParam(name="usedFoodId")int id, @RequestParam(name="quantity")float quantity,HttpSession session) {
		Bill bill=(Bill) session.getAttribute("bill");
		float totalMoney=0;
		float totalQuantity=0;
		for(UsedFood f:bill.getBookedTable().getListUsedFood())
		{
			if(f.getId()==id)
			{
				f.setQuantity(quantity);
				f.setTotalMoney(quantity*f.getPrice());
			}
			totalMoney+=f.getTotalMoney();
			totalQuantity+=f.getQuantity();
		}
		bill.getBookedTable().setTotalMoney(totalMoney);
		bill.getBookedTable().setTotalQuantity(totalQuantity);
		bill.setTotalMoney(totalMoney-bill.getDiscount());
		session.setAttribute("bill", bill);
		return "bill";
		
	}
	@GetMapping("/deleteUsedFood")
	private String deleteUsedFood(@RequestParam(name="usedFoodId")int id,HttpSession session) {
		Bill bill=(Bill) session.getAttribute("bill");
		float totalMoney=0;
		float totalQuantity=0;
		List<UsedFood>list=bill.getBookedTable().getListUsedFood();
		for(int i=0;i<list.size();i++)
		{
			if(list.get(i).getId()==id)
			{
				list.remove(i);
				break;
			}
		}
		for(int i=0;i<list.size();i++)
		{
			totalMoney+=list.get(i).getTotalMoney();
			totalQuantity+=list.get(i).getQuantity();
		}
		bill.getBookedTable().setListUsedFood(list);
		bill.getBookedTable().setTotalMoney(totalMoney);
		bill.getBookedTable().setTotalQuantity(totalQuantity);
		bill.setTotalMoney(totalMoney-bill.getDiscount());
		session.setAttribute("bill", bill);
		return "bill";
		
	}
	@GetMapping("/addUsedFood")
	private String addUsedFood(@RequestParam(name="chon")int id, @RequestParam(name="quantity")float quantity, HttpSession session) {
		List<Food>listFood=(List<Food>) session.getAttribute("listFood");
		Food food=new Food();
		for(Food f:listFood)
		{
			if(f.getId()==id) {
				food=f;
				break;
			}
		}
		Bill bill=(Bill) session.getAttribute("bill");
		List<UsedFood>list=bill.getBookedTable().getListUsedFood();
		float totalMoney=0;
		float totalQuantity=0;
		int mark=0;
		for(UsedFood f:list) {
			if(f.getFood().getId()==id) {
				f.setQuantity(f.getQuantity()+quantity);
				f.setTotalMoney(f.getQuantity()*f.getPrice());
				mark=1;
			}
			totalMoney+=f.getTotalMoney();
			totalQuantity+=f.getQuantity();
		}
		if(mark==0) {
			UsedFood u=new UsedFood();
			u.setFood(food);
			u.setPrice(food.getPrice());
			u.setQuantity(quantity);
			u.setTotalMoney(u.getQuantity()*u.getPrice());
			list.add(u);
			totalMoney+=u.getTotalMoney();
			totalQuantity+=u.getQuantity();
		}
		bill.getBookedTable().setListUsedFood(list);
		bill.getBookedTable().setTotalMoney(totalMoney);
		bill.getBookedTable().setTotalQuantity(totalQuantity);
		bill.setTotalMoney(totalMoney-bill.getDiscount());
		session.setAttribute("bill", bill);
		session.setAttribute("listFood", null);
		return "bill";
	}
	@GetMapping("/save")
	private String savebill(@RequestParam(name="typepayment")String typepayment,Model model, HttpSession session) {
		Bill bill=(Bill) session.getAttribute("bill");
		bill.getBookedTable().setCheckOut(bill.getPaymentDate());
		bill.setPaymentType(typepayment);
		bill.getBookedTable().setIspayment(true);
		bill=rest.postForObject("https://kttkserver.azurewebsites.net/bill/save", bill, Bill.class);
		if(bill==null) {
			model.addAttribute("result","fail");
		}else {
			model.addAttribute("result", "success");
		}
		session.setAttribute("listFood", null);
		session.setAttribute("listUnpaid", null);
		return "bill";
	}
}
