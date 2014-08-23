package com.contoh.adapter;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.contoh.model.Product;
import com.hanselandpetal.catalog.MainActivity;

public class FlowerAdapter extends ArrayAdapter<Product> {
	
	private Context context;
	private List<Product> productList;

	public FlowerAdapter(Context context, int resource,List<Product> objects) {
		super(context, resource, objects);
		
		this.context = context;
		this.productList = objects;
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(com.hanselandpetal.catalog.R.layout.item_layout, parent, false);
		
		Product product = productList.get(position);
		TextView productName =  (TextView)view.findViewById(com.hanselandpetal.catalog.R.id.textView1);
		
		productName.setText(product.getName());
		
		if(product.getBitmap()!=null){
			ImageView imageView = (ImageView)view.findViewById(com.hanselandpetal.catalog.R.id.imageView1);
			imageView.setImageBitmap(product.getBitmap());
		}
		else{
			FlowerAndView container =new FlowerAndView ();
			ImageLoader imageLoader = new ImageLoader();
			container.product = product;
			container.view = view;
			
			imageLoader.execute(container);
		}
		
		return view;
	}
	
	class FlowerAndView {
		public Product product;
		public View view;
		public Bitmap bitmap;
		
	}
	
	private class ImageLoader extends AsyncTask<FlowerAndView, Void, FlowerAndView>{

		@Override
		protected FlowerAndView doInBackground(FlowerAndView... FlowerAndViews) {
			FlowerAndView container = FlowerAndViews[0];
			
			Product product = container.product;
			
			String imageUrl = MainActivity.PHOTO_BASE_URL + product.getPhoto();

			try {
				InputStream input = (InputStream) new URL(imageUrl).getContent();
				Bitmap bitmap = BitmapFactory.decodeStream(input);
				product.setBitmap(bitmap);
				input.close();
				container.bitmap=bitmap;
				return container;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(FlowerAndView result) {
			ImageView imageView = (ImageView)result.view.findViewById(com.hanselandpetal.catalog.R.id.imageView1);
			imageView.setImageBitmap(result.bitmap);
		}
		
	}
	
	

}
