package com.scanshop.plugins;

import java.util.List;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.scanshop.ScanShop;
import com.scanshop.dao.BasketItemDAO;
import com.scanshop.model.Basket;
import com.scanshop.model.BasketItem;

/**
 * This class echoes a string called from JavaScript.
 */
public class LoadBasketPlugin extends CordovaPlugin {
	
	public CallbackContext callbackContext;
	
	private Gson gson = null;
	
	public LoadBasketPlugin() {
		gson = new Gson();
	}
	
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    	
    	this.callbackContext = callbackContext;
    	Context context = ((ScanShop)cordova.getActivity()).getContext();
    	
    	// set the callback on the main activity
    	((ScanShop)cordova.getActivity()).setCallbackContext(callbackContext);
    	
    	Log.e("Execute : ", "Loading basket " + action);
    	
        if (action.equals("loadBasket")) {
        	int basketId = (Integer)args.get(0);
            this.loadBasket(basketId, callbackContext, context);
            return true;
        } 
        return false;
    }

    public String loadBasket(int basketId, CallbackContext callbackContext, Context context) {
    	
    	List<BasketItem> basketItems = null;
    	
    	BasketItemDAO basketItemDAO = null;
    	String basketItemsString = null;
    	
    	try {
        	Log.e("loadBasket", "basketId " + basketId);
	        if (basketId != -1) { 
	        	
	        	Log.e("loadBasket", "getContext()");
	        	
	        	// get the db
	        	basketItemDAO = BasketItemDAO.getInstance(context);
	        	basketItemDAO.checkDatabaseOpen();
	        	Log.e("loadBasket", "BasketItemDAO");
	        	
	        	Log.e("baskets", " basketId : [" + basketId + "]");
	        	Basket basket = new Basket();
	        	basket.setId(basketId);
	        	basketItems = basketItemDAO.onGetBasketItems(basket);
	        	Log.e("basketItems", basketItems.size() + "," + basketId);
	        	
	        	basketItemsString = gson.toJson(basketItems);
	        	Log.e("loaded basket items", basketItemsString);
	        	
	            //callbackContext.success(basketItemsString);
	    
	        } else {
	            //callbackContext.error("Expected one non-empty string argument.");
	        }
    	} catch (Exception ex) {
    		Log.e("loadBasket", ex.getMessage());
    	} finally {
        	basketItemDAO.close();
    	}
    	
    	return basketItemsString;
    }

    
}

