package ma.enset.version_sma;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import ma.enset.QLUtils;

import java.util.Random;

public class QAgent extends Agent {
    double[][] qTable = new double[QLUtils.GRID_SIZE * QLUtils.GRID_SIZE][QLUtils.ACTIONS_SIZE];

    // Récupérer stateI et stateJ à partir des propriétés
    int stateI = 0;
    int stateJ = 0;

    @Override
    public void setup() {
        System.out.println("Bonjour ! L'agent " + getAID().getName() + " est prêt.");
        SequentialBehaviour sb = new SequentialBehaviour();

        sb.addSubBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                resetState();
            }
        });
        sb.addSubBehaviour(new Behaviour() {
            int currentState;
            int newState;
            int iteration;

            @Override
            public void action() {
                runQLearning();
            }

            @Override
            public boolean done() {
                return iteration >= QLUtils.MAX_EPOCHS || finished();
            }
        });

        sb.addSubBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                //printQTable();
                sendQTable();
            }
        });
        addBehaviour(sb);
    }

    private void resetState() {
        stateI = (Integer) getArguments()[0];
        stateJ = (Integer) getArguments()[1];
    }

    private int chooseAction(double epsilon) {
        Random random = new Random();
        int bestAction = 0;
        double bestQValue = Double.MIN_VALUE;
        if (random.nextDouble() < epsilon) {
            // Exploration : Choix d'une action aléatoire
            bestAction = random.nextInt(QLUtils.ACTIONS_SIZE);
        } else {
            // Exploitation : Choix de l'action avec la plus grande récompense
            int state = stateI * QLUtils.GRID_SIZE + stateJ; // Convertir la position en un nombre unique
            for (int i = 0; i < QLUtils.ACTIONS_SIZE; i++) {
                if (qTable[state][i] > bestQValue) {
                    bestQValue = qTable[state][i];
                    bestAction = i;
                }
            }
        }
        return bestAction;
    }

    private boolean finished() {
        return QLUtils.GRID[stateI][stateJ] == 1;
    }

    private int executeAction(int action) {
        // Rebondir en arrière si l'action n'est pas valide
        stateI = Math.max(0, Math.min(stateI + QLUtils.ACTIONS[action][0], QLUtils.GRID_SIZE - 1));
        stateJ = Math.max(0, Math.min(stateJ + QLUtils.ACTIONS[action][1], QLUtils.GRID_SIZE - 1));
        return stateI * QLUtils.GRID_SIZE + stateJ;
    }

    private void printBestPath() {
        resetState();
        System.out.println("L'agent " + getAID().getName() + " commence à (" + stateI + "," + stateJ + ")");

        while (!finished()) {
            int currentState = stateI * QLUtils.GRID_SIZE + stateJ;
            int action = chooseAction(0); // Pas d'exploration
            System.out.println("(" + stateI + "," + stateJ + ") -> " + action);

            int newState = executeAction(action);

        }
        System.out.println("Objectif atteint : (" + stateI + " " + stateJ + ")");
    }

    public void sendQTable() {
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("QLearning");
        dfd.addServices(sd);
        DFAgentDescription[] result = null;
        try {
            result = DFService.search(this, dfd);
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(result[0].getName());
        for (double[] doubles : qTable) {
            for (double aDouble : doubles) {
                msg.setContent(msg.getContent() + aDouble + ",");
            }
            msg.setContent(msg.getContent() + "\n");
        }
        //System.out.println("Message envoyé : " + msg.getContent());
        send(msg);
    }

    @Override
    public void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }
    }

    public void runQLearning() {
        // Dans le MAS : c'est un comportement OneShot
        int iteration = 0;
        int currentState;
        int newState;
        resetState();
        // C'est un comportement cyclique
        while (iteration < QLUtils.MAX_EPOCHS) {
            resetState();
            while (!finished()) {
                // État courant et meilleure action
                currentState = stateI * QLUtils.GRID_SIZE + stateJ;
                int action = chooseAction(0.3);

                // Prochain état et sa meilleure action
                newState = executeAction(action);
                int action2 = chooseAction(0);

                // Mise à jour de la Q-Table
                qTable[currentState][action] = qTable[currentState][action]
                        + QLUtils.ALPHA * (QLUtils.GRID[stateI][stateJ]
                        + QLUtils.GAMMA * qTable[newState][action2]
                        - qTable[currentState][action]
                );
            }
            iteration++;
        }
        printBestPath();
    }
}
