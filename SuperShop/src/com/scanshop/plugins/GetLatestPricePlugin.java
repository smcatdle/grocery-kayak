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

import com.company.propertyprice.network.GetPriceTask;
import com.company.propertyprice.network.GetProductTask;
import com.scanshop.ScanShop;

/**
 * This class echoes a string called from JavaScript.
 */
public class GetLatestPricePlugin extends CordovaPlugin {
	
	public static final String MYMOBILEBASKET_PRICE_SERVICE = "http://scanshop-mobileventures.rhcloud.com/server-rest-0.0.1-SNAPSHOT/priceCheck?service=checkPrice&barcode=";
	//public static final String MYMOBILEBASKET_PRICE_SERVICE = "http://mymobilebasket.appspot.com/priceCheck?service=checkPrice&barcode=";
	
	public CallbackContext callbackContext;
	
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    	
    	this.callbackContext = callbackContext;
    	
    	// set the callback on the main activity
    	((ScanShop)cordova.getActivity()).setCallbackContext(callbackContext);
    	
        if (action.equals("echo")) {
        	String barcode = (String)args.get(0); 
        	Log.e("onPostExecute : ", "Checking price for barcode : [" + barcode + "]");
            this.echo(barcode, callbackContext);
            return true;
        }
        return false;
    }

    private void echo(String barcode, CallbackContext callbackContext) {
        if (barcode != null && barcode.length() > 0) { 
        	
    		// get the price from the server
            Context context = ((ScanShop)cordova.getActivity()).getContext();
            final ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            
            if (networkInfo != null && networkInfo.isConnected()) {
            	GetPriceTask task = new GetPriceTask();
            	task.setCallbackContext(callbackContext);
            	task.execute(MYMOBILEBASKET_PRICE_SERVICE + URLEncoder.encode(barcode));
            } else {
            	Log.e("onPostExecute : ", "No network connection available.");
            }
            
            //callbackContext.success("Orange Javas");
    
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

}

