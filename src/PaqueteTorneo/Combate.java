package PaqueteTorneo;

import java.util.ArrayList;
import java.util.List;
import PaqueteCombatiente.Combatiente;
import PaqueteTecnica.Tecnica;
import PaqueteAcontecimientos.*;

public class Combate {
    public static final int RECUPERACION_ENERGIA = 5;
    private final Pareja primera;
    private final Pareja segunda;
    private final int ronda;
    private final int limiteAsaltos;
    private final List<Observador> observadores = new ArrayList<>();
    private boolean ejecutado;
    private int asalto;

    public Combate(Pareja primera, Pareja segunda, int ronda, int limiteAsaltos) {
        if (primera == null || segunda == null || primera.getId() == segunda.getId()
                || ronda < 1 || limiteAsaltos < 1) {
            throw new IllegalArgumentException("Datos de combate invalidos.");
        }
        for (Combatiente c : primera.getIntegrantes()) {
            if (segunda.getIntegrantes().contains(c)) {
                throw new IllegalArgumentException("Combatiente en las dos parejas.");
            }
        }
        this.primera = primera; this.segunda = segunda;
        this.ronda = ronda; this.limiteAsaltos = limiteAsaltos;
    }
    public Pareja getPrimera() { return primera; }
    public Pareja getSegunda() { return segunda; }
    public int getAsaltosJugados() { return asalto; }
    public void agregarObservador(Observador o) {
        if (o == null) { throw new IllegalArgumentException("Observador nulo."); }
        if (!observadores.contains(o)) { observadores.add(o); }
    }
    public void quitarObservador(Observador o) { observadores.remove(o); }
    public void notificar(Acontecimiento e) {
        for (Observador o : new ArrayList<>(observadores)) { o.notificar(e); }
    }
    private void anunciar(TipoAcontecimiento tipo, String datos) {
        notificar(new Acontecimiento(tipo, ronda, asalto, datos));
    }
    public Pareja ejecutar() {
        if (ejecutado) { throw new IllegalStateException("El combate ya se ejecuto."); }
        // Comprobar los cuatro antes de cambiar el estado de cualquiera.
        for (Combatiente c : todos()) {
            if (c.isEnCombate()) { throw new IllegalStateException("Combatiente ocupado."); }
        }
        ejecutado = true;
        primera.iniciarCombate(); segunda.iniciarCombate();
        try {
            anunciar(TipoAcontecimiento.INICIO_COMBATE, primera.getNombre() + "  VS  " + segunda.getNombre());
            Pareja empieza = primera.getId() < segunda.getId() ? primera : segunda;
            Pareja otra = empieza == primera ? segunda : primera;
            for (asalto = 1; asalto <= limiteAsaltos; asalto++) {
                for (Combatiente c : todos()) { c.recuperarEnergia(RECUPERACION_ENERGIA); }
                anunciar(TipoAcontecimiento.INICIO_ASALTO, "Asalto " + asalto);
                jugarTurno(empieza, otra);
                if (otra.ambosCaidos()) { break; }
                jugarTurno(otra, empieza);
                if (empieza.ambosCaidos()) { break; }
            }
            asalto = Math.min(asalto, limiteAsaltos);
            Pareja ganadora;
            String motivo;
            if (primera.ambosCaidos() || segunda.ambosCaidos()) {
                ganadora = primera.ambosCaidos() ? segunda : primera;
                motivo = "Eliminacion de ambos rivales";
            } else {
                int vida1 = primera.vidaTotal(), vida2 = segunda.vidaTotal();
                ganadora = vida1 > vida2 ? primera : vida2 > vida1 ? segunda : empieza;
                motivo = vida1 == vida2 ? "Limite de asaltos: empate de vida, gana el menor ID"
                        : "Limite de asaltos: mayor vida total";
            }
            notificar(new Acontecimiento(TipoAcontecimiento.RESULTADO_COMBATE, ronda, asalto,
                    "Ganan " + ganadora.getNombre() + ". " + motivo + ".\n"
                    + primera.getNombre() + ": " + primera.vidaTotal() + " de vida | "
                    + segunda.getNombre() + ": " + segunda.vidaTotal() + " de vida",
                    primera.getId(), segunda.getId(), ganadora.getId(), primera.vidaTotal(), segunda.vidaTotal()));
            return ganadora;
        } finally {
            primera.finalizarCombate(); segunda.finalizarCombate();
        }
    }
    private List<Combatiente> todos() {
        List<Combatiente> resultado = primera.getIntegrantes();
        resultado.addAll(segunda.getIntegrantes());
        return resultado;
    }
    private void jugarTurno(Pareja propia, Pareja rival) {
        for (Combatiente c : propia.getIntegrantes()) {
            if (rival.ambosCaidos()) { return; }
            if (c.estaEnPie()) { seleccionarAccion(c, propia, rival); }
        }
    }
    private void seleccionarAccion(Combatiente c, Pareja propia, Pareja rival) {
        Tecnica elegida = null;
        // Primera recuperacion util en el orden de la escuela.
        for (Tecnica t : c.getTecnicas()) {
            if (t.esRecuperacion() && c.getVidaActual() < c.getVidaMaxima()
                    && propia.puedeUsarTecnica(c, t)) { elegida = t; break; }
        }
        if (elegida == null && !c.tieneDefensaActiva()) {
            for (Tecnica t : c.getTecnicas()) {
                if (t.esDefensiva() && propia.puedeUsarTecnica(c, t)) { elegida = t; break; }
            }
        }
        if (elegida == null) {
            for (Tecnica t : c.getTecnicas()) {
                if (!t.esDefensiva() && !t.esRecuperacion() && propia.puedeUsarTecnica(c, t)
                        && (elegida == null || c.getUsosRestantes(t) > c.getUsosRestantes(elegida))) {
                    elegida = t;
                }
            }
        }
        Combatiente objetivo = elegida != null && (elegida.esRecuperacion() || elegida.esDefensiva())
                ? c : seleccionarObjetivo(rival);
        int vidaAntes = objetivo.getVidaActual();
        String accion;
        if (elegida == null) {
            c.atacar(objetivo); accion = "Ataque basico";
        } else {
            propia.usarTecnica(c, elegida, objetivo); accion = elegida.getNombreVisible();
        }
        anunciar(TipoAcontecimiento.ACCION_REALIZADA, c.getNombre() + " -> " + objetivo.getNombre()
                + " | " + accion + " | Vida " + vidaAntes + " -> " + objetivo.getVidaActual()
                + " | Energia de " + c.getNombre() + ": " + c.getEnergiaActual()
                + (elegida == null ? "" : " | Usos restantes: " + c.getUsosRestantes(elegida)));
        if (!objetivo.estaEnPie()) {
            anunciar(TipoAcontecimiento.ELIMINACION, objetivo.getNombre() + " queda fuera de combate.");
        }
    }
    private Combatiente seleccionarObjetivo(Pareja rival) {
        Combatiente elegido = null;
        for (Combatiente c : rival.getIntegrantes()) {
            if (c.estaEnPie() && (elegido == null || c.getVidaActual() < elegido.getVidaActual()
                    || c.getVidaActual() == elegido.getVidaActual() && c.getId() < elegido.getId())) {
                elegido = c;
            }
        }
        return elegido;
    }
}
