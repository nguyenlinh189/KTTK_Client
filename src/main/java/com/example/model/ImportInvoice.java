package com.example.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ImportInvoice {
	private int id;
	private float totalMoney;
	private float totalQuantity;
	private String note="";
	private Date dateImport;
	private List<DetailInvoice>listDetailInvoice=new ArrayList<>();
	private Supplier supplier;
	public ImportInvoice(float totalMoney, float totalQuantity, String note, Date dateImport,
			List<DetailInvoice> listDetailInvoice, Supplier supplier) {
		super();
		this.totalMoney = totalMoney;
		this.totalQuantity = totalQuantity;
		this.note = note;
		this.dateImport = dateImport;
		this.listDetailInvoice = listDetailInvoice;
		this.supplier = supplier;
	}
}
