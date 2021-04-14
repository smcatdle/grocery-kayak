/**
 * 
 */
package com.scanshop.managers;

import java.lang.reflect.Type;
import java.util.List;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scanshop.model.BasketItem;
import com.scanshop.plugins.DeleteItemPlugin;
import com.scanshop.plugins.LoadBasketPlugin;
import com.scanshop.plugins.SaveBasketPlugin;

/**
 * @author smcardle
 *
 */
public class BasketManager {

	private LoadBasketPlugin loadBasketPlugin = null;
	private SaveBasketPlugin saveBasketPlugin = null;
	private DeleteItemPlugin deleteItemPlugin = null;
	private Gson gson = null;
	
	public BasketManager() {
		loadBasketPlugin = new LoadBasketPlugin();
		saveBasketPlugin = new SaveBasketPlugin();
		deleteItemPlugin = new DeleteItemPlugin();
		gson = new Gson();
	}
	
	/**
	 * Manage the loading of baskets
	 * @param basketId
	 * @param context
	 * @return
	 */
	public List<BasketItem> loadBasket(int basketId, Context context) {
		
		List<BasketItem> basketItems = null;
		
		String basketString = loadBasketPlugin.loadBasket(basketId, null, context);
		
		// Convert the JSON string to objects
        Type collectionType = new TypeToken<List<BasketItem>>(){}.getType();
        basketItems = gson.fromJson(basketString, collectionType);
        
        return basketItems;
	}
	
	/**
	 * Manage the saving of baskets
	 * @param basketString
	 * @param context
	 */
	public void saveBasket(List<BasketItem> basket, Context context) {
		saveBasketPlugin.saveBasket(basket, null, context);
	}
	
	public void deleteBasketItem(BasketItem basketItem, Context context) {
		deleteItemPlugin.deleteItem(basketItem, null, context);
	}
	
}
