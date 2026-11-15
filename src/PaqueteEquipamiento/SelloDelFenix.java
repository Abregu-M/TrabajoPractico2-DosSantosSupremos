package PaqueteEquipamiento;

import PaqueteCombatiente.Combatiente;

public class SelloDelFenix extends Equipamiento {
	public SelloDelFenix() {
		super("SelloDelFenix", CategoriaEquipamiento.SELLO, 0, 0, 30, 0);
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
