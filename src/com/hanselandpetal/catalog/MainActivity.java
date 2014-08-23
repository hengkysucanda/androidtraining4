package com.hanselandpetal.catalog;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.contoh.model.Product;
import com.contoh.parsers.ProductJSONPars;

public class MainActivity extends Activity {

	TextView output;
	ProgressBar pb;
	List<MyTask> tasks;
	List <Product> products;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
//		Initialize the TextView for vertical scrolling
		output = (TextView) findViewById(R.id.textView);
		output.setMovementMethod(new ScrollingMovementMethod());
		
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
		for(Product prd : products){
			output.append(prd.getName()+"\n");
			
		}
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
			Log.e("CALVIN",data+ " " + params[0]);
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
			
			products = ProductJSONPars.parseProduct(result);
			
			
			updateDisplay();
			
			tasks.remove(this);
			
			if(tasks.size()==0)
			pb.setVisibility(View.INVISIBLE);
		}
		
	}

}