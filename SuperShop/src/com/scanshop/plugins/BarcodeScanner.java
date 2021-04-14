package com.scanshop.plugins;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.util.Log;

import com.scanshop.ScanShop;

/**
 * This class echoes a string called from JavaScript.
 */
public class BarcodeScanner extends CordovaPlugin {
	
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
        if (message != null && message.length() > 0) { 
        	Log.e("Scan", "scan 2 " + "Initiating scan..");
        	
        	Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        	intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
            intent.putExtra("SCAN_HEIGHT", 300);
            intent.putExtra("SCAN_WIDTH", 300); 
        	this.cordova.getActivity().startActivityForResult(intent, 0);
        	
            Log.e("echo : ", "scan 1 " + message);
            
            //callbackContext.success("Orange Javas");
    
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

}

