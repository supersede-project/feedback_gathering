package monitoring.tools;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import monitoring.model.MonitoringData;

public class TestSocialSearcherAPI {

	public static void main(String[] args) {

		try {
			
			long start = System.currentTimeMillis();

			String key = "63dc2a9e0fb6693c36ac5c1b0edadd87";
			String keywordExpression = "(Angela Merkel OR Donald Trump) AND (Emmanuel Macron)";
			String q = "\"" + keywordExpression.replaceAll(" AND ", "\"AND\"")
					.replaceAll("[()]", "").replaceAll(" OR ", "\"OR\"") + "\"";
			System.out.println("keywordExpression: " + q + "\n");
			String network = "twitter";
			String fields = "text,user,posted,url";
			String limit = "100";
			String request = "https://api.social-searcher.com/v2/search?q=" + URLEncoder.encode(q, "UTF-8") + "&key="
					+ key + "&network=" + network + "&fields=" + fields + "&limit=" + limit;

			URL url = new URL(request);
			URLConnection con = url.openConnection();
			InputStream in = con.getInputStream();
			String encoding = con.getContentEncoding();
			encoding = encoding == null ? "UTF-8" : encoding;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buf = new byte[8192];
			int len = 0;
			while ((len = in.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}
			String content = new String(baos.toByteArray(), encoding);
			baos.close();
			in.close();

			JSONArray json_array = new JSONObject(content).getJSONArray("posts");

			List<MonitoringData> data = new ArrayList<>();
			for (int i = 0; i < json_array.length(); i++) {

				JSONObject json = json_array.getJSONObject(i);
				JSONObject user = json.getJSONObject("user");
				String author = "@" + user.getString("url").substring(user.getString("url").lastIndexOf("/") + 1,
						user.getString("url").length());
				String id = json.getString("url").substring(json.getString("url").lastIndexOf("/") + 1,
						json.getString("url").length());
				String timeStamp = json.getString("posted");
				String message = json.getString("text");
				String link = json.getString("url");
				MonitoringData dataObj = new MonitoringData(id, timeStamp, message, author, link);
				data.add(dataObj);

			}
			
			double elapsedTime = (System.currentTimeMillis() - start) / 1000.0;
			System.out.printf("Data sent successfully! Elapsed Time: %.2fsec", elapsedTime);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
