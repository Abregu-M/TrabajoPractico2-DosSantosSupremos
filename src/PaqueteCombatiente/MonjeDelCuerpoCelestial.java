package PaqueteCombatiente;

public class MonjeDelCuerpoCelestial extends Combatiente {
	MonjeDelCuerpoCelestial(int id, String nombre) {
		super(id, nombre, 100, 40);
	}

	@Override
	public int modificarDanioRecibido(int base) {
		return Math.max(0, super.modificarDanioRecibido(base) - 3);
	}
}
