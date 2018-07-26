import EnigmaCompetition.Ally;
import EnigmaCompetition.Competition;
import EnigmaCracking.DM;
import EnigmaCracking.Tasks.TaskLevels;
import Machine.MachineProxy;
import Utils.generalUtils;
import XmlParsing.Ex3XmlParser;
import agentUtilities.EnigmaDictionary;
import com.sun.xml.internal.bind.v2.TODO;
import sun.awt.Mutex;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/newAllySignIn")
public class newAllyServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = Utils.CookieUtils.getUserCookie(req.getCookies()).getValue();
        //TODO:: generate a different cookie for ally so you can retrieve a competition from it, or add to competition allies memeber
        Competition competition = Utils.generalUtils.getCompetitionFromIndex(Integer.parseInt(req.getParameter("selectCompetition")), getServletContext());
        DM dm = buildDmFromCompetition(competition, generalUtils.generatePort());
        Ally ally = new Ally(username, dm, generalUtils.generatePort());
        competition.addAlly(ally);
        Utils.CookieUtils.setCompetitionFromCookie(competition, req.getCookies(), getServletContext());
        resp.sendRedirect("/selectCompetition.html");
    }

    private DM buildDmFromCompetition(Competition competition,int allyPort) {
        DM dm = null;
        try {
            MachineProxy machineProxy = competition.getIntegrator().getMachine().clone();
            String stringToDecrypt = competition.getEncryptedString();
            EnigmaDictionary enigmaDictionary = competition.getDictionary();
            int taskLevel = competition.getTaskLevel();
            ///change numAgents!!
             dm = new DM(machineProxy, enigmaDictionary, stringToDecrypt,1, 100, taskLevel, new Mutex(), allyPort);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return dm;

    }


}
