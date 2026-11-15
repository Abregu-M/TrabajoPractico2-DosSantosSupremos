package PaqueteEquipamiento;

import PaqueteCombatiente.Combatiente;

public abstract class Equipamiento {
	private final String nombre;
	private final CategoriaEquipamiento categoria;
	private final int modificadorDanioProvocado;
	private final int modificadorDanioRecibido;
	private final int modificadorVidaMaxima;
	private final int modificadorEnergiaMaxima;

	protected Equipamiento(String nombre, CategoriaEquipamiento categoria, int modificadorDanioProvocado,
			int modificadorDanioRecibido, int modificadorVidaMaxima, int modificadorEnergiaMaxima) {
		if (nombre == null || nombre.trim().isEmpty() || categoria == null) {
			throw new IllegalArgumentException("Nombre y categoria obligatorios.");
		}
		if (modificadorDanioProvocado < 0 || modificadorDanioRecibido < 0 || modificadorVidaMaxima < 0
				|| modificadorEnergiaMaxima < 0) {
			throw new IllegalArgumentException("Los modificadores no pueden ser negativos.");
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

	public CategoriaEquipamiento getCategoria() {
		return categoria;
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

	public abstract void aplicarEfecto(Combatiente combatiente);

	public abstract void removerEfecto(Combatiente combatiente);
}
