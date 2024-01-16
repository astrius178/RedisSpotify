package RedisSpotify.Project;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class Principal 
{
    public static void main( String[] args )
    {
    	Jedis jedis = new Jedis("localhost", 6379);
    	
    	List<Artista> artistIds = new ArrayList<>();
    	
    	try {
			String acceso = APISpotify.getAccessToken();
			Scanner scanner = new Scanner(System.in);
			System.out.println("Introduce el nombre del artisata a buscar:");
		    String nombreArtista = scanner.nextLine();
			artistIds = APISpotify.getSampleArtistIds(acceso,nombreArtista);
			for (Artista ar:artistIds) {
				System.out.println(ar);
				guardarArtistaRedis(jedis, ar);
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		System.out.println("hola");
		}
    }
    private static void guardarArtistaRedis(Jedis jedis, Artista artista) {
        String key = "artista:" + artista.getId();
        String nombre = artista.getNombre();
       
        // Iniciar una transacción
        Transaction t = jedis.multi();
        try {
            t.set(key, nombre);
            // Ejecutar la transacción
            t.exec();
        } catch (Exception e) {
            // En caso de error, descartar la transacción
            t.discard();
        }
    }
}
