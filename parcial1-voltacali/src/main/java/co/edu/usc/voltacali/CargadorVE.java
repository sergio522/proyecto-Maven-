package co.edu.usc.voltacali;

import java.util.Vector;

/**
 * Clase que representa un cargador de vehiculo electrico.
 */
public class CargadorVE {

    /**
     * Enum para los tipos de conector.
     */
    public enum TipoConector {
        /** Conector Tipo 1. */
        TIPO_1,
        /** Conector Tipo 2. */
        TIPO_2,
        /** Conector CCS2. */
        CCS2,
        /** Conector CHADEMO. */
        CHADEMO,
        /** Conector GBT. */
        GBT
    }

    /**
     * Enum para los tipos de cargador.
     */
    public enum TipoCargador {
        /** Cargador de pared. */
        MURAL,
        /** Cargador pedestal. */
        PEDESTAL,
        /** Cargador rapido DC. */
        RAPIDO_DC,
        /** Cargador ultrarrapido. */
        ULTRARRAPIDO,
        /** Cargador portatil. */
        PORTATIL,
        /** Cargador bidireccional V2G. */
        BIDIRECCIONAL_V2G
    }

    /**
     * Enum para las ubicaciones.
     */
    public enum Ubicacion {
        /** Ubicacion centro comercial. */
        CENTRO_COMERCIAL,
        /** Ubicacion universidad. */
        UNIVERSIDAD,
        /** Ubicacion estacion de servicio. */
        ESTACION_SERVICIO,
        /** Ubicacion parqueadero publico. */
        PARQUEADERO_PUBLICO,
        /** Ubicacion residencial. */
        RESIDENCIAL,
        /** Ubicacion hotel. */
        HOTEL,
        /** Ubicacion terminal. */
        TERMINAL,
        /** Ubicacion flota corporativa. */
        FLOTA_CORPORATIVA
    }

    /** Limite de potencia de la red en kW. */
    public static final double LIMITE_RED = 50.0;
    /** Incremento por defecto en kW. */
    public static final double INCREMENTO_DEFECTO = 5.0;

    /** Total de cargadores creados. */
    private static int totalCargadores;
    /** Contador global de registros de sesion. */
    private static int contadorRegistros;

    /** Marca del equipo. */
    private String fabricante;
    /** Año de instalacion. */
    private int anioInstalacion;
    /** Voltaje nominal de operacion. */
    private int voltajeNominal;
    /** Conector principal. */
    private TipoConector tipoConector;
    /** Categoria del equipo. */
    private TipoCargador tipoCargador;
    /** Conectores disponibles. */
    private int numeroConectores;
    /** Puestos de parqueo. */
    private int puestosParqueo;
    /** Potencia maxima del equipo. */
    private double potenciaMaxima;
    /** Ubicacion del cargador. */
    private Ubicacion ubicacion;
    /** Potencia actual entregada. */
    private double potenciaActual;

    /** Bitacora de sesiones del cargador. */
    private Vector<RegistroSesion> bitacora;

    /**
     * Clase interna no estatica para registrar las sesiones de carga.
     */
    public class RegistroSesion {
        /** Identificador del registro. */
        private int idRegistro;
        /** Marca del fabricante capturada. */
        private String fabricante;
        /** Año de instalacion capturado. */
        private int anioInstalacion;
        /** Potencia actual capturada. */
        private double potenciaActual;
        /** Descripcion del evento. */
        private String evento;
        /** Estado del evento (valido o no). */
        private boolean valido;

        /**
         * Constructor para RegistroSesion.
         *
         * @param evento Descripcion del evento.
         * @param valido Si fue valido o no.
         */
        public RegistroSesion(String evento, boolean valido) {
            contadorRegistros++;
            this.idRegistro = contadorRegistros;
            this.fabricante = CargadorVE.this.fabricante;
            this.anioInstalacion = CargadorVE.this.anioInstalacion;
            this.potenciaActual = CargadorVE.this.potenciaActual;
            this.evento = evento;
            this.valido = valido;
        }

        /**
         * Describe el registro.
         *
         * @return Descripcion formateada del registro.
         */
        public String describir() {
            return String.format("#%d | %s | Año: %d | Pot: %.2f kW | Evento: %s | Valido: %b",
                    idRegistro, fabricante, anioInstalacion, potenciaActual, evento, valido);
        }

        /**
         * Obtiene si el registro es valido.
         *
         * @return true si es valido, false en caso contrario.
         */
        public boolean isValido() {
            return valido;
        }

        /**
         * Obtiene la potencia actual registrada.
         *
         * @return Potencia actual en kW.
         */
        public double getPotenciaActual() {
            return potenciaActual;
        }
    }

    /**
     * Constructor reducido temporal.
     *
     * @param fabricante Marca.
     * @param anioInstalacion Año.
     * @param potenciaMaxima Potencia max.
     */
    public CargadorVE(String fabricante, int anioInstalacion, double potenciaMaxima) {
        this.fabricante = fabricante;
        this.anioInstalacion = anioInstalacion;
        this.potenciaMaxima = potenciaMaxima;
        this.potenciaActual = 0.0;
        this.bitacora = new Vector<>();
        totalCargadores++;
    }

