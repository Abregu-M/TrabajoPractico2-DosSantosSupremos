package PaqueteEfectos;

import PaqueteCombatiente.Combatiente;

public interface Efecto {

    void aplicarAlInicioDelTurno(Combatiente combatiente);

    int modificarDanioProvocado(int danio);

    boolean haFinalizado();
}