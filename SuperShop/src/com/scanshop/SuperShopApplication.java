/**
 * 
 */
package com.scanshop;

import java.util.List;

import com.scanshop.dao.BasketDAO;
import com.scanshop.dao.BasketItemDAO;
import com.scanshop.managers.BasketManager;
import com.scanshop.managers.RemoteServerManager;
import com.scanshop.model.Basket;
import com.scanshop.model.BasketItem;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * @author smcardle
 *
 */
public class SuperShopApplication extends Application {

	private BasketManager basketManager = null;
	private RemoteServerManager remoteServerManager = null;

	private static SuperShopApplication singleton;
	
	public SuperShopApplication getInstance(){
		return singleton;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		singleton = this;
		basketManager = new BasketManager();
		remoteServerManager = new RemoteServerManager();
	}
	
	public BasketManager getBasketManager() {
		return basketManager;
	}
	
	public RemoteServerManager getRemoteServerManager() {
		return remoteServerManager;
	}
	
    public void testBasketDB(Context context) {
    	
    	// get the db
    	BasketDAO basketDAO = BasketDAO.getInstance(context);
    	
    	// get the db
    	BasketItemDAO basketItemDAO = BasketItemDAO.getInstance(context);
		
    	// tesco
    	Basket tescoBasket = new Basket();
    	tescoBasket.setName("Tesco");
    	tescoBasket.setType("t");	
    	basketDAO.onAddBasket(tescoBasket);
    	
    	// add some items to tesco basket
    	BasketItem item1 = new BasketItem();
    	item1.setBarcode("45355345345");
    	item1.setBasketId(tescoBasket.getId());
    	item1.setDescription("Item1");
    	item1.setPrice(1.99);
    	item1.setAmount(2);
    	item1.setType("t");
    	basketItemDAO.onAddBasketItem(item1);
    	
    	// superquinn
    	Basket superquinnBasket = new Basket();
    	superquinnBasket.setName("Superquinn");
    	superquinnBasket.setType("q");	
    	basketDAO.onAddBasket(superquinnBasket);
    	
    	// supervalu
    	Basket supervaluBasket = new Basket();
    	supervaluBasket.setName("SupervaluBasket");
    	supervaluBasket.setType("v");
    	basketDAO.onAddBasket(supervaluBasket);
    
    	
    	// lidl
    	Basket lidlBasket = new Basket();
    	lidlBasket.setName("LidlBasket");
    	lidlBasket.setType("l");
    	basketDAO.onAddBasket(lidlBasket);
    	
    	// aldi
    	Basket aldiBasket = new Basket();
    	aldiBasket.setName("AldiBasket");
    	aldiBasket.setType("a");
    	basketDAO.onAddBasket(aldiBasket);
    	
    	
    	// display the baskets
    	List<Basket> baskets = basketDAO.onGetBaskets();
    	for (Basket basket : baskets) {
    		Log.e("Basket", "Basket : " + basket.getName());
    		
    		List<BasketItem> basketItems = basketItemDAO.onGetBasketItems(basket);
    		
    		// display the basket items for this basket
    		for (BasketItem basketItem : basketItems) {
    			Log.e("BasketItem", "BasketItem - Description : [" + basketItem.getDescription() + "] , Price : [" + basketItem.getPrice() + "] , Amount : [" + basketItem.getAmount() + "] , Barcode : [" + basketItem.getBarcode() + "] , Type : [" + basketItem.getType() + "] ,");
    		}
    	}
    	
    	basketItemDAO.close();
    	basketDAO.close();
    }
    
    
 
}
