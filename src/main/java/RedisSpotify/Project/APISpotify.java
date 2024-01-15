package RedisSpotify.Project;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class APISpotify {
	
	private static final String clientId = "543191e2fffa48ba958c42ea57c49ec0"; // reemplaza con tu Client ID
    private static final String clientSecret = "a4a4d78e02f04bf68f301a9ae1f627bb"; //
    
	 static String getAccessToken() throws Exception {
		 
        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost("https://accounts.spotify.com/api/token");

        String encoding = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
        post.setHeader("Authorization", "Basic " + encoding);
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        post.setEntity(new StringEntity("grant_type=client_credentials"));

        HttpResponse response = client.execute(post);
        String json = EntityUtils.toString(response.getEntity());

        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        return jsonObject.get("access_token").getAsString(); // Retorna el access token
        
    }
	 
	 static List<Artista> getSampleArtistIds(String accessToken, String nombreArtista) {
		    List<Artista> artistas = new ArrayList<>();
		    try {
		        HttpClient client = HttpClients.createDefault();
		        String query = nombreArtista;
		        HttpGet request = new HttpGet("https://api.spotify.com/v1/search?q=" + URLEncoder.encode(query, StandardCharsets.UTF_8) + "&type=artist");
		        
		        request.setHeader("Authorization", "Bearer " + accessToken);

		        HttpResponse response = client.execute(request);
		        String json = EntityUtils.toString(response.getEntity());
		        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
		        JsonArray artists = jsonObject.getAsJsonObject("artists").getAsJsonArray("items");

		        for (int i = 0; i < 1; i++) {
		            JsonObject artist = artists.get(i).getAsJsonObject();
		            String id = artist.get("id").getAsString();
		            String nombre = artist.get("name").getAsString();
		            artistas.add(new Artista(id, nombre));
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return artistas;
		}
	 
	   static String getArtistInfo(String artistId) throws Exception {
	    	String accessToken = getAccessToken();
	        HttpClient client = HttpClients.createDefault();
	        HttpGet request = new HttpGet("https://api.spotify.com/v1/artists/" + artistId);
	        request.setHeader("Authorization", "Bearer " + accessToken);

	        HttpResponse response = client.execute(request);
	        return EntityUtils.toString(response.getEntity());
	    }
}
