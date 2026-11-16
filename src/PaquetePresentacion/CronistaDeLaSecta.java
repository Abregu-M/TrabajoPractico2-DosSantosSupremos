package PaquetePresentacion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import PaqueteAcontecimientos.*;
import PaqueteTorneo.Pareja;

public class CronistaDeLaSecta implements Observador {
	private final List<Acontecimiento> historial = new ArrayList<>();
	private final Map<Integer, Pareja> parejas = new HashMap<>();
	private final Map<Integer, Acontecimiento> ultimosResultados = new HashMap<>();

	// Identidades para devolver List<Pareja>; los resultados siempre son capturas.
	public CronistaDeLaSecta(List<Pareja> inscriptas) {
		for (Pareja p : inscriptas) {
			parejas.put(p.getId(), p);
		}
	}

	@Override
	public void notificar(Acontecimiento evento) {
		historial.add(evento);
		if (evento.getTipo() == TipoAcontecimiento.RESULTADO_COMBATE) {
			ultimosResultados.put(evento.getIdPrimera(), evento);
			ultimosResultados.put(evento.getIdSegunda(), evento);
		}
	}

	public List<Acontecimiento> getHistorial() {
		return new ArrayList<>(historial);
	}

	public Acontecimiento getUltimoResultado(Pareja pareja) {
		return ultimosResultados.get(pareja.getId());
	}

	public List<Pareja> obtenerTablaPosiciones() {
		List<Pareja> tabla = new ArrayList<>();
		for (Integer id : ultimosResultados.keySet()) {
			Pareja p = parejas.get(id);
			if (p == null) {
				throw new IllegalStateException("El Cronista no conoce una pareja del resultado.");
			}
			tabla.add(p);
		}
		Collections.sort(tabla, new Comparator<Pareja>() {
			@Override
			public int compare(Pareja a, Pareja b) {
				Acontecimiento ra = getUltimoResultado(a), rb = getUltimoResultado(b);
				int orden = Integer.compare(rb.getRonda(), ra.getRonda());
				if (orden == 0) {
					orden = Integer.compare(rb.vidaDe(b.getId()), ra.vidaDe(a.getId()));
				}
				if (orden == 0) {
					orden = Integer.compare(a.getId(), b.getId());
				}
				return orden;
			}
		});
		return tabla;
	}
}
