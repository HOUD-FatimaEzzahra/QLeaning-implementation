package ma.enset.version_sma;


import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import ma.enset.QLUtils;

public class SimpleContainer {
    public static void main(String[] args) throws StaleProxyException {
        Runtime runtime = Runtime.instance();
        ProfileImpl profile = new ProfileImpl();
        profile.setParameter(ProfileImpl.MAIN_HOST, "localhost");
        AgentContainer agentContainer = runtime.createAgentContainer(profile);
        for (int i = 0; i < QLUtils.NUMBER_OF_AGENTS; i++) {
            try {
                AgentController serverAgent= agentContainer.createNewAgent("agent" + i, QAgent.class.getName(),new Object[]{0,i});
                serverAgent.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
