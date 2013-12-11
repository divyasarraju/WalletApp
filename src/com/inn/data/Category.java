

package com.inn.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Category implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	private String type;
	private String name;
	private String descr;
	
	private List<ItemDetail> itemList = new ArrayList<ItemDetail>();

	public Category(String type, String name, String descr) {
		this.type = type;
		this.name = name;
		this.descr = descr;
	}

	public String gettype() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public List<ItemDetail> getItemList() {
		return itemList;
	}

	public void addItemDetails(ItemDetail itemDetail) {
		itemList.add(itemDetail);
	}

}
