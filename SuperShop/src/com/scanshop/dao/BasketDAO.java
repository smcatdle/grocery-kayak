

package com.scanshop.dao;

import java.util.ArrayList;
import java.util.List;

import com.scanshop.model.Basket;
import com.scanshop.model.BasketItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;


public class BasketDAO extends SQLiteOpenHelper {

	private static BasketDAO instance = null;
	private static SQLiteDatabase dbConnection = null;
	private Context context = null;
	
    public static final int DATABASE_VERSION = 1;
    //public static final String DATABASE_NAME = Environment.getExternalStorageDirectory().getPath() + "/downloads/scanshop/ScanShop_v1.db";
    public static final String DATABASE_NAME = "ScanShop_v1.db";
    public static final String DATABASE_PATH = "/data/data/com.mymobilebasket/databases/";
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS basket";
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS basket (id INTEGER PRIMARY KEY, name TEXT, type TEXT)";

    public BasketDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        
        this.context = context;
        
        dbConnection = getWritableDatabase();
        
        if (dbConnection != null) {
        	Log.e("BasketDAO : ", "dbConnection" + "not null");
        }
        
        onUpgrade(dbConnection, 1, 2);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

	
	// instance method
	public static BasketDAO getInstance(Context context) {
		
        if (instance == null) {
        	instance = new BasketDAO(context);
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
     * Delete a basket
     * 
     * @param basket  The basket to delete
     * 
     */
    public int onDeleteBasket(Basket basket) {
    	
    	Log.e("BasketDAO : ", "onDeleteBasket" + basket.getName());
    	
    	String[] selectionArgs = {String.valueOf(basket.getId())};
    	
    	return dbConnection.delete("basket",  "id = ?", selectionArgs);
    	
    }
    
    /**
     * Update a basket
     * 
     * @param basket  The new basket to update
     * 
     */
    public int onUpdateBasket(Basket basket) {
    	
    	Log.e("BasketDAO : ", "onUpdateBasket" + basket.getName());
    	
    	String[] selectionArgs = {String.valueOf(basket.getId())};
    	ContentValues values = new ContentValues();
    	values.put("name", basket.getName());
    	values.put("type", basket.getType());
    	
    	return dbConnection.update("basket", values, "id = ?", selectionArgs);
    	
    }
    
    /**
     * Add a new basket to the table
     * 
     * @param basket  The new basket to add
     * 
     */
    public long onAddBasket(Basket basket) {
    	
    	Log.e("BasketDAO : ", "onAddBasket" + basket.getName());
    	
    	//onCreate(dbConnection);
    	
    	ContentValues values = new ContentValues();
    	values.put("name", basket.getName());
    	values.put("type", basket.getType());

    	long id = dbConnection.insert("basket", null, values);
    	
    	basket.setId(id);
    	
    	return id;

    }
    

    /**
     * Get the baskets
     * 
     * 
     */
    public List<Basket> onGetBaskets() {
    	
    	List<Basket> baskets = new ArrayList<Basket>();
    	String[] columns = {"id", "name", "type"};
    	String[] selectionArgs = null;
    	
    	Cursor cursor = dbConnection.query("basket", columns, null, null, null, null, "id");
    		
    	Log.e("BasketDAO : ", " Row Count : [" + cursor.getCount() + "]");
    	
		if  (cursor.moveToFirst()) {
		        do {
		        	Log.e("BasketDAO : ", "onGetBaskets");
		        	Basket basket = new Basket();
		        	
		        	basket.setId(cursor.getInt(cursor.getColumnIndex("id")));
		        	basket.setName(cursor.getString(cursor.getColumnIndex("name")));
		        	basket.setType(cursor.getString(cursor.getColumnIndex("type")));
		        	
		        	baskets.add(basket);
		        	
		    } while (cursor.moveToNext());
		}
		
		if(cursor!= null && !cursor.isClosed()) cursor.close();
		
		return baskets;
    	
    }


    
    public void onCreate(SQLiteDatabase db) {
    	Log.e("BasketDAO : ", "onCreate");
        db.execSQL(SQL_CREATE_ENTRIES);
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);

	}

}
