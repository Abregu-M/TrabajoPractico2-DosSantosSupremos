package PaqueteCombatiente;

public class CultivadorDeLaEspada extends Combatiente {
	CultivadorDeLaEspada(int id, String nombre) {
		super(id, nombre, 100, 40);
	}

	@Override
	public int modificarDanioProvocado(int base) {
		return super.modificarDanioProvocado(base) + 5;
	}
}
