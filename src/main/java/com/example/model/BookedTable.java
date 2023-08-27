package com.example.model;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookedTable {
	private int id;
	private String checkIn;
	private String checkOut;
	private String note;
	private boolean ispayment;
	private float totalMoney;
	private float totalQuantity;
	private Furniture furniture;
	private List<UsedFood>listUsedFood=new ArrayList<>();
	private User customer;
}
