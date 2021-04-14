package com.scanshop.plugins;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.util.Log;

import com.scanshop.ScanShop;

/**
 * This class echoes a string called from JavaScript.
 */
public class SearchProductsByVoicePlugin extends CordovaPlugin {
	
	public static final String MYMOBILEBASKET_PRICE_SERVICE = "http://supershop-mobileventures2.rhcloud.com/server-rest-0.0.1-SNAPSHOT/priceCheck?service=search&searchString=";
	//public static final String MYMOBILEBASKET_PRICE_SERVICE = "http://scanshop-mobileventures.rhcloud.com/server-rest-0.0.1-SNAPSHOT/priceCheck?service=search&searchString=";
	//public static final String MYMOBILEBASKET_PRICE_SERVICE = "http://mymobilebasket.appspot.com/priceCheck?service=checkPrice&barcode=";
	
	public CallbackContext callbackContext;
	
    public static int SPEECH_REQUEST_CODE = 1234;
	
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    	
    	this.callbackContext = callbackContext;
    	
    	// set the callback on the main activity
    	((ScanShop)cordova.getActivity()).setCallbackContext(callbackContext);
    	
        if (action.equals("speechToText")) {
        	String searchString = (String)args.get(0); 
        	Log.e("onPostExecute : ", "Speech to Text : [" + searchString + "]");
            this.echo(searchString, callbackContext);
            return true;
        }
        return false;
    }

    public void echo(String searchString, CallbackContext callbackContext) {
        if (searchString != null && searchString.length() > 0) { 
        	
    		// get the price from the server
            Context context = ((ScanShop)cordova.getActivity()).getContext();

            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say the product");
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 100);
            this.cordova.getActivity().startActivityForResult(intent, SPEECH_REQUEST_CODE);
            
            //callbackContext.success("Orange Javas");
    
        } else {
            //callbackContext.error("Expected one non-empty string argument.");
        }
    }

}

