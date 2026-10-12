package model;

import java.util.List;

public class EstadoSistema {
    private final boolean seguro;
    private final List<Integer> secuenciaSegura;
    private final int[][] necesidad;
    private final String mensaje;

    public EstadoSistema(boolean seguro, List<Integer> secuenciaSegura, int[][] necesidad, String mensaje) {
        this.seguro = seguro;
        this.secuenciaSegura = secuenciaSegura;
        this.necesidad = necesidad;
        this.mensaje = mensaje;
    }

    public boolean isSeguro() {
        return seguro;
    }

    public List<Integer> getSecuenciaSegura() {
        return secuenciaSegura;
    }

    public int[][] getNecesidad() {
        return necesidad;
    }

    public String getMensaje() {
        return mensaje;
    }
}
