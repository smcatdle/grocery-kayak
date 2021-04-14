

package com.scanshop.dao;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.scanshop.model.Category;
import com.scanshop.model.Product;
import com.scanshop.model.Shelf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;


public class ProductDAO extends SQLiteOpenHelper {

	private static ProductDAO instance = null;
	private static SQLiteDatabase dbConnection = null;
	
    public static final int DATABASE_VERSION = 1;
    //public static final String DATABASE_NAME = Environment.getExternalStorageDirectory().getPath() + "/downloads/scanshop/ScanShop_v1.db";
    public static final String DATABASE_NAME = "ScanShop_v1.db";
    public static final String DATABASE_PATH = "/data/data/com.mymobilebasket/databases/";
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS product";
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS product (id INTEGER PRIMARY KEY, barcode TEXT, description TEXT, price DOUBLE, imageurl TEXT, category TEXT, subcategory TEXT, type TEXT)";

    public ProductDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        dbConnection = getWritableDatabase();
        
        if (dbConnection != null) {
        	Log.e("DBHelper : ", "dbConnection" + "not null");
        }
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

	
	// instance method
	public static ProductDAO getInstance(Context context) {
		
        if (instance == null) {
        	instance = new ProductDAO(context);
        }
        
        return instance;
    }
   
    /**
     * Update a product
     * 
     * @param product  The new product content to update
     * 
     */
    public int onUpdateProduct(Product product) {
    	
    	String[] selectionArgs = {product.getBarcode()};
    	ContentValues values = new ContentValues();
    	values.put("description", product.getDescription());
    	values.put("price", product.getPrice());
    	//values.put("imageurl", product.getUrl());
    	values.put("category", product.getCategory());
    	values.put("subcategory", product.getSubCategory());
    	values.put("type", product.getType());
    	
    	return dbConnection.update("product", values, "barcode = ?", selectionArgs);
    	
    }
    
    /**
     * Add a new product to the table
     * 
     * @param product  The new product content to add
     * 
     */
    public long onAddProduct(Product product) {
    	
    	Log.e("DBHelper : ", "onAddProduct" + product.getBarcode());
    	
    	ContentValues values = new ContentValues();
    	values.put("description", product.getDescription());
    	values.put("price", product.getPrice());
    	values.put("barcode", product.getBarcode());
    	values.put("category", product.getCategory());
    	values.put("subcategory", product.getSubCategory());
    	values.put("type", product.getType());
    	
    	return dbConnection.insert("product", null, values);
    }
    

    /**
     * Get the products
     * 
     * @param product  The product criteria to use
     * 
     */
    public List<Product> onGetProducts(Product product) {
    	
    	List<Product> products = new ArrayList<Product>();
    	String[] columns = {"barcode", "description", "price"/*, "imageUrl"*/, "category", "subcategory", "type"};
    	String[] selectionArgs = {product.getBarcode()};
    	
    	Cursor cursor = dbConnection.query("product", columns, "barcode = ?" , selectionArgs, null, null, "barcode");
    		
		if  (cursor.moveToFirst()) {
		        do {
		        	Log.e("DBHelper : ", "onGetProducts" + product.getBarcode());
		        	Product p = new Product();
		        	
		        	p.setDescription(cursor.getString(cursor.getColumnIndex("description")));
		        	p.setBarcode(cursor.getString(cursor.getColumnIndex("barcode")));
		        	p.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
		        	//p.setUrl(cursor.getString(cursor.getColumnIndex("imageUrl")));
		        	p.setCategory(cursor.getString(cursor.getColumnIndex("category")));
		        	p.setSubCategory(cursor.getString(cursor.getColumnIndex("subcategory")));
		        	p.setType(cursor.getString(cursor.getColumnIndex("type")));
		        	
		        	products.add(p);
		        	
		    } while (cursor.moveToNext());
		}
		
		if(cursor!= null && !cursor.isClosed()) cursor.close();
		
		return products;
    	
    }
    
