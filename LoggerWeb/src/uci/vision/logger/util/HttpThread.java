package uci.vision.logger.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpThread extends Thread{
	String url;
	public HttpThread(String url){
		this.url = url;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();

		HttpClient httpclient = new DefaultHttpClient();
		try {
			//String configHost = LogConfig.readLogConfig().readLogConfig().getConfigHost();
			HttpGet httpget = new HttpGet(url);

			System.out.println("executing request " + httpget.getURI());

			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));

				//	        String line = "";
				//	        while ((line = rd.readLine()) != null) {
				//	          return line;
				//	        }
			}
			httpget.abort();
			httpclient.getConnectionManager().shutdown();

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}

	}
}