package com.jmr.pmanager;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Database {

	private static Database instance = new Database();
	private static final String SECRET_CODE_1 = "7aMGW8N9NXwknuns8CyvSlaI4LbC9dBixTDmN0NuU2HMAoW3z";
	private static final String SECRET_CODE_2 = "GNXL6iok2tSVmpo21zlhdBNdhbd9xALpORkzSqgAodybOIEaS";
	
	private Database() {	
	}
	
	public PlayerInformation getPlayerInformation(String uuid) {
		try {
			String url = "http://droid-rat.com/bukkit/getInfo.php";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	 
			//add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			
			String urlParameters = "uuid=" + uuid;
			
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
	 
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			String[] split = response.toString().split(" ");
			System.out.println(response);
			PlayerInformation info = new PlayerInformation(uuid, split[1], Integer.parseInt(split[2]), (split.length == 4 ? split[3].replace("_", " ").split(",") : new String[] {}));
			return info;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void savePlayer(PlayerInformation info) {
		try {
			String url = "http://droid-rat.com/bukkit/saveInfo.php";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	 
			//add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			
			String ownedSpells = "";
			for (String item : info.getOwnedSpells()) {
				ownedSpells += item + ",";
			}
			String urlParameters = "name=" + info.getName() + "&uuid=" + info.getUuid() + "&rank=default&coins=" + info.getCoins() + "&ownedSpells=" + ownedSpells + "&s1=" + SECRET_CODE_1 + "&s2=" + SECRET_CODE_2;
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Database getInstance() {
		return instance;
	}
	
}
