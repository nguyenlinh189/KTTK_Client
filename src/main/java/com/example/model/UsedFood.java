package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsedFood {
	private int id;
	private float price;
	private float quantity;
	private String note;
	private float totalMoney;
	private Food food;
}
