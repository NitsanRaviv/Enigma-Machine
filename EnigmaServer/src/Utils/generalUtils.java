package Utils;

import EnigmaCompetition.Competition;
import EnigmaCompetition.Ubout;

import javax.servlet.ServletContext;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class generalUtils {
    private static Set<Integer> ports;
    private static Random random = new Random();

    public static int generatePort() {
        if (ports == null) {
            init();
        }
        int randy = random.nextInt();
        if (randy < 0)
            randy *= -1;
        randy = randy % 65535;
        while (ports.contains(randy) == true) {
            randy = random.nextInt();
            if (randy < 0)
                randy *= -1;
            randy = randy % 65535;
        }
        ports.add(randy);
        return randy;
    }

    private static void init() {
        ports = new HashSet<>();
        ports.add(8080);
        ports.add(80);
        ports.add(8443);
        ports.add(21707);
    }

    public static Competition getCompetitionFromIndex(int selectCompetition, ServletContext servletContext) {
        try {
            List<Competition> competitions = (List<Competition>) servletContext.getAttribute("competitions");
            return competitions.get(selectCompetition - 1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
