package nwl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GetLatestMCID {
	private String url = "https://sessionserver.mojang.com/session/minecraft/profile/";
	HttpURLConnection http;
	UUID uuid;

	public GetLatestMCID(UUID uuid) {
		this.uuid = uuid;
		url += uuid.toString().replaceAll("-", "");
		try {
			http = (HttpURLConnection) new URL(url).openConnection();
			http.setRequestMethod("GET");
			http.connect();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	public String get() {
		String response = null;
		try {
			if ((response = getResponse(http)) == null) {
				return null;
			}
		} catch (IOException e) {
			return null;
		}
		String name = null;
		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = jsonParser.parse(response).getAsJsonObject();
		name = jsonObject.get("name").getAsString();

		return name;
	}

	private String getResponse(HttpURLConnection urlConnection) throws IOException {
		int statusCode = urlConnection.getResponseCode();
		if (statusCode != 200) {
			urlConnection.disconnect();
			return null;
		}

		InputStream stream = urlConnection.getInputStream();
		StringBuffer sb = new StringBuffer();
		String line = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		stream.close();
		urlConnection.disconnect();
		return sb.toString();
	}
}
