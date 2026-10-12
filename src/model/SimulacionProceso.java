package model;

import java.util.Arrays;

public class SimulacionProceso {
    private final int id;
    private final int[] maximo;
    private final int[] asignacion;
    private final int[] necesidad;
    private EstadoProceso estado;

    public SimulacionProceso(int id, int[] maximo, int[] asignacion) {
        this.id = id;
        this.maximo = Arrays.copyOf(maximo, maximo.length);
        this.asignacion = Arrays.copyOf(asignacion, asignacion.length);
        this.necesidad = new int[maximo.length];
        for (int i = 0; i < maximo.length; i++) {
            this.necesidad[i] = this.maximo[i] - this.asignacion[i];
        }
        this.estado = EstadoProceso.ESPERANDO;
    }

    public int getId() {
        return id;
    }

    public int[] getMaximo() {
        return maximo;
    }

    public int[] getAsignacion() {
        return asignacion;
    }

    public int[] getNecesidad() {
        return necesidad;
    }

    public EstadoProceso getEstado() {
        return estado;
    }

    public void setEstado(EstadoProceso estado) {
        this.estado = estado;
    }
}