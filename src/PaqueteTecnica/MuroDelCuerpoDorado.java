package PaqueteTecnica;
import PaqueteCombatiente.Combatiente;

public class MuroDelCuerpoDorado extends Tecnica {
    @Override
    public boolean esDefensiva() { return true; }

    public MuroDelCuerpoDorado() { super("MuroDelCuerpoDorado", 8); }
    @Override
    public void ejecutar(Combatiente ejecutor, Combatiente objetivo) {
        validarObjetivo(ejecutor, objetivo, true);
        ejecutor.activarDefensa(5);
    }
}
