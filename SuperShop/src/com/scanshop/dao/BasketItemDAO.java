

package com.scanshop.dao;

import java.util.ArrayList;
import java.util.List;

import com.scanshop.model.Basket;
import com.scanshop.model.BasketItem;
import com.scanshop.model.Product;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;


public class BasketItemDAO extends SQLiteOpenHelper {

	private static BasketItemDAO instance = null;
	private static SQLiteDatabase dbConnection = null;
	
    public static final int DATABASE_VERSION = 1;
    //public static final String DATABASE_NAME = Environment.getExternalStorageDirectory().getPath() + "/downloads/scanshop/ScanShop_v1.db";
    public static final String DATABASE_NAME = "ScanShop_v1.db";
    public static final String DATABASE_PATH = "/data/data/com.mymobilebasket/databases/";
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS basketitem";
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS basketitem (id INTEGER PRIMARY KEY, basketid INTEGER, barcode TEXT, description TEXT, price DOUBLE, amount INTEGER, type TEXT, FOREIGN KEY(basketid) REFERENCES basket(id))";

    public BasketItemDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        dbConnection = getWritableDatabase();
        
        if (dbConnection != null) {
        	Log.e("BasketItemDAO : ", "dbConnection" + "not null");
        }
        
        onUpgrade(dbConnection, 1, 2);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

	
	// instance method
	public static BasketItemDAO getInstance(Context context) {
		
        if (instance == null) {
        	instance = new BasketItemDAO(context);
        }
        
        return instance;
    }
   
	public void checkDatabaseOpen() {
		
        // check the database is open
        if (!dbConnection.isOpen()) {
        	dbConnection = getWritableDatabase();
        }
        
	}
	
    /**
     * Update a basketItem
     * 
     * @param basketItem  The basket item to update
     * 
     */
    public int onUpdateBasketItem(BasketItem basketItem) {
    	
    	Log.e("BasketItemDAO : ", "onUpdateBaskeItem" + basketItem.getDescription());
    	
    	String[] selectionArgs = {String.valueOf(basketItem.getId()),String.valueOf(basketItem.getBasketId())};
    	ContentValues values = new ContentValues();
    	values.put("description", basketItem.getDescription());
    	values.put("price", basketItem.getPrice());
    	values.put("amount", basketItem.getAmount());
    	values.put("type", basketItem.getType());
    	
    	return dbConnection.update("basketitem", values, "id = ?, basketid=?", selectionArgs);
    	
    }
    
    /**
     * Delete a basketItem
     * 
     * @param basketItem  The basket item to delete
     * 
     */
    public int onDeleteBasketItem(BasketItem basketItem) {
    	
    	Log.e("BasketItemDAO : ", "onDeleteBasketItem" + basketItem.getDescription());
    	
    	String[] selectionArgs = {String.valueOf(basketItem.getId())};
    	
    	return dbConnection.delete("basketitem",  "id = ?", selectionArgs);
    	
    }
    
    /**
     * Add a new basket item to the table
     * 
     * @param item  The new basket item to add
     * 
     */
    public long onAddBasketItem(BasketItem item) {
    	
    	Log.e("BasketItemDAO : ", "onAddBasketItem" + item.getDescription());
    	
    	onCreate(dbConnection);
    	
    	ContentValues values = new ContentValues();
    	values.put("basketid", item.getBasketId());
    	values.put("barcode", item.getBarcode());
    	values.put("description", item.getDescription());
    	values.put("price", item.getPrice());
    	values.put("amount", item.getAmount());
    	values.put("type", item.getType());

    	long id = dbConnection.insert("basketitem", null, values);
    	
    	item.setId(id);
    	
    	return id;
    }
    

    /**
     * Get the BasketItems
     * 
     * @param basket  The basket criteria to use
     * 
     */
    public List<BasketItem> onGetBasketItems(Basket basket) {
    	
    	List<BasketItem> basketItems = new ArrayList<BasketItem>();
    	String[] columns = {"id", "basketid", "barcode", "description", "price", "amount",  "type"};
    	String[] selectionArgs = { String.valueOf(basket.getId())};
    	
    	Cursor cursor = dbConnection.query("basketitem", columns, "basketid = ?" , selectionArgs, null, null, "id");
    		
		if  (cursor.moveToFirst()) {
		        do {
		        	Log.e("BasketItemDAO : ", "onGetBasketItems" + basket.getName());
		        	BasketItem item = new BasketItem();
		        	item.setId(cursor.getInt(cursor.getColumnIndex("id")));
		        	item.setBasketId(cursor.getInt(cursor.getColumnIndex("basketid")));
		        	item.setDescription(cursor.getString(cursor.getColumnIndex("description")));
		        	item.setBarcode(cursor.getString(cursor.getColumnIndex("barcode")));
		        	item.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
		        	item.setAmount(cursor.getInt(cursor.getColumnIndex("amount")));
		        	item.setType(cursor.getString(cursor.getColumnIndex("type")));
		        	
		        	basketItems.add(item);
		        	
		    } while (cursor.moveToNext());
		}
		
		if(cursor!= null && !cursor.isClosed()) cursor.close();
		 
		return basketItems;
    	
    }


    
    public void onCreate(SQLiteDatabase db) {
    	Log.e("BasketItemDAO : ", "onCreate");
        db.execSQL(SQL_CREATE_ENTRIES);
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);

	}

}
