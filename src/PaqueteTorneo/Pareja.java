package PaqueteTorneo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import PaqueteCombatiente.Combatiente;
import PaqueteTecnica.Tecnica;

public class Pareja {
    private static int siguienteId = 1;
    private final int id;
    private final List<Combatiente> integrantes = new ArrayList<>();
    private final Map<Tecnica, Combatiente> registroTecnicasDelCombate = new HashMap<>();

    public Pareja(Combatiente primero, Combatiente segundo) {
        if (primero == null || segundo == null || primero.equals(segundo)) {
            throw new IllegalArgumentException("Una pareja requiere dos combatientes distintos.");
        }
        id = siguienteId++;
        // Dentro de la pareja siempre actua primero el de menor ID.
        if (primero.getId() < segundo.getId()) {
            integrantes.add(primero); integrantes.add(segundo);
        } else {
            integrantes.add(segundo); integrantes.add(primero);
        }
    }

    public int getId() { return id; }
    public List<Combatiente> getIntegrantes() { return new ArrayList<>(integrantes); }
    public String getNombre() {
        return integrantes.get(0).getNombre() + " y " + integrantes.get(1).getNombre();
    }
    public boolean ambosCaidos() {
        return !integrantes.get(0).estaEnPie() && !integrantes.get(1).estaEnPie();
    }
    public int vidaTotal() {
        return integrantes.get(0).getVidaActual() + integrantes.get(1).getVidaActual();
    }
    public boolean puedeUsarTecnica(Combatiente combatiente, Tecnica tecnica) {
        if (!integrantes.contains(combatiente) || tecnica == null
                || !combatiente.estaEnPie() || combatiente.getUsosRestantes(tecnica) == 0
                || combatiente.getEnergiaActual() < combatiente.modificarCostoEnergia(tecnica.getCostoEnergia())) {
            return false;
        }
        Combatiente anterior = registroTecnicasDelCombate.get(tecnica);
        return anterior == null || anterior.equals(combatiente)
                || !anterior.estaEnPie() || anterior.getUsosRestantes(tecnica) == 0;
    }
    public void usarTecnica(Combatiente combatiente, Tecnica tecnica, Combatiente objetivo) {
        if (!puedeUsarTecnica(combatiente, tecnica)) {
            throw new IllegalStateException("La pareja no puede usar esa tecnica ahora.");
        }
        combatiente.usarTecnica(tecnica, objetivo);
        registroTecnicasDelCombate.put(tecnica, combatiente);
    }
    public void iniciarCombate() {
        for (Combatiente c : integrantes) {
            if (c.isEnCombate()) { throw new IllegalStateException("Integrante ya en combate."); }
        }
        registroTecnicasDelCombate.clear();
        for (Combatiente c : integrantes) { c.iniciarCombate(); }
    }
    public void finalizarCombate() {
        for (Combatiente c : integrantes) { c.finalizarCombate(); }
        registroTecnicasDelCombate.clear();
    }
}
