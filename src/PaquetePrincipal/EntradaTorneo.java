package PaquetePrincipal;

import java.util.Scanner;
import java.util.NoSuchElementException;
import PaqueteCombatiente.Combatiente;
import PaqueteCombatiente.CreadorCombatientes;
import PaqueteCombatiente.Escuela;
import PaquetePresentacion.HeraldoImperial;
import PaqueteTorneo.Pareja;
import PaqueteTorneo.Torneo;

public class EntradaTorneo {
	private final Scanner scanner;
	private final HeraldoImperial heraldo;
	private final CreadorCombatientes creador;
	private final Escuela[] escuelas;
	private final String[] tipos = { "Cultivador de la Espada", "Monje del Cuerpo Celestial", "Hechicero del Vacio" };

	public EntradaTorneo(Scanner scanner, HeraldoImperial heraldo, CreadorCombatientes creador, Escuela[] escuelas) {
		this.scanner = scanner;
		this.heraldo = heraldo;
		this.creador = creador;
		this.escuelas = escuelas.clone();
	}

	public Torneo cargarTorneo() {

		heraldo.anunciar("===============================================================");
		heraldo.anunciar("                INSCRIPCION AL TORNEO DE LOS INMORTALES");
		heraldo.anunciar("===============================================================");
		heraldo.anunciar("\nMODALIDAD DE INSCRIPCION");
		heraldo.anunciar("1. Carga manual de todas las parejas");
		heraldo.anunciar("2. Crear primera pareja manualmente y completar el resto");

		int modalidad = leerOpcion(2);

		int cantidad;
		do {
			cantidad = leerEntero("Ingrese la cantidad de parejas (4 u 8):");
			if (cantidad != 4 && cantidad != 8) {
				heraldo.anunciar("El torneo debe tener 4 u 8 parejas.");
			}
		} while (cantidad != 4 && cantidad != 8);

		Torneo torneo = new Torneo(20);

		if (modalidad == 1) {
			cargarParejasManualmente(torneo, cantidad);
		} else {
			cargarParejasMixtas(torneo, cantidad);
		}

		return torneo;
	}

	private Combatiente cargarCombatiente(String titulo) {
		heraldo.anunciar("\n" + titulo);
		String nombre;
		do {
			heraldo.anunciar("Nombre del combatiente:");
			nombre = leerLinea().trim();
			if (nombre.isEmpty()) {
				heraldo.anunciar("El nombre no puede estar vacio.");
			}
		} while (nombre.isEmpty());

		heraldo.anunciar("Tipo de combatiente:");
		for (int i = 0; i < tipos.length; i++) {
			heraldo.anunciar((i + 1) + ". " + tipos[i]);
		}
		int tipo = leerOpcion(tipos.length);

		heraldo.anunciar("Escuela que lo prepara:");
		for (int i = 0; i < escuelas.length; i++) {
			heraldo.anunciar((i + 1) + ". " + escuelas[i].getNombre());
		}
		int escuela = leerOpcion(escuelas.length);
		return creador.crear(tipos[tipo - 1], nombre, escuelas[escuela - 1]);
	}

	private int leerOpcion(int maximo) {
		int opcion;
		do {
			opcion = leerEntero("Elija una opcion:");
			if (opcion < 1 || opcion > maximo) {
				heraldo.anunciar("Ingrese una opcion entre 1 y " + maximo + ".");
			}
		} while (opcion < 1 || opcion > maximo);
		return opcion;
	}

	private int leerEntero(String mensaje) {
		// Leer siempre lineas evita dejar pendiente el Enter de nextInt().
		while (true) {
			heraldo.anunciar(mensaje);
			String linea = leerLinea();
			try {
				return Integer.parseInt(linea.trim());
			} catch (NumberFormatException e) {
				heraldo.anunciar("Debe ingresar un numero entero.");
			}
		}
	}

	private String leerLinea() {
		if (!scanner.hasNextLine()) {
			throw new NoSuchElementException("Se termino la entrada de datos.");
		}
		return scanner.nextLine();
	}

	private void cargarParejasManualmente(Torneo torneo, int cantidad) {
		for (int i = 1; i <= cantidad; i++) {
			heraldo.anunciar("\n---------------- PAREJA " + i + " ----------------");

			Combatiente primero = cargarCombatiente("PRIMER INTEGRANTE");
			Combatiente segundo = cargarCombatiente("SEGUNDO INTEGRANTE");

			Pareja pareja = new Pareja(primero, segundo);
			torneo.inscribir(pareja);

			heraldo.anunciar("Pareja inscripta: " + pareja.getNombre());
		}
	}

	private void cargarParejasMixtas(Torneo torneo, int cantidad) {
		heraldo.anunciar("\n---------------- PAREJA 1 ----------------");

		Combatiente primero = cargarCombatiente("PRIMER INTEGRANTE");
		Combatiente segundo = cargarCombatiente("SEGUNDO INTEGRANTE");

		torneo.inscribir(new Pareja(primero, segundo));

		String[][] participantes = { { "Liang Feng", "Cultivador de la Espada", "Escuela de la Espada Celestial" },
				{ "Aurelia", "Hechicero del Vacio", "Palacio del Vacio" },

				{ "Wei Chen", "Monje del Cuerpo Celestial", "Monasterio del Cuerpo Dorado" },
				{ "Kael Storm", "Cultivador de la Espada", "Escuela de la Espada Celestial" },

				{ "Shen Rui", "Hechicero del Vacio", "Palacio del Vacio" },
				{ "Valeria", "Monje del Cuerpo Celestial", "Monasterio del Cuerpo Dorado" },

				{ "Darius Blackthorn", "Cultivador de la Espada", "Escuela de la Espada Celestial" },
				{ "Yun Hua", "Hechicero del Vacio", "Palacio del Vacio" },

				{ "Bai Long", "Monje del Cuerpo Celestial", "Monasterio del Cuerpo Dorado" },
				{ "Eldric", "Cultivador de la Espada", "Escuela de la Espada Celestial" },

				{ "Feng Wu", "Hechicero del Vacio", "Palacio del Vacio" },
				{ "Morgana", "Monje del Cuerpo Celestial", "Monasterio del Cuerpo Dorado" },

				{ "Cassian Veyr", "Cultivador de la Espada", "Escuela de la Espada Celestial" },
				{ "Su Mei", "Hechicero del Vacio", "Palacio del Vacio" } };

		for (int i = 0; i < (cantidad - 1) * 2; i += 2) {
			Combatiente a = creador.crear(participantes[i][1], participantes[i][0], buscarEscuela(participantes[i][2]));

			Combatiente b = creador.crear(participantes[i + 1][1], participantes[i + 1][0],
					buscarEscuela(participantes[i + 1][2]));

			Pareja pareja = new Pareja(a, b);
			torneo.inscribir(pareja);

			heraldo.anunciar("Pareja inscripta: " + pareja.getNombre());
		}
	}

	private Escuela buscarEscuela(String nombre) {
		for (Escuela escuela : escuelas) {
			if (escuela.getNombre().equals(nombre)) {
				return escuela;
			}
		}

		throw new IllegalArgumentException("Escuela inexistente: " + nombre);
	}

}
