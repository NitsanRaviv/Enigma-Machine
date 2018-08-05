import EnigmaCompetition.Ally;
import EnigmaCompetition.Competition;
import EnigmaCracking.DM;
import Machine.MachineProxy;
import Utils.CookieUtils;
import Utils.generalUtils;
import agentUtilities.EnigmaDictionary;
import com.google.gson.JsonObject;
import sun.awt.Mutex;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/getAllyPort")
public class getAllyPortServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = Utils.CookieUtils.getUserCookie(req.getCookies()).getValue();
        Ally ally = CookieUtils.getAllyFromUserName(username, getServletContext());
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(ally.getUsername(), ally.getPort());
        if(ally.getSecondRun() == false) {
            DM dm = buildDmFromCompetition(ally.getPort(), ally.getMutex());
            dm.setAllyObserver(ally);
            ally.setDm(dm);
            dm.start();
        }
        else {
            ally.getDm().run();
        }
        resp.getOutputStream().print(jsonObject.toString());
        //next screen is initiate agents screen which Ally will be until set up all agents
        // for now - one agent
    }

    private DM buildDmFromCompetition(int allyPort, Mutex mutex) {
        DM dm = null;
        //possible to change task size
        dm = new DM(0, 100, mutex, allyPort);
        return dm;
    }

}
