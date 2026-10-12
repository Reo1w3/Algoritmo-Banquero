package ui;

import model.EstadoSistema;

import javax.swing.*;
import java.awt.*;

public class PanelResultados extends JPanel {

    private final JTextArea areaTexto;

    public PanelResultados() {
        setLayout(new BorderLayout());
        areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 13));
        add(new JScrollPane(areaTexto), BorderLayout.CENTER);
    }

    public void mostrarResultados(EstadoSistema estado, int numProcesos, int numRecursos) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== REPORTE DE EJECUCIÓN ===\n\n");
        sb.append("Estado: ").append(estado.getMensaje()).append("\n\n");

        sb.append("Matriz Need (Necesidad):\n");
        int[][] need = estado.getNecesidad();
        for (int i = 0; i < numProcesos; i++) {
            sb.append(String.format("P%-2d: ", i));
            for (int j = 0; j < numRecursos; j++) {
                sb.append(String.format("%3d ", need[i][j]));
            }
            sb.append("\n");
        }

        sb.append("\n");
        if (estado.isSeguro()) {
            sb.append("Secuencia segura: ");
            for (int i = 0; i < estado.getSecuenciaSegura().size(); i++) {
                sb.append("P").append(estado.getSecuenciaSegura().get(i));
                if (i < estado.getSecuenciaSegura().size() - 1) {
                    sb.append(" -> ");
                }
            }
            sb.append("\n");
        } else {
            sb.append("No existe una secuencia segura viable.\n");
        }

        areaTexto.setText(sb.toString());
    }
}