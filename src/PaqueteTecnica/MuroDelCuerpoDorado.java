package paqueteTecnica;

import paqueteCombatiente.Combatiente;

public class MuroDelCuerpoDorado extends Tecnica{

	public MuroDelCuerpoDorado(String nombre, int costoEnergia) {
		super(nombre, costoEnergia);
	}

	@Override
	public void ejecutar(Combatiente ejecutor, Combatiente objetivo) {
		objetivo.activarDefensaTemporal();
	}
}
