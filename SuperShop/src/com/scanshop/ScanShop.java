package com.scanshop;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.cordova.Config;
import org.apache.cordova.DroidGap;
import org.apache.cordova.api.CallbackContext;

import com.company.propertyprice.network.GetPriceTask;
import com.scanshop.dao.BasketDAO;
import com.scanshop.dao.BasketItemDAO;
import com.scanshop.dao.ProductDAO;
import com.scanshop.model.Basket;
import com.scanshop.model.BasketItem;
import com.scanshop.model.Category;
import com.scanshop.model.Product;
import com.scanshop.model.Shelf;
import com.scanshop.plugins.SearchProductsByVoicePlugin;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;

public class ScanShop extends DroidGap {

	public static final String MYMOBILEBASKET_PRICE_SERVICE = "http://supershop-mobileventures2.rhcloud.com/server-rest-0.0.1-SNAPSHOT/priceCheck?service=checkPrice&barcode=";
	
	private CallbackContext callbackContext = null;
	
	private long callbackId = 0;
	
    private TextToSpeech tts;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	Log.e("onCreate : ", "scan 4 " + "");
    	
        super.onCreate(savedInstanceState);
        
        // Set by <content src="index.html" /> in config.xml
        super.loadUrl(Config.getStartUrl());
        //super.loadUrl("file:///android_asset/www/index.html")
        
        Log.e("onCreate : ", "scan 5 " + "");
       
    	BasketDAO basketDAO = BasketDAO.getInstance(getContext());
    	BasketItemDAO basketItemDAO = BasketItemDAO.getInstance(getContext());
    	
    	//createBasketDB(basketDAO, basketItemDAO);
    	//deleteAllBasketItems(basketDAO, basketItemDAO);
    	//deleteBaskets(basketDAO);
        //createBasketDB(basketDAO, basketItemDAO);

    	testBasketDB();
    	
    	basketItemDAO.close();
    	basketDAO.close();
    	
        //getShelves("t");
    	//loadProductsFromXML();
        
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    private void showDialog(int title, CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
      }
    
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	
	   //if (requestCode == 0) {
		     // if (resultCode == RESULT_OK) {
		         String scanResult = intent.getStringExtra("SCAN_RESULT");
		         String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
		         
