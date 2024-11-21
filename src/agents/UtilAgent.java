package agents;
import gui.SupplyGraph;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

public class UtilAgent extends Agent {

    private int tot_supply = 1000; //energie totale
    private List<String> demande = new ArrayList<>(); //liste des consommateurs
    private List<Integer> demandeEnergie = new ArrayList<>(); //liste des quantités d'énergie demandées
    private SupplyGraph supplyGraph;

    @Override
    protected void setup() {
        System.out.println("Agent utilitaire " + getLocalName() + " initialisé");

        SwingUtilities.invokeLater(() -> supplyGraph = new SupplyGraph());

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = myAgent.receive();
                if (msg != null) {

                    String contenu = msg.getContent();

                    if (contenu.contains("Demande de")) {

                        String consumerName = msg.getSender().getLocalName();
                        int energieDemandee = parseRequestedEnergy(contenu);

                        demande.add(consumerName);
                        demandeEnergie.add(energieDemandee);

                        System.out.println("Demande reçu de " + consumerName + ": " + energieDemandee + " kWh");
                        allouerEnergie();

                    } else if (contenu.startsWith("Energie disponible :")) {
                        int energieFromSource = Integer.parseInt(contenu.split(": ")[1].split(" ")[0]);
                        tot_supply += energieFromSource;

                        System.out.println("Le nouveau supply total est: " + tot_supply + " kWh");

                        if (supplyGraph != null) {
                            SwingUtilities.invokeLater(() -> supplyGraph.updateGraph(tot_supply));
                        }
                    } else {
                        block(5000);
                    }
                }
            }

            //allocation de l'energie à chaque consommateur en fonction de sa demande
            private void allouerEnergie() {
                if (tot_supply <= 0) {
                    System.out.println("Pas d'énergie disponible pour allocation.");
                    return;
                }

                for (int i = 0; i < demande.size(); i++) {
                    String consumer = demande.get(i);
                    int requestedEnergy = demandeEnergie.get(i);

                    int allocatedEnergy = Math.min(requestedEnergy, tot_supply);

                    ACLMessage response = new ACLMessage(ACLMessage.INFORM);
                    response.addReceiver(getAID(consumer));
                    response.setContent("Allocation: " + allocatedEnergy + " kWh");
                    send(response);

                    tot_supply -= allocatedEnergy;

                    System.out.println("Alloué " + allocatedEnergy + " kWh à " + consumer);
                    System.out.println("Energie restante: " + tot_supply + " kWh");

                    if (tot_supply <= 0) {
                        System.out.println("Pas d'énergie disponible.");
                        break;
                    }
                }

                demande.clear();
                demandeEnergie.clear();

                if (supplyGraph != null) {
                    SwingUtilities.invokeLater(() -> supplyGraph.updateGraph(tot_supply));
                }
            }

            //extraction de energie demandé
            private int parseRequestedEnergy(String content) {
                try {
                    String[] parts = content.split(" ");
                    return Integer.parseInt(parts[2]); // "Demande de X kWh"
                } catch (Exception e) {
                    System.err.println("Erreur lors de l'analyse de la demande: " + content);
                    return 0;
                }
            }
        });
    }
}
