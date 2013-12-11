
package com.inn.data;

import java.io.Serializable;

public class ItemDetail implements Serializable {
	
	
	
	private static final long serialVersionUID = 1L;
	private String name, qty, price, total  ;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the qty
	 */
	public String getQty() {
		return qty;
	}

	/**
	 * @param qty the qty to set
	 */
	public void setQty(String qty) {
		this.qty = qty;
	}

	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}

	/**
	 * @return the total
	 */
	public String getTotal() {
		return total;
	}

	public ItemDetail(String name, String qty, String price, String total) {
		super();
		this.name = name;
		this.qty = qty;
		this.price = price;
		this.total = total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(String total) {
		this.total = total;
	}
	
	

	

	
	
}
