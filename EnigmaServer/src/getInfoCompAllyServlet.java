import EnigmaCompetition.Ally;
import EnigmaCompetition.Competition;
import Utils.CookieUtils;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


//TODO:: change servlet to start competition if everyone is ready - so for all allies start DM.

@WebServlet("/getInfoCompAlly")
public class getInfoCompAllyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = Utils.CookieUtils.getUserCookie(req.getCookies()).getValue();
        Ally ally = CookieUtils.getAllyFromUserName(username, getServletContext());
        Competition competition = ally.getCompetition();
        JsonObject jsonObject = new JsonObject();
        if (competition.isRunning() == false && competition.isFinished() == false) {
            jsonObject.addProperty("started", "no");
            jsonObject.addProperty("sizeOfMission", competition.getIntegrator().getTaskDifficulty(competition.getTaskLevel()));
            jsonObject.addProperty("encryptedString", competition.getEncryptedString());
        }
        //competition started
        else{
            jsonObject.addProperty("winner", competition.getWinner());
            jsonObject.addProperty("alliesAndAgents", competition.getAlliesAndAgents());
            jsonObject.addProperty("allyAndPotentials", ally.getAgentPotentials());
        }
        resp.getOutputStream().print(jsonObject.toString());
    }
}

