package paqueteCombatiente;

public class HechiceroDelVacio extends Combatiente{

	public HechiceroDelVacio(String nombre, int vidaMax, int energiaMax) {
		super(nombre, vidaMax, energiaMax);
	}
	
	@Override
	protected int modificarCostoEnergia(int costoBase) {
	    return (int) (super.modificarCostoEnergia(costoBase) * 0.8);
	}
}
