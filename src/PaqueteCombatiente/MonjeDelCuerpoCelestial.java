package PaqueteCombatiente;

public class MonjeDelCuerpoCelestial extends Combatiente{

	public MonjeDelCuerpoCelestial(String nombre, int vidaMax, int energiaMax) {
		super(nombre, vidaMax, energiaMax);
	}
	
	@Override
	protected int calcularDanioRecibido(int danioBase) {
	    return (int) (super.calcularDanioRecibido(danioBase) * 0.8);
	}
}
