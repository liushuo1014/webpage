package db.mongodb;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.catalina.storeconfig.IStoreConfig;
import org.bson.Document;
import org.eclipse.jdt.internal.compiler.ast.AND_AND_Expression;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

//import STATIC method "eq", eq only used to filter items.
import static com.mongodb.client.model.Filters.eq;

import db.DBConnection;
import entity.Item;
import entity.Item.ItemBuilder;
import external.TicketMasterClient;

public class MongoDBConnection implements DBConnection {
	private MongoClient mongoClient;
	private MongoDatabase db;
	
	//ctor for MongoDBConnection to make connection and database
	public MongoDBConnection() {
		try {
			//establish the connection
			mongoClient = MongoClients.create();
			db = mongoClient.getDatabase(MongoDBUtil.DB_NAME);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void close() {
		if (mongoClient != null) {
			try {
				mongoClient.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean registerUser(String userId, String password, String firstname, String lastname) {
		FindIterable<Document> iterable = db.getCollection("users").find(eq("user_id", userId));
		if (iterable.first() == null) {
			db.getCollection("users").insertOne(new Document().append("first_name", 
					firstname).append("last_name", lastname).append("password",
							password).append("user_id", userId));
			return true;
		}
		return false;
 	}

	@Override
	public void setFavoriteItems(String userId, List<String> itemIds) {
		// TODO Auto-generated method stub
		if (db == null) {
			return;
		}
		try {
			db.getCollection("users").updateOne(eq("user_id",userId), new Document("$push", 
					new Document("favorite", new Document("$each", itemIds))));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@Override
	public void unsetFavoriteItems(String userId, List<String> itemIds) {
		// TODO Auto-generated method stub
		if (db == null) {
			return;
		}
		try {
			db.getCollection("users").updateOne(eq("user_id", userId), new Document("$pullAll", 
					new Document("favorite", itemIds)));
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public Set<String> getFavoriteItemIds(String userId) {
		// TODO Auto-generated method stub
		if (db == null) {
			return new HashSet<>();
		}
		Set<String> favoriteItems = new HashSet<String>();
		FindIterable<Document> iterable = db.getCollection("users").find(eq("user_id", userId));
		if (iterable.first() != null && iterable.first().containsKey("favorite") ) {
			@SuppressWarnings("unchecked")
			List<String> favorList = (List<String>) iterable.first().get("favorite");
			favoriteItems.addAll(favorList);
		}
		return favoriteItems;
	}

	@Override
	public Set<Item> getFavoriteItems(String userId) {
		// TODO Auto-generated method stub
		if (db == null) {
			return new HashSet<>();
		}
		Set<Item> favoriteItems = new HashSet<>();
		Set<String> itemIds = getFavoriteItemIds(userId);
		for(String itemId: itemIds) {
			FindIterable<Document> iterable = db.getCollection("items").find(eq("item_id", itemId));
			if(iterable.first() != null) {
				Document doc = iterable.first();	
				ItemBuilder builder = new ItemBuilder();
				builder.setItemId(doc.getString("item_id"));
				builder.setName(doc.getString("name"));
				builder.setAddress(doc.getString("address"));
				builder.setUrl(doc.getString("url"));
				builder.setImageUrl(doc.getString("image_url"));
				builder.setRating(doc.getDouble("rating"));
				builder.setDistance(doc.getDouble("distance"));
				builder.setCategories(getCategories(itemId));
				
				favoriteItems.add(builder.build());

			}
		}
		return favoriteItems;
	}

	@Override
	public Set<String> getCategories(String itemId) {
		// TODO Auto-generated method stub
		if (db == null) {
			return new HashSet<>();
		}
		Set<String> categories = new HashSet<>();
		FindIterable<Document> iterable = db.getCollection("items").find(eq("item_id", itemId));
		if(iterable.first() != null && iterable.first().containsKey("categories")) {
			@SuppressWarnings("unchecked")
			List<String> list = (List<String>) iterable.first().get("categories");
			categories.addAll(list);
		}
		
		return categories;
	}

	@Override
	public List<Item> searchItems(double lat, double lon, String term) {
		List<Item> items = null;
		try {
			TicketMasterClient ticketMasterClient = new TicketMasterClient();
			items = ticketMasterClient.search(lat, lon, term);
			for(Item item: items) {
				saveItem(item);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
				
		
		return items;
		
	}

	@Override
	public void saveItem(Item item) {
		// TODO Auto-generated method stub
		if (db == null) {
			return;
		}
		try {
			//find to see if this item has been saved, else create new document and save.
			FindIterable<Document> iterable = db.getCollection("items").find(eq("item_id", item.getItemId()));
			if (iterable.first() == null) {
				db.getCollection("items").insertOne(new Document().append("item_id", item.getItemId()).append("name", 
						item.getName()).append("address",item.getAddress()).append("distance", 
								item.getDistance()).append("url", item.getUrl()).append("rating", 
										item.getRating()).append("image_url", 
												item.getImageUrl()).append("categoties", item.getCategories()));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		

	}

	@Override
	public String getFullname(String userId) {
		FindIterable<Document> iterable = db.getCollection("users").find(eq("user_id", userId));
		if (iterable.first() != null) {
			String firstname = iterable.first().getString("first_name");
			String lastname = iterable.first().getString("last_name");
			return firstname + ' ' + lastname;
		}
		return "full name not found";
	}

	@Override
	public boolean verifyLogin(String userId, String password) {
		FindIterable<Document> iterable = db.getCollection("users").find(eq("user_id", userId));
		if (iterable.first() != null) {
			String passwordInDB = iterable.first().getString("password");
			return passwordInDB.equals(password);
		}
		return false;
	}

}
