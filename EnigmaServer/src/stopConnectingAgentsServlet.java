import EnigmaCompetition.Ally;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/stopConnectingAgents")
public class stopConnectingAgentsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       Ally ally = Utils.CookieUtils.getAllyFromUserName(Utils.CookieUtils.getUserCookie(req.getCookies()), getServletContext());
       ally.getDm().setKeepConnectingAgents(false);
       ally.setState(Ally.State.waitingToStart);
       ///for tests reasons only! do this when competition starts
       synchronized (ally.getMutex()){
           ally.getMutex().notifyAll();
       }
    }

}
