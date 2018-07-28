import EnigmaCompetition.Ally;
import com.google.gson.JsonObject;

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
       Ally ally = Utils.CookieUtils.getAllyFromUserName(Utils.CookieUtils.getUserCookie(req.getCookies()), getServletContext());
       JsonObject jsonObject = new JsonObject();
       jsonObject.addProperty(ally.getUsername(), ally.getPort());
       startDmIfneeded(ally);
       resp.getOutputStream().print(jsonObject.toString());
       //next sreen is initiate agents screen which Ally will be until set up all agents
        // for now - one agent
    }

    private void startDmIfneeded(Ally ally){
        if(ally.getState().equals(Ally.State.inActive)) {
            ally.getMutex().lock();
            ally.getDm().start();
            ally.setState(Ally.State.waitingForAgents);
        }
    }
}
