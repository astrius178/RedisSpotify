package RedisSpotify.Project;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class Principal 
{
    public static void main( String[] args )
    {
    	try {
            // Configura tus credenciales de Spotify
            String clientId = "tu_client_id"; //Esta en e txt que os he pasado
            String clientSecret = "tu_client_secret"; //Esta en e txt que os he pasado

            // Paso 1: Obtener el token de acceso
            HttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost("https://accounts.spotify.com/api/token");

            String encoding = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
            post.setHeader("Authorization", "Basic " + encoding);
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");
            post.setEntity(new StringEntity("grant_type=client_credentials"));

            HttpResponse response = client.execute(post);
            String json = EntityUtils.toString(response.getEntity());
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
            String accessToken = jsonObject.get("access_token").getAsString();

            // Paso 2: Realizar una solicitud para obtener artistas
            String query = "Bad"; // Reemplaza con el nombre del artista que deseas buscar
            query = URLEncoder.encode(query, StandardCharsets.UTF_8);

            HttpGet getArtists = new HttpGet("https://api.spotify.com/v1/search?q=" + query + "&type=artist");
            getArtists.setHeader("Authorization", "Bearer " + accessToken);

            HttpResponse artistResponse = client.execute(getArtists);
            String artistJson = EntityUtils.toString(artistResponse.getEntity());
            JsonObject artistObject = JsonParser.parseString(artistJson).getAsJsonObject();

            // Procesar la respuesta para obtener los artistas
            JsonObject artists = artistObject.getAsJsonObject("artists");
            JsonArray items = artists.getAsJsonArray("items");

            for (JsonElement item : items) {
                JsonObject artist = item.getAsJsonObject();
                String artistName = artist.get("name").getAsString();
                System.out.println("Artista: " + artistName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
