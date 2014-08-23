package com.hanselandpetal.catalog;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.contoh.adapter.FlowerAdapter;
import com.contoh.model.Product;
import com.contoh.parsers.ProductJSONPars;

public class MainActivity extends ListActivity {

	ProgressBar pb;
	List<MyTask> tasks;
	List <Product> products;
	
	public static final String PHOTO_BASE_URL = "http://services.hanselandpetal.com/photos/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
//		Initialize the TextView for vertical scrolling
		
		pb=(ProgressBar) findViewById(R.id.progressBar1);
		pb.setVisibility(View.INVISIBLE);
		tasks=new ArrayList<MainActivity.MyTask>();
		products = new ArrayList<Product>();
		//isOnline();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_do_task) {
			if(isOnline()){
				requestData();
			}else{
				Toast.makeText(this, "Network is not available", Toast.LENGTH_LONG).show();
			}
		}
		return false;
	}

	private void requestData() {
		MyTask task = new MyTask();
		task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://services.hanselandpetal.com/secure/flowers.json");
	}

	protected void updateDisplay() {
		FlowerAdapter adapter = new FlowerAdapter(this, R.layout.item_layout, products);
		setListAdapter(adapter);
	}
	protected boolean isOnline (){
		ConnectivityManager cm  = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if(netInfo!=null && netInfo.isConnectedOrConnecting()){
			return true;
		}
		return false;
	}
	public class MyTask extends AsyncTask<String, String, String> {
		
		@Override
		protected void onPreExecute() {
			
			if(tasks.size()==0){
				pb.setVisibility(View.VISIBLE);
			}
			
			tasks.add(this);
		}
		
		@Override
		protected String doInBackground(String... params) {
		
			String data = HttpManager.getData(params[0],"feeduser","feedpassword");
			products = ProductJSONPars.parseProduct(data);
			
			
			
			return data;
			//return "Task complete";
		}
		
		
		@Override
		protected void onProgressUpdate(String... values) {
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(result==null){
				Toast.makeText(MainActivity.this, "Can't connect to web service", Toast.LENGTH_LONG).show();
				return;
			}
			
			updateDisplay();
			
			tasks.remove(this);
			
			if(tasks.size()==0)
			pb.setVisibility(View.INVISIBLE);
		}
		
	}

}