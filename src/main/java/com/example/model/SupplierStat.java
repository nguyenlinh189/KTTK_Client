package com.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SupplierStat extends Supplier{
	private float totalMoney;
	private float totalQuantity;
	public SupplierStat(float totalMoney, float totalQuantity) {
		super();
		this.totalMoney = totalMoney;
		this.totalQuantity = totalQuantity;
	}
	
}
