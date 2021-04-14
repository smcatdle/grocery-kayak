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
import com.scanshop.dao.ProductDAO;
import com.scanshop.model.Category;
import com.scanshop.model.Shelf;

/**
 * This class echoes a string called from JavaScript.
 */
public class ProductAislesPligin extends CordovaPlugin {
	
	public CallbackContext callbackContext;
	
	private Gson gson = null;
	
	public ProductAislesPligin() {
		gson = new Gson();
	}
	
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    	
    	this.callbackContext = callbackContext;
    	
    	// set the callback on the main activity
    	((ScanShop)cordova.getActivity()).setCallbackContext(callbackContext);
    	
    	Log.e("Execute : ", "scan 3 " + action);
    	
        if (action.equals("getCategories")) {
            String message = action;
            this.getCategories(message, callbackContext);
            return true;
        } else if (action.equals("getShelves")) {
            String category = (String)args.get(0);
            Log.e("getShelves : ", category);
            this.getShelves(category, callbackContext);
            return true;
        }
        return false;
    }

    private void getCategories(String message, CallbackContext callbackContext) {
    	
    	String categoryString = null;
    	
    	try {

	        if (message != null && message.length() > 0) { 
	        	Log.e("getCategories", "scan 2 " + "Initiating scan..");
	        	
	        	Context context = ((ScanShop)cordova.getActivity()).getContext();
	        	
	        	// create the db
	        	ProductDAO dbHelper = ProductDAO.getInstance(context);
	        	  
	    		// get the categories from the db
	        	List<Category> categoryList  = dbHelper.onGetCategories("t");
	        	categoryString = gson.toJson(categoryList);
	        	
	            Log.e("echo : ", "scan 1 " + categoryString);
	            
	            callbackContext.success(categoryString);
	    
	        } else {
	            callbackContext.error("Expected one non-empty string argument.");
	        }
    	} catch (Exception ex) {
    		
    	}
    }

    private void getShelves(String category, CallbackContext callbackContext) {
    	
    	String shelfString = null;
    	
    	try {

	        if (category != null && category.length() > 0) { 
	        	Log.e("getShelves", "scan 2 " + "Initiating scan..");
	        	
	        	Context context = ((ScanShop)cordova.getActivity()).getContext();
	        	
	        	// create the db
	        	ProductDAO dbHelper = ProductDAO.getInstance(context);
	        	  
	    		// get the categories from the db
	        	List<Shelf> shelfList  = dbHelper.onGetCategoryShelves(category, "t");
	        	shelfString = gson.toJson(shelfList);
	        	
	            Log.e("echo : ", "scan 1 " + shelfString);
	            
	            callbackContext.success(shelfString);
	    
	        } else {
	            callbackContext.error("Expected one non-empty string argument.");
	        }
    	} catch (Exception ex) {
    		
    	}
    }
    
}

