package PaqueteEfectos;

import PaqueteCombatiente.Combatiente;

public class Veneno implements Efecto {
	
	private final int danioPorTurno;
	private int turnosRestantes;

	
	public Veneno(int danioPorTurno, int turnosRestantes) {
	    if (danioPorTurno <= 0 || turnosRestantes <= 0) {
	        throw new IllegalArgumentException("El daño y la duración deben ser positivos.");
	    }

	    this.danioPorTurno = danioPorTurno;
	    this.turnosRestantes = turnosRestantes;
	}
	
	@Override
	public void aplicarAlInicioDelTurno(Combatiente combatiente) {
	    combatiente.recibirDanio(danioPorTurno);
	    turnosRestantes--;
	}

	@Override
	public boolean haFinalizado() {
	    return turnosRestantes <= 0;
	}
	
	@Override
	public int modificarDanioProvocado(int danio) {
		return danio;
	}
}
