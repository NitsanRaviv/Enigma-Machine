import EnigmaCompetition.Ally;
import EnigmaCompetition.Competition;
import Utils.CookieUtils;
import Utils.generalUtils;
import sun.awt.Mutex;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@WebServlet("/newAllySignIn")
public class newAllyServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = Utils.CookieUtils.getUserCookie(req.getCookies()).getValue();
        Ally ally = CookieUtils.getAllyFromUserName(username, getServletContext());
        if(ally == null) {
            int port = generalUtils.generatePort();
            Mutex allyMutex = new Mutex();
            ally = new Ally(username, port, allyMutex);
            ally.setState(Ally.State.inActive);
            List<Ally> allies = CookieUtils.getAllyList(getServletContext());
            allies.add(ally);
            //TODO:: generate a different cookie for ally so you can retrieve a competition from it, or add to competition allies memeber
        }
        resp.sendRedirect("/initiateAgents.html");
    }
}
