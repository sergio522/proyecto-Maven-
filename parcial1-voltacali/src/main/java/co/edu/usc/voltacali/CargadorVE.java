package co.edu.usc.voltacali;

/**
 * Clase que representa un Cargador de Vehiculo Electrico (VE).
 */
public class CargadorVE {

    /** Incremento por defecto de potencia en kW. */
    public static final double INCREMENTO_DEFECTO = 5.0;
    /** Limite maximo de potencia permitido por la red electrica en kW. */
    public static final double LIMITE_RED = 100.0;
    /** Tamano inicial por defecto de la bitacora de cambios. */
    public static final int TAMANO_BITACORA_INICIAL = 10;
    /** Voltaje por defecto en voltios. */
    public static final int VOLTAJE_DEFECTO = 220;
    /** Potencia por defecto para c1. */
    public static final double POTENCIA_DEFECTO_C1 = 60.0;

    /** Enum para los tipos de conector. */
    public enum TipoConector {
        /** Conector CCS1. */
        CCS1,
        /** Conector CCS2. */
        CCS2,
        /** Conector Tipo 2. */
        TIPO_2,
        /** Conector CHAdeMO. */
        CHADEMO,
        /** Conector GB/T. */
        GBT
    }

    /** Enum para los tipos de cargador. */
    public enum TipoCargador {
        /** Cargador Lento AC. */
        LENTO_AC,
        /** Cargador Semirrapido AC. */
        SEMIRRAPIDO_AC,
        /** Cargador Rapido AC. */
        RAPIDO_AC,
        /** Cargador Rapido DC. */
        RAPIDO_DC,
        /** Cargador Ultrarrapido. */
        ULTRARRAPIDO,
        /** Cargador Mural. */
        MURAL
    }

    /** Enum para la ubicacion del cargador. */
    public enum Ubicacion {
        /** Ubicacion Universidad. */
        UNIVERSIDAD,
        /** Ubicacion Centro Comercial. */
        CENTRO_COMERCIAL,
        /** Ubicacion Estacion de Servicio. */
        ESTACION_SERVICIO,
        /** Ubicacion Parqueadero Publico. */
        PARQUEADERO_PUBLICO,
        /** Ubicacion Residencial. */
        RESIDENCIAL,
        /** Ubicacion Hospital. */
        HOSPITAL,
        /** Ubicacion Hotel. */
        HOTEL,
        /** Ubicacion Via Publica. */
        VIA_PUBLICA
    }

    /** Clase interna para representar una entrada en la bitacora. */
    public static class RegistroBitacora {
        private String fabricante;
        private int anioInstalacion;
        private double potenciaRegistrada;
        private String evento;
        private boolean pasoExitoso;

        /**
         * Constructor para RegistroBitacora.
         *
         * @param fab Fabricante del cargador.
         * @param anio Anio de instalacion.
         * @param pot Potencia en el momento del evento.
         * @param ev Descripcion del evento.
         * @param exitoso Indica si la operacion fue exitosa.
         */
        public RegistroBitacora(String fab, int anio, double pot, String ev, boolean exitoso) {
            this.fabricante = fab;
            this.anioInstalacion = anio;
            this.potenciaRegistrada = pot;
            this.evento = ev;
            this.pasoExitoso = exitoso;
        }

        public String getFabricante() {
            return fabricante;
        }

        public int getAnioInstalacion() {
            return anioInstalacion;
        }

        public double getPotenciaRegistrada() {
            return potenciaRegistrada;
        }

        public String getEvento() {
            return evento;
        }

        public boolean isPasoExitoso() {
            return pasoExitoso;
        }

        @Override
        public String toString() {
            return String.format("%s | Anio: %d | Pot: %.2f kW | Evento: %s | Valido: %b",
                    fabricante, anioInstalacion, potenciaRegistrada, evento, pasoExitoso);
        }
    }

    private static int totalCargadores;
    private static int contadorRegistros;

    private String fabricante;
    private int anioInstalacion;
    private int voltajeNominal;
    private TipoConector tipoConector;
    private TipoCargador tipoCargador;
    private int numeroConectores;
    private int puestosParqueo;
    private double potenciaMaxima;
    private double potenciaActual;
    private Ubicacion ubicacion;

    private RegistroBitacora[] bitacora;
    private int cantidadRegistros;

