package PaqueteCombatiente;

public class CreadorCombatientes {
	// Compartido para evitar IDs repetidos si se usan dos creadores.
	private static int proximoId = 1;

	public Combatiente crear(String tipo, String nombre, Escuela escuela) {
		if (escuela == null) {
			throw new IllegalArgumentException("Escuela obligatoria.");
		}
		Combatiente combatiente;
		if ("Cultivador de la Espada".equals(tipo)) {
			combatiente = new CultivadorDeLaEspada(proximoId, nombre);
		} else if ("Monje del Cuerpo Celestial".equals(tipo)) {
			combatiente = new MonjeDelCuerpoCelestial(proximoId, nombre);
		} else if ("Hechicero del Vacio".equals(tipo) || "Hechicero del Vacío".equals(tipo)) {
			combatiente = new HechiceroDelVacio(proximoId, nombre);
		} else {
			throw new IllegalArgumentException("Tipo desconocido.");
		}
		escuela.preparar(combatiente);
		proximoId++;
		return combatiente;
	}
}
