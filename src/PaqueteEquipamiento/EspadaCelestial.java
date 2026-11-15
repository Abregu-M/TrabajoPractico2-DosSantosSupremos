package PaqueteEquipamiento;

import PaqueteCombatiente.Combatiente;

// AGREGADO - PARTE 1. Hereda los efectos y las validaciones comunes.
public class EspadaCelestial extends Equipamiento {
	public EspadaCelestial() {
		super("EspadaCelestial", CategoriaEquipamiento.ARMA, 5, 0, 0, 0);
	}

	@Override
	public void aplicarEfecto(Combatiente combatiente) {
		combatiente.equipar(this);
	}

	@Override
	public void removerEfecto(Combatiente combatiente) {
		combatiente.desequipar(this);
	}
}
