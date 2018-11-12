package nwl;

import java.io.IOException;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetLatestMCID {
	private String url = "https://sessionserver.mojang.com/session/minecraft/profile/";
	OkHttpClient client;
	Request request;
	UUID uuid;

	public GetLatestMCID(UUID uuid) {
		this.uuid = uuid;
		url += uuid.toString().replaceAll("-", "");
		client = new OkHttpClient();
		request = new Request.Builder().url(url).build();
	}

	public String get() {
		String name = null;
		try {
			Response response = client.newCall(request).execute();
			JSONObject jsonObject = new JSONObject(response.body().string());
			name = jsonObject.getString("name");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return name;
	}
}
