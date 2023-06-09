package ma.enset.version_sequentielle;

import java.util.Random;

import static ma.enset.QLUtils.*;

public class QLearning {

    private double[][] qTable = new double[GRID_SIZE*GRID_SIZE][ACTIONS_SIZE];
    // Définir le point de départ
    private int stateI;
    private int stateJ;

    public QLearning(){
    }

    private void resetState(){
        stateI = 0;
        stateJ = 0;
    }

    // Choisissez une action en utilisant la stratégie epsilon-greedy
    private int chooseAction(double epsilon){
        Random random = new Random();
        int bestAction = 0;
        double bestQValue = Double.MIN_VALUE;

        if(random.nextDouble() < epsilon){
            // Exploration : Choisissez une action aléatoire
            bestAction = random.nextInt(ACTIONS_SIZE);
        }else{
            // Exploitation : Choisissez l'action avec la plus grande récompense
            int state = stateI * GRID_SIZE + stateJ; // Convertir la position en un nombre unique
            for (int i=0; i<ACTIONS_SIZE; i++){
                if(qTable[state][i] > bestQValue){
                    bestQValue = qTable[state][i];
                    bestAction = i;
                }
            }
        }

        return bestAction;
    }

    private boolean finished(){
        return GRID[stateI][stateJ] == 1;
    }

    private int executeAction(int action){
        // Revenir en arrière si l'action n'est pas valide
        stateI = Math.max(0, Math.min(stateI + ACTIONS[action][0], GRID_SIZE-1));
        stateJ = Math.max(0, Math.min(stateJ + ACTIONS[action][1], GRID_SIZE-1));
        return stateI * GRID_SIZE + stateJ;
    }

    private void printQTable(){
        System.out.println("***********qTable***********");
        for (double[] row : qTable){
            System.out.print("|");
            for (double value : row){
                System.out.print(value + "|\t");
            }
            System.out.println("|");
        }
        resetState();
        while (!finished()){
            int currentState = stateI * GRID_SIZE + stateJ;
            int action = chooseAction(0); // Pas d'exploration
            System.out.println("État : (" + currentState + ") | Meilleure action : " + action);

            int newState = executeAction(action);
            System.out.println("Nouvel état : (" + newState + ")");
        }
        //System.out.println("État final : (" + stateI + " " + stateJ + ")");
    }

    public void runQLearning(){
        int iteration = 0;
        int currentState;
        int newState;
        resetState();
        while (iteration < MAX_EPOCHS){
            resetState();
            while (!finished()){
                // État actuel et meilleure action
                currentState = stateI * GRID_SIZE + stateJ;
                int action = chooseAction(0.3);

                // Prochain état et sa meilleure action
                newState = executeAction(action);
                int action2 = chooseAction(0);

                // Mettre à jour la table Q
                qTable[currentState][action] = qTable[currentState][action]
                        + ALPHA * (GRID[stateI][stateJ]
                        + GAMMA * qTable[newState][action2]
                        - qTable[currentState][action]);
            }
            iteration++;
        }
        printQTable();
    }
}
