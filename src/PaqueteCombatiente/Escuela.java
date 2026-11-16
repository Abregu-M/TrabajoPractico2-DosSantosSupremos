package PaqueteCombatiente;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

import PaqueteEquipamiento.*;
import PaqueteTecnica.Tecnica;

public class Escuela {
	private final String nombre;
	private final List<Tecnica> tecnicasIniciales;
	private final List<Equipamiento> equipoInicial;

	public Escuela(String nombre, List<Tecnica> tecnicas, List<Equipamiento> equipo) {
		if (nombre == null || nombre.trim().isEmpty() || tecnicas == null || equipo == null || tecnicas.contains(null)
				|| equipo.contains(null) || equipo.size() > 3) {
			throw new IllegalArgumentException("Configuracion de escuela invalida.");
		}
		Set<CategoriaEquipamiento> categorias = new HashSet<>();
		for (Equipamiento e : equipo) {
			if (!categorias.add(e.getCategoria())) {
				throw new IllegalArgumentException("Categorias repetidas.");
			}
		}
		if (new HashSet<>(tecnicas).size() != tecnicas.size()) {
			throw new IllegalArgumentException("Tecnicas repetidas.");
		}
		this.nombre = nombre;
		tecnicasIniciales = new ArrayList<>(tecnicas);
		equipoInicial = new ArrayList<>(equipo);
	}

	public String getNombre() {
		return nombre;
	}

	public void preparar(Combatiente combatiente) {
		if (combatiente == null || combatiente.isEnCombate() || !combatiente.getEquipamientos().isEmpty()
				|| !combatiente.getTecnicas().isEmpty()) {
			throw new IllegalArgumentException("Debe ser un combatiente sin preparar.");
		}
		for (Tecnica t : tecnicasIniciales) {
			combatiente.aprenderTecnica(t);
		}
		for (Equipamiento e : equipoInicial) {
			combatiente.equipar(e);
		}
	}
}
