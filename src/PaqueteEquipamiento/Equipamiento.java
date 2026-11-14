package PaqueteEquipamiento;

import PaqueteCombatiente.Combatiente;

public class Equipamiento {

    private String nombre;
    private CategoriaEquipamiento categoria;

    private int modificadorDanioProvocado;
    private int modificadorDanioRecibido;
    private int modificadorVidaMaxima;
    private int modificadorEnergiaMaxima;
    
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

	public void aplicarEfecto(Combatiente combatiente);

    public void removerEfecto(Combatiente combatiente);
}
