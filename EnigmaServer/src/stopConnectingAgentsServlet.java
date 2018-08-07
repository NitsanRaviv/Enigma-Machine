import EnigmaCompetition.Ally;
import Utils.CookieUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


//TODO:: change servlet to start competition if everyone is ready - so for all allies start DM.

@WebServlet("/stopConnectingAgents")
public class stopConnectingAgentsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = Utils.CookieUtils.getUserCookie(req.getCookies()).getValue();
        Ally ally = CookieUtils.getAllyFromUserName(username, getServletContext());
        stopConnectingAgents(ally);
        resp.sendRedirect("/theGameAlies.html");
    }

    private void stopConnectingAgents(Ally ally) {
        ally.getDm().setKeepConnectingAgents(false);
        ally.setState(Ally.State.waitingToStart);
    }

}

