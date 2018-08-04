import EnigmaCompetition.Ally;
import EnigmaCompetition.Competition;
import EnigmaCracking.DM;
import Machine.MachineProxy;
import Utils.CookieUtils;
import agentUtilities.EnigmaDictionary;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.xml.internal.bind.v2.TODO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


//TODO:: change servlet to start competition if everyone is ready - so for all allies start DM.

@WebServlet("/canStartCompetition")
public class canStartCompetitionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Competition competition = Utils.CookieUtils.getCompetitionFromCookie(req.getCookies(), getServletContext());
        JsonObject jsonObject = new JsonObject();
        if(competition.isRunning()){
            jsonObject.addProperty("started", "yes");
            jsonObject.addProperty("alliesAndAgents", competition.getAlliesAndAgents());
            jsonObject.addProperty("allAlliesPotentials", competition.getAllAllyPotentials());
        }
        else if(competition.isFinished()){
            jsonObject.addProperty("finished", "yes");
            jsonObject.addProperty("alliesAndAgents", competition.getAlliesAndAgents());
            jsonObject.addProperty("allAlliesPotentials", competition.getAllAllyPotentials());
            //add logout functionality!
        }
        else{
            if(competition.checkReady()){
                competition.runCompetition();
                jsonObject.addProperty("started", "yes");
            }
            else{//competition waiting to start
                jsonObject.addProperty("started", "no");
            }
        }
        resp.getOutputStream().print(jsonObject.toString());
    }
}

