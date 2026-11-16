package PaqueteTorneo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import PaqueteAcontecimientos.*;
import PaqueteCombatiente.Combatiente;

public class Torneo {
	private final List<Pareja> parejas = new ArrayList<>();
	private final Set<Integer> idsParejas = new HashSet<>();
	private final Set<Integer> idsCombatientes = new HashSet<>();
	private final List<List<Combate>> rondas = new ArrayList<>();
	private final List<Observador> observadores = new ArrayList<>();
	private final int limiteAsaltos;
	private boolean iniciado;
	private Pareja campeona;

	public Torneo() {
		this(20);
	}

	public Torneo(int limiteAsaltos) {
		if (limiteAsaltos < 1) {
			throw new IllegalArgumentException("Limite invalido.");
		}
		this.limiteAsaltos = limiteAsaltos;
	}

	public void inscribir(Pareja pareja) {
		if (iniciado) {
			throw new IllegalStateException("Torneo iniciado.");
		}
		if (pareja == null || idsParejas.contains(pareja.getId())) {
			throw new IllegalArgumentException("Pareja nula o repetida.");
		}
		for (Combatiente c : pareja.getIntegrantes()) {
			if (idsCombatientes.contains(c.getId()) || c.isEnCombate()) {
				throw new IllegalArgumentException("Combatiente repetido u ocupado.");
			}
		}
		parejas.add(pareja);
		idsParejas.add(pareja.getId());
		for (Combatiente c : pareja.getIntegrantes()) {
			idsCombatientes.add(c.getId());
		}
	}

	public List<Pareja> getParejas() {
		return new ArrayList<>(parejas);
	}

	public List<List<Combate>> getRondas() {
		List<List<Combate>> copia = new ArrayList<>();
		for (List<Combate> ronda : rondas) {
			copia.add(new ArrayList<>(ronda));
		}
		return copia;
	}

	public Pareja getCampeona() {
		return campeona;
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

	public void notificar(Acontecimiento evento) {
		for (Observador o : new ArrayList<>(observadores)) {
			o.notificar(evento);
		}
	}

	private void validarCantidadParejas() {
		if (parejas.size() != 4 && parejas.size() != 8) {
			throw new IllegalStateException("El torneo requiere 4 u 8 parejas.");
		}
	}

	private List<Combate> armarCruces(List<Pareja> enOrden) {
		List<Combate> cruces = new ArrayList<>();
		for (int i = 0; i < enOrden.size(); i += 2) {
			Combate combate = new Combate(enOrden.get(i), enOrden.get(i + 1), rondas.size() + 1, limiteAsaltos);
			// El torneo reenvia los hechos a todos sus interesados.
			combate.agregarObservador(new Observador() {
				@Override
				public void notificar(Acontecimiento evento) {
					Torneo.this.notificar(evento);
				}
			});
			cruces.add(combate);
		}
		return cruces;
	}

	public void iniciar() {
		if (iniciado) {
			throw new IllegalStateException("El torneo ya se inicio.");
		}
		validarCantidadParejas();
		iniciado = true;
		List<Pareja> enOrden = getParejas();
		while (enOrden.size() > 1) {
			List<Combate> cruces = armarCruces(enOrden);
			rondas.add(cruces);
			List<Pareja> ganadoras = new ArrayList<>();
			for (Combate c : cruces) {
				ganadoras.add(c.ejecutar());
			}
			enOrden = ganadoras;
		}
		campeona = enOrden.get(0);
		notificar(new Acontecimiento(TipoAcontecimiento.PROCLAMACION, rondas.size(), 0,
				campeona.getNombre() + " son los DOS SANTOS SUPREMOS."));
	}
}