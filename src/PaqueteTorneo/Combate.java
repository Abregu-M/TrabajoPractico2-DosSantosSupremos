package PaqueteTorneo;

import java.util.ArrayList;
import java.util.List;

import PaqueteAcontecimientos.*;
import PaqueteCombatiente.Combatiente;
import PaqueteTecnica.Tecnica;
import PaqueteEfectos.Determinacion;
import PaqueteTecnica.AguijonDeLaSerpienteInmortal;

public class Combate {
	public static final int RECUPERACION_ENERGIA = 5;
	private final Pareja primerPareja;
	private final Pareja segundaPareja;
	private final int ronda;
	private final int limiteAsaltos;
	private final List<Observador> observadores = new ArrayList<>();
	private boolean ejecutado;
	private int asalto;
	private static final int BONUS_DETERMINACION = 8;
	private static final double UMBRAL_CURACION = 0.40;
	
	public Combate(Pareja primera, Pareja segunda, int ronda, int limiteAsaltos) {
		if (primera == null || segunda == null || primera.getId() == segunda.getId() || ronda < 1
				|| limiteAsaltos < 1) {
			throw new IllegalArgumentException("Datos de combate invalidos.");
		}
		for (Combatiente c : primera.getIntegrantes()) {
			if (segunda.getIntegrantes().contains(c)) {
				throw new IllegalArgumentException("Combatiente en las dos parejas.");
			}
		}
		this.primerPareja = primera;
		this.segundaPareja = segunda;
		this.ronda = ronda;
		this.limiteAsaltos = limiteAsaltos;
	}

	public Pareja getPrimera() {
		return primerPareja;
	}

	public Pareja getSegunda() {
		return segundaPareja;
	}

	public int getAsaltosJugados() {
		return asalto;
	}

	public void agregarObservador(Observador o) {
		if (o == null) {
			throw new IllegalArgumentException("Observador nulo.");
		}
		if (!observadores.contains(o)) {
			observadores.add(o);
		}
	}

	public void quitarObservador(Observador o) {
		observadores.remove(o);
	}

	public void notificar(Acontecimiento e) {
		for (Observador o : new ArrayList<>(observadores)) {
			o.notificar(e);
		}
	}

	private void anunciar(TipoAcontecimiento tipo, String datos) {
		notificar(new Acontecimiento(tipo, ronda, asalto, datos));
	}

	private void anunciarAccion(Combatiente atacante, Combatiente objetivo, String accion, int vidaAntes,
			int usosRestantes) {

		Acontecimiento evento = new Acontecimiento(ronda, asalto, atacante.getNombre(), objetivo.getNombre(), accion,
				vidaAntes, objetivo.getVidaActual(), atacante.getEnergiaActual(), usosRestantes);

		notificar(evento);
	}

	public Pareja ejecutar() {
		if (ejecutado) {
			throw new IllegalStateException("El combate ya se ejecuto.");
		}
		// Comprobar los cuatro antes de cambiar el estado de cualquiera.
		for (Combatiente c : todos()) {
			if (c.isEnCombate()) {
				throw new IllegalStateException("Combatiente ocupado.");
			}
		}
		ejecutado = true;
		primerPareja.iniciarCombate();
		segundaPareja.iniciarCombate();
		try {
			anunciar(TipoAcontecimiento.INICIO_COMBATE, primerPareja.getNombre() + "  VS  " + segundaPareja.getNombre());
			Pareja empieza = primerPareja.getId() < segundaPareja.getId() ? primerPareja : segundaPareja;
			Pareja otra = empieza == primerPareja ? segundaPareja : primerPareja;
			for (asalto = 1; asalto <= limiteAsaltos; asalto++) {
				for (Combatiente c : todos()) {
					c.recuperarEnergia(RECUPERACION_ENERGIA);
				}
				anunciar(TipoAcontecimiento.INICIO_ASALTO, "Asalto " + asalto);
				jugarTurno(empieza, otra);
				if (otra.ambosCaidos()) {
					break;
				}
				jugarTurno(otra, empieza);
				if (empieza.ambosCaidos()) {
					break;
				}
			}
			asalto = Math.min(asalto, limiteAsaltos);
			Pareja ganadora;
			String motivo;
			if (primerPareja.ambosCaidos() || segundaPareja.ambosCaidos()) {
				ganadora = primerPareja.ambosCaidos() ? segundaPareja : primerPareja;
				motivo = "Eliminacion de ambos rivales";
			} else {
				int vida1 = primerPareja.vidaTotal(), vida2 = segundaPareja.vidaTotal();
				ganadora = vida1 > vida2 ? primerPareja : vida2 > vida1 ? segundaPareja : empieza;
				motivo = vida1 == vida2 ? "Limite de asaltos: empate de vida, gana el menor ID"
						: "Limite de asaltos: mayor vida total";
			}
			notificar(new Acontecimiento(TipoAcontecimiento.RESULTADO_COMBATE, ronda, asalto,
					"Ganan " + ganadora.getNombre() + ". " + motivo + ".\n" + primerPareja.getNombre() + ": "
							+ primerPareja.vidaTotal() + " de vida | " + segundaPareja.getNombre() + ": " + segundaPareja.vidaTotal()
							+ " de vida",
					primerPareja.getId(), segundaPareja.getId(), ganadora.getId(), primerPareja.vidaTotal(), segundaPareja.vidaTotal()));
			return ganadora;
		} finally {
			primerPareja.finalizarCombate();
			segundaPareja.finalizarCombate();
		}
	}

