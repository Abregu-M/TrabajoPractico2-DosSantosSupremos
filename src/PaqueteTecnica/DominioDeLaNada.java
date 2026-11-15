package PaqueteTecnica;
import PaqueteCombatiente.Combatiente;

public class DominioDeLaNada extends Tecnica {
    public DominioDeLaNada() { super("DominioDeLaNada", 10); }
    @Override
    public void ejecutar(Combatiente ejecutor, Combatiente objetivo) {
        validarObjetivo(ejecutor, objetivo, false);
        objetivo.recibirDanio(ejecutor.modificarDanioProvocado(15));
        objetivo.perderEnergia(8);
    }
}