    /**
     * Get the list of all shelf names for a given chain
     * 
     * @param chain  The supermarket chain to search
     * 
     */
    public List<Shelf> onGetShelves(String chain) {
    	
    	List<Shelf> shelves = new ArrayList<Shelf>();
    	String[] columns = {"category", "subcategory", "type"};
    	String[] selectionArgs = {chain};
    	
    	//Cursor cursor = dbConnection.query(true, "product", columns, "type = ?" , selectionArgs, null, null, "type");
    	Cursor cursor = dbConnection.query(true, "product", columns, null, null, "subcategory", null, null, null);
    		
		if  (cursor.moveToFirst()) {
		        do {
		        	Log.e("DBHelper : ", "onGetShelves  " + cursor.getString(cursor.getColumnIndex("category")) + ":" + cursor.getString(cursor.getColumnIndex("subcategory")));
		        	Shelf shelf = new Shelf();
		        	
		        	shelf.setChain(cursor.getString(cursor.getColumnIndex("type")));
		        	shelf.setCategory(cursor.getString(cursor.getColumnIndex("category")));
		        	shelf.setShelfName(cursor.getString(cursor.getColumnIndex("subcategory")));
		        	
		        	shelves.add(shelf);
		        	
		    } while (cursor.moveToNext());
		}
		
		if(cursor!= null && !cursor.isClosed()) cursor.close();
		
		return shelves;
    	
    }
    
    /**
     * Get the list of all shelf names for a given category and chain
     * 
     * @param chain  The supermarket chain to search
     * 
     */
    public List<Shelf> onGetCategoryShelves(String category, String chain) {
    	
    	List<Shelf> shelves = new ArrayList<Shelf>();
    	String[] columns = {"category", "subcategory", "type"};
    	String[] selectionArgs = {category, chain};
    	
    	//Cursor cursor = dbConnection.query(true, "product", columns, "type = ?" , selectionArgs, null, null, "type");
    	Cursor cursor = dbConnection.query(true, "product", columns, "category=? and type = ?", selectionArgs, "subcategory", null, null, null);
    		
		if  (cursor.moveToFirst()) {
		        do {
		        	Log.e("DBHelper : ", "onGetCategoryShelves  " + cursor.getString(cursor.getColumnIndex("category")) + ":" + cursor.getString(cursor.getColumnIndex("subcategory")));
		        	Shelf shelf = new Shelf();
		        	
		        	shelf.setChain(cursor.getString(cursor.getColumnIndex("type")));
		        	shelf.setCategory(cursor.getString(cursor.getColumnIndex("category")));
		        	shelf.setShelfName(cursor.getString(cursor.getColumnIndex("subcategory")));
		        	
		        	shelves.add(shelf);
		        	
		    } while (cursor.moveToNext());
		}
		
		if(cursor!= null && !cursor.isClosed()) cursor.close();
		
		return shelves;
    	
    }
    
    
    /**
     * Get the list of all categories for a given chain
     * 
     * @param chain  The supermarket chain to search
     * 
     */
    public List<Category> onGetCategories(String chain) {
    	
    	List<Category> categories = new ArrayList<Category>();
    	String[] columns = {"category", "type"};
    	String[] selectionArgs = {chain};

    	//Cursor cursor = dbConnection.query(true, "product", columns, "type = ?", selectionArgs, "category", null, null, null);
    	Cursor cursor = dbConnection.query(true, "product", columns, "type=?", selectionArgs, null, null, null, null);
    	
		if  (cursor.moveToFirst()) {
		        do {
		        	Log.e("DBHelper : ", "onGetCategories  " + cursor.getString(cursor.getColumnIndex("category")));
		        	Category category = new Category();
		        	
		        	category.setChain(cursor.getString(cursor.getColumnIndex("type")));
		        	category.setCategory(cursor.getString(cursor.getColumnIndex("category")));
		        	
		        	categories.add(category);
		        	
		    } while (cursor.moveToNext());
		}
		
		if(cursor!= null && !cursor.isClosed()) cursor.close();
		
		return categories;
    	
    }
    
    public void onCreate(SQLiteDatabase db) {
    	Log.e("DBHelper : ", "onCreate");
        db.execSQL(SQL_CREATE_ENTRIES);
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);

	}
    
    /**
     * Copies the database from assets directory when initially installed.
     * 
     * @throws IOException
     */
    private void copyDataBase(Context context) throws IOException {
        InputStream myInput = context.getAssets().open(DATABASE_NAME);
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }
}
