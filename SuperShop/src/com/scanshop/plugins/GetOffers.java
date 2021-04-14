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

import com.company.propertyprice.network.GetProductTask;
import com.scanshop.ScanShop;

/**
 * This class echoes a string called from JavaScript.
 */
public class GetOffers extends CordovaPlugin {
	
	public static final String MYMOBILEBASKET_PROMOTIONS_SERVICE = "http://scanshop-mobileventures.rhcloud.com/server-rest-0.0.1-SNAPSHOT/priceCheck?service=getPromotions&promotionRanking=400&shelfNames=" + URLEncoder.encode("Hairspray");
	
	public CallbackContext callbackContext;
	
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    	
    	this.callbackContext = callbackContext;
    	
    	// set the callback on the main activity
    	((ScanShop)cordova.getActivity()).setCallbackContext(callbackContext);
    	
    	Log.e("Execute : ", "scan 3 " + action);
    	
        if (action.equals("echo")) {
            String message = "This is a fantasic app!!"; //args.getString(0); 
            this.echo(message, callbackContext);
            return true;
        }
        return false;
    }

    private void echo(String message, CallbackContext callbackContext) {
    	
    	try {

	        if (message != null && message.length() > 0) { 
	        	Log.e("Scan", "scan 2 " + "Initiating scan..");
	        	
	    		// get the promotions from the server
	            Context context = ((ScanShop)cordova.getActivity()).getContext();
	            final ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	            
	            if (networkInfo != null && networkInfo.isConnected()) {
	            	GetProductTask task = new GetProductTask();
	            	task.setCallbackContext(callbackContext);
	            	task.execute(MYMOBILEBASKET_PROMOTIONS_SERVICE);
	            } else {
	            	Log.e("onPostExecute : ", "No network connection available.");
	            }
	        	
	            Log.e("echo : ", "scan 1 " + message);
	            
	            //callbackContext.success("Orange Javas");
	    
	        } else {
	            callbackContext.error("Expected one non-empty string argument.");
	        }
    	} catch (Exception ex) {
    		
    	}
    }

}

