package co.edu.usc.voltacali;

import java.util.Vector;

public class CargadorVE {

    // ======================================================
    // Parte D a): Enums anidados
    // ======================================================
    public enum TipoConector {
        TIPO_1,
        TIPO_2,
        CCS2,
        CHADEMO,
        GBT
    }

    public enum TipoCargador {
        MURAL,
        PEDESTAL,
        RAPIDO_DC,
        ULTRARRAPIDO,
        PORTATIL,
        BIDIRECCIONAL_V2G
    }

    public enum Ubicacion {
        CENTRO_COMERCIAL,
        UNIVERSIDAD,
        ESTACION_SERVICIO,
        PARQUEADERO_PUBLICO,
        RESIDENCIAL,
        HOTEL,
        TERMINAL,
        FLOTA_CORPORATIVA
    }

    // ======================================================
    // Parte D e): Miembros estáticos
    // ======================================================
    public static int totalCargadores = 0;
    public static int contadorRegistros = 0;
    public static final double LIMITE_RED = 50.0;
    public static final double INCREMENTO_DEFECTO = 5.0;

    // ======================================================
    // Parte A: Atributos privados
    // ======================================================
    private String fabricante;
    private int anioInstalacion;
    private int voltajeNominal;
    private TipoConector tipoConector;
    private TipoCargador tipoCargador;
    private int numeroConectores;
    private int puestosParqueo;
    private double potenciaMaxima;
    private Ubicacion ubicacion;
    private double potenciaActual;

    // Parte D c): Bitácora
    private Vector<RegistroSesion> bitacora;

    // ======================================================
    // Parte D b): Clase interna no estática RegistroSesion
    // ======================================================
    public class RegistroSesion {
        private int idRegistro;
        private String fabricante;
        private int anioInstalacion;
        private double potenciaActual;
        private String evento;
        private boolean valido;

        public RegistroSesion(String evento, boolean valido) {
            contadorRegistros++;
            this.idRegistro = contadorRegistros;
            this.fabricante = CargadorVE.this.fabricante;
            this.anioInstalacion = CargadorVE.this.anioInstalacion;
            this.potenciaActual = CargadorVE.this.potenciaActual;
            this.evento = evento;
            this.valido = valido;
        }

        public String describir() {
            return String.format("#%d | %s | Año: %d | Pot: %.2f kW | Evento: %s | Valido: %b",
                    idRegistro, fabricante, anioInstalacion, potenciaActual, evento, valido);
        }

        public boolean isValido() {
            return valido;
        }

        public double getPotenciaActual() {
            return potenciaActual;
        }
    }

    // Constructor temporal
    public CargadorVE(String fabricante, int anioInstalacion, double potenciaMaxima) {
        this.fabricante = fabricante;
        this.anioInstalacion = anioInstalacion;
        this.potenciaMaxima = potenciaMaxima;
        this.potenciaActual = 0.0;
        this.bitacora = new Vector<>();
        totalCargadores++;
    }

    // ======================================================
    // Parte A: Métodos Getters y Setters
    // ======================================================
    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public int getAnioInstalacion() {
        return anioInstalacion;
    }

    public void setAnioInstalacion(int anioInstalacion) {
        this.anioInstalacion = anioInstalacion;
    }

    public int getVoltajeNominal() {
        return voltajeNominal;
    }

    public void setVoltajeNominal(int voltajeNominal) {
        this.voltajeNominal = voltajeNominal;
    }

    public TipoConector getTipoConector() {
        return tipoConector;
    }

    public void setTipoConector(TipoConector tipoConector) {
        this.tipoConector = tipoConector;
    }

    public TipoCargador getTipoCargador() {
        return tipoCargador;
    }

    public void setTipoCargador(TipoCargador tipoCargador) {
        this.tipoCargador = tipoCargador;
    }

    public int getNumeroConectores() {
        return numeroConectores;
    }

    public void setNumeroConectores(int numeroConectores) {
        this.numeroConectores = numeroConectores;
    }

    public int getPuestosParqueo() {
        return puestosParqueo;
    }

    public void setPuestosParqueo(int puestosParqueo) {
        this.puestosParqueo = puestosParqueo;
    }

    public double getPotenciaMaxima() {
        return potenciaMaxima;
    }

    public void setPotenciaMaxima(double potenciaMaxima) {
        this.potenciaMaxima = potenciaMaxima;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public double getPotenciaActual() {
        return potenciaActual;
    }

    public void setPotenciaActual(double potenciaActual) {
        if (potenciaActual < 0 || potenciaActual > this.potenciaMaxima) {
            System.out.println("Rechazado: Potencia fuera de limites (" + potenciaActual + " kW).");
            bitacora.add(new RegistroSesion("Intento setPotenciaActual (" + potenciaActual + " kW)", false));
        } else {
            this.potenciaActual = potenciaActual;
            bitacora.add(new RegistroSesion("setPotenciaActual a " + potenciaActual + " kW", true));
        }
    }

    public Vector<RegistroSesion> getBitacora() {
        return bitacora;
    }
}