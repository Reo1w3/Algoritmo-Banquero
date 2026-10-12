package ui;

import model.AlgoritmoBanquero;
import model.EstadoProceso;
import model.SimulacionProceso;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class PanelAnimacion extends JPanel {

    private AlgoritmoBanquero banquero;
    private String logEstado = "Listo para iniciar simulación";
    private String detalleDeadlock = "";

    public PanelAnimacion() {
        setBackground(new Color(24, 24, 27));
    }

    public void actualizarSimulacion(AlgoritmoBanquero banquero, String log) {
        this.banquero = banquero;
        this.logEstado = log;
        repaint();
    }

    public void setDetalleDeadlock(String detalle) {
        this.detalleDeadlock = detalle;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (banquero == null) return;

        // 1. Cabecera técnica
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Consolas", Font.BOLD, 15));
        g2.drawString("RECURSOS DISPONIBLES [A, B, C]: " + Arrays.toString(banquero.getDisponibles()), 30, 35);

        g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        g2.setColor(new Color(161, 161, 170));
        g2.drawString("Paso: " + logEstado, 30, 60);

        // 2. Procesos
        List<SimulacionProceso> procesos = banquero.getProcesos();
        int y = 85;
        int altoCaja = 48;
        int anchoCaja = getWidth() - 60;

        for (SimulacionProceso p : procesos) {
            g2.setColor(getColorPorEstado(p.getEstado()));
            g2.fillRoundRect(30, y, anchoCaja, altoCaja, 8, 8);

            g2.setColor(new Color(255, 255, 255, 60));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(30, y, anchoCaja, altoCaja, 8, 8);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Consolas", Font.BOLD, 14));
            g2.drawString("P" + p.getId() + " [" + p.getEstado() + "]", 45, y + 29);

            g2.setFont(new Font("Consolas", Font.PLAIN, 12));
            g2.setColor(new Color(228, 228, 231));
            String info = String.format("Alloc: %-9s Max: %-9s Need: %-9s",
                    Arrays.toString(p.getAsignacion()),
                    Arrays.toString(p.getMaximo()),
                    Arrays.toString(p.getNecesidad()));
            g2.drawString(info, 200, y + 29);

            y += altoCaja + 12;
        }

        // 3. Panel de Diagnóstico de Deadlock al final
        if (!detalleDeadlock.isEmpty()) {
            g2.setColor(new Color(127, 29, 29, 140));
            g2.fillRoundRect(30, y + 10, anchoCaja, 70, 8, 8);

            g2.setColor(new Color(239, 68, 68));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(30, y + 10, anchoCaja, 70, 8, 8);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Consolas", Font.BOLD, 12));
            String[] lineas = detalleDeadlock.split("\n");
            int dy = y + 32;
            for (String linea : lineas) {
                g2.drawString(linea, 45, dy);
                dy += 18;
            }
        }
    }

    private Color getColorPorEstado(EstadoProceso estado) {
        switch (estado) {
            case EJECUTANDO: return new Color(180, 83, 9);   // Ámbar
            case FINALIZADO: return new Color(21, 128, 61);  // Verde
            case BLOQUEADO:  return new Color(153, 27, 27);  // Rojo
            default:         return new Color(39, 39, 42);   // Gris
        }
    }
}