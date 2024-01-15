package RedisSpotify.Project;

public class Artista {
    private String id;
    private String nombre;

    public Artista(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Getters y setters	
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

	@Override
	public String toString() {
		return "Artista [id=" + id + ", nombre=" + nombre + "]";
	}
}