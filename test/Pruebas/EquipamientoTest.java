package Pruebas;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import PaqueteCombatiente.Combatiente;
import PaqueteEquipamiento.*;

// AGREGADO - PARTE 1. No se necesita un torneo para probar estas reglas.
class EquipamientoTest {
	// Subclase solo para pruebas: los tres tipos reales se agregan en la parte 2.
	private static class CombatienteDePrueba extends Combatiente {
		CombatienteDePrueba(int id, String nombre) {
			super(id, nombre, 100, 40);
		}
	}

	private Combatiente nuevo() {
		return new CombatienteDePrueba(1, "Aurelio");
	}

	@Test
	void cuartoEquipoSeRechazaSinCambios() {
		Combatiente c = nuevo();
		c.equipar(new EspadaCelestial());
		c.equipar(new ArmaduraDorada());
		c.equipar(new AnilloEspiritual());
		assertThrows(IllegalStateException.class, () -> c.equipar(new SelloDelFenix()));
		assertEquals(3, c.getEquipamientos().size());
		assertEquals(100, c.getVidaMax());
		assertEquals(60, c.getEnergiaMax());
	}

	@Test
	void categoriaRepetidaSeRechazaSinCambios() {
		Combatiente c = nuevo();
		c.equipar(new SelloDelFenix());
		assertThrows(IllegalArgumentException.class, () -> c.equipar(new SelloDelFenix()));
		assertEquals(1, c.getEquipamientos().size());
		assertEquals(130, c.getVidaMax());
		assertEquals(100, c.getVidaActual());
	}

	@Test
	void mismoObjetoNoSePuedeEquiparDosVeces() {
		Combatiente c = nuevo();
		Equipamiento equipo = new AnilloEspiritual();
		c.equipar(equipo);
		assertThrows(IllegalArgumentException.class, () -> equipo.aplicarEfecto(c));
		assertEquals(60, c.getEnergiaMax());
	}

	@Test
	void aumentarMaximosNoRecuperaActuales() {
		Combatiente c = nuevo();
		c.setVidaActual(70);
		c.setEnergiaActual(10);
		c.equipar(new SelloDelFenix());
		c.equipar(new AnilloEspiritual());
		assertEquals(130, c.getVidaMax());
		assertEquals(60, c.getEnergiaMax());
		assertEquals(70, c.getVidaActual());
		assertEquals(10, c.getEnergiaActual());
	}

	@Test
	void quitarSelloRecortaVidaAlNuevoMaximo() {
		Combatiente c = nuevo();
		Equipamiento sello = new SelloDelFenix();
		c.equipar(sello);
		c.setVidaActual(125);
		c.desequipar(sello);
		assertEquals(100, c.getVidaMax());
		assertEquals(100, c.getVidaActual());
	}

	@Test
	void quitarAnilloRecortaEnergiaAlNuevoMaximo() {
		Combatiente c = nuevo();
		Equipamiento anillo = new AnilloEspiritual();
		anillo.aplicarEfecto(c);
		c.setEnergiaActual(55);
		anillo.removerEfecto(c);
		assertEquals(40, c.getEnergiaMax());
		assertEquals(40, c.getEnergiaActual());
	}

	@Test
	void quitarEquipoNoRecuperaValoresBajos() {
		Combatiente c = nuevo();
		Equipamiento sello = new SelloDelFenix();
		c.equipar(sello);
		c.setVidaActual(20);
		c.desequipar(sello);
		assertEquals(20, c.getVidaActual());
	}

	@Test
	void noPermiteCambiarEquipoDuranteCombate() {
		Combatiente c = nuevo();
		Equipamiento espada = new EspadaCelestial();
		c.equipar(espada);
		c.iniciarCombate();
		assertThrows(IllegalStateException.class, () -> c.equipar(new ArmaduraDorada()));
		assertThrows(IllegalStateException.class, () -> c.desequipar(espada));
		assertThrows(IllegalStateException.class, () -> espada.removerEfecto(c));
		assertEquals(1, c.getEquipamientos().size());
		assertEquals(15, c.modificarDanioProvocado(10));
	}

