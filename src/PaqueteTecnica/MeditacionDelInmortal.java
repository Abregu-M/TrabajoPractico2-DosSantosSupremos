package PaqueteTecnica;

import PaqueteCombatiente.Combatiente;

public class MeditacionDelInmortal extends Tecnica {
	@Override
	public boolean esRecuperacion() {
		return true;
	}

	public MeditacionDelInmortal() {
		super("MeditacionDelInmortal", 35);
	}

	@Override
	public void ejecutar(Combatiente ejecutor, Combatiente objetivo) {
		validarObjetivo(ejecutor, objetivo, true);
		ejecutor.recuperarVida(15);
	}
}
