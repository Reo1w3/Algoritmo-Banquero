package ui;

import model.AlgoritmoBanquero;
import model.EstadoProceso;
import model.SimulacionProceso;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    private final PanelAnimacion panelAnimacion;
    private JButton btnCasoSeguro;
    private JButton btnCasoDeadlock;

    public VentanaPrincipal() {
        setTitle("Simulador Gráfico: Algoritmo del Banquero");
        setSize(880, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panelAnimacion = new PanelAnimacion();
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel panelControl = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        btnCasoSeguro = new JButton("Simular Estado Seguro");
        btnCasoDeadlock = new JButton("Simular Estado Inseguro (Deadlock)");

        panelControl.add(btnCasoSeguro);
        panelControl.add(btnCasoDeadlock);

        btnCasoSeguro.addActionListener(e -> ejecutar(true));
        btnCasoDeadlock.addActionListener(e -> ejecutar(false));

        add(panelControl, BorderLayout.NORTH);
        add(panelAnimacion, BorderLayout.CENTER);
    }

    private void ejecutar(boolean casoSeguro) {
        btnCasoSeguro.setEnabled(false);
        btnCasoDeadlock.setEnabled(false);
        panelAnimacion.setDetalleDeadlock("");

        // Estado Seguro estándar vs Caso Deadlock real
        int[] disponibles = casoSeguro ? new int[]{3, 3, 2} : new int[]{1, 0, 0};

        int[][] maximo = {
                {7, 5, 3}, // P0
                {3, 2, 2}, // P1
                {9, 0, 2}, // P2
                {2, 2, 2}, // P3
                {4, 3, 3}  // P4
        };

        int[][] asignacion = {
                {0, 1, 0}, // P0
                {2, 0, 0}, // P1
                {3, 0, 2}, // P2
                {2, 1, 1}, // P3
                {0, 0, 2}  // P4
        };

        List<SimulacionProceso> listaProcesos = new ArrayList<>();
        for (int i = 0; i < maximo.length; i++) {
            listaProcesos.add(new SimulacionProceso(i, maximo[i], asignacion[i]));
        }

        AlgoritmoBanquero banquero = new AlgoritmoBanquero(disponibles, listaProcesos);
        panelAnimacion.actualizarSimulacion(banquero, "Comprobando condiciones de asignación iniciales...");

        SwingWorker<Void, String> worker = new SwingWorker<>() {
            private final List<String> ordenSecuencia = new ArrayList<>();

            @Override
            protected Void doInBackground() throws Exception {
                int total = listaProcesos.size();
                int terminados = 0;

                while (terminados < total) {
                    boolean progreso = false;

                    for (SimulacionProceso p : listaProcesos) {
                        if (p.getEstado() == EstadoProceso.FINALIZADO) continue;

                        panelAnimacion.actualizarSimulacion(banquero, "Evaluando P" + p.getId() + ": Need " + Arrays.toString(p.getNecesidad()) + " <= Available " + Arrays.toString(banquero.getDisponibles()) + "?");
                        Thread.sleep(1000);

                        if (banquero.puedeEjecutar(p)) {
                            p.setEstado(EstadoProceso.EJECUTANDO);
                            panelAnimacion.actualizarSimulacion(banquero, "P" + p.getId() + " puede completarse. Ejecutando y reteniendo asignación...");
                            Thread.sleep(1200);

                            banquero.liberarRecursos(p);
                            p.setEstado(EstadoProceso.FINALIZADO);
                            ordenSecuencia.add("P" + p.getId());
                            panelAnimacion.actualizarSimulacion(banquero, "P" + p.getId() + " finalizado. Recursos liberados a Available.");
                            Thread.sleep(900);

                            terminados++;
                            progreso = true;
                            break;
                        } else {
                            panelAnimacion.actualizarSimulacion(banquero, "P" + p.getId() + " NO puede ejecutarse (Recursos insuficientes). Continúa en espera.");
                            Thread.sleep(800);
                        }
                    }

                    // No hubo ningún proceso que pudiera satisfacer su Need con los recursos libres
                    if (!progreso) {
                        List<String> bloqueados = new ArrayList<>();
                        for (SimulacionProceso p : listaProcesos) {
                            if (p.getEstado() != EstadoProceso.FINALIZADO) {
                                p.setEstado(EstadoProceso.BLOQUEADO);
                                bloqueados.add("P" + p.getId());
                            }
                        }

                        String detalle = "DIAGNÓSTICO: CONDICIÓN DE INTERBLOQUEO DETECTADA\n"
                                + "• Procesos bloqueados: " + String.join(", ", bloqueados) + "\n"
                                + "• Motivo: Todos los procesos pendientes requieren Need > Available sin liberar Allocation.";

                        panelAnimacion.setDetalleDeadlock(detalle);
                        panelAnimacion.actualizarSimulacion(banquero, "ESTADO INSEGURO / DEADLOCK");
                        return null;
                    }
                }

                String sec = String.join(" -> ", ordenSecuencia);
                panelAnimacion.actualizarSimulacion(banquero, "ESTADO SEGURO GARANTIZADO | Secuencia: " + sec);
                return null;
            }

            @Override
            protected void done() {
                btnCasoSeguro.setEnabled(true);
                btnCasoDeadlock.setEnabled(true);
            }
        };

        worker.execute();
    }
}