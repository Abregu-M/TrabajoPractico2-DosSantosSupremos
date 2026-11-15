package paqueteEquipamiento;

public class Equipamiento {

    private String nombre;
    private CategoriaEquipamiento categoria;

    private final int modificadorDanioProvocado;
    private final int modificadorDanioRecibido;
    private final int modificadorVidaMaxima;
    private final int modificadorEnergiaMaxima;
    
    public CategoriaEquipamiento getCategoria() {
        return categoria;
    }

    public Equipamiento(String nombre, CategoriaEquipamiento categoria, int modificadorDanioProvocado,
			int modificadorDanioRecibido, int modificadorVidaMaxima, int modificadorEnergiaMaxima) {
		
    	if (nombre == null || nombre.trim().isEmpty()) {
	        throw new IllegalArgumentException("El nombre no puede estar vacío.");
	    }
    	if (categoria == null) {
    	    throw new IllegalArgumentException("La categoría no puede ser nula.");
    	}
    	
		this.nombre = nombre;
		this.categoria = categoria;
		this.modificadorDanioProvocado = modificadorDanioProvocado;
		this.modificadorDanioRecibido = modificadorDanioRecibido;
		this.modificadorVidaMaxima = modificadorVidaMaxima;
		this.modificadorEnergiaMaxima = modificadorEnergiaMaxima;
	}

	public String getNombre() {
		return nombre;
	}

	public int getModificadorDanioProvocado() {
		return modificadorDanioProvocado;
	}

	public int getModificadorDanioRecibido() {
		return modificadorDanioRecibido;
	}

	public int getModificadorVidaMaxima() {
		return modificadorVidaMaxima;
	}

	public int getModificadorEnergiaMaxima() {
		return modificadorEnergiaMaxima;
	}
}
