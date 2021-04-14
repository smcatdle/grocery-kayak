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
public class DeleteItemPlugin extends CordovaPlugin {
	
	public CallbackContext callbackContext;
	
	private Gson gson = null;
	
	public DeleteItemPlugin() {
		gson = new Gson();
	}
	
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    	
    	this.callbackContext = callbackContext;
    	
    	// set the callback on the main activity
    	((ScanShop)cordova.getActivity()).setCallbackContext(callbackContext);
    	
    	Log.e("Execute : ", "delete item " + action);
    	
        if (action.equals("deleteItem")) {
        	String basketItemString = (String)args.get(0);
        	Context context = ((ScanShop)cordova.getActivity()).getContext();
	        BasketItem basketItem = gson.fromJson(basketItemString, BasketItem.class);
            this.deleteItem(basketItem, callbackContext, context);
            return true;
        } 
        return false;
    }

    public void deleteItem(BasketItem basketItem, CallbackContext callbackContext, Context context) {

    	String status = null;
    	
    	try {
    		
        	Log.e("delete basket item", basketItem.toString());
        	
        	// get the db
        	BasketItemDAO basketItemDAO = BasketItemDAO.getInstance(context);
        	basketItemDAO.checkDatabaseOpen();
	        
	        // We presume the basket item has already been created.
	        if (basketItem != null) {
	        	
	        	// delete the basket item
        		if (basketItem.getId() >= 0) {
        			basketItemDAO.onDeleteBasketItem(basketItem);
        			Log.e("basketItem Add ", basketItem.getId() + "," + basketItem.getBasketId());
        		}
	        		
	        }
            
	        basketItemDAO.close();
            //callbackContext.success(status);
	    

    	} catch (Exception ex) {
    		Log.e("deleteItem", ex.getMessage());
    	}
    }

    
}

