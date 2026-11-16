package PaqueteCombatiente;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import PaqueteEquipamiento.Equipamiento;
import PaqueteTecnica.Tecnica;

import java.util.LinkedHashMap;

// MODIFICADO - PARTE 1: estado, identidad y reglas de equipamiento.
public abstract class Combatiente {
	private final int id;
	private final String nombre;
	private int vidaMaxima;
	private int vidaActual;
	private int energiaMaxima;
	private int energiaActual;
	private boolean enCombate = false;
	private final Map<Tecnica, Integer> usosPorTecnica = new LinkedHashMap<>();
	private int defensaTecnica;

	private final List<Equipamiento> equipamientos;

	// El ID lo recibira del CreadorCombatientes, como indica el UML.
	protected Combatiente(int id, String nombre, int vidaMax, int energiaMax) {
		if (id <= 0 || vidaMax <= 0 || energiaMax <= 0) {
			throw new IllegalArgumentException("ID, vida y energia deben ser positivos.");
		}
		if (nombre == null || nombre.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre no puede estar vacio.");
		}
		this.id = id;
		this.nombre = nombre;
		this.vidaMaxima = vidaMax;
		this.vidaActual = vidaMax;
		this.energiaMaxima = energiaMax;
		this.energiaActual = energiaMax;
		this.equipamientos = new ArrayList<>();
	}

	public int getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public int getVidaActual() {
		return vidaActual;
	}

	public int getEnergiaActual() {
		return energiaActual;
	}

	public boolean isEnCombate() {
		return enCombate;
	}

	public int getVidaMaxima() {
		return vidaMaxima;
	}

	public int getEnergiaMaxima() {
		return energiaMaxima;
	}

	public int getVidaMax() {
		return getVidaMaxima();
	}

	public int getEnergiaMax() {
		return getEnergiaMaxima();
	}

	public void setVidaActual(int vidaActual) {
		if (vidaActual < 0 || vidaActual > getVidaMax()) {
			throw new IllegalArgumentException("Vida fuera de los limites.");
		}
		this.vidaActual = vidaActual;
	}

	public void setEnergiaActual(int energiaActual) {
		if (energiaActual < 0 || energiaActual > getEnergiaMax()) {
			throw new IllegalArgumentException("Energia fuera de los limites.");
		}
		this.energiaActual = energiaActual;
	}

	public List<Equipamiento> getEquipamientos() {
		return new ArrayList<>(equipamientos);
	}

	public void equipar(Equipamiento equipo) {
		validarCambioEquipamiento();
		if (equipo == null) {
			throw new IllegalArgumentException("El equipo no puede ser nulo.");
		}
		if (equipamientos.size() == 3) {
			throw new IllegalStateException("Solo se permiten tres equipamientos.");
		}
		for (Equipamiento equipado : equipamientos) {
			if (equipado.getCategoria() == equipo.getCategoria()) {
				throw new IllegalArgumentException("Ya lleva un equipo de esa categoria.");
			}
		}
		equipamientos.add(equipo);
		vidaMaxima += equipo.getModificadorVidaMaxima();
		energiaMaxima += equipo.getModificadorEnergiaMaxima();
	}

	public void desequipar(Equipamiento equipo) {
		validarCambioEquipamiento();
		if (equipo == null || !equipamientos.contains(equipo)) {
			throw new IllegalArgumentException("No lleva ese equipamiento.");
		}
		equipamientos.remove(equipo);
		vidaMaxima -= equipo.getModificadorVidaMaxima();
		energiaMaxima -= equipo.getModificadorEnergiaMaxima();
		ajustarActualesAMaximos();
	}

	private void validarCambioEquipamiento() {
		if (enCombate) {
			throw new IllegalStateException("No se puede cambiar equipo durante el combate.");
		}
	}

	private void ajustarActualesAMaximos() {
		vidaActual = Math.min(vidaActual, getVidaMax());
		energiaActual = Math.min(energiaActual, getEnergiaMax());
	}

