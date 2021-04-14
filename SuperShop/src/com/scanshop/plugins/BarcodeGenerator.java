package com.scanshop.plugins;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.util.Log;

import com.scanshop.ScanShop;

/**
 * This class generates a barcode image from a 1-D number.
 */
public class BarcodeGenerator extends CordovaPlugin {
	
	public CallbackContext callbackContext;
	
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    	
    	String barcode = null;
    	
    	this.callbackContext = callbackContext;
    	barcode = args.getString(1);
    	
    	// set the callback on the main activity
    	((ScanShop)cordova.getActivity()).setCallbackContext(callbackContext);
    	
        if (action.equals("generateBarcode")) { 
            this.displayBarcodeImage(barcode, callbackContext);
            return true;
        }
        
        return false;
    }

    private void displayBarcodeImage(String barcode, CallbackContext callbackContext) {
    	
        if (barcode != null && barcode.length() > 0) { 
        	
        	Log.e("BarcodeGenerator", "Displaying barcode for string : [" + barcode + "]");
        	
        	Intent intent = new Intent("com.google.zxing.client.android.ENCODE");

        	intent.putExtra("ENCODE_FORMAT", "UPC_A");
        	intent.putExtra("ENCODE_DATA", barcode);

        	this.cordova.getActivity().startActivity(intent); 
        	
        	this.cordova.getActivity().startActivityForResult(intent, 0);
    
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
}

