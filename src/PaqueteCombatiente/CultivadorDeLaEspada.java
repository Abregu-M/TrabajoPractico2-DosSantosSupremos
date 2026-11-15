package paqueteCombatiente;

public class CultivadorDeLaEspada extends Combatiente {

	public CultivadorDeLaEspada(String nombre, int vidaMax, int energiaMax) {
		super(nombre, vidaMax, energiaMax);
	}
	
	@Override
	protected int modificarDanioProvocado(int danioBase) {
	    return (int) (super.modificarDanioProvocado(danioBase) * 1.2);
	}
}
