package com.jmr.db;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Database {

	private static Database instance = new Database();
	
	private Database() {	
	}
	
	public PlayerInformation getPlayerInformation(String name) {
		try {
			String url = "http://droid-rat.com/bukkit/getInfo.php";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	 
			//add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			
			String urlParameters = "name=" + name;
	 
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
	 
			int responseCode = con.getResponseCode();
	 
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			String[] split = response.toString().split(" ");
			PlayerInformation info = new PlayerInformation(name, split[0], Integer.parseInt(split[1]));
			return info;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Database getInstance() {
		return instance;
	}
	
}
