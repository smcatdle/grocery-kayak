/**
 * 
 */
package com.scanshop.tasks.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.cordova.api.CallbackContext;

import android.os.AsyncTask;
import android.util.Log;

/**
 * @author smcardle
 *
 */
public class GetProductTask extends AsyncTask {
	
	private CallbackContext callbackContext = null;
	
    @Override
    protected String doInBackground(Object... urls) {
          
        // params comes from the execute() call: params[0] is the url.
        try {
            return downloadUrl((String)urls[0]);
        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }
    
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(Object result) {
    	Log.e("onPostExecute : ", (String)result);

		// add product to basket
		this.callbackContext.success((String)result);
    	
   }
    
	 // Given a URL, establishes an HttpUrlConnection and retrieves
	 // the web page content as a InputStream, which it returns as
	 // a string.
	 private String downloadUrl(String myurl) throws IOException {
	     InputStream is = null;
	         
	     Log.e("downloadUrl : ", myurl);
	     
	     try {
	         URL url = new URL(myurl);
	         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	         conn.setReadTimeout(10000 /* milliseconds */);
	         conn.setConnectTimeout(15000 /* milliseconds */);
	         conn.setRequestMethod("GET");
	         conn.setDoInput(true);
	         // Starts the query
	         conn.connect();
	         int response = conn.getResponseCode();
	         Log.d("GetProductTask", "The response is: " + response);
	         is = conn.getInputStream();
	
	         // Convert the InputStream into a string
	         String contentAsString = readIt(is);
	         return contentAsString;
	         
	     // Makes sure that the InputStream is closed after the app is
	     // finished using it.
	     } finally {
	         if (is != null) {
	             is.close();
	         } 
	     }
	 }
	 
	//Reads an InputStream and converts it to a String.
	 public String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
	     Reader reader = null;
	     final StringBuilder out = new StringBuilder();
	     final char[] buffer = new char[1024];
	     reader = new InputStreamReader(stream, "UTF-8");        

	      for (;;) {
	          int rsz = reader.read(buffer, 0, buffer.length);
	          if (rsz < 0)
	            break;
	          out.append(buffer, 0, rsz);
	      }
	      
	     return new String(out);
	 }

	/**
	 * @return the callbackContext
	 */
	public CallbackContext getCallbackContext() {
		
		return callbackContext;
	}

	/**
	 * @param callbackContext the callbackContext to set
	 */
	public void setCallbackContext(CallbackContext callbackContext) {
		this.callbackContext = callbackContext;
	}
	 

}
