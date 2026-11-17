package PaquetePrincipal;

import java.util.Arrays;
import java.util.Scanner;
import java.util.NoSuchElementException;
import PaqueteCombatiente.*;
import PaqueteEquipamiento.*;
import PaquetePresentacion.*;
import PaqueteTecnica.*;
import PaqueteTorneo.*;

public class Main {
	public static void main(String[] args) {
		CreadorCombatientes creador = new CreadorCombatientes();
		
		Tecnica corte = new CorteDeLosMilAmaneceres();
		Tecnica rugido = new RugidoDeLaBestiaPrimordial();
		Tecnica dominio = new DominioDeLaNada();
		Tecnica meditacion = new MeditacionDelInmortal();
		Tecnica muro = new MuroDelCuerpoDorado();
		Tecnica aguijon = new AguijonDeLaSerpienteInmortal();
		
		Escuela espada = new Escuela("Escuela de la Espada Celestial", Arrays.asList(corte, rugido),
				Arrays.<Equipamiento>asList(new EspadaCelestial()));
		
		Escuela palacio = new Escuela("Palacio del Vacio", Arrays.asList(dominio, meditacion, aguijon),
				Arrays.<Equipamiento>asList(new AnilloEspiritual()));
		
		Escuela monasterio = new Escuela("Monasterio del Cuerpo Dorado", Arrays.asList(muro, meditacion),
				Arrays.<Equipamiento>asList(new ArmaduraDorada(), new SelloDelFenix()));
		
		Escuela[] escuelas = { espada, palacio, monasterio };
		
		HeraldoImperial heraldo = new HeraldoImperial();
		// Main configura las escuelas; el usuario elige sus participantes.
		Scanner scanner = new Scanner(System.in);
		EntradaTorneo entrada = new EntradaTorneo(scanner, heraldo, creador, escuelas);
		Torneo torneo;
		try {
			torneo = entrada.cargarTorneo();
		} catch (NoSuchElementException e) {
			heraldo.anunciar("Carga interrumpida. El torneo no se inicio.");
			return;
		}
		CronistaDeLaSecta cronista = new CronistaDeLaSecta(torneo.getParejas());
		torneo.agregarObservador(heraldo);
		torneo.agregarObservador(cronista);
		heraldo.mostrarInscripciones(torneo.getParejas());
		torneo.iniciar();
		// El Cronista ordena usando capturas; el Heraldo solo presenta.
		heraldo.mostrarTabla(cronista.obtenerTablaPosiciones());
	}
}
