package model;

import java.util.Arrays;
import java.util.List;

public class AlgoritmoBanquero {

    private final int[] disponibles;
    private final List<SimulacionProceso> procesos;
    private final int numRecursos;

    public AlgoritmoBanquero(int[] disponiblesIniciales, List<SimulacionProceso> procesos) {
        this.numRecursos = disponiblesIniciales.length;
        this.disponibles = Arrays.copyOf(disponiblesIniciales, numRecursos);
        this.procesos = procesos;
    }

    public synchronized int[] getDisponibles() {
        return Arrays.copyOf(disponibles, numRecursos);
    }

    public synchronized List<SimulacionProceso> getProcesos() {
        return procesos;
    }

    public synchronized boolean puedeEjecutar(SimulacionProceso p) {
        for (int r = 0; r < numRecursos; r++) {
            if (p.getNecesidad()[r] > disponibles[r]) {
                return false;
            }
        }
        return true;
    }

    public synchronized void liberarRecursos(SimulacionProceso p) {
        for (int r = 0; r < numRecursos; r++) {
            disponibles[r] += p.getAsignacion()[r];
        }
    }
}