package com.scanshop.plugins;

import java.net.URLEncoder;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.company.propertyprice.listview.MyListActivity;
import com.company.propertyprice.listview.SwipeDismissActivity;
import com.company.propertyprice.network.GetPriceTask;
import com.company.propertyprice.network.GetProductTask;
import com.company.propertyprice.network.GetPropertiesInViewport;
import com.scanshop.ScanShop;

/**
 * This class echoes a string called from JavaScript.
 */
public class SearchProductsPlugin extends CordovaPlugin {
	
	public static final String MYMOBILEBASKET_PRICE_SERVICE = "http://supershop-mobileventures2.rhcloud.com/server-rest-0.0.1-SNAPSHOT/priceCheck?service=search&searchString=";	
	//public static final String MYMOBILEBASKET_PRICE_SERVICE = "http://scanshop-mobileventures.rhcloud.com/server-rest-0.0.1-SNAPSHOT/priceCheck?service=search&searchString=";
	//public static final String MYMOBILEBASKET_PRICE_SERVICE = "http://mymobilebasket.appspot.com/priceCheck?service=checkPrice&barcode=";
	
	public CallbackContext callbackContext;
	
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    	
    	this.callbackContext = callbackContext;
    	
    	// set the callback on the main activity
    	((ScanShop)cordova.getActivity()).setCallbackContext(callbackContext);
    	
        if (action.equals("loadSuggestions")) {
        	String searchString = (String)args.get(0);
            Context context = ((ScanShop)cordova.getActivity()).getContext();
        	Log.e("onPostExecute : ", "Searching for product : [" + searchString + "]");
            this.search(searchString, callbackContext, context, null);
            return true;
        }
        return false;
    }

    public void search(String searchString, CallbackContext callbackContext, Context context, MyListActivity callback) {
        if (searchString != null && searchString.length() > 0) { 
        	
    		// get the price from the server
            final ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            
            if (networkInfo != null && networkInfo.isConnected()) {
            	GetPropertiesInViewport task = new GetPropertiesInViewport();
            	task.setCallback(callback);
            	task.execute(MYMOBILEBASKET_PRICE_SERVICE + URLEncoder.encode(searchString));
            } else {
            	Log.e("onPostExecute : ", "No network connection available.");
            }
            
            //callbackContext.success("Orange Javas");
    
        } else {
            //callbackContext.error("Expected one non-empty string argument.");
        }
    }

}

