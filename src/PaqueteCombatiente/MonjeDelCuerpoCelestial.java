package PaqueteCombatiente;

public class MonjeDelCuerpoCelestial extends Combatiente{

	public MonjeDelCuerpoCelestial(String nombre, int vidaMax, int energiaMax) {
		super(nombre, vidaMax, energiaMax);
	}
	
	@Override
	protected int modificarCostoEnergia(int danioBase) {
	    return (int) (super.modificarCostoEnergia(danioBase) * 0.8);
	}
}
