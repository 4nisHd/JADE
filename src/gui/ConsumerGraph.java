package gui;

import javax.swing.*;
import java.awt.*;

public class ConsumerGraph extends JFrame {

    private final DefaultListModel<String> energyDataModel;
    private final JList<String> energyDataList;

    public ConsumerGraph() {

        setTitle("Consommation d'énergie");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        energyDataModel = new DefaultListModel<>();
        energyDataList = new JList<>(energyDataModel);
        add(new JScrollPane(energyDataList), BorderLayout.CENTER);

    }

    public void updateGraph(int energySupplied) {

        String entry = "énergie reçue: " + energySupplied + " kwh";
        energyDataModel.addElement(entry);
    }
}
