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
    /** Voltaje por defecto en voltios. */
    public static final int VOLTAJE_DEFECTO = 220;
    /** Minutos por hora para conversiones de tiempo. */
    public static final double MINUTOS_POR_HORA = 60.0;

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

    // ======================================================
    // Familia 1: Constructores
    // ======================================================

    /**
     * Constructor completo con 9 parametros.
     *
     * @param fabricante Marca.
     * @param anioInstalacion Año.
     * @param voltajeNominal Voltaje.
     * @param tipoConector Conector.
     * @param tipoCargador Categoria.
     * @param numeroConectores Cantidad conectores.
     * @param puestosParqueo Puestos.
     * @param potenciaMaxima Potencia max.
     * @param ubicacion Sitio.
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
        this.bitacora = new Vector<>();
        totalCargadores++;
    }

    /**
     * Constructor reducido. Usa este(...) con valores por defecto especificados.
     *
     * @param fabricante Marca.
     * @param anioInstalacion Año.
     * @param potenciaMaxima Potencia max.
     */
    public CargadorVE(String fabricante, int anioInstalacion, double potenciaMaxima) {
        this(fabricante, anioInstalacion, VOLTAJE_DEFECTO, TipoConector.TIPO_2,
                TipoCargador.PEDESTAL, 1, 1, potenciaMaxima,
                Ubicacion.PARQUEADERO_PUBLICO);
    }

    /**
     * Constructor copia. Duplica las caracteristicas tecnicas, fija potencia en 0 y crea bitacora vacia.
     *
     * @param otro Objeto a copiar.
     */
    public CargadorVE(CargadorVE otro) {
        this(otro.fabricante, otro.anioInstalacion, otro.voltajeNominal,
                otro.tipoConector, otro.tipoCargador, otro.numeroConectores,
                otro.puestosParqueo, otro.potenciaMaxima, otro.ubicacion);
    }

    // ======================================================
    // Familia 2: aumentarPotencia
    // ======================================================

    /**
     * Aumenta la potencia usando INCREMENTO_DEFECTO.
     */
    public void aumentarPotencia() {
        aumentarPotencia(INCREMENTO_DEFECTO);
    }

    /**
     * Aumenta la potencia actual en el valor especificado.
     *
     * @param incremento Valor a sumar.
     */
    public void aumentarPotencia(double incremento) {
        double nuevaPotencia = this.potenciaActual + incremento;
        if (nuevaPotencia > this.potenciaMaxima) {
            System.out.println("Rechazado: La potencia excede la potencia maxima permitida.");
            bitacora.add(new RegistroSesion("Intento aumentarPotencia en " + incremento + " kW", false));
        } else {
            this.potenciaActual = nuevaPotencia;
            bitacora.add(new RegistroSesion("aumentarPotencia en " + incremento + " kW", true));
        }
    }

    /**
     * Aplica el incremento paso a paso n veces. Se detiene si un paso no es valido.
     *
     * @param incremento Valor a sumar en cada paso.
     * @param veces Cantidad de iteraciones.
     */
    public void aumentarPotencia(double incremento, int veces) {
        for (int i = 0; i < veces; i++) {
            double nuevaPotencia = this.potenciaActual + incremento;
            if (nuevaPotencia > this.potenciaMaxima) {
                System.out.println("Rechazado en el paso " + (i + 1) + ": excede el limite.");
                bitacora.add(new RegistroSesion("Intento aumentarPotencia paso " + (i + 1), false));
                break;
            }
            this.potenciaActual = nuevaPotencia;
            bitacora.add(new RegistroSesion("aumentarPotencia paso " + (i + 1) + " a "
                    + potenciaActual + " kW", true));
        }
    }

    // ======================================================
    // Familia 3: tiempoEstimadoCarga
    // ======================================================

    /**
     * Calcula tiempo estimado usando la potencia actual.
     *
     * @param energiaKWh Energia requerida en kWh.
     * @return Horas estimadas o -1 si potencia actual es 0.
     */
    public double tiempoEstimadoCarga(double energiaKWh) {
        if (this.potenciaActual == 0) {
            System.out.println("Aviso: La potencia actual es 0 kW, no se puede calcular el tiempo.");
            return -1.0;
        }
        return energiaKWh / this.potenciaActual;
    }

    /**
     * Calcula tiempo estimado usando una potencia programada.
     *
     * @param energiaKWh Energia requerida en kWh.
     * @param potenciaProgramada Potencia a evaluar.
     * @return Horas estimadas o -1 si potencia programada es <= 0.
     */
    public double tiempoEstimadoCarga(double energiaKWh, double potenciaProgramada) {
        if (potenciaProgramada <= 0) {
            System.out.println("Aviso: La potencia programada debe ser mayor a 0 kW.");
            return -1.0;
        }
        return energiaKWh / potenciaProgramada;
    }

    /**
     * Calcula tiempo estimado sumando el tiempo de las pausas.
     *
     * @param energiaKWh Energia requerida en kWh.
     * @param pausas Cantidad de pausas.
     * @param minutosPorPausa Duracion de cada pausa en minutos.
     * @return Horas estimadas totales.
     */
    public double tiempoEstimadoCarga(double energiaKWh, int pausas, double minutosPorPausa) {
        double tiempoBase = tiempoEstimadoCarga(energiaKWh);
        if (tiempoBase == -1.0) {
            return -1.0;
        }
        double horasPausas = (pausas * minutosPorPausa) / MINUTOS_POR_HORA;
        return tiempoBase + horasPausas;
    }

    // ======================================================
    // Familia 4: Métodos estáticos filtrar
    // ======================================================

    /**
     * Filtra cargadores por TipoConector.
     *
     * @param arreglo Arreglo de cargadores.
     * @param conector Criterio de conector.
     * @return Arreglo nuevo con el tamaño exacto.
     */
    public static CargadorVE[] filtrar(CargadorVE[] arreglo, TipoConector conector) {
        int cont = 0;
        for (CargadorVE c : arreglo) {
            if (c != null && c.getTipoConector() == conector) {
                cont++;
            }
        }
        CargadorVE[] resultado = new CargadorVE[cont];
        int idx = 0;
        for (CargadorVE c : arreglo) {
            if (c != null && c.getTipoConector() == conector) {
                resultado[idx++] = c;
            }
        }
        return resultado;
    }

    /**
     * Filtra cargadores por TipoCargador.
     *
     * @param arreglo Arreglo de cargadores.
     * @param tipo Criterio de tipo de cargador.
     * @return Arreglo nuevo con el tamaño exacto.
     */
    public static CargadorVE[] filtrar(CargadorVE[] arreglo, TipoCargador tipo) {
        int cont = 0;
        for (CargadorVE c : arreglo) {
            if (c != null && c.getTipoCargador() == tipo) {
                cont++;
            }
        }
        CargadorVE[] resultado = new CargadorVE[cont];
        int idx = 0;
        for (CargadorVE c : arreglo) {
            if (c != null && c.getTipoCargador() == tipo) {
                resultado[idx++] = c;
            }
        }
        return resultado;
    }

    /**
     * Filtra cargadores por Ubicacion.
     *
     * @param arreglo Arreglo de cargadores.
     * @param ubicacion Criterio de ubicacion.
     * @return Arreglo nuevo con el tamaño exacto.
     */
    public static CargadorVE[] filtrar(CargadorVE[] arreglo, Ubicacion ubicacion) {
        int cont = 0;
        for (CargadorVE c : arreglo) {
            if (c != null && c.getUbicacion() == ubicacion) {
                cont++;
            }
        }
        CargadorVE[] resultado = new CargadorVE[cont];
        int idx = 0;
        for (CargadorVE c : arreglo) {
            if (c != null && c.getUbicacion() == ubicacion) {
                resultado[idx++] = c;
            }
        }
        return resultado;
    }

    // ======================================================
    // Familia 5: mostrar y métodos de comportamiento
    // ======================================================

    /**
     * Muestra la informacion basica del cargador.
     */
    public void mostrar() {
        mostrar(false);
    }

    /**
     * Muestra la informacion del cargador y opcionalmente la bitacora completa.
     *
     * @param detallado Si es true, imprime la bitacora.
     */
    public void mostrar(boolean detallado) {
        System.out.println("Fabricante: " + fabricante);
        System.out.println("Año Instalacion: " + anioInstalacion);
        System.out.println("Voltaje Nominal: " + voltajeNominal + " V");
        System.out.println("Tipo Conector: " + tipoConector);
        System.out.println("Tipo Cargador: " + tipoCargador);
        System.out.println("Numero Conectores: " + numeroConectores);
        System.out.println("Puestos Parqueo: " + puestosParqueo);
        System.out.println("Potencia Maxima: " + potenciaMaxima + " kW");
        System.out.println("Ubicacion: " + ubicacion);
        System.out.println("Potencia Actual: " + potenciaActual + " kW");

        if (detallado) {
            System.out.println("--- BITACORA ---");
            for (RegistroSesion r : bitacora) {
                System.out.println(r.describir());
            }
        }
    }

    /**
     * Reduce la potencia actual en el valor especificado.
     *
     * @param decremento Valor a restar.
     */
    public void reducirPotencia(double decremento) {
        double nuevaPotencia = this.potenciaActual - decremento;
        if (nuevaPotencia < 0) {
            System.out.println("Rechazado: La potencia no puede ser negativa.");
            bitacora.add(new RegistroSesion("Intento reducirPotencia en " + decremento + " kW", false));
        } else {
            this.potenciaActual = nuevaPotencia;
            bitacora.add(new RegistroSesion("reducirPotencia en " + decremento + " kW", true));
        }
    }

    /**
     * Deja la potencia actual en 0.
     */
    public void cortarCarga() {
        this.potenciaActual = 0.0;
        bitacora.add(new RegistroSesion("cortarCarga ejecutado", true));
    }

    // ======================================================
    // Getters y Setters
    // ======================================================

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
