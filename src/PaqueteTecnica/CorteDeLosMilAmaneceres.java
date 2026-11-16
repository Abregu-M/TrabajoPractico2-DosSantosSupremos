package PaqueteTecnica;

import PaqueteCombatiente.Combatiente;

public class CorteDeLosMilAmaneceres extends Tecnica {
	public CorteDeLosMilAmaneceres() {
		super("CorteDeLosMilAmaneceres", 8);
	}

	@Override
	public void ejecutar(Combatiente ejecutor, Combatiente objetivo) {
		validarObjetivo(ejecutor, objetivo, false);
		objetivo.recibirDanio(ejecutor.modificarDanioProvocado(20));
	}
}
