package PaqueteTecnica;

import PaqueteCombatiente.Combatiente;

public class RugidoDeLaBestiaPrimordial extends Tecnica {
	public RugidoDeLaBestiaPrimordial() {
		super("RugidoDeLaBestiaPrimordial", 15);
	}

	@Override
	public void ejecutar(Combatiente ejecutor, Combatiente objetivo) {
		validarObjetivo(ejecutor, objetivo, false);
		objetivo.recibirDanio(ejecutor.modificarDanioProvocado(30));
	}
}
