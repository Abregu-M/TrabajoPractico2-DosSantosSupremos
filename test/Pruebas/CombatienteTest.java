package Pruebas;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import PaqueteCombatiente.*;
import PaqueteTecnica.*;
import PaqueteEquipamiento.Equipamiento;

class CombatienteTest {
	private Combatiente crear(String tipo, Tecnica tecnica) {
		Escuela escuela = new Escuela("Escuela de prueba", Arrays.asList(tecnica),
				Collections.<Equipamiento>emptyList());
		return new CreadorCombatientes().crear(tipo, "Alumno", escuela);
	}

	@Test
	void tecnicaTieneDistintoDanioSegunEjecutor() {
		Tecnica corte = new CorteDeLosMilAmaneceres();
		Combatiente espada = crear("Cultivador de la Espada", corte);
		Combatiente hechicero = crear("Hechicero del Vacio", corte);
		Combatiente blanco = crear("Hechicero del Vacio", corte);
		espada.iniciarCombate();
		hechicero.iniciarCombate();
		blanco.iniciarCombate();
		espada.usarTecnica(corte, blanco);
		assertEquals(75, blanco.getVidaActual());
		blanco.setVidaActual(100);
		hechicero.usarTecnica(corte, blanco);
		assertEquals(80, blanco.getVidaActual());
		assertEquals(34, hechicero.getEnergiaActual());
	}

	@Test
	void sinEnergiaNoConsumeUsoNiDania() {
		Tecnica corte = new CorteDeLosMilAmaneceres();
		Combatiente a = crear("Cultivador de la Espada", corte);
		Combatiente b = crear("Monje del Cuerpo Celestial", corte);
		a.iniciarCombate();
		b.iniciarCombate();
		a.setEnergiaActual(0);
		assertThrows(IllegalStateException.class, () -> a.usarTecnica(corte, b));
		assertEquals(5, a.getUsosRestantes(corte));
		assertEquals(100, b.getVidaActual());
	}

	@Test
	void usosSeConservanEntreCombatesYSeAgotan() {
		Tecnica meditacion = new MeditacionDelInmortal();
		Combatiente a = crear("Cultivador de la Espada", meditacion);
		for (int i = 0; i < 5; i++) {
			a.iniciarCombate();
			a.setVidaActual(50);
			a.usarTecnica(meditacion, a);
			assertEquals(65, a.getVidaActual());
			a.finalizarCombate();
		}
		a.iniciarCombate();
		assertEquals(100, a.getVidaActual());
		assertEquals(40, a.getEnergiaActual());
		assertThrows(IllegalArgumentException.class, () -> a.usarTecnica(meditacion, a));
		assertEquals(0, a.getUsosRestantes(meditacion));
	}

	@Test
	void caidoNoActuaNiPuedeSerObjetivo() {
		Tecnica corte = new CorteDeLosMilAmaneceres();
		Combatiente a = crear("Cultivador de la Espada", corte);
		Combatiente b = crear("Hechicero del Vacio", corte);
		a.iniciarCombate();
		b.iniciarCombate();
		b.setVidaActual(0);
		assertThrows(IllegalStateException.class, () -> b.atacar(a));
		assertThrows(IllegalStateException.class, () -> b.usarTecnica(corte, a));
		assertThrows(IllegalArgumentException.class, () -> a.atacar(b));
		assertThrows(IllegalArgumentException.class, () -> a.usarTecnica(corte, b));
		assertEquals(40, a.getEnergiaActual());
		assertEquals(5, a.getUsosRestantes(corte));
	}

	@Test
	void defensaTerminaAlFinalizarCombate() {
		Tecnica muro = new MuroDelCuerpoDorado();
		Combatiente a = crear("Monje del Cuerpo Celestial", muro);
		a.iniciarCombate();
		a.usarTecnica(muro, a);
		a.recibirDanio(20);
		assertEquals(88, a.getVidaActual());
		a.finalizarCombate();
		a.iniciarCombate();
		a.recibirDanio(20);
		assertEquals(83, a.getVidaActual());
		assertFalse(a.tieneDefensaActiva());
	}

	@Test
	void basicoNoConsumeEnergiaNiUsos() {
		Tecnica corte = new CorteDeLosMilAmaneceres();
		Combatiente a = crear("Cultivador de la Espada", corte);
		Combatiente b = crear("Hechicero del Vacio", corte);
		a.iniciarCombate();
		b.iniciarCombate();
		a.atacar(b);
		assertEquals(75, b.getVidaActual());
		assertEquals(40, a.getEnergiaActual());
		assertEquals(5, a.getUsosRestantes(corte));
	}

	@Test
	void creadorAsignaIdsEnOrdenInclusoEntreCreadores() {
		Tecnica corte = new CorteDeLosMilAmaneceres();
		Combatiente a = crear("Hechicero del Vacio", corte);
		Combatiente b = crear("Monje del Cuerpo Celestial", corte);
		assertEquals(a.getId() + 1, b.getId());
		assertEquals(Arrays.asList(corte), b.getTecnicas());
		b.getUsosPorTecnica().clear();
		assertEquals(5, b.getUsosRestantes(corte));
	}

	@Test
	void dominioDrenaEnergiaYRugidoEsMasFuerte() {
		Tecnica dominio = new DominioDeLaNada();
		Combatiente a = crear("Hechicero del Vacio", dominio);
		Combatiente b = crear("Hechicero del Vacio", dominio);
		a.aprenderTecnica(new RugidoDeLaBestiaPrimordial());
		a.iniciarCombate();
		b.iniciarCombate();
		a.usarTecnica(dominio, b);
		assertEquals(85, b.getVidaActual());
		assertEquals(32, b.getEnergiaActual());
		a.usarTecnica(a.getTecnicas().get(1), b);
		assertEquals(55, b.getVidaActual());
	}
}
