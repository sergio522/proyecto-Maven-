package co.edu.usc.voltacali;

/**
 * Clase principal de ejecucion del caso de prueba obligatorio.
 */
@SuppressWarnings("checkstyle:MagicNumber")
public class App {

    /**
     * Constructor privado.
     */
    protected App() {
    }

    /**
     * Metodo principal main.
     *
     * @param args Argumentos de ejecucion.
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    public static void main(String[] args) {

        // Paso 1: Crear la flota
        CargadorVE c1 = new CargadorVE("ABB", 2023, 400,
                CargadorVE.TipoConector.CCS2, CargadorVE.TipoCargador.RAPIDO_DC,
                2, 2, CargadorVE.POTENCIA_DEFECTO_C1, CargadorVE.Ubicacion.UNIVERSIDAD);

        CargadorVE c2 = new CargadorVE("Siemens", 2022, CargadorVE.VOLTAJE_DEFECTO,
                CargadorVE.TipoConector.TIPO_2, CargadorVE.TipoCargador.MURAL,
                1, 1, 22.0, CargadorVE.Ubicacion.CENTRO_COMERCIAL);

        CargadorVE c3 = new CargadorVE("Delta", 2024, 800,
                CargadorVE.TipoConector.CCS2, CargadorVE.TipoCargador.ULTRARRAPIDO,
                2, 2, 150.0, CargadorVE.Ubicacion.ESTACION_SERVICIO);

        CargadorVE c4 = new CargadorVE("Wallbox", 2021, CargadorVE.VOLTAJE_DEFECTO,
                CargadorVE.TipoConector.TIPO_2, CargadorVE.TipoCargador.MURAL,
                1, 1, 11.0, CargadorVE.Ubicacion.RESIDENCIAL);

        CargadorVE c5 = new CargadorVE("Enel X", 2025, 22.0);

        CargadorVE[] flota = {c1, c2, c3, c4, c5};

        // Paso 2: Sesion de carga sobre C1
        c1.setPotenciaActual(40.0);
        System.out.printf("[P01] Potencia C1: %.1f kW%n", c1.getPotenciaActual());

        c1.aumentarPotencia(15.0);
        System.out.printf("[P02] Potencia C1: %.1f kW%n", c1.getPotenciaActual());

        System.out.printf("[P03] Tiempo estimado (66 kWh): %.2f horas%n", c1.tiempoEstimadoCarga(66.0));

        c1.aumentarPotencia(10.0); // Debe ser rechazado (daría 65 > 60)
        System.out.printf("[P04] Potencia C1 (tras rechazo): %.1f kW%n", c1.getPotenciaActual());

        c1.reducirPotencia(30.0);
        System.out.printf("[P05] Potencia C1: %.1f kW%n", c1.getPotenciaActual());

        System.out.printf("[P06] Tiempo estimado (50 kWh, 2 pausas de 15m): %.2f horas%n",
                c1.tiempoEstimadoCarga(50.0, 2, 15.0));

        System.out.printf("[P07] Tiempo estimado programado (50 kWh a 40 kW): %.2f horas%n",
                c1.tiempoEstimadoCarga(50.0, 40.0));

        c1.aumentarPotencia(); // Usa por defecto (+5.0)
        System.out.printf("[P08] Potencia C1: %.1f kW%n", c1.getPotenciaActual());

        c1.aumentarPotencia(5.0, 3);
        System.out.printf("[P09] Potencia C1: %.1f kW%n", c1.getPotenciaActual());

        c1.reducirPotencia(50.0); // Debe ser rechazado
        System.out.printf("[P10] Potencia C1 (tras rechazo): %.1f kW%n", c1.getPotenciaActual());

        c1.cortarCarga();
        System.out.printf("[P11] Potencia C1: %.1f kW%n", c1.getPotenciaActual());

        System.out.printf("[P12] Tiempo estimado con potencia en 0: %.2f%n", c1.tiempoEstimadoCarga(10.0));

        // Paso 3: Operaciones sobre el resto de la flota
        c2.setPotenciaActual(22.0);
        c3.setPotenciaActual(120.0);
        c4.aumentarPotencia(7.4);
        c5.aumentarPotencia(30.0); // Rechazado pues max es 22
        System.out.printf("[P13] C2: %.1f kW | C3: %.1f kW | C4: %.1f kW | C5: %.1f kW%n",
                c2.getPotenciaActual(), c3.getPotenciaActual(),
                c4.getPotenciaActual(), c5.getPotenciaActual());

        // Paso 4: Estadisticas y validaciones
        System.out.println("[P14] Conteo por tipo de cargador:");
        CargadorVE.contarPorTipo(flota);

        System.out.printf("[P15] Promedio de potencia actual: %.2f kW%n", CargadorVE.promedioPotencia(flota));

        System.out.print("[P16] Mayor potencia actual: ");
        CargadorVE.mayorPotencia(flota);

        System.out.printf("[P17] Excesos de potencia contratada: %d%n", CargadorVE.excesosDePotenciaContratada(flota));

        System.out.println("[P18] Filtrados de flota:");
        int t2Count = CargadorVE.filtrar(flota, CargadorVE.TipoConector.TIPO_2).length;
        int murCount = CargadorVE.filtrar(flota, CargadorVE.TipoCargador.MURAL).length;
        int uniCount = CargadorVE.filtrar(flota, CargadorVE.Ubicacion.UNIVERSIDAD).length;
        System.out.printf(" - TIPO_2: %d | MURAL: %d | UNIVERSIDAD: %d%n", t2Count, murCount, uniCount);

        System.out.println("[P19] C5 (valores por defecto):");
        c5.mostrar(false);

        CargadorVE copiaC3 = new CargadorVE(c3);
        System.out.printf("[P20] Copia C3 -> Fab: %s | PotActual: %.1f kW | Bitacora: %d | Total: %d%n",
                copiaC3.getFabricante(), copiaC3.getPotenciaActual(),
                copiaC3.getCantidadRegistros(), CargadorVE.getTotalCargadores());

        System.out.println("\n[P21] C1 con bitacora completa:");
        c1.mostrar(true);

        System.out.printf("[P22] Contador total de registros: %d%n", CargadorVE.getContadorRegistros());

        System.out.println("[P23] Pruebas con null:");
        CargadorVE[] flotaNull = {c1, null, c3};
        System.out.printf(" - Promedio con null: %.2f kW%n", CargadorVE.promedioPotencia(flotaNull));
        System.out.print(" - ContarPorTipo con null: ");
        CargadorVE.contarPorTipo(null);
        System.out.println("(Excepciones controladas)");

        // RUTA INDIVIDUAL (N = 52, r = 0)
        int n = 52;
        int r = 0;
        System.out.printf("%n[R] Ruta individual -> N = %d, r = %d%n", n, r);
        int conectoresBuscados = (n % 3) + 1; // 52 % 3 + 1 = 2
        CargadorVE[] resRuta = CargadorVE.cargadoresPorConectores(flota, conectoresBuscados);
        System.out.printf("[R] Cargadores con %d conectores: %d encontrado(s)%n", conectoresBuscados, resRuta.length);

        // PARTE F: Extension personalizada C6
        int d1 = 5;
        int d2 = 2;
        CargadorVE c6 = new CargadorVE("USC-52", 2017, CargadorVE.VOLTAJE_DEFECTO,
                CargadorVE.TipoConector.TIPO_2, CargadorVE.TipoCargador.RAPIDO_DC,
                3, 3, 72.0, CargadorVE.Ubicacion.RESIDENCIAL);

        System.out.println("\n[X01] Datos de C6:");
        System.out.printf("N: %d | d1: %d | d2: %d%n", n, d1, d2);
        c6.mostrar(false);

        c6.setPotenciaActual(c6.getPotenciaMaxima() / 2.0); // 36 kW
        c6.aumentarPotencia(d2 + 5, d1 + 1); // +7 kW por 6 pasos
        System.out.printf("[X02] Potencia final C6: %.1f kW%n", c6.getPotenciaActual());

        System.out.printf("[X03] Tiempo estimado C6 (N + 10 = 62 kWh): %.2f horas%n", c6.tiempoEstimadoCarga(n + 10));

        CargadorVE[] flotaExtendida = new CargadorVE[flota.length + 1];
        for (int i = 0; i < flota.length; i++) {
            flotaExtendida[i] = flota[i];
        }
        flotaExtendida[flotaExtendida.length - 1] = c6;
        System.out.println("[X04] C6 agregado a la flota extendida.");

        System.out.println("[X05] Estadisticas de flota extendida:");
        CargadorVE.contarPorTipo(flotaExtendida);
        System.out.printf("Promedio potencia: %.2f kW%n", CargadorVE.promedioPotencia(flotaExtendida));
        System.out.print("Mayor potencia: ");
        CargadorVE.mayorPotencia(flotaExtendida);
        System.out.printf("Excesos potencia: %d%n", CargadorVE.excesosDePotenciaContratada(flotaExtendida));

        System.out.printf("[X06] Total cargadores: %d | Total registros: %d%n",
                CargadorVE.getTotalCargadores(), CargadorVE.getContadorRegistros());
        System.out.println("\nBitacora de C6:");
        c6.mostrar(true);
    }
}
