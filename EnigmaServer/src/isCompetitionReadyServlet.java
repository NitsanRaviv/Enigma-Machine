import EnigmaCompetition.Ally;
import EnigmaCompetition.Competition;
import EnigmaCracking.DM;
import Machine.MachineProxy;
import Utils.generalUtils;
import agentUtilities.EnigmaDictionary;
import sun.awt.Mutex;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/isCompetitionReadyUboat")
public class isCompetitionReadyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = Utils.CookieUtils.getUserCookie(req.getCookies()).getValue();
        Competition competition = Utils.CookieUtils.getCompetitionFromCookie(req.getCookies(), getServletContext());
        if(competition.checkReady() == true)
            //TODO send competition data
            resp.sendRedirect("/startCompetitionUboat.html");
        else
        {
            //TODO tell participants to wait more
        }
    }


}
