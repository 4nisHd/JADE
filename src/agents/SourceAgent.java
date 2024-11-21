package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class SourceAgent extends Agent {


    private int energieFournie;

    @Override
    protected void setup() {
        System.out.println("Agent Renouvelable " + getLocalName() + " initialisé.");

        addBehaviour(new TickerBehaviour(this, 1000) {
            @Override
            protected void onTick() {
                //generation aléatoire d'une quantité d'energie
                energieFournie = 75 + (int) (Math.random() * 51);

                //message INFORM
                ACLMessage miseAJour = new ACLMessage(ACLMessage.INFORM);

                miseAJour.addReceiver(new AID("UtilAgent", AID.ISLOCALNAME));

                miseAJour.setContent("Energie disponible : " + energieFournie + " kWh");


                send(miseAJour);
            }
        });
    }
}
