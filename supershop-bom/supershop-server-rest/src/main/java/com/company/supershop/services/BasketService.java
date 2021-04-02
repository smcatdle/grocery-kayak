package com.company.supershop.services;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import com.company.supershop.model.Basket;
import com.company.supershop.model.BasketItem;
import com.company.supershop.model.ProductMVO;


public interface BasketService {

	public List<Basket> getBaskets();
	
	public List<BasketItem> getBasketItems(int basketId, int accountId);
	
	public List<BasketItem> addBasketItem(ProductMVO productMVO, int accountId, boolean refresh);
	
	public List<BasketItem> updateBasketItem(BasketItem basketItem, int accountId, boolean refresh);
	
	public List<BasketItem> deleteBasketItem(int basketId, int basketItemId, int accountId, boolean refresh);
	
	public List<BasketItem> swapBasketItem(ProductMVO item, int basketId, int basketItemId, int accountId, boolean refresh);
}