	public void iniciarCombate() {
		if (enCombate) {
			throw new IllegalStateException("Ya esta en combate.");
		}
		vidaActual = getVidaMax();
		energiaActual = getEnergiaMax();
		defensaTecnica = 0;
		enCombate = true;
	}

	public void finalizarCombate() {
		enCombate = false;
		defensaTecnica = 0;
	}

	public int modificarDanioProvocado(int base) {
		int total = base;
		for (Equipamiento equipo : equipamientos) {
			total += equipo.getModificadorDanioProvocado();
		}
		return Math.max(0, total);
	}

	public int modificarDanioRecibido(int base) {
		int total = base - defensaTecnica;
		for (Equipamiento equipo : equipamientos) {
			total -= equipo.getModificadorDanioRecibido();
		}
		return Math.max(0, total);
	}

	public int modificarCostoEnergia(int base) {
		return Math.max(0, base);
	}

	public boolean estaEnPie() {
		return vidaActual > 0;
	}

	public void aprenderTecnica(Tecnica tecnica) {
		validarCambioEquipamiento();
		if (tecnica == null || usosPorTecnica.containsKey(tecnica)) {
			throw new IllegalArgumentException("Tecnica nula o ya aprendida.");
		}
		usosPorTecnica.put(tecnica, 0);
	}

	public Map<Tecnica, Integer> getUsosPorTecnica() {
		return new LinkedHashMap<>(usosPorTecnica);
	}

	public int getUsosRestantes(Tecnica tecnica) {
		Integer usados = usosPorTecnica.get(tecnica);
		return usados == null ? 0 : 5 - usados;
	}

	public List<Tecnica> getTecnicas() {
		return new ArrayList<>(usosPorTecnica.keySet());
	}

	private void validarActor() {
		if (!enCombate || !estaEnPie()) {
			throw new IllegalStateException("Debe estar en combate y en pie.");
		}
	}

	public void atacar(Combatiente objetivo) {
		validarActor();
		if (objetivo == null || objetivo == this || !objetivo.enCombate || !objetivo.estaEnPie()) {
			throw new IllegalArgumentException("Objetivo invalido.");
		}
		objetivo.recibirDanio(modificarDanioProvocado(10));
	}

	public void usarTecnica(Tecnica tecnica, Combatiente objetivo) {
		validarActor();
		if (tecnica == null || getUsosRestantes(tecnica) == 0) {
			throw new IllegalArgumentException("No conoce la tecnica o no tiene usos.");
		}
		int costo = modificarCostoEnergia(tecnica.getCostoEnergia());
		if (energiaActual < costo) {
			throw new IllegalStateException("Energia insuficiente.");
		}
		// La tecnica valida el objetivo antes de aplicar su efecto.
		tecnica.ejecutar(this, objetivo);
		energiaActual -= costo;
		usosPorTecnica.put(tecnica, usosPorTecnica.get(tecnica) + 1);
	}

	public void recibirDanio(int danio) {
		vidaActual = Math.max(0, vidaActual - modificarDanioRecibido(danio));
	}

	public void perderEnergia(int cantidad) {
		energiaActual = Math.max(0, energiaActual - Math.max(0, cantidad));
	}

	public void recuperarVida(int cantidad) {
		if (estaEnPie()) {
			vidaActual = Math.min(vidaMaxima, vidaActual + Math.max(0, cantidad));
		}
	}

	public void recuperarEnergia(int cantidad) {
		if (estaEnPie()) {
			energiaActual = Math.min(energiaMaxima, energiaActual + Math.max(0, cantidad));
		}
	}

	public boolean tieneDefensaActiva() {
		return defensaTecnica > 0;
	}

	public void activarDefensa(int cantidad) {
		defensaTecnica = Math.max(defensaTecnica, cantidad);
	}

	@Override
	public final int hashCode() {
		return Integer.hashCode(id);
	}

	@Override
	public final boolean equals(Object otro) {
		if (this == otro) {
			return true;
		}
		if (!(otro instanceof Combatiente)) {
			return false;
		}
		Combatiente combatiente = (Combatiente) otro;
		return id == combatiente.id;
	}
}