	private List<Combatiente> todos() {
		List<Combatiente> resultado = primerPareja.getIntegrantes();
		resultado.addAll(segundaPareja.getIntegrantes());
		return resultado;
	}

	private void jugarTurno(Pareja propia, Pareja rival) {
		for (Combatiente c : propia.getIntegrantes()) {
			if (rival.ambosCaidos()) {
				return;
			}
			if (c.estaEnPie()) {
			    int vidaAntes = c.getVidaActual();

			    c.iniciarTurno();

			    int vidaDespues = c.getVidaActual();

			    if (vidaDespues < vidaAntes) {
			        anunciar(TipoAcontecimiento.DANIO_POR_VENENO,
			                String.format("%s recibe %d de daño por veneno (%d -> %d).",
			                        c.getNombre(),
			                        vidaAntes - vidaDespues,
			                        vidaAntes,
			                        vidaDespues));
			    }

			    if (c.estaEnPie()) {
			        seleccionarAccion(c, propia, rival);
			    } else {
			        procesarEliminacion(c, propia);
			    }
			}
		}
	}

	private void seleccionarAccion(Combatiente c, Pareja propia, Pareja rival) {
		Tecnica elegida = null;
		// Primera recuperacion util en el orden de la escuela.
		for (Tecnica t : c.getTecnicas()) {
			if (t.esRecuperacion() && c.getVidaActual() < c.getVidaMaxima() * UMBRAL_CURACION && propia.puedeUsarTecnica(c, t)) {
				elegida = t;
				break;
			}
		}
		if (elegida == null && !c.tieneDefensaActiva()) {
			for (Tecnica t : c.getTecnicas()) {
				if (t.esDefensiva() && propia.puedeUsarTecnica(c, t)) {
					elegida = t;
					break;
				}
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
		Combatiente objetivo = elegida != null && (elegida.esRecuperacion() || elegida.esDefensiva()) ? c
				: seleccionarObjetivo(rival);
		int vidaAntes = objetivo.getVidaActual();
		String accion;
		if (elegida == null) {
			c.atacar(objetivo);
			accion = "Ataque basico";
		} else {
		    propia.usarTecnica(c, elegida, objetivo);
		    accion = elegida.getNombreVisible();
		}
		anunciarAccion(c, objetivo, accion, vidaAntes,
		        elegida == null ? -1 : c.getUsosRestantes(elegida));

		if (elegida instanceof AguijonDeLaSerpienteInmortal) {
		    anunciar(TipoAcontecimiento.VENENO_APLICADO, objetivo.getNombre());
		}
		if (!objetivo.estaEnPie()) {
			procesarEliminacion(objetivo, rival);
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
	
	private Combatiente obtenerCompanero(Combatiente eliminado, Pareja propia) {
	    for (Combatiente c : propia.getIntegrantes()) {
	        if (!c.equals(eliminado)) {
	            return c;
	        }
	    }
	    return null;
	}
	
	private void procesarEliminacion(Combatiente eliminado, Pareja pareja) {

	    anunciar(TipoAcontecimiento.ELIMINACION,
	            eliminado.getNombre() + " queda fuera de combate.");

	    Combatiente companero = obtenerCompanero(eliminado, pareja);

	    if (companero != null && companero.estaEnPie()) {
	        companero.agregarEfecto(new Determinacion(BONUS_DETERMINACION));

	        anunciar(TipoAcontecimiento.DETERMINACION_ACTIVADA,
	                companero.getNombre());
	    }
	}
}
