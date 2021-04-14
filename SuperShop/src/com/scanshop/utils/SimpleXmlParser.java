/**
 * 
 */
package com.scanshop.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;
import android.util.Xml;

import com.scanshop.model.Product;

/**
 * @author smcardle
 *
 */
public class SimpleXmlParser {
    // We don't use namespaces
    private static final String ns = null;
   
    public static List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
   	     /*Reader reader = null;
	     reader = new InputStreamReader(in);        
	     char[] buffer = new char[500];
	     reader.read(buffer);
	     
	     
            Log.e("parse", "scan 7 " + new String(buffer));*/
            
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(new InputStreamReader(in));
            int eventType = parser.getEventType();
            parser.nextTag();
            Log.e("parser.nextTag();", "scan 7 " + "");
            return readFeed(parser);
        } catch (XmlPullParserException ex) {
	    	  Log.e("Error loading products file : " + ex, "");
        	return null;
        } finally {
            in.close();
        }
    }
 
    private static List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();
        //parser.require(XmlPullParser.START_TAG, ns, "A");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("X")) {
                entries.add(readProduct(parser));
            } else {
                skip(parser);
            }
        }  
        return entries;
    }
    
    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
            case XmlPullParser.END_TAG:
                depth--;
                break;
            case XmlPullParser.START_TAG:
                depth++;
                break;
            }
        }
     }
    
	 // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
	 // to their respective "read" methods for processing. Otherwise, skips the tag.
	 private static Product readProduct(XmlPullParser parser) throws XmlPullParserException, IOException {
		 
	     parser.require(XmlPullParser.START_TAG, ns, "X");
	     String description = null;
	     String barcode = null;
	     String price = null;
	     String category = null;
	     String subCategory = null;
	     String type = null;
	     
	     while (parser.next() != XmlPullParser.END_TAG) {
	         if (parser.getEventType() != XmlPullParser.START_TAG) {
	             continue;
	         }
	         String name = parser.getName();
	         if (name.equals("N")) {
	             description = readTag(parser, "N");
	         } else if (name.equals("B")) {
	             barcode = readTag(parser, "B");
	         } else if (name.equals("P")) {
	             price = readTag(parser, "P"); 
	        	 Log.e("Product : [", price);
	        	 
	        	 if (price == null || price.trim().equals("") || price.contains(",")) {
	        		 price = "0.00";
	        	 } else {
		             String trimmedPrice = price.trim();
		             if (trimmedPrice.contains(".")) {
			             String[] split = trimmedPrice.split("\\.");
			             String split0 = split[0];
			             String split1 = split[1];
			             int index = (split1.length() > 1) ? "1234567890".indexOf(split1.substring(1,2)) : -1;
			             String formattedCent = (index != -1) ? split1.substring(0,2) : split1.substring(0,1) + "0";
			             price = split0 + "." + formattedCent;
		             } else {
		            	 price = trimmedPrice + ".00";
		             }
	        	 }
	         } else if (name.equals("D")) {
	             category = readTag(parser, "D");
	         } else if (name.equals("F")) {
	             subCategory = readTag(parser, "F");
	         } else {
	             skip(parser);
	         }
	     }
	     
	     type = "t";
	     
	     Product product = new Product();
	     product.setDescription(description);
	     product.setBarcode(barcode);
	     product.setPrice(new Double(price).doubleValue());
	     product.setCategory(category);
	     product.setSubCategory(subCategory);
	     product.setType(type);
	     
	     Log.e("Product : [", barcode);
	     barcode = null;
	     return product;
	 }
	 
	 // Processes tags in the feed.
	 private static String readTag(XmlPullParser parser, String tagName) throws IOException, XmlPullParserException {
	     parser.require(XmlPullParser.START_TAG, ns, tagName);
	     String tag = readText(parser);
	     parser.require(XmlPullParser.END_TAG, ns, tagName);
	     return tag;
	 }
	 
	// For the tags title and summary, extracts their text values.
	 private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
	     String result = "";
	     if (parser.next() == XmlPullParser.TEXT) {
	         result = parser.getText();
	         parser.nextTag();
	     }
	     return result;
	 }


 
}
