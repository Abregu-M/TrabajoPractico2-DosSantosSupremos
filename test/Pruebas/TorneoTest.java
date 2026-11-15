package Pruebas;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import PaqueteCombatiente.Combatiente;
import PaqueteTecnica.*;
import PaqueteTorneo.*;
import PaqueteAcontecimientos.*;
import PaquetePresentacion.CronistaDeLaSecta;

class TorneoTest {
    private static int id = 10000;
    private static class Guerrero extends Combatiente {
        Guerrero(String nombre, int vida) { super(id++, nombre, vida, 40); }
    }
    private Combatiente crear(String nombre) { return new Guerrero(nombre, 100); }
    private Pareja pareja(String nombre) { return new Pareja(crear(nombre + "1"), crear(nombre + "2")); }
    private CronistaDeLaSecta cronista(Pareja... parejas) {
        return new CronistaDeLaSecta(Arrays.asList(parejas));
    }
    private List<Acontecimiento> acciones(CronistaDeLaSecta cronista) {
        List<Acontecimiento> resultado = new ArrayList<>();
        for (Acontecimiento e : cronista.getHistorial()) {
            if (e.getTipo() == TipoAcontecimiento.ACCION_REALIZADA) { resultado.add(e); }
        }
        return resultado;
    }
    private void registrarUso(Pareja p, Combatiente c, Tecnica t) {
        p.usarTecnica(c, t, c);
    }
    @Test void parejaOrdenaIntegrantesYRechazaRepetidos() {
        Combatiente a = crear("a"), b = crear("b");
        Pareja p = new Pareja(b,a);
        assertEquals(a,p.getIntegrantes().get(0));
        assertThrows(IllegalArgumentException.class, () -> new Pareja(a,a));
        assertThrows(IllegalArgumentException.class, () -> new Pareja(null,a));
        p.getIntegrantes().clear(); assertEquals(2,p.getIntegrantes().size());
    }
    @Test void companeroSeBloqueaHastaAgotarUsosYSeReiniciaElRegistro() {
        Combatiente a = crear("a"), b = crear("b");
        Tecnica t = new MuroDelCuerpoDorado();
        a.aprenderTecnica(t); b.aprenderTecnica(t);
        Pareja p = new Pareja(a,b); p.iniciarCombate();
        registrarUso(p,a,t);
        assertFalse(p.puedeUsarTecnica(b,t));
        assertThrows(IllegalStateException.class, () -> registrarUso(p,b,t));
        for (int i=0;i<4;i++) { registrarUso(p,a,t); }
        assertTrue(p.puedeUsarTecnica(b,t));
        registrarUso(p,b,t);
        assertEquals(4,b.getUsosRestantes(t));
        p.finalizarCombate(); p.iniciarCombate();
        assertEquals(0,a.getUsosRestantes(t));
        assertTrue(p.puedeUsarTecnica(b,t)); p.finalizarCombate();
    }
    @Test void companeroSeLiberaCuandoElPrimeroCae() {
        Combatiente a = crear("a"), b = crear("b");
        Tecnica t = new MuroDelCuerpoDorado();
        a.aprenderTecnica(t); b.aprenderTecnica(t);
        Pareja p = new Pareja(a,b); p.iniciarCombate(); registrarUso(p,a,t);
        a.setVidaActual(0); assertTrue(p.puedeUsarTecnica(b,t));
        assertEquals(4,a.getUsosRestantes(t)); p.finalizarCombate();
    }
    @Test void nuevoCombateLiberaTecnicaAunqueElPrimeroConserveUsos() {
        Combatiente a = crear("a"), b = crear("b");
        Tecnica t = new MuroDelCuerpoDorado();
        a.aprenderTecnica(t); b.aprenderTecnica(t);
        Pareja p = new Pareja(a,b); p.iniciarCombate(); registrarUso(p,a,t);
        p.finalizarCombate(); p.iniciarCombate();
        assertTrue(p.puedeUsarTecnica(b,t)); assertEquals(4,a.getUsosRestantes(t));
        p.finalizarCombate();
    }
    @Test void duplicadosSeRechazanSinInscribirParcialmente() {
        Torneo t = new Torneo(); Pareja p = pareja("p"); t.inscribir(p);
        assertThrows(IllegalArgumentException.class, () -> t.inscribir(p));
        Pareja repetida = new Pareja(crear("nuevo"),p.getIntegrantes().get(0));
        assertThrows(IllegalArgumentException.class, () -> t.inscribir(repetida));
        assertEquals(1,t.getParejas().size());
        t.inscribir(new Pareja(repetida.getIntegrantes().get(1),crear("otro")));
        assertEquals(2,t.getParejas().size());
    }
    @Test void soloCuatroUOchoParejas() {
        for (int cantidad : new int[] {0,1,2,3,5,6,7,9}) {
            Torneo t = new Torneo(1);
            for (int i=0;i<cantidad;i++) { t.inscribir(pareja("p"+i)); }
            assertThrows(IllegalStateException.class, () -> t.iniciar());
            assertTrue(t.getRondas().isEmpty());
        }
    }
    @Test void crucesSiguenInscripcionYOrdenDeGanadorasConCuatroYOcho() {
        for (int cantidad : new int[] {4,8}) {
            Torneo t = new Torneo(1); List<Pareja> inscriptas = new ArrayList<>();
            for (int i=0;i<cantidad;i++) { inscriptas.add(pareja("p"+i)); }
            // El orden de inscripcion difiere del orden de IDs.
            for (int i=cantidad-1;i>=0;i--) { t.inscribir(inscriptas.get(i)); }
            CronistaDeLaSecta cr = new CronistaDeLaSecta(t.getParejas()); t.agregarObservador(cr);
            t.iniciar();
            assertEquals(cantidad==4 ? 2 : 3,t.getRondas().size());
            List<Pareja> orden = t.getParejas();
            for (int ronda=0;ronda<t.getRondas().size();ronda++) {
                List<Pareja> ganadoras = new ArrayList<>();
                List<Combate> cruces = t.getRondas().get(ronda);
                for (int i=0;i<cruces.size();i++) {
                    assertEquals(orden.get(i*2),cruces.get(i).getPrimera());
                    assertEquals(orden.get(i*2+1),cruces.get(i).getSegunda());
                    // Basicos simetricos a un asalto: gana por ID tras igual vida.
                    Pareja a=orden.get(i*2), b=orden.get(i*2+1);
                    ganadoras.add(a.getId()<b.getId()?a:b);
                }
                orden=ganadoras;
            }
            assertEquals(orden.get(0),t.getCampeona());
            assertEquals(cantidad,cr.obtenerTablaPosiciones().size());
            assertEquals(TipoAcontecimiento.PROCLAMACION,
                    cr.getHistorial().get(cr.getHistorial().size()-1).getTipo());
            assertThrows(IllegalStateException.class, () -> t.iniciar());
            assertThrows(IllegalStateException.class, () -> t.inscribir(pareja("tarde")));
        }
    }
    @Test void turnosCompletosYMenoresIdsPrimero() {
        Pareja a=pareja("a"), b=pareja("b");
        Combate c=new Combate(b,a,1,1); CronistaDeLaSecta cr=cronista(a,b);
        c.agregarObservador(cr); c.ejecutar(); List<Acontecimiento> lista=acciones(cr);
        assertEquals(4,lista.size());
        assertTrue(lista.get(0).obtenerDatos().startsWith("a1 ->"));
        assertTrue(lista.get(1).obtenerDatos().startsWith("a2 ->"));
        assertTrue(lista.get(2).obtenerDatos().startsWith("b1 ->"));
        assertTrue(lista.get(3).obtenerDatos().startsWith("b2 ->"));
    }
    @Test void objetivoMenosVidaYDesempatePorId() {
        Pareja a=pareja("a");
        Combatiente bajo=new Guerrero("bajo",50), alto=crear("alto");
        Pareja b=new Pareja(alto,bajo);
        Combate c=new Combate(a,b,1,1); CronistaDeLaSecta cr=cronista(a,b);
        c.agregarObservador(cr); c.ejecutar();
        assertTrue(acciones(cr).get(0).obtenerDatos().startsWith("a1 -> bajo"));
        // Sin diferencia de vida, se apunta al de menor ID.
        Pareja x=pareja("x"), y=pareja("y"); cr=cronista(x,y);
        c=new Combate(x,y,1,1); c.agregarObservador(cr); c.ejecutar();
        assertTrue(acciones(cr).get(0).obtenerDatos().startsWith("x1 -> y1"));
    }
    @Test void finInmediatoCuandoCaenAmbosSinTurnosDeCaidos() {
        Pareja a=pareja("a"), b=new Pareja(new Guerrero("b1",5),new Guerrero("b2",5));
        Combate c=new Combate(a,b,1,20); CronistaDeLaSecta cr=cronista(a,b);
        c.agregarObservador(cr); assertEquals(a,c.ejecutar());
        assertEquals(1,c.getAsaltosJugados()); assertEquals(2,acciones(cr).size());
        int eliminaciones=0;
        for (Acontecimiento e:cr.getHistorial()) {
            if(e.getTipo()==TipoAcontecimiento.ELIMINACION) { eliminaciones++; }
        }
        assertEquals(2,eliminaciones); assertTrue(b.ambosCaidos());
        assertFalse(a.getIntegrantes().get(0).isEnCombate());
    }
    @Test void limiteDesempataPorVidaYLuegoId() {
        Pareja a=pareja("a"),b=pareja("b");
        Combate c=new Combate(b,a,1,1);
        assertEquals(a,c.ejecutar()); assertEquals(1,c.getAsaltosJugados());
        assertEquals(a.vidaTotal(),b.vidaTotal());
        Pareja rica=new Pareja(new Guerrero("r1",200),new Guerrero("r2",200));
        assertEquals(rica,new Combate(a,rica,1,1).ejecutar());
        assertThrows(IllegalStateException.class, () -> c.ejecutar());
    }
    @Test void tecnicaOfensivaEligeMasUsosYEmpataPorOrdenDeEscuela() {
        Tecnica corte=new CorteDeLosMilAmaneceres(),rugido=new RugidoDeLaBestiaPrimordial();
        Combatiente a=crear("a"); a.aprenderTecnica(corte); a.aprenderTecnica(rugido);
        Pareja p=new Pareja(a,crear("companero")),r=pareja("r");
        CronistaDeLaSecta cr=cronista(p,r); Combate c=new Combate(p,r,1,2);
        c.agregarObservador(cr); c.ejecutar();
        assertTrue(acciones(cr).get(0).obtenerDatos().contains("Corte De Los Mil Amaneceres"));
        assertTrue(acciones(cr).get(4).obtenerDatos().contains("Rugido De La Bestia Primordial"));
    }
    @Test void recuperacionAntesQueDefensaYDefensaAntesQueAtaque() {
        Pareja atacante=pareja("atacante");
        Combatiente a=crear("sanador");
        a.aprenderTecnica(new CorteDeLosMilAmaneceres());
        a.aprenderTecnica(new MuroDelCuerpoDorado());
        a.aprenderTecnica(new MeditacionDelInmortal());
        Pareja defensor=new Pareja(a,crear("auxiliar"));
        CronistaDeLaSecta cr=cronista(atacante,defensor);
        Combate c=new Combate(atacante,defensor,1,1); c.agregarObservador(cr); c.ejecutar();
        assertTrue(acciones(cr).get(2).obtenerDatos().contains("Meditacion Del Inmortal"));
        // Con vida completa, el primer turno elige defensa aunque el corte este antes.
        Pareja rival=pareja("otro"); cr=cronista(defensor,rival);
        c=new Combate(defensor,rival,1,1); c.agregarObservador(cr); c.ejecutar();
        assertTrue(acciones(cr).get(0).obtenerDatos().contains("Muro Del Cuerpo Dorado"));
    }
    @Test void regeneracionPorAsaltoYBasicoCuandoCompaneroBloqueaTecnica() {
        Tecnica t=new DominioDeLaNada(); Combatiente a=crear("a"),b=crear("b");
        a.aprenderTecnica(t); b.aprenderTecnica(t);
        Pareja p=new Pareja(a,b), rival=pareja("r");
        CronistaDeLaSecta cr=cronista(p,rival); Combate c=new Combate(p,rival,1,2);
        c.agregarObservador(cr); c.ejecutar();
        assertTrue(acciones(cr).get(1).obtenerDatos().contains("Ataque basico"));
        assertEquals(25,a.getEnergiaActual()); // 40 - 10 + 5 - 10
        assertEquals(40,b.getEnergiaActual());
    }
    @Test void tablaUsaCapturasOrdenadasPorRondaVidaEId() {
        Pareja a=pareja("a"),b=pareja("b"),c=pareja("c"),d=pareja("d");
        CronistaDeLaSecta cr=cronista(a,b,c,d);
        cr.notificar(new Acontecimiento(TipoAcontecimiento.RESULTADO_COMBATE,1,1,"semifinal",
                a.getId(),b.getId(),a.getId(),100,20));
        cr.notificar(new Acontecimiento(TipoAcontecimiento.RESULTADO_COMBATE,1,1,"semifinal",
                c.getId(),d.getId(),c.getId(),100,20));
        cr.notificar(new Acontecimiento(TipoAcontecimiento.RESULTADO_COMBATE,2,1,"final",
                a.getId(),c.getId(),c.getId(),10,30));
        assertEquals(Arrays.asList(c,a,b,d),cr.obtenerTablaPosiciones());
        a.iniciarCombate(); c.iniciarCombate();
        assertEquals(10,cr.getUltimoResultado(a).vidaDe(a.getId()));
        assertEquals(Arrays.asList(c,a,b,d),cr.obtenerTablaPosiciones());
        cr.getHistorial().clear(); assertEquals(3,cr.getHistorial().size());
        a.finalizarCombate(); c.finalizarCombate();
    }
    @Test void reproducibleMismasParejasMismoEstadoMismoHistorial() {
        List<Pareja> parejas=Arrays.asList(pareja("a"),pareja("b"),pareja("c"),pareja("d"));
        List<String> primero=null; Pareja campeona=null;
        for(int repeticion=0;repeticion<2;repeticion++) {
            // Reconstruir el mismo estado inicial. No hay tecnicas en este escenario.
            for(Pareja p:parejas) { for(Combatiente c:p.getIntegrantes()) {
                c.setVidaActual(c.getVidaMaxima()); c.setEnergiaActual(c.getEnergiaMaxima());
            } }
            Torneo t=new Torneo(3); for(Pareja p:parejas) { t.inscribir(p); }
            CronistaDeLaSecta cr=new CronistaDeLaSecta(parejas); t.agregarObservador(cr); t.iniciar();
            List<String> firma=new ArrayList<>();
            for(Acontecimiento e:cr.getHistorial()) {
                firma.add(e.getTipo()+"/"+e.getRonda()+"/"+e.getAsalto()+"/"+e.obtenerDatos()
                        +"/"+e.getIdPrimera()+"/"+e.getIdSegunda()+"/"+e.getIdGanadora()
                        +"/"+e.getVidaPrimera()+"/"+e.getVidaSegunda());
            }
            if(repeticion==0) { primero=firma; campeona=t.getCampeona(); }
            else { assertEquals(primero,firma); assertEquals(campeona,t.getCampeona()); }
        }
    }
    @Test void observadoresNuevosRecibenSinCambiarLogica() {
        Torneo t=new Torneo(1); for(int i=0;i<4;i++) { t.inscribir(pareja("p"+i)); }
        final List<Acontecimiento> recibidos=new ArrayList<>();
        Observador observador=new Observador() {
            @Override public void notificar(Acontecimiento e) { recibidos.add(e); }
        };
        CronistaDeLaSecta cr=new CronistaDeLaSecta(t.getParejas());
        t.agregarObservador(observador); t.agregarObservador(cr); t.iniciar();
        assertEquals(cr.getHistorial(),recibidos);
    }
    @Test void sinEnergiaOSinUsosSeleccionaBasico() {
        Tecnica cara = new Tecnica("Imposible", 100) {
            @Override public void ejecutar(Combatiente a, Combatiente b) {
                fail("No debe ejecutarse una tecnica sin energia.");
            }
        };
        Combatiente a=crear("a"); a.aprenderTecnica(cara);
        Pareja p=new Pareja(a,crear("b")),r=pareja("r");
        CronistaDeLaSecta cr=cronista(p,r); Combate c=new Combate(p,r,1,1);
        c.agregarObservador(cr); c.ejecutar();
        assertTrue(acciones(cr).get(0).obtenerDatos().contains("Ataque basico"));
        Tecnica corte = new CorteDeLosMilAmaneceres();
        a=crear("agotado"); a.aprenderTecnica(corte);
        Combatiente blanco=new Guerrero("blanco",1000);
        a.iniciarCombate(); blanco.iniciarCombate();
        for(int i=0;i<5;i++) { a.usarTecnica(corte,blanco); }
        a.finalizarCombate(); blanco.finalizarCombate();
        p=new Pareja(a,crear("auxiliar")); r=pareja("r"); cr=cronista(p,r);
        c=new Combate(p,r,1,1); c.agregarObservador(cr); c.ejecutar();
        assertTrue(acciones(cr).get(0).obtenerDatos().contains("Ataque basico"));
        assertEquals(0,a.getUsosRestantes(corte));
    }
    @Test void segundoCombateRestauraCaidosPeroNoUsosYEventoNoCambia() {
        Combatiente a=crear("a"); Tecnica t=new CorteDeLosMilAmaneceres();
        a.aprenderTecnica(t); Pareja p=new Pareja(a,crear("b")),r=pareja("r");
        CronistaDeLaSecta cr=cronista(p,r); Combate c=new Combate(p,r,1,1);
        c.agregarObservador(cr); c.ejecutar();
        Acontecimiento resultado=cr.getUltimoResultado(p);
        int vidaGuardada=resultado.vidaDe(p.getId()); String texto=resultado.obtenerDatos();
        a.setVidaActual(0); a.setEnergiaActual(0);
        c=new Combate(p,r,2,1); c.agregarObservador(cr); c.ejecutar();
        assertTrue(a.estaEnPie()); assertEquals(3,a.getUsosRestantes(t));
        assertEquals(32,a.getEnergiaActual());
        assertEquals(vidaGuardada,resultado.vidaDe(p.getId()));
        assertEquals(texto,resultado.obtenerDatos());
    }
    @Test void caidoNoRecuperaEnergiaNiVuelveAActuarEnAsaltosSiguientes() {
        Pareja a=pareja("a"); Combatiente fragil=new Guerrero("fragil",5);
        Combatiente resistente=new Guerrero("resistente",500);
        Tecnica drenaje=new DominioDeLaNada();
        a.getIntegrantes().get(0).aprenderTecnica(drenaje);
        Pareja b=new Pareja(fragil,resistente); CronistaDeLaSecta cr=cronista(a,b);
        Combate c=new Combate(a,b,1,3); c.agregarObservador(cr); c.ejecutar();
        assertEquals(0,fragil.getVidaActual());
        assertEquals(32,fragil.getEnergiaActual()); // drenado al caer, sin regeneracion posterior
        for(Acontecimiento e:acciones(cr)) {
            assertFalse(e.obtenerDatos().startsWith("fragil ->"));
            if(e.getAsalto()>1) { assertFalse(e.obtenerDatos().contains("-> fragil")); }
        }
    }
}