		         if (requestCode == SearchProductsByVoicePlugin.SPEECH_REQUEST_CODE)
		         {
		             if (resultCode == RESULT_OK)
		             {
		                 ArrayList<String> matches = intent
		                     .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
		                 Log.e("onActivityResult : ", matches.get(0));
		                 /*if (matches.size() == 0)
		                 {
		                     tts.speak("Heard nothing", TextToSpeech.QUEUE_FLUSH, null);
		                 }
		                 else
		                 {
		                     String mostLikelyThingHeard = matches.get(0);
		                     String magicWord = this.getResources().getString(R.string.magicword);
		                     if (mostLikelyThingHeard.equals(magicWord))
		                     {
		                         tts.speak("You said the magic word!", TextToSpeech.QUEUE_FLUSH, null);
		                     }
		                     else
		                     {
		                         tts.speak("The magic word is not " + mostLikelyThingHeard + " try again", TextToSpeech.QUEUE_FLUSH, null);
		                     }
		                 }*/
		                 this.callbackContext.success(matches.get(0));
		             } else
		             {
		                 Log.e(TAG, "result NOT ok");
		             }
		             
		         } else if (scanResult != null) {

		 	    	String barcode = scanResult;
		 	    	
		 	    	Log.e("onActivityResult : ", "scan 4 [" + barcode + "]");
		 	    	
		 	    	if (barcode != null) {
		 	    		
		 	    		// get the price from the server
		 	            Context context = getContext();
		 	            final ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 	            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		 	            
		 	            if (networkInfo != null && networkInfo.isConnected()) {
		 	            	GetPriceTask task = new GetPriceTask();
		 	            	task.setCallbackContext(callbackContext);
		 	            	task.execute(MYMOBILEBASKET_PRICE_SERVICE + barcode);
		 	            } else {
		 	            	Log.e("onPostExecute : ", "No network connection available.");
		 	            }   		

		 	    	} else {
		 	    		Log.e("Scanned product not found in the database", "");
		 	    	}
		 	    	
		         // Handle successful scan
		      } else if (resultCode == RESULT_CANCELED) {
		         // Handle cancel
		    	  Log.e("snake Scan Result6", "");
		      }
		   }
	  // }
    //}
    
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
    
    private Product lookupProduct(String criteria) {
    	
    	ProductDAO db = ProductDAO.getInstance(getContext());
    	Product p = new Product();
    	p.setBarcode(criteria);
    	
    	Log.e("Checking barcode", criteria);
    	
    	List<Product> products = db.onGetProducts(p);
    	
    	// return the first product found
    	if (!products.isEmpty()) {
    		return products.get(0);
    	} else {
    		Log.e("MISSING PRODUCT barcode : [", criteria + "]");
    		return null;
    	}
    }
    
    public void loadProductsFromXML() {
    	        
    	String file = Environment.getExternalStorageDirectory().getPath() + "/downloads/scanshop/TescoProducts.xml";
    	
    	Log.e("loadProductsFromXML : ", "scan 6 " + "");
    	
	      try {           
	    	  
	    	  Log.e("try loadProductsFromXML : ", "scan 7 " + "");
	    	  File source = new File(file);
	    	  FileInputStream in = new FileInputStream(source);
	    	  Log.e("try loadProductsFromXML : ", "scan 8 " + source.exists());
	    	  
	    	  // create the db
	    	  ProductDAO dbHelper = ProductDAO.getInstance(getContext());
	    	  
	    	  /*List<Product> products = SimpleXmlParser.parse(in);
	    	  
	    	  // add the products to the db
	    	  for (Product product : products) {
	    		  dbHelper.onAddProduct(product);
	    	  }*/
	    	  
	    	  // test the entries have been inserted ok
	    	  Product testProduct = new Product();
	    	  testProduct.setBarcode("3600540333599"); 

	    	  List<Product> testProducts  = dbHelper.onGetProducts(testProduct);
	    	  Log.e("Found Product ::: : ", "scan 4 [" + testProducts.get(0).getDescription() + "]");
	    	  
	      } catch (Exception ex) {
	    	  Log.e("Error loading products file : " + file, "");
	      }
    }
    
    public void getShelves(String chain) {
    	
  	  // create the db
  	  ProductDAO dbHelper = ProductDAO.getInstance(getContext());
  	  
	  //List<Shelf> shelves  = dbHelper.onGetShelves(chain);
  	  List<Shelf> shelves  = dbHelper.onGetCategoryShelves("Food Cupboard", chain);
  	  List<Category> categories  = dbHelper.onGetCategories(chain);
	  
	  Log.e("getShelves", shelves.get(0).getShelfName());
    }
    
    private List<String> getSD() {
        List<String> item = new ArrayList<String>();
        File f = new File("/data/scanshop");
        File[] files = f.listFiles();

        for(int i = 0; i < files.length; i++) {
            File file = files[i];
            //take the file name only
            long size = file.length()/1024;

            String myfile = file.getPath().substring(file.getPath().lastIndexOf("/")+1,file.getPath().length()).toLowerCase(); 
            //item.add(myfile);
            item.add(myfile+"             "+"Size:"+size+" KB");

            Log.e("SD File : " + file, "");
            //item.add(file.length());
        }
        return item;
    }   
    
    // TODO: Call this the first time the app is opened
    private void createBasketDB(BasketDAO basketDAO, BasketItemDAO basketItemDAO) {
    	
    	Basket basket = new Basket();
    	basket.setName("Tesco");
    	basket.setType("t");
    	
    	basketDAO.onAddBasket(basket);
    	
    	BasketItem basketItem = new BasketItem();
    	basketItem.setBasketId(basket.getId());
    	basketItem.setBarcode("45355345345");
    	basketItem.setDescription("Item1");
    	basketItem.setPrice(1.99);
    	basketItem.setAmount(2);
    	basketItem.setType("t");
    	basketItemDAO.onAddBasketItem(basketItem);
    	
    	List<Basket> baskets = basketDAO.onGetBaskets();
    	Log.e("Baskets", "Basket : " + ((Basket)baskets.get(0)).getName());

    }
    
    private void testBasketDB() {
    	
    	// get the db
    	BasketDAO basketDAO = BasketDAO.getInstance(getContext());
    	
    	// get the db
    	BasketItemDAO basketItemDAO = BasketItemDAO.getInstance(getContext());
    	
    	deleteAllBasketItems(basketDAO, basketItemDAO);
    	deleteBaskets(basketDAO);
		
    	// tesco
    	Basket tescoBasket = new Basket();
    	tescoBasket.setName("Tesco");
    	tescoBasket.setType("t");	
    	basketDAO.onAddBasket(tescoBasket);
    	
    	// add some items to tesco basket
    	BasketItem item1 = new BasketItem();
    	item1.setBarcode("45355345345");
    	item1.setBasketId(tescoBasket.getId());
    	item1.setDescription("Item1");
    	item1.setPrice(1.99);
    	item1.setAmount(2);
    	item1.setType("t");

    	/*BasketItem item2 = new BasketItem();
    	item2.setBarcode("4573625525525");
    	item2.setBasketId(tescoBasket.getId());
    	item2.setDescription("Item2");
    	item2.setPrice(1.99);
    	item2.setAmount(4);
    	item2.setType("t");

    	BasketItem item3 = new BasketItem();
    	item3.setBarcode("84574645634636");
    	item3.setBasketId(tescoBasket.getId());
    	item3.setDescription("Item3");
    	item3.setPrice(1.99);
    	item3.setAmount(1);
    	item3.setType("t");

    	BasketItem item4 = new BasketItem();
    	item4.setBarcode("3325252252336");
    	item4.setBasketId(tescoBasket.getId());
    	item4.setDescription("Item4");
    	item4.setPrice(1.99);
    	item4.setAmount(3);
    	item4.setType("t");
    	
    	basketItemDAO.onAddBasketItem(item1);
    	basketItemDAO.onAddBasketItem(item2);
    	basketItemDAO.onAddBasketItem(item3);
    	basketItemDAO.onAddBasketItem(item4);*/
    	
    	// superquinn
    	Basket superquinnBasket = new Basket();
    	superquinnBasket.setName("Superquinn");
    	superquinnBasket.setType("q");	
    	basketDAO.onAddBasket(superquinnBasket);
    	
    	// add some items to superquinn basket
    	/*BasketItem item5 = new BasketItem();
    	item5.setBarcode("64645636");
    	item5.setBasketId(superquinnBasket.getId());
    	item5.setDescription("item5");
    	item5.setPrice(1.99);
    	item5.setAmount(3);
    	item5.setType("q");

    	BasketItem item6 = new BasketItem();
    	item6.setBarcode("678658658568");
    	item6.setBasketId(superquinnBasket.getId());
    	item6.setDescription("item6");
    	item6.setPrice(1.99);
    	item6.setAmount(1);
    	item6.setType("q");

    	BasketItem item7 = new BasketItem();
    	item7.setBarcode("2312312312");
    	item7.setBasketId(superquinnBasket.getId());
    	item7.setDescription("item7");
    	item7.setPrice(1.99);
    	item7.setAmount(1);
    	item7.setType("q");

    	BasketItem item8 = new BasketItem();
    	item8.setBarcode("78678678678");
    	item8.setBasketId(superquinnBasket.getId());
    	item8.setDescription("item8");
    	item8.setPrice(1.99);
    	item8.setAmount(1);
    	item8.setType("q");
    	
    	basketItemDAO.onAddBasketItem(item5);
    	basketItemDAO.onAddBasketItem(item6);
    	basketItemDAO.onAddBasketItem(item7);
    	basketItemDAO.onAddBasketItem(item8);*/
    	
    	// supervalu
    	Basket supervaluBasket = new Basket();
    	supervaluBasket.setName("SupervaluBasket");
    	supervaluBasket.setType("v");
    	basketDAO.onAddBasket(supervaluBasket);
    	
    	// add some items to supervalu basket
    	/*BasketItem item9 = new BasketItem();
    	item9.setBarcode("678658658568");
    	item9.setBasketId(supervaluBasket.getId());
    	item9.setDescription("item6");
    	item9.setPrice(1.99);
    	item9.setAmount(1);
    	item9.setType("q");

    	BasketItem item10 = new BasketItem();
    	item10.setBarcode("2312312312");
    	item10.setBasketId(supervaluBasket.getId());
    	item10.setDescription("item7");
    	item10.setPrice(1.99);
    	item10.setAmount(1);
    	item10.setType("q");

    	BasketItem item11 = new BasketItem();
    	item11.setBarcode("78678678678");
    	item11.setBasketId(supervaluBasket.getId());
    	item11.setDescription("item8");
    	item11.setPrice(1.99);
    	item11.setAmount(1);
    	item11.setType("q");
    	
    	basketItemDAO.onAddBasketItem(item9);
    	basketItemDAO.onAddBasketItem(item10);
    	basketItemDAO.onAddBasketItem(item11);*/
    	
    	// lidl
    	Basket lidlBasket = new Basket();
    	lidlBasket.setName("LidlBasket");
    	lidlBasket.setType("l");
    	basketDAO.onAddBasket(lidlBasket);
    	
    	// aldi
    	Basket aldiBasket = new Basket();
    	aldiBasket.setName("AldiBasket");
    	aldiBasket.setType("a");
    	basketDAO.onAddBasket(aldiBasket);
    	
    	
    	// display the baskets
    	List<Basket> baskets = basketDAO.onGetBaskets();
    	for (Basket basket : baskets) {
    		Log.e("Basket", "Basket : " + basket.getName());
    		
    		List<BasketItem> basketItems = basketItemDAO.onGetBasketItems(basket);
    		
    		// display the basket items for this basket
    		for (BasketItem basketItem : basketItems) {
    			Log.e("BasketItem", "BasketItem - Description : [" + basketItem.getDescription() + "] , Price : [" + basketItem.getPrice() + "] , Amount : [" + basketItem.getAmount() + "] , Barcode : [" + basketItem.getBarcode() + "] , Type : [" + basketItem.getType() + "] ,");
    		}
    	}
    	
    	//deleteAllBasketItems(basketDAO, basketItemDAO);
    	//deleteBaskets(basketDAO);
    	
    	basketItemDAO.close();
    	basketDAO.close();
    }
    
    private void deleteBasketItems(BasketItemDAO basketItemDAO, Basket basket) {
    	
    	basketItemDAO.checkDatabaseOpen();
    	
    	// delete the basket items
		List<BasketItem> basketItems = basketItemDAO.onGetBasketItems(basket);
		
		// display the basket items for this basket
		for (BasketItem basketItem : basketItems) {
			Log.e("deleteBasketItems : ", basketItem.getDescription());
			basketItemDAO.onDeleteBasketItem(basketItem);
		}
		
    }
    
    private void deleteAllBasketItems(BasketDAO basketDAO, BasketItemDAO basketItemDAO) {
    	
    	basketDAO.checkDatabaseOpen();
    	basketItemDAO.checkDatabaseOpen();
    	
    	List<Basket> baskets = basketDAO.onGetBaskets();
    	for (Basket basket : baskets) {
    		Log.e("deleteAllBasketItems : ", basket.getName());
    		
    		List<BasketItem> basketItems = basketItemDAO.onGetBasketItems(basket);
    		
    		// delete the basket items for this basket
    		for (BasketItem basketItem : basketItems) {
    			Log.e("deleteAllBasketItems : ", basketItem.getDescription());
    			basketDAO.onDeleteBasket(basket);
    		}
    	}
		Basket basket = new Basket();
		basket.setId(0);
    	basketDAO.onDeleteBasket(basket);
    }
    
    private void deleteBaskets(BasketDAO basketDAO) {
    	
    	basketDAO.checkDatabaseOpen();
    	
    	// delete the baskets
		List<Basket> baskets = basketDAO.onGetBaskets();
		
		// display the basket items for this basket
		for (Basket basket : baskets) {
			Log.e("deleteBaskets : ", basket.getName());
			basketDAO.onDeleteBasket(basket);
		}
		
    }
    
}
