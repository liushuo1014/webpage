package rpc;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entity.Item;

public class RpcHelper {
	
	public static JSONObject readJSONObject(HttpServletRequest request) {
		StringBuilder sBuilder = new StringBuilder();
		try(BufferedReader reader = request.getReader()) {
			String line = null;
			while((line = reader.readLine()) != null) {
				sBuilder.append(line);
			}
			return new JSONObject(sBuilder.toString());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new JSONObject();
	}
	//write a JSONArray to HTTP response
	public static void writeJsonArray(HttpServletResponse response,JSONArray list) throws IOException{
		response.setContentType("application/json");
		//response.setHeader("Access-Control-Allow-Origin", "*");
//		try {
//			response.getWriter().print(array.getJSONObject(1).getString("username"));
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		};	
		response.getWriter().print(list);

	}
	
	//write a JSONObject to HTTP response
	public static void writeJsonObject(HttpServletResponse response, JSONObject obj) throws IOException{
		response.setContentType("application/json");
		//response.setHeader("Access-Control-Allow-Origin", "*");

		response.getWriter().print(obj);
	}
}
