package com.duende84.webservicejson;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity {

	private final String LOG_TAG = "LOG";
	TextView JSONtext;
	ImageView IMAGEview;
	HttpClient cliente;
	JSONObject json;

	final static String URL = "http://androides.herokuapp.com/friends.json";

	private Drawable createDrawableFromURL(String urlString) {
		Drawable image = null;
		try {
			URL url = new URL(urlString);
			InputStream is = (InputStream) url.getContent();
			image = Drawable.createFromStream(is, "src");
		} catch (MalformedURLException e) {

			image = null;
		} catch (IOException e) {
			image = null;
		}

		return image;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

	}

	public void setea(View view) {
		JSONtext = (TextView) findViewById(R.id.jsontext);
		IMAGEview = (ImageView) findViewById(R.id.loaderImageView);
		cliente = new DefaultHttpClient();
		new Read().execute("foto");
	}

	public JSONObject usuario() throws ClientProtocolException, IOException,
			JSONException {

		StringBuilder url = new StringBuilder(URL);

		HttpGet get = new HttpGet(url.toString());
		HttpResponse r = cliente.execute(get);
		int status = r.getStatusLine().getStatusCode();
		if (status == 200) {
			HttpEntity e = r.getEntity();
			String data = EntityUtils.toString(e);
			JSONArray datos = new JSONArray(data);
			JSONObject la = datos.getJSONObject(0);
			return la;
		} else {
			Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT);
			return null;
		}
	}

	public class Read extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
				json = usuario();
				return json.getString(params[0]);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			IMAGEview.setImageDrawable(createDrawableFromURL(result));
			JSONtext.setText(result);
		}
	}

}
