package com.scanshop.plugins;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scanshop.ScanShop;
import com.scanshop.dao.BasketItemDAO;
import com.scanshop.model.BasketItem;

/**
 * This class echoes a string called from JavaScript.
 */
public class SaveBasketPlugin extends CordovaPlugin {
	
	public CallbackContext callbackContext;
	
	private Gson gson = null;
	
	public SaveBasketPlugin() {
		gson = new Gson();
	}
	
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    	
    	List<BasketItem> basketItems = null;
    	
    	this.callbackContext = callbackContext;
    	Context context = ((ScanShop)cordova.getActivity()).getContext();
    	// set the callback on the main activity
    	((ScanShop)cordova.getActivity()).setCallbackContext(callbackContext);
    	
    	Log.e("Execute : ", "scan 3 " + action);
    	
        if (action.equals("saveBasket")) {
        	String basketString = (String)args.get(0);
        	
	        Type collectionType = new TypeToken<List<BasketItem>>(){}.getType();
	        basketItems = gson.fromJson(basketString, collectionType);
	        
            this.saveBasket(basketItems, callbackContext, context);
            return true;
        } 
        return false;
    }

    public void saveBasket(List<BasketItem> basket, CallbackContext callbackContext, Context context) {
    	

    	String status = null;
    	
    	try {
    		
        	Log.e("save basket items", basket.toString());

        	// get the db
        	BasketItemDAO basketItemDAO = BasketItemDAO.getInstance(context);
        	basketItemDAO.checkDatabaseOpen();
	        
	        // We presume the basket has already been created.
	        if (basket != null && basket.size() > 0) {
	        	
	        	// save each basket item
	        	for (BasketItem item: basket) {

	        		if (item.getBasketId() >= 0) {
		        		// Is this a new basket item or existing
		        		if (item.getId() == -1) {
		        			basketItemDAO.onAddBasketItem(item);
		        			Log.e("basketItem Add ", item.getId() + "," + item.getBasketId());
		        		} else {
		        			Log.e("basketItem Update", item.getId() + "," + item.getBasketId());
		        			//basketItemDAO.onUpdateBasketItem(item);
		        		}
	        		}
	        	}
	            
		        basketItemDAO.close();
	            //callbackContext.success(status);
	    
	        } else {
	            //callbackContext.error("Expected one non-empty string argument.");
	        }
    	} catch (Exception ex) {
    		Log.e("saveBasket", ex.getMessage());
    	}
    }

    
}

