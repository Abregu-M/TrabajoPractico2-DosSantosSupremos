package PaqueteCombatiente;

public class HechiceroDelVacio extends Combatiente {
	HechiceroDelVacio(int id, String nombre) {
		super(id, nombre, 100, 40);
	}

	@Override
	public int modificarCostoEnergia(int base) {
		return Math.max(0, super.modificarCostoEnergia(base) - 2);
	}
}
