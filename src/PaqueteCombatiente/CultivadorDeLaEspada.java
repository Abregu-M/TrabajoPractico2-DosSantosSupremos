package PaqueteCombatiente;

public class CultivadorDeLaEspada extends Combatiente {

	public CultivadorDeLaEspada(String nombre, int vidaMax, int energiaMax) {
		super(nombre, vidaMax, energiaMax);
	}
	
	@Override
	protected int calcularDanioProvocado(int danioBase) {
	    return (int) (super.calcularDanioProvocado(danioBase) * 1.2);
	}
}
