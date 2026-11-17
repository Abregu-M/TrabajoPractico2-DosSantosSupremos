package PaqueteEfectos;

import PaqueteCombatiente.Combatiente;

public class Determinacion implements Efecto {

	private final int bonificacionDanio;

	public Determinacion(int bonificacionDanio) {
		if (bonificacionDanio <= 0) {
			throw new IllegalArgumentException("La bonificación debe ser positiva.");
		}

		this.bonificacionDanio = bonificacionDanio;
	}

	@Override
	public void aplicarAlInicioDelTurno(Combatiente combatiente) {
		// No hace nada
	}

	@Override
	public int modificarDanioProvocado(int danio) {
	    return danio + bonificacionDanio;
	}

	@Override
	public boolean haFinalizado() {
	    return false;
	}
}