	@Test
	void permiteCambiarEquipoEntreCombates() {
		Combatiente c = nuevo();
		Equipamiento espada = new EspadaCelestial();
		c.equipar(espada);
		c.iniciarCombate();
		c.finalizarCombate();
		c.desequipar(espada);
		c.equipar(new ArmaduraDorada());
		assertEquals(10, c.modificarDanioProvocado(10));
		assertEquals(7, c.modificarDanioRecibido(10));
	}

	@Test
	void efectosCoexistenYSeRemuevenIndividualmente() {
		Combatiente c = nuevo();
		Equipamiento espada = new EspadaCelestial();
		c.equipar(espada);
		c.equipar(new ArmaduraDorada());
		c.equipar(new AnilloEspiritual());
		assertEquals(15, c.modificarDanioProvocado(10));
		assertEquals(7, c.modificarDanioRecibido(10));
		assertEquals(0, c.modificarDanioRecibido(2));
		assertEquals(60, c.getEnergiaMax());
		c.desequipar(espada);
		assertEquals(10, c.modificarDanioProvocado(10));
		assertEquals(7, c.modificarDanioRecibido(10));
		assertEquals(60, c.getEnergiaMax());
	}

	@Test
	void consultarEquipoNoPermiteModificarElOriginal() {
		Combatiente c = nuevo();
		c.equipar(new SelloDelFenix());
		c.getEquipamientos().clear();
		c.getEquipamientos().add(new EspadaCelestial());
		assertEquals(1, c.getEquipamientos().size());
		assertEquals(130, c.getVidaMax());
		assertEquals(10, c.modificarDanioProvocado(10));
	}

	@Test
	void quitarEquipoAjenoSeRechazaSinCambios() {
		Combatiente c = nuevo();
		c.equipar(new AnilloEspiritual());
		assertThrows(IllegalArgumentException.class, () -> c.desequipar(new AnilloEspiritual()));
		assertEquals(60, c.getEnergiaMax());
		assertEquals(1, c.getEquipamientos().size());
	}

	@Test
	void equipoNuloSeRechazaSinCambios() {
		Combatiente c = nuevo();
		assertThrows(IllegalArgumentException.class, () -> c.equipar(null));
		assertThrows(IllegalArgumentException.class, () -> c.desequipar(null));
		assertTrue(c.getEquipamientos().isEmpty());
	}

	@Test
	void comenzarCombateRestauraInclusoAlCaido() {
		Combatiente c = nuevo();
		c.equipar(new SelloDelFenix());
		c.equipar(new AnilloEspiritual());
		c.setVidaActual(0);
		c.setEnergiaActual(0);
		c.iniciarCombate();
		assertEquals(130, c.getVidaActual());
		assertEquals(60, c.getEnergiaActual());
		assertTrue(c.isEnCombate());
	}

	@Test
	void iniciarOtraVezNoPermiteCurarseEnCombate() {
		Combatiente c = nuevo();
		c.iniciarCombate();
		c.setVidaActual(20);
		assertThrows(IllegalStateException.class, () -> c.iniciarCombate());
		assertEquals(20, c.getVidaActual());
	}

	@Test
	void identidadDependeDelIdYNoDelNombre() {
	    Combatiente a = nuevo();
	    Combatiente b = new CombatienteDePrueba(1, "Otro nombre");
	    Combatiente distinto = new CombatienteDePrueba(2, "Aurelio");
	    
	    assertEquals(a, b);
	    assertEquals(a.hashCode(), b.hashCode());
	    assertNotEquals(a, distinto);
	    assertNotEquals(a, null);
	    assertNotEquals(a, "Aurelio");
	}

	@Test
	void estadoInvalidoNoSeAsigna() {
		Combatiente c = nuevo();
		assertThrows(IllegalArgumentException.class, () -> c.setVidaActual(-1));
		assertThrows(IllegalArgumentException.class, () -> c.setVidaActual(101));
		assertThrows(IllegalArgumentException.class, () -> c.setEnergiaActual(-1));
		assertThrows(IllegalArgumentException.class, () -> c.setEnergiaActual(41));
		assertEquals(100, c.getVidaActual());
		assertEquals(40, c.getEnergiaActual());
	}
}
