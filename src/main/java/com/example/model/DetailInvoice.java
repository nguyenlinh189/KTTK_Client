package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailInvoice {
	private int id;
	private float unitPrice;
	private float quantity;
	private float totalMoney;
	private String note="";
	private Ingredient ingredient;
}
