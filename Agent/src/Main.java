import enigmaAgent.EnigmaWebAgent;

public class Main {
    public static void main(String[] args){
        String[] portAndhost = args[0].split(":");
        String host = portAndhost[0];
        int port = Integer.parseInt(portAndhost[1]);
        EnigmaWebAgent webAgent = new EnigmaWebAgent(host, port);
        try {
            webAgent.run();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
