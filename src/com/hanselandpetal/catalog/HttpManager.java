package com.hanselandpetal.catalog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Base64;

public class HttpManager {
	public static String getData(String uri){
		BufferedReader reader = null;
		URL url;
		try {
			url = new URL(uri);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			StringBuilder sb = new StringBuilder();
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line;
			while((line = reader.readLine()) != null){
				sb.append(line + "\n");
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}
		
		public static String getData(String uri,String username, String password){
			BufferedReader reader = null;
			
			byte[] loginbytes = (username + ":" +password).getBytes();
			StringBuilder loginBuilder = new StringBuilder()
				.append("Basic ")
				.append(Base64.encodeToString(loginbytes, Base64.DEFAULT));
			
			URL url;
			try {
				url = new URL(uri);
				HttpURLConnection con = (HttpURLConnection)url.openConnection();
				
				con.addRequestProperty("Authorization", loginBuilder.toString());
				
				StringBuilder sb = new StringBuilder();
				reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String line;
				while((line = reader.readLine()) != null){
					sb.append(line + "\n");
				}
				return sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}finally{
				if(reader != null){
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
		}
//		punya apache
//		AndroidHttpClient client = AndroidHttpClient.newInstance("AndroidAgent");
//		HttpGet request = new HttpGet(uri);
//		HttpResponse response;
//		try {
//			response = client.execute(request);
//			return EntityUtils.toString(response.getEntity());
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}finally{
//			client.close();
//		}
	
}
