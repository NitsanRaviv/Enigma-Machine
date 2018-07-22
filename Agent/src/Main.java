import enigmaAgent.EnigmaWebAgent;

public class Main {
    public static void main(String[] args){
        int port = Integer.parseInt(args[0]);
        EnigmaWebAgent webAgent = new EnigmaWebAgent(port);
        try {
            webAgent.run();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
