package PaqueteAcontecimientos;

// Captura inmutable: no guarda referencias a combatientes ni parejas mutables.

public final class Acontecimiento {

    private final TipoAcontecimiento tipo;
    private final int ronda;
    private final int asalto;
    private final String datosDelMomento;

    private final int idPrimera;
    private final int idSegunda;
    private final int idGanadora;
    private final int vidaPrimera;
    private final int vidaSegunda;

    private final String atacante;
    private final String objetivo;
    private final String accion;
    private final int vidaAntes;
    private final int vidaDespues;
    private final int energiaRestante;
    private final int usosRestantes;

    public Acontecimiento(TipoAcontecimiento tipo, int ronda, int asalto, String datos) {
        this(tipo, ronda, asalto, datos, 0, 0, 0, 0, 0);
    }

    public Acontecimiento(TipoAcontecimiento tipo, int ronda, int asalto, String datos,
            int idPrimera, int idSegunda, int idGanadora,
            int vidaPrimera, int vidaSegunda) {

        if (tipo == null || datos == null || ronda < 1 || asalto < 0) {
            throw new IllegalArgumentException("Acontecimiento invalido.");
        }

        this.tipo = tipo;
        this.ronda = ronda;
        this.asalto = asalto;
        this.datosDelMomento = datos;
        this.idPrimera = idPrimera;
        this.idSegunda = idSegunda;
        this.idGanadora = idGanadora;
        this.vidaPrimera = vidaPrimera;
        this.vidaSegunda = vidaSegunda;

        this.atacante = null;
        this.objetivo = null;
        this.accion = null;
        this.vidaAntes = 0;
        this.vidaDespues = 0;
        this.energiaRestante = 0;
        this.usosRestantes = -1;
    }

    // Constructor específico para ACCION_REALIZADA
    public Acontecimiento(int ronda, int asalto, String atacante, String objetivo,
            String accion, int vidaAntes, int vidaDespues,
            int energiaRestante, int usosRestantes) {

        if (ronda < 1 || asalto < 0 || atacante == null || objetivo == null || accion == null) {
            throw new IllegalArgumentException("Acontecimiento invalido.");
        }

        this.tipo = TipoAcontecimiento.ACCION_REALIZADA;
        this.ronda = ronda;
        this.asalto = asalto;
        this.datosDelMomento = null;

        this.idPrimera = 0;
        this.idSegunda = 0;
        this.idGanadora = 0;
        this.vidaPrimera = 0;
        this.vidaSegunda = 0;

        this.atacante = atacante;
        this.objetivo = objetivo;
        this.accion = accion;
        this.vidaAntes = vidaAntes;
        this.vidaDespues = vidaDespues;
        this.energiaRestante = energiaRestante;
        this.usosRestantes = usosRestantes;
    }

    public TipoAcontecimiento getTipo() {
        return tipo;
    }

    public int getRonda() {
        return ronda;
    }

    public int getAsalto() {
        return asalto;
    }

    public String obtenerDatos() {
        return datosDelMomento;
    }

    public int getIdPrimera() {
        return idPrimera;
    }

    public int getIdSegunda() {
        return idSegunda;
    }

    public int getIdGanadora() {
        return idGanadora;
    }

    public int getVidaPrimera() {
        return vidaPrimera;
    }

    public int getVidaSegunda() {
        return vidaSegunda;
    }

    public int vidaDe(int id) {
        if (id == idPrimera) {
            return vidaPrimera;
        }
        if (id == idSegunda) {
            return vidaSegunda;
        }
        throw new IllegalArgumentException("Pareja ajena al resultado.");
    }

    public String getAtacante() {
        return atacante;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public String getAccion() {
        return accion;
    }

    public int getVidaAntes() {
        return vidaAntes;
    }

    public int getVidaDespues() {
        return vidaDespues;
    }

    public int getEnergiaRestante() {
        return energiaRestante;
    }

    public int getUsosRestantes() {
        return usosRestantes;
    }
}