package agents;
import java.util.Random;

import gui.ConsumerGraph;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import javax.swing.SwingUtilities;

public class ConsumerAgent extends Agent {

    private int energieDemandee;
    private ConsumerGraph consumerGraph;

    @Override
    protected void setup() {
        energieDemandee = 25 + (int) (Math.random() * 51);
        Object[] args = getArguments();
        if (args != null && args.length > 0 && args[0] instanceof ConsumerGraph) {
            consumerGraph = (ConsumerGraph) args[0];
        } else {
            doDelete();
            return;
        }

        System.out.println("Agent " + getLocalName() + " initialisé");

        addBehaviour(new TickerBehaviour(this, 1000) { // 1000 ms = 1 second
            @Override
            protected void onTick() {
                energieDemandee = 25 + (int) (Math.random() * 51);

                //envoi REQUEST à UtilAgent
                ACLMessage demande = new ACLMessage(ACLMessage.REQUEST);
                demande.addReceiver(new AID("UtilAgent", AID.ISLOCALNAME));
                demande.setContent("Demande de " + energieDemandee + " kWh");
                send(demande);
                System.out.println("Consommateur: " + getLocalName() + " demande " + energieDemandee + " kWh");

                ACLMessage reponse = receive();
                if (reponse != null) {
                    System.out.println(getLocalName() + " a reçu : " + reponse.getContent());

                    int energieFournie = parseEnergyFromResponse(reponse.getContent());

                    SwingUtilities.invokeLater(() -> consumerGraph.updateGraph(energieFournie));
                } else {
                    block(500);
                }
            }
        });
    }

    private int parseEnergyFromResponse(String content) {
        try {

            if (content.startsWith("Allocation:")) {
                String[] parts = content.split(":");
                String energyPart = parts[1].trim();
                String energyValue = energyPart.split(" ")[0];
                return Integer.parseInt(energyValue);
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }
}
