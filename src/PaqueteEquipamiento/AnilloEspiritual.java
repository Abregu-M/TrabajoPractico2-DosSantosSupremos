package PaqueteEquipamiento;

import PaqueteCombatiente.Combatiente;

//  Hereda los efectos y las validaciones comunes.
public class AnilloEspiritual extends Equipamiento {
	public AnilloEspiritual() {
		super("AnilloEspiritual", CategoriaEquipamiento.ANILLO, 0, 0, 0, 20);
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
