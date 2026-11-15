package PaqueteCombatiente;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import PaqueteEquipamiento.Equipamiento;
import PaqueteTecnica.Tecnica;

public abstract class Combatiente {
	private static int contadorId = 0;
	private final int id;
	
	private final String nombre;
	private final int vidaMaxBase;
	private final int energiaMaxBase;
	
	private int vidaActual;
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
	    this.vidaMaxBase = vidaMax;
	    this.vidaActual = vidaMax;
	    this.energiaMaxBase = energiaMax;
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
	    int vidaMax = vidaMaxBase;

	    for (Equipamiento e : equipamientos) {
	        vidaMax += e.getModificadorVidaMaxima();
	    }

	    return vidaMax;
	}

	public int getVidaActual() {
		return vidaActual;
	}

	public int getEnergiaMax() {
	    int energiaMax = energiaMaxBase;

	    for (Equipamiento e : equipamientos) {
	        energiaMax += e.getModificadorEnergiaMaxima();
	    }

	    return energiaMax;
	}

	public int getEnergiaActual() {
		return energiaActual;
	}

	public Map<Tecnica, Integer> getUsosRestantesPorTecnica() {
	    return new HashMap<>(usosRestantesPorTecnica);
	}

	public Set<Equipamiento> getEquipamientos() {
	    return new HashSet<>(equipamientos);
	}

	public void recibirDanio(int danio) {
	    if (danio <= 0) {
	        throw new IllegalArgumentException("El daño recibido debe ser positivo");
	    }
	    
	    int danioFinal = modificarDanioRecibido(danio);
	    vidaActual = Math.max(0, vidaActual - danioFinal);
	}

	public void recuperarVida(int cantidad) {
		if (cantidad <= 0) {
	        throw new IllegalArgumentException("La vida recuperada debe ser positiva");
	    }
	    vidaActual = Math.min(getVidaMax(), vidaActual + cantidad);
	}

	public void recuperarEnergia(int cantidad) {
		if (cantidad <= 0) {
	        throw new IllegalArgumentException("La energía recuperada debe ser positiva");
	    }
	    energiaActual = Math.min(getEnergiaMax(), energiaActual + cantidad);
	}
	
	public void equipar(Equipamiento equipamiento) {
	    if (equipamiento == null) {
	        throw new IllegalArgumentException("El equipamiento no puede ser nulo.");
	    }

	    if (equipamientos.size() >= 3) {
	        throw new IllegalArgumentException("El combatiente no puede llevar más de 3 equipamientos.");
	    }

	    for (Equipamiento e : equipamientos) {
	        if (e.getCategoria() == equipamiento.getCategoria()) {
	            throw new IllegalArgumentException("Ya existe un equipamiento de esa categoría.");
	        }
	    }

	    equipamientos.add(equipamiento);
	}
	
	public void desequipar(Equipamiento equipamiento) {
	    if (!equipamientos.remove(equipamiento)) {
	        throw new IllegalArgumentException("El combatiente no tiene equipado ese objeto.");
	    }

	    if (vidaActual > getVidaMax()) {
	        vidaActual = getVidaMax();
	    }

	    if (energiaActual > getEnergiaMax()) {
	        energiaActual = getEnergiaMax();
	    }
	}
	
	public void aprenderTecnica(Tecnica tecnica) {
	    if (tecnica == null) {
	        throw new IllegalArgumentException("La técnica no puede ser nula.");
	    }
	    if (usosRestantesPorTecnica.containsKey(tecnica)) {
	        throw new IllegalArgumentException("El combatiente ya conoce esa técnica.");
	    }

	    usosRestantesPorTecnica.put(tecnica, 5);
	}
	
	public void usarTecnica(Tecnica tecnica, Combatiente objetivo) {
	    if (tecnica == null || objetivo == null) {
	        throw new IllegalArgumentException("La técnica y el objetivo no pueden ser nulos.");
	    }

	    if (!usosRestantesPorTecnica.containsKey(tecnica)) {
	        throw new IllegalArgumentException("El combatiente no conoce esa técnica.");
	    }

	    if (usosRestantesPorTecnica.get(tecnica) <= 0) {
	        throw new IllegalStateException("No quedan usos disponibles.");
	    }
	    
	    int costoEnergia = modificarCostoEnergia(tecnica.getCostoEnergia());

	    if (energiaActual < costoEnergia) {
	        throw new IllegalStateException("No hay energía suficiente.");
	    }
	    
	    tecnica.ejecutar(this, objetivo);

	    energiaActual -= costoEnergia;
	    usosRestantesPorTecnica.put(tecnica, usosRestantesPorTecnica.get(tecnica) - 1);
	}
	
	protected int modificarDanioProvocado(int danioBase) {
	    return danioBase;
	}
	
	protected int modificarDanioRecibido(int danioBase) {
	    return danioBase;
	}
	
	protected int modificarCostoEnergia(int costoBase) {
	    return costoBase;
	}
}