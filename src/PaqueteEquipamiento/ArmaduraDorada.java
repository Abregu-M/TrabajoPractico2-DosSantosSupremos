package PaqueteEquipamiento;

import PaqueteCombatiente.Combatiente;

// AGREGADO - PARTE 1. Hereda los efectos y las validaciones comunes.
public class ArmaduraDorada extends Equipamiento {
	public ArmaduraDorada() {
		super("ArmaduraDorada", CategoriaEquipamiento.ARMADURA, 0, 3, 0, 0);
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
