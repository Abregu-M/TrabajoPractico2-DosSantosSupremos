package PaqueteCombatiente;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Combatiente {
	private static int contadorId = 0;
	private final int id;
	
	private final String nombre;
	private int vidaMax;
	private int vidaActual;
	private int energiaMax;
	private int energiaActual;
	
	private Map<Tecnica, Integer> usosRestantesPorTecnica;
	private Set<Equipamiento> equipamientos;
	
	public Combatiente(String nombre, int vidaMax, int energiaMax) {
		 if (vidaMax <= 1 || energiaMax <= 1) {
		        throw new IllegalArgumentException("La vida y la energía máximas deben ser mayores a 1.");
		    }
		 
		 if (nombre == null || nombre.trim().isEmpty()) {
		        throw new IllegalArgumentException("El nombre no puede estar vacío.");
		    }
		
	    this.id = ++contadorId;
	    this.nombre = nombre;
	    this.vidaMax = vidaMax;
	    this.vidaActual = vidaMax;
	    this.energiaMax = energiaMax;
	    this.energiaActual = energiaMax;
	    
	    this.usosRestantesPorTecnica = new HashMap<>();
	    this.equipamientos = new HashSet<>();
	}

	public int getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public int getVidaMax() {
		return vidaMax;
	}

	public int getVidaActual() {
		return vidaActual;
	}

	public int getEnergiaMax() {
		return energiaMax;
	}

	public int getEnergiaActual() {
		return energiaActual;
	}

	public Map<Tecnica, Integer> getUsosRestantesPorTecnica() {
		return usosRestantesPorTecnica;
	}

	public Set<Equipamiento> getEquipamientos() {
		return equipamientos;
	}

	public void setVidaActual(int vidaActual) {
		this.vidaActual = vidaActual;
	}

	public void setEnergiaActual(int energiaActual) {
		this.energiaActual = energiaActual;
	}

	public void setUsosRestantesPorTecnica(Map<Tecnica, Integer> usosRestantesPorTecnica) {
		this.usosRestantesPorTecnica = usosRestantesPorTecnica;
	}

	public void setEquipamientos(Set<Equipamiento> equipamientos) {
		this.equipamientos = equipamientos;
	}
}