package com.example.model;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bill {
	private int id;
	private String paymentDate;
	private String paymentType;
	private String note;
	private float discount;
	private float totalMoney;
	private BookedTable bookedTable;
	private User staff;
	
}
