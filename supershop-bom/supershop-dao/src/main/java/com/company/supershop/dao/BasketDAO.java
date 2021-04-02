package com.company.supershop.dao;

import java.util.List;

import com.company.supershop.model.Basket;
import com.company.supershop.model.BasketItem;


public interface BasketDAO {
	
	public List<Basket> getBaskets();
	
	public List<BasketItem> getBasketItems(int basketId, int accountId);

	public List<BasketItem> getAllBasketItems(int accountId);
	
	public BasketItem getBasketItem(int basketItemId, int accountId);
	
	public void attachDirty(BasketItem instance);
	
	public void update(BasketItem instance);
	
	public List<Basket> findByChain(String chain);
	
	public void deleteBasketItem(int basketItemId, int accountId);
	
}
