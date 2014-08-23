package com.contoh.parsers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.contoh.model.Product;

public class ProductJSONPars {
	public static List<Product> parseProduct(String content){
		try {
			JSONArray ar = new JSONArray(content);
			List<Product> productList = new ArrayList<Product>();
			for (int i = 0; i < ar.length(); i++) {
				JSONObject obj = ar.getJSONObject(i);
				Product product = new Product();
				product.setProductId(obj.getInt("productId"));
				product.setCategory(obj.getString("category"));
				product.setInstructions(obj.getString("instructions"));
				product.setPhoto(obj.getString("photo"));
				product.setPrice(obj.getDouble("price"));
				product.setName(obj.getString("name"));
				productList.add(product);
				
			}
			return productList;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}
