package ma.enset.version_sma;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import ma.enset.QLUtils;

public class MasterAgent extends Agent {
    protected void setup() {
        // Description de l'agent pour le service de l'agent DF (Directory Facilitator)
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        // Description du service offert par l'agent
        ServiceDescription sd = new ServiceDescription();
        sd.setType("QLearning");
        sd.setName("masterAgent");
        dfd.addServices(sd);

        try {
            // Enregistrement de l'agent auprès de l'agent DF
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }

        // Comportement cyclique de l'agent
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                // Réception du message
                ACLMessage receivedMessage = receive();

                // Création de la table Q vide
                double[][] qTable = new double[QLUtils.GRID_SIZE * QLUtils.GRID_SIZE][QLUtils.ACTIONS_SIZE];

                if (receivedMessage != null) {
                    // Réception de la table Q depuis l'agent
                    System.out.println(receivedMessage.getSender().getName());
                    String qTabletring = receivedMessage.getContent();
                    String[] rows = qTabletring.split("\n");

                    // Suppression de la première ligne contenant "null"
                    rows[0] = rows[0].substring(4);

                    // Remplissage de la table Q à partir des valeurs reçues
                    for (int i = 0; i < rows.length; i++) {
                        String[] columns = rows[i].split(",");
                        for (int j = 0; j < columns.length; j++) {
                            qTable[i][j] = Double.parseDouble(columns[j]);
                        }
                    }

                    // Affichage de la table Q
                    for (int i = 0; i < QLUtils.GRID_SIZE * QLUtils.GRID_SIZE; i++) {
                        for (int j = 0; j < QLUtils.ACTIONS_SIZE; j++) {
                            System.out.print(qTable[i][j] + " ");
                        }
                        System.out.println();
                    }
                } else {
                    // Attente de nouveaux messages
                    block();
                }
            }
        });
    }
}