    /**
     * Obtiene el total de cargadores.
     *
     * @return Total de cargadores.
     */
    public static int getTotalCargadores() {
        return totalCargadores;
    }

    /**
     * Establece el total de cargadores.
     *
     * @param totalCargadores Total de cargadores.
     */
    public static void setTotalCargadores(int totalCargadores) {
        CargadorVE.totalCargadores = totalCargadores;
    }

    /**
     * Obtiene el contador de registros.
     *
     * @return Contador de registros.
     */
    public static int getContadorRegistros() {
        return contadorRegistros;
    }

    /**
     * Establece el contador de registros.
     *
     * @param contadorRegistros Contador de registros.
     */
    public static void setContadorRegistros(int contadorRegistros) {
        CargadorVE.contadorRegistros = contadorRegistros;
    }

    /**
     * Obtiene el fabricante.
     *
     * @return Fabricante del equipo.
     */
    public String getFabricante() {
        return fabricante;
    }

    /**
     * Establece el fabricante.
     *
     * @param fabricante Marca a asignar.
     */
    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    /**
     * Obtiene el año de instalacion.
     *
     * @return Año de instalacion.
     */
    public int getAnioInstalacion() {
        return anioInstalacion;
    }

    /**
     * Establece el año de instalacion.
     *
     * @param anioInstalacion Año a asignar.
     */
    public void setAnioInstalacion(int anioInstalacion) {
        this.anioInstalacion = anioInstalacion;
    }

    /**
     * Obtiene el voltaje nominal.
     *
     * @return Voltaje nominal.
     */
    public int getVoltajeNominal() {
        return voltajeNominal;
    }

    /**
     * Establece el voltaje nominal.
     *
     * @param voltajeNominal Voltaje a asignar.
     */
    public void setVoltajeNominal(int voltajeNominal) {
        this.voltajeNominal = voltajeNominal;
    }

    /**
     * Obtiene el tipo de conector.
     *
     * @return TipoConector actual.
     */
    public TipoConector getTipoConector() {
        return tipoConector;
    }

    /**
     * Establece el tipo de conector.
     *
     * @param tipoConector Conector a asignar.
     */
    public void setTipoConector(TipoConector tipoConector) {
        this.tipoConector = tipoConector;
    }

    /**
     * Obtiene el tipo de cargador.
     *
     * @return TipoCargador actual.
     */
    public TipoCargador getTipoCargador() {
        return tipoCargador;
    }

    /**
     * Establece el tipo de cargador.
     *
     * @param tipoCargador Tipo de cargador a asignar.
     */
    public void setTipoCargador(TipoCargador tipoCargador) {
        this.tipoCargador = tipoCargador;
    }

    /**
     * Obtiene el numero de conectores.
     *
     * @return Cantidad de conectores.
     */
    public int getNumeroConectores() {
        return numeroConectores;
    }

    /**
     * Establece el numero de conectores.
     *
     * @param numeroConectores Cantidad de conectores a asignar.
     */
    public void setNumeroConectores(int numeroConectores) {
        this.numeroConectores = numeroConectores;
    }

    /**
     * Obtiene los puestos de parqueo.
     *
     * @return Cantidad de puestos.
     */
    public int getPuestosParqueo() {
        return puestosParqueo;
    }

    /**
     * Establece los puestos de parqueo.
     *
     * @param puestosParqueo Puestos a asignar.
     */
    public void setPuestosParqueo(int puestosParqueo) {
        this.puestosParqueo = puestosParqueo;
    }

    /**
     * Obtiene la potencia maxima.
     *
     * @return Potencia maxima en kW.
     */
    public double getPotenciaMaxima() {
        return potenciaMaxima;
    }

    /**
     * Establece la potencia maxima.
     *
     * @param potenciaMaxima Potencia maxima a asignar.
     */
    public void setPotenciaMaxima(double potenciaMaxima) {
        this.potenciaMaxima = potenciaMaxima;
    }

    /**
     * Obtiene la ubicacion.
     *
     * @return Ubicacion actual.
     */
    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    /**
     * Establece la ubicacion.
     *
     * @param ubicacion Ubicacion a asignar.
     */
    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    /**
     * Obtiene la potencia actual.
     *
     * @return Potencia actual en kW.
     */
    public double getPotenciaActual() {
        return potenciaActual;
    }

    /**
     * Establece la potencia actual validando limites.
     *
     * @param potenciaActual Nueva potencia.
     */
    public void setPotenciaActual(double potenciaActual) {
        if (potenciaActual < 0 || potenciaActual > this.potenciaMaxima) {
            System.out.println("Rechazado: Potencia fuera de limites (" + potenciaActual + " kW).");
            bitacora.add(new RegistroSesion("Intento setPotenciaActual (" + potenciaActual + " kW)", false));
        } else {
            this.potenciaActual = potenciaActual;
            bitacora.add(new RegistroSesion("setPotenciaActual a " + potenciaActual + " kW", true));
        }
    }

    /**
     * Obtiene la bitacora de sesiones.
     *
     * @return Vector con los registros.
     */
    public Vector<RegistroSesion> getBitacora() {
        return bitacora;
    }
}
