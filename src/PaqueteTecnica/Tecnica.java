package PaqueteTecnica;

import PaqueteCombatiente.Combatiente;

public abstract class Tecnica {
    public boolean esRecuperacion() { return false; }
    public boolean esDefensiva() { return false; }
    public String getNombreVisible() {
        return nombre.replaceAll("([a-z])([A-Z])", "$1 $2");
    }

	private final String nombre;
	private final int costoEnergia;

	protected Tecnica(String nombre, int costoEnergia) {
		if (nombre == null || nombre.trim().isEmpty() || costoEnergia < 0) {
			throw new IllegalArgumentException("Datos de tecnica invalidos.");
		}
		this.nombre = nombre;
		this.costoEnergia = costoEnergia;
	}

	public String getNombre() {
		return nombre;
	}

	public int getCostoEnergia() {
		return costoEnergia;
	}

	// Entrada del efecto: el gasto y los usos se controlan en usarTecnica.
	public abstract void ejecutar(Combatiente ejecutor, Combatiente objetivo);

	protected void validarObjetivo(Combatiente ejecutor, Combatiente objetivo, boolean personal) {
		if (ejecutor == null || objetivo == null || !ejecutor.isEnCombate() || !objetivo.isEnCombate()
				|| !ejecutor.estaEnPie() || !objetivo.estaEnPie() || (personal && ejecutor != objetivo)
				|| (!personal && ejecutor == objetivo)) {
			throw new IllegalArgumentException("Objetivo invalido para la tecnica.");
		}
	}
}
