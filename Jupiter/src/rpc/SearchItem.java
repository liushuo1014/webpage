package rpc;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;

import java.util.List;
import java.util.Set;

import external.TicketMasterClient;
import external.GeoHash;
import entity.Item;
/**
 * Servlet implementation class SearchItem
 */
@WebServlet("/search")
public class SearchItem extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchItem() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null) {
			response.setStatus(403);
			return;
		}

		// optional
		String userId = session.getAttribute("user_id").toString(); 
		//String userId = request.getParameter("user_id");
		double lat = Double.parseDouble(request.getParameter("lat"));
		double lon = Double.parseDouble(request.getParameter("lon"));
		String term = request.getParameter("term");
		DBConnection connection = DBConnectionFactory.getConnection();
		try {
			//since we already define searchItems in MySQLConnection using ticketMasterClient
			//we can directly use searchItems in MySQLConnection
			List<Item> items = connection.searchItems(lat, lon, term);
			Set<String> favoriteItemIds = connection.getFavoriteItemIds(userId);
			JSONArray array = new JSONArray();
			//loop thru requestions to check if the item is being favoured
			for (Item item: items) {
				JSONObject obj = item.toJsonObject();
				obj.put("favorite", favoriteItemIds.contains(item.getItemId()));
				array.put(obj);
				//array.put(item.toJsonObject());
			}
			RpcHelper.writeJsonArray(response, array);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			connection.close();
		}
//		TicketMasterClient client = new TicketMasterClient();
//		//RpcHelper.writeJsonArray(response, client.search(lat,lon,null));
//		
//		//change to JSONArray since RpcHelper is taking JSONArray
//		List<Item>items = client.search(lat, lon, null);
//		JSONArray array = new JSONArray();
//		for (Item item: items) {
//			array.put(item.toJsonObject());
//		}
//		RpcHelper.writeJsonArray(response, array);
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
