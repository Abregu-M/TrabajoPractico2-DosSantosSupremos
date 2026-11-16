package PaquetePresentacion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import PaqueteAcontecimientos.*;
import PaqueteCombatiente.Combatiente;
import PaqueteTorneo.Pareja;
import PaqueteEquipamiento.Equipamiento;

// Unica clase que escribe por pantalla. La logica no depende de estos textos.
public class HeraldoImperial implements Observador {
	private final Map<Integer, Acontecimiento> resultados = new HashMap<>();
	private int ultimaRonda;

	public void anunciar(String mensaje) {
		System.out.println(mensaje);
	}

	public void mostrarEstado(Combatiente c) {
		anunciar(String.format("  %-15s Vida %3d/%-3d  Energia %2d/%-2d", c.getNombre(), c.getVidaActual(),
				c.getVidaMaxima(), c.getEnergiaActual(), c.getEnergiaMaxima()));
	}

	public void mostrarInscripciones(List<Pareja> parejas) {
	    anunciar("===============================================================");
	    anunciar("                  EL TORNEO DE LOS INMORTALES");
	    anunciar("              La eleccion de los Dos Santos Supremos");
	    anunciar("===============================================================");
	    anunciar("\nPAREJAS INSCRIPTAS: " + parejas.size());

	    for (int i = 0; i < parejas.size(); i++) {
	        Pareja pareja = parejas.get(i);

	        anunciar(String.format("\n  %d. %s", i + 1, pareja.getNombre()));

	        for (Combatiente combatiente : pareja.getIntegrantes()) {
	            String equipamientos = combatiente.getEquipamientos().isEmpty()
	                    ? "Sin equipamiento"
	                    : combatiente.getEquipamientos().stream()
	                            .map(Equipamiento::getNombre)
	                            .collect(java.util.stream.Collectors.joining(", "));

	            anunciar(String.format("     %-15s | %s",
	                    combatiente.getNombre(), equipamientos));
	        }
	    }
	}

	@Override
	public void notificar(Acontecimiento e) {
		switch (e.getTipo()) {
		case INICIO_COMBATE:
			if (e.getRonda() != ultimaRonda) {
				ultimaRonda = e.getRonda();
				anunciar("\n===============================================================");
				anunciar("                         RONDA " + ultimaRonda);
				anunciar("===============================================================");
			}
			anunciar("\n  " + e.obtenerDatos());
			anunciar("---------------------------------------------------------------");
			break;
		case INICIO_ASALTO:
			anunciar("\n  [ " + e.obtenerDatos() + " ]");
			anunciar(String.format("    %-18s    %-18s | %-30s | %-10s | %-7s | %-5s", "ATACANTE", "OBJETIVO", "ACCION",
					"VIDA", "ENERGIA", "USOS"));
			break;
		case ACCION_REALIZADA:
			anunciar(String.format("    %-18s -> %-18s | %-30s | %3d -> %-3d | %7d | %2s", e.getAtacante(),
					e.getObjetivo(), e.getAccion(), e.getVidaAntes(), e.getVidaDespues(), e.getEnergiaRestante(),
					e.getUsosRestantes() == -1 ? "-" : String.valueOf(e.getUsosRestantes())));
			break;
		case ELIMINACION:
			anunciar("    ** " + e.obtenerDatos());
			break;
		case RESULTADO_COMBATE:
			resultados.put(e.getIdPrimera(), e);
			resultados.put(e.getIdSegunda(), e);
			anunciar("\n  RESULTADO - RONDA " + e.getRonda());
			anunciar("  " + e.obtenerDatos().replace("\n", "\n  "));
			break;
		case PROCLAMACION:
			anunciar("\n===============================================================");
			anunciar("                      DOS SANTOS SUPREMOS");
			anunciar("  " + e.obtenerDatos());
			anunciar("===============================================================");
			break;
		default:
			break;
		}
	}

	public void mostrarTabla(List<Pareja> posiciones) {
		anunciar("\nTABLA DE POSICIONES");
		anunciar(String.format("%-5s %-36s %-7s %-7s", "Pos.", "Pareja", "Ronda", "Vida"));
		anunciar("---------------------------------------------------------------");
		int posicion = 1;
		for (Pareja p : posiciones) {
			Acontecimiento e = resultados.get(p.getId());
			anunciar(String.format("%-5d %-36s %-7d %-7d", posicion++, p.getNombre(), e.getRonda(),
					e.vidaDe(p.getId())));
		}
		anunciar("---------------------------------------------------------------");
		anunciar("Orden: ultima ronda jugada, vida final y menor identificador.");
	}
}
