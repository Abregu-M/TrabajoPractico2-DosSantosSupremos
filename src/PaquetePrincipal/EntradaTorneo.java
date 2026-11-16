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
		int cantidad;
		do {
			cantidad = leerEntero("Ingrese la cantidad de parejas (4 u 8):");
			if (cantidad != 4 && cantidad != 8) {
				heraldo.anunciar("El torneo debe tener 4 u 8 parejas.");
			}
		} while (cantidad != 4 && cantidad != 8);

		Torneo torneo = new Torneo(20);
		for (int i = 1; i <= cantidad; i++) {
			heraldo.anunciar("\n---------------- PAREJA " + i + " ----------------");
			Combatiente primero = cargarCombatiente("PRIMER INTEGRANTE");
			Combatiente segundo = cargarCombatiente("SEGUNDO INTEGRANTE");
			Pareja pareja = new Pareja(primero, segundo);
			torneo.inscribir(pareja);
			heraldo.anunciar("Pareja inscripta: " + pareja.getNombre());
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
}
