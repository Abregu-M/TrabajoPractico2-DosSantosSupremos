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

    public Acontecimiento(TipoAcontecimiento tipo, int ronda, int asalto, String datos) {
        this(tipo, ronda, asalto, datos, 0, 0, 0, 0, 0);
    }
    public Acontecimiento(TipoAcontecimiento tipo, int ronda, int asalto, String datos,
            int idPrimera, int idSegunda, int idGanadora, int vidaPrimera, int vidaSegunda) {
        if (tipo == null || datos == null || ronda < 1 || asalto < 0) {
            throw new IllegalArgumentException("Acontecimiento invalido.");
        }
        this.tipo = tipo; this.ronda = ronda; this.asalto = asalto;
        datosDelMomento = datos;
        this.idPrimera = idPrimera; this.idSegunda = idSegunda;
        this.idGanadora = idGanadora;
        this.vidaPrimera = vidaPrimera; this.vidaSegunda = vidaSegunda;
    }
    public TipoAcontecimiento getTipo() { return tipo; }
    public int getRonda() { return ronda; }
    public int getAsalto() { return asalto; }
    public String obtenerDatos() { return datosDelMomento; }
    public int getIdPrimera() { return idPrimera; }
    public int getIdSegunda() { return idSegunda; }
    public int getIdGanadora() { return idGanadora; }
    public int getVidaPrimera() { return vidaPrimera; }
    public int getVidaSegunda() { return vidaSegunda; }
    public int vidaDe(int id) {
        if (id == idPrimera) { return vidaPrimera; }
        if (id == idSegunda) { return vidaSegunda; }
        throw new IllegalArgumentException("Pareja ajena al resultado.");
    }
}
