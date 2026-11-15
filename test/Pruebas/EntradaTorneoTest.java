package Pruebas;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import PaquetePrincipal.EntradaTorneo;
import PaqueteCombatiente.*;
import PaqueteEquipamiento.*;
import PaqueteTecnica.*;
import PaqueteTorneo.*;
import PaquetePresentacion.HeraldoImperial;

class EntradaTorneoTest {
    private EntradaTorneo entrada(String datos) {
        Escuela[] escuelas = {
            new Escuela("Espada", Arrays.<Tecnica>asList(new CorteDeLosMilAmaneceres()),
                    Arrays.<Equipamiento>asList(new EspadaCelestial())),
            new Escuela("Palacio", Arrays.<Tecnica>asList(new DominioDeLaNada()),
                    Arrays.<Equipamiento>asList(new AnilloEspiritual())),
            new Escuela("Monasterio", Arrays.<Tecnica>asList(new MuroDelCuerpoDorado()),
                    Arrays.<Equipamiento>asList(new ArmaduraDorada()))
        };
        // No se lee la salida de pantalla: se verifica el estado cargado.
        HeraldoImperial silencioso = new HeraldoImperial() {
            @Override public void anunciar(String mensaje) { }
        };
        return new EntradaTorneo(new Scanner(datos), silencioso, new CreadorCombatientes(), escuelas);
    }
    private String participantes(int desde, int hasta) {
        String texto = "";
        for (int i = desde; i < hasta; i++) {
            texto += "Persona " + i + "\n" + (i % 3 + 1) + "\n" + (i % 3 + 1) + "\n";
        }
        return texto;
    }
    @Test void permiteCargarCuatroYOchoParejasYEjecutarElTorneo() {
        for (int cantidad : new int[] {4, 8}) {
            Torneo torneo = entrada(cantidad + "\n" + participantes(0,cantidad*2)).cargarTorneo();
            assertEquals(cantidad,torneo.getParejas().size());
            assertEquals("Persona 0",torneo.getParejas().get(0).getIntegrantes().get(0).getNombre());
            torneo.iniciar(); assertNotNull(torneo.getCampeona());
            assertEquals(cantidad == 4 ? 2 : 3,torneo.getRondas().size());
        }
    }
    @Test void repiteAnteTextoCantidadInvalidaNombreVacioYOpcionesInvalidas() {
        String datos="abc\n5\n4\n   \n  Ana Maria  \nx\n0\n4\n1\n-1\n9\n2\n"
                + participantes(1,8);
        Torneo torneo=entrada(datos).cargarTorneo();
        Combatiente ana=torneo.getParejas().get(0).getIntegrantes().get(0);
        assertEquals("Ana Maria",ana.getNombre());
        assertEquals(15,ana.modificarDanioProvocado(10)); // Cultivador
        assertEquals(60,ana.getEnergiaMaxima()); // Escuela Palacio
        assertEquals(4,torneo.getParejas().size());
    }
    @Test void finalDeEntradaNoDejaUnBucleInfinito() {
        assertThrows(NoSuchElementException.class, () -> entrada("").cargarTorneo());
        assertThrows(NoSuchElementException.class, () -> entrada("4\nAna\n1\n").cargarTorneo());
    }
}
