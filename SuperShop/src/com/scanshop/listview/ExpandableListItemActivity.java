package com.scanshop.listview;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.propertyprice.listview.MyListActivity;
import com.haarman.listviewanimations.itemmanipulation.ExpandableListItemAdapter;
import com.haarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;
import com.scanshop.R;
import com.scanshop.model.Basket;
import com.scanshop.model.BasketItem;

public class ExpandableListItemActivity extends MyListActivity {

	private MyExpandableListItemAdapter mExpandableListItemAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mExpandableListItemAdapter = new MyExpandableListItemAdapter(this, getItems());
		AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(mExpandableListItemAdapter);
		alphaInAnimationAdapter.setAbsListView(getListView());
		alphaInAnimationAdapter.setInitialDelayMillis(500);
		getListView().setAdapter(alphaInAnimationAdapter);

		//Toast.makeText(this, R.string.explainexpand, Toast.LENGTH_LONG).show();
	}

	public static class MyExpandableListItemAdapter extends ExpandableListItemAdapter<BasketItem> {

		private Context mContext;
		private LruCache<Integer, Bitmap> mMemoryCache;

		/**
		 * Creates a new ExpandableListItemAdapter with the specified list, or an empty list if
		 * items == null.
		 */
		public MyExpandableListItemAdapter(Context context, List<BasketItem> items) {
			super(context, R.layout.activity_expandablelistitem_card, R.id.activity_expandablelistitem_card_title, R.id.activity_expandablelistitem_card_content/*, R.id.btn_expand*/, items);
			mContext = context;

			final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

			// Use 1/8th of the available memory for this memory cache.
			final int cacheSize = maxMemory;
			mMemoryCache = new LruCache<Integer, Bitmap>(cacheSize) {
				@Override
				protected int sizeOf(Integer key, Bitmap bitmap) {
					// The cache size will be measured in kilobytes rather than
					// number of items.
					return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
				}
			};
		}

		@Override
		public View getTitleView(int position, View convertView, ViewGroup parent) {
			TextView tv = (TextView) convertView;
			if (tv == null) {
				tv = new TextView(mContext);
			}
			tv.setText(mContext.getString(R.string.expandorcollapsecard, ((BasketItem)getItem(position)).getDescription()));
			return tv;
		}

		@Override
		public View getContentView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = (ImageView) convertView;
			if (imageView == null) {
				imageView = new ImageView(mContext);
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			}

			int imageResId;
			imageResId = R.drawable.img_nature1;

			Bitmap bitmap = getBitmapFromMemCache(imageResId);
			if (bitmap == null) {
				bitmap = BitmapFactory.decodeResource(mContext.getResources(), imageResId);
				addBitmapToMemoryCache(imageResId, bitmap);
			}
			imageView.setImageBitmap(bitmap);

			return imageView;
		}

		private void addBitmapToMemoryCache(int key, Bitmap bitmap) {
			if (getBitmapFromMemCache(key) == null) {
				mMemoryCache.put(key, bitmap);
			}
		}

		private Bitmap getBitmapFromMemCache(int key) {
			return mMemoryCache.get(key);
		}
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_expandablelistitem, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_expandable_limit:
			mLimited = !mLimited;
			item.setChecked(mLimited);
			mExpandableListItemAdapter.setLimit(mLimited ? 2 : 0);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}*/
}
