package paqueteTecnica;

import paqueteCombatiente.Combatiente;

public class CorteDeLosMilAmaneceres extends Tecnica {

    private static final int DANIO_BASE = 20;

    public CorteDeLosMilAmaneceres() {
        super("Corte de los Mil Amaneceres", 15);
    }

    @Override
    public void ejecutar(Combatiente ejecutor, Combatiente objetivo) {
        int danio = ejecutor.modificarDanioProvocadoFinal(DANIO_BASE);
        objetivo.recibirDanio(danio);
    }
}