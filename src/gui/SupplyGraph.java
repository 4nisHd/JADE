package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SupplyGraph extends JFrame {

    private ArrayList<Integer> supplyData = new ArrayList<>();
    private JPanel graphPanel;

    public SupplyGraph() {


        setTitle("Quantité totale d'energie dans Utility Agent");

        setSize(600, 400);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        graphPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGraph((Graphics2D) g);
            }
        };
        graphPanel.setBackground(Color.WHITE);

        add(graphPanel);
        setVisible(true);
    }

    public void updateGraph(int supply) {
        supplyData.add(supply);
        graphPanel.repaint();
    }

    private void drawGraph(Graphics2D g) {
        if (supplyData.isEmpty()) return;

        int width = graphPanel.getWidth();
        int height = graphPanel.getHeight();
        int padding = 50;
        int visibleDataCount = 20; //Max data points
        int startIndex = Math.max(0, supplyData.size() - visibleDataCount);
        int dataCount = Math.min(supplyData.size(), visibleDataCount);

        int step = (width - 2 * padding) / (dataCount - 1);
        int maxSupply = supplyData.stream().max(Integer::compare).orElse(1);

        g.setColor(Color.BLACK);
        g.drawLine(padding, height - padding, width - padding, height - padding); // X-axis
        g.drawLine(padding, padding, padding, height - padding);

        g.drawString("temps", width / 2, height - 10);
        g.drawString("supply (kwh)", 10, height / 2);

        int numYTicks = 5;
        for (int i = 0; i <= numYTicks; i++) {
            int y = height - padding - i * (height - 2 * padding) / numYTicks;
            int value = i * maxSupply / numYTicks;

            g.drawLine(padding - 5, y, padding, y);
            g.drawString(String.valueOf(value), padding - 40, y + 5); // Value label
        }

        for (int i = 0; i < dataCount; i++) {
            int x = padding + i * step;
            int timeLabel = startIndex + i + 1;
            g.drawLine(x, height - padding, x, height - padding + 5);
            if (i % 2 == 0) {
                g.drawString(String.valueOf(timeLabel), x - 5, height - padding + 20);
            }
        }

        for (int i = 1; i < dataCount; i++) {
            int x1 = padding + (i - 1) * step;
            int y1 = height - padding - (supplyData.get(startIndex + i - 1) * (height - 2 * padding) / maxSupply);
            int x2 = padding + i * step;
            int y2 = height - padding - (supplyData.get(startIndex + i) * (height - 2 * padding) / maxSupply);

            if (supplyData.get(startIndex + i) < supplyData.get(startIndex + i - 1)) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.GREEN);
            }

            g.setStroke(new BasicStroke(3));
            g.drawLine(x1, y1, x2, y2);
        }
    }
}