    /**
     * Constructor completo.
     *
     * @param fabricante Fabricante.
     * @param anioInstalacion Anio de instalacion.
     * @param voltajeNominal Voltaje nominal.
     * @param tipoConector Tipo de conector.
     * @param tipoCargador Tipo de cargador.
     * @param numeroConectores Numero de conectores.
     * @param puestosParqueo Puestos de parqueo.
     * @param potenciaMaxima Potencia maxima.
     * @param ubicacion Ubicacion.
     */
    @SuppressWarnings("checkstyle:ParameterNumber")
    public CargadorVE(String fabricante, int anioInstalacion, int voltajeNominal,
                      TipoConector tipoConector, TipoCargador tipoCargador,
                      int numeroConectores, int puestosParqueo,
                      double potenciaMaxima, Ubicacion ubicacion) {
        this.fabricante = fabricante;
        this.anioInstalacion = anioInstalacion;
        this.voltajeNominal = voltajeNominal;
        this.tipoConector = tipoConector;
        this.tipoCargador = tipoCargador;
        this.numeroConectores = numeroConectores;
        this.puestosParqueo = puestosParqueo;
        this.potenciaMaxima = potenciaMaxima;
        this.ubicacion = ubicacion;
        this.potenciaActual = 0.0;
        this.bitacora = new RegistroBitacora[TAMANO_BITACORA_INICIAL];
        this.cantidadRegistros = 0;
        totalCargadores++;
    }

    /**
     * Constructor reducido.
     *
     * @param fabricante Fabricante.
     * @param anioInstalacion Anio de instalacion.
     * @param potenciaMaxima Potencia maxima.
     */
    public CargadorVE(String fabricante, int anioInstalacion, double potenciaMaxima) {
        this(fabricante, anioInstalacion, VOLTAJE_DEFECTO, TipoConector.TIPO_2, TipoCargador.LENTO_AC,
                1, 1, potenciaMaxima, Ubicacion.PARQUEADERO_PUBLICO);
    }

    /**
     * Constructor copia.
     *
     * @param otro Cargador a copiar.
     */
    public CargadorVE(CargadorVE otro) {
        if (otro != null) {
            this.fabricante = otro.fabricante;
            this.anioInstalacion = otro.anioInstalacion;
            this.voltajeNominal = otro.voltajeNominal;
            this.tipoConector = otro.tipoConector;
            this.tipoCargador = otro.tipoCargador;
            this.numeroConectores = otro.numeroConectores;
            this.puestosParqueo = otro.puestosParqueo;
            this.potenciaMaxima = otro.potenciaMaxima;
            this.potenciaActual = otro.potenciaActual;
            this.ubicacion = otro.ubicacion;
            this.cantidadRegistros = otro.cantidadRegistros;
            this.bitacora = new RegistroBitacora[Math.max(TAMANO_BITACORA_INICIAL, otro.bitacora.length)];
            for (int i = 0; i < otro.cantidadRegistros; i++) {
                this.bitacora[i] = otro.bitacora[i];
            }
            totalCargadores++;
        }
    }

    private void agregarABitacora(String evento, boolean exitoso) {
        if (cantidadRegistros == bitacora.length) {
            RegistroBitacora[] nuevoArreglo = new RegistroBitacora[bitacora.length * 2];
            System.arraycopy(bitacora, 0, nuevoArreglo, 0, bitacora.length);
            bitacora = nuevoArreglo;
        }
        bitacora[cantidadRegistros] = new RegistroBitacora(fabricante, anioInstalacion,
                potenciaActual, evento, exitoso);
        cantidadRegistros++;
        contadorRegistros++;
    }

    public static int getTotalCargadores() {
        return totalCargadores;
    }

    public static int getContadorRegistros() {
        return contadorRegistros;
    }

    public String getFabricante() {
        return fabricante;
    }

    public double getPotenciaActual() {
        return potenciaActual;
    }

    public double getPotenciaMaxima() {
        return potenciaMaxima;
    }

    public TipoConector getTipoConector() {
        return tipoConector;
    }

    public TipoCargador getTipoCargador() {
        return tipoCargador;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public int getCantidadRegistros() {
        return cantidadRegistros;
    }

    public RegistroBitacora[] getBitacora() {
        return bitacora;
    }

    /**
     * Establece la potencia actual del cargador.
     *
     * @param potencia Nueva potencia.
     * @return true si se aplico con exito.
     */
    public boolean setPotenciaActual(double potencia) {
        if (potencia >= 0.0 && potencia <= potenciaMaxima) {
            this.potenciaActual = potencia;
            agregarABitacora("setPotenciaActual en " + potencia + " kW", true);
            return true;
        }
        agregarABitacora("setPotenciaActual rechazada (" + potencia + " kW)", false);
        return false;
    }

    /**
     * Aumenta la potencia con incremento explicito.
     *
     * @param incremento Valor a incrementar.
     * @return true si fue exitoso.
     */
    public boolean aumentarPotencia(double incremento) {
        double nueva = potenciaActual + incremento;
        if (nueva <= potenciaMaxima) {
            potenciaActual = nueva;
            agregarABitacora("aumentarPotencia en " + incremento + " kW", true);
            return true;
        }
        agregarABitacora("aumentarPotencia rechazada (" + incremento + " kW)", false);
        return false;
    }

    /**
     * Aumenta la potencia usando el incremento por defecto.
     *
     * @return true si fue exitoso.
     */
    public boolean aumentarPotencia() {
        return aumentarPotencia(INCREMENTO_DEFECTO);
    }

    /**
     * Intenta aumentar la potencia en 'pasos' repetidos.
     *
     * @param incremento Valor por paso.
     * @param pasos Cantidad de pasos.
     * @return true si al menos un paso tuvo exito.
     */
    public boolean aumentarPotencia(double incremento, int pasos) {
        boolean alMenosUno = false;
        for (int i = 1; i <= pasos; i++) {
            boolean res = aumentarPotencia(incremento);
            if (res) {
                alMenosUno = true;
            }
        }
        return alMenosUno;
    }

    /**
     * Reduce la potencia actual.
     *
     * @param decremento Valor a reducir.
     * @return true si fue exitoso.
     */
    public boolean reducirPotencia(double decremento) {
        double nueva = potenciaActual - decremento;
        if (nueva >= 0.0) {
            potenciaActual = nueva;
            agregarABitacora("reducirPotencia en " + decremento + " kW", true);
            return true;
        }
        agregarABitacora("reducirPotencia rechazada (" + decremento + " kW)", false);
        return false;
    }

    /**
     * Corta la carga dejando la potencia actual en 0.
     */
    public void cortarCarga() {
        potenciaActual = 0.0;
        agregarABitacora("cortarCarga ejecutado", true);
    }

    /**
     * Calcula el tiempo estimado de carga segun la potencia actual.
     *
     * @param energiaKwh Energia requerida.
     * @return Horas estimadas o -1 si potencia es 0.
     */
    public double tiempoEstimadoCarga(double energiaKwh) {
        if (potenciaActual <= 0.0) {
            return -1.0;
        }
        return energiaKwh / potenciaActual;
    }

    /**
     * Calcula el tiempo estimado segun una potencia programada.
     *
     * @param energiaKwh Energia requerida.
     * @param potenciaProgramada Potencia programada.
     * @return Horas estimadas.
     */
    public double tiempoEstimadoCarga(double energiaKwh, double potenciaProgramada) {
        if (potenciaProgramada <= 0.0) {
            return -1.0;
        }
        return energiaKwh / potenciaProgramada;
    }

    /**
     * Calcula el tiempo estimado con pausas.
     *
     * @param energiaKwh Energia requerida.
     * @param numeroPausas Cantidad de pausas.
     * @param minutosPorPausa Duracion por pausa en minutos.
     * @return Horas estimadas.
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    public double tiempoEstimadoCarga(double energiaKwh, int numeroPausas, double minutosPorPausa) {
        double base = tiempoEstimadoCarga(energiaKwh);
        if (base < 0.0) {
            return -1.0;
        }
        double horasPausa = (numeroPausas * minutosPorPausa) / 60.0;
        return base + horasPausa;
    }

    /**
     * Muestra informacion del cargador.
     *
     * @param incluirBitacora true para imprimir la bitacora completa.
     */
    public void mostrar(boolean incluirBitacora) {
        System.out.println("Fabricante: " + fabricante);
        System.out.println("Anio Instalacion: " + anioInstalacion);
        System.out.println("Voltaje Nominal: " + voltajeNominal + " V");
        System.out.println("Tipo Conector: " + tipoConector);
        System.out.println("Tipo Cargador: " + tipoCargador);
        System.out.println("Numero Conectores: " + numeroConectores);
        System.out.println("Puestos Parqueo: " + puestosParqueo);
        System.out.println("Potencia Maxima: " + potenciaMaxima + " kW");
        System.out.println("Ubicacion: " + ubicacion);
        System.out.println("Potencia Actual: " + potenciaActual + " kW");
        if (incluirBitacora) {
            System.out.println("--- BITACORA ---");
            for (int i = 0; i < cantidadRegistros; i++) {
                System.out.println("#" + (i + 1) + " | " + bitacora[i]);
            }
        }
    }

    /**
     * Filtra cargadores por tipo de conector.
     *
     * @param flota Arreglo de cargadores.
     * @param tc Tipo de conector a buscar.
     * @return Arreglo filtrado.
     */
    public static CargadorVE[] filtrar(CargadorVE[] flota, TipoConector tc) {
        if (flota == null) {
            return new CargadorVE[0];
        }
        int c = 0;
        for (CargadorVE carg : flota) {
            if (carg != null && carg.tipoConector == tc) {
                c++;
            }
        }
        CargadorVE[] res = new CargadorVE[c];
        int idx = 0;
        for (CargadorVE carg : flota) {
            if (carg != null && carg.tipoConector == tc) {
                res[idx++] = carg;
            }
        }
        return res;
    }

    /**
     * Filtra cargadores por tipo de cargador.
     *
     * @param flota Arreglo de cargadores.
     * @param tc Tipo de cargador a buscar.
     * @return Arreglo filtrado.
     */
    public static CargadorVE[] filtrar(CargadorVE[] flota, TipoCargador tc) {
        if (flota == null) {
            return new CargadorVE[0];
        }
        int c = 0;
        for (CargadorVE carg : flota) {
            if (carg != null && carg.tipoCargador == tc) {
                c++;
            }
        }
        CargadorVE[] res = new CargadorVE[c];
        int idx = 0;
        for (CargadorVE carg : flota) {
            if (carg != null && carg.tipoCargador == tc) {
                res[idx++] = carg;
            }
        }
        return res;
    }

    /**
     * Filtra cargadores por ubicacion.
     *
     * @param flota Arreglo de cargadores.
     * @param u Ubicacion a buscar.
     * @return Arreglo filtrado.
     */
    public static CargadorVE[] filtrar(CargadorVE[] flota, Ubicacion u) {
        if (flota == null) {
            return new CargadorVE[0];
        }
        int c = 0;
        for (CargadorVE carg : flota) {
            if (carg != null && carg.ubicacion == u) {
                c++;
            }
        }
        CargadorVE[] res = new CargadorVE[c];
        int idx = 0;
        for (CargadorVE carg : flota) {
            if (carg != null && carg.ubicacion == u) {
                res[idx++] = carg;
            }
        }
        return res;
    }

    /**
     * Cuenta la cantidad de cargadores existentes por cada tipo de cargador.
     *
     * @param flota Arreglo de cargadores.
     */
    public static void contarPorTipo(CargadorVE[] flota) {
        if (flota == null) {
            return;
        }
        for (TipoCargador tc : TipoCargador.values()) {
            int cnt = 0;
            for (CargadorVE carg : flota) {
                if (carg != null && carg.tipoCargador == tc) {
                    cnt++;
                }
            }
            if (cnt > 0) {
                System.out.println(tc + ": " + cnt);
            }
        }
    }

    /**
     * Calcula el promedio de potencia actual en la flota.
     *
     * @param flota Arreglo de cargadores.
     * @return Promedio en kW.
     */
    public static double promedioPotencia(CargadorVE[] flota) {
        if (flota == null || flota.length == 0) {
            return 0.0;
        }
        double suma = 0.0;
        int count = 0;
        for (CargadorVE carg : flota) {
            if (carg != null) {
                suma += carg.potenciaActual;
                count++;
            }
        }
        return count == 0 ? 0.0 : suma / count;
    }

    /**
     * Encuentra e imprime el cargador con mayor potencia actual.
     *
     * @param flota Arreglo de cargadores.
     */
    public static void mayorPotencia(CargadorVE[] flota) {
        if (flota == null) {
            return;
        }
        CargadorVE max = null;
        for (CargadorVE carg : flota) {
            if (carg != null) {
                if (max == null || carg.potenciaActual > max.potenciaActual) {
                    max = carg;
                }
            }
        }
        if (max != null) {
            System.out.println("Fabricante: " + max.fabricante + ", Potencia: " + max.potenciaActual + " kW");
        }
    }

    /**
     * Cuenta cuantos cargadores superan el limite de la red.
     *
     * @param flota Arreglo de cargadores.
     * @return Cantidad de excesos.
     */
    public static int excesosDePotenciaContratada(CargadorVE[] flota) {
        if (flota == null) {
            return 0;
        }
        int cnt = 0;
        for (CargadorVE carg : flota) {
            if (carg != null && carg.potenciaActual > LIMITE_RED) {
                cnt++;
            }
        }
        return cnt;
    }

    /**
     * Busca cargadores segun el numero de conectores (Ruta 0).
     *
     * @param flota Arreglo de cargadores.
     * @param conectores Cantidad de conectores requerida.
     * @return Arreglo de cargadores coincidentes.
     */
    public static CargadorVE[] cargadoresPorConectores(CargadorVE[] flota, int conectores) {
        if (flota == null) {
            return new CargadorVE[0];
        }
        int c = 0;
        for (CargadorVE carg : flota) {
            if (carg != null && carg.numeroConectores == conectores) {
                c++;
            }
        }
        if (c == 0) {
            System.out.println("No hay resultados para " + conectores + " conectores.");
            return new CargadorVE[0];
        }
        CargadorVE[] res = new CargadorVE[c];
        int idx = 0;
        for (CargadorVE carg : flota) {
            if (carg != null && carg.numeroConectores == conectores) {
                res[idx++] = carg;
            }
        }
        return res;
    }
}
