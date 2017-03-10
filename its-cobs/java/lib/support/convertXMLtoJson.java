package lib.support;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

public class convertXMLtoJson {
	static final String XML_PATH = "resources/localization.xml";
	static JSONObject json;
	
	public static JSONObject getJSonLocalization() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(XML_PATH));
		String line = "", str = "";
		
		while ((line = br.readLine()) != null) {
			str += line;
		}
		json = XML.toJSONObject(str);
		
		br.close();
		return json;
	}
	
	public static void main(String[] args) throws JSONException, IOException {
		System.out.println(getJSonLocalization());
	}
}
