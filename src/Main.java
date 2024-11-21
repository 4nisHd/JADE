import gui.ConsumerGraph;
import gui.SupplyGraph;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class Main {
    public static void main(String[] args) {
        Runtime execution = Runtime.instance();
        Profile profil = new ProfileImpl();


        profil.setParameter(Profile.MAIN_HOST, "localhost");
        profil.setParameter(Profile.MAIN_PORT, "1099");

        AgentContainer conteneurPrincipal = execution.createMainContainer(profil);

        try {
            //graphs pour ConsumerAgents
            ConsumerGraph consumerGraph1 = new ConsumerGraph();
            consumerGraph1.setTitle("Consommation energie - Consumer 1");
            consumerGraph1.setVisible(true); // Ensure window is displayed

            ConsumerGraph consumerGraph2 = new ConsumerGraph();
            consumerGraph2.setTitle("consommation energie - Consumer 2");
            consumerGraph2.setVisible(true); // Ensure window is displayed




            //instantiation des agents avec graphs
            AgentController utilAgent = conteneurPrincipal.createNewAgent(
                    "UtilAgent", "agents.UtilAgent", new Object[]{});
            AgentController sourceAgent = conteneurPrincipal.createNewAgent("SourceAgent", "agents.SourceAgent", null);

            AgentController consumerAgent1 = conteneurPrincipal.createNewAgent(
                    "Consumer1", "agents.ConsumerAgent", new Object[]{consumerGraph1});
            AgentController consumerAgent2 = conteneurPrincipal.createNewAgent(
                    "Consumer2", "agents.ConsumerAgent", new Object[]{consumerGraph2});


            //start
            utilAgent.start();          //agent utilitaire
            sourceAgent.start();        //un agent source
            consumerAgent1.start();     //deux agents consommateurs
            consumerAgent2.start();



        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
