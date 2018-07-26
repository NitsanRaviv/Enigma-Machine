import EnigmaCompetition.Competition;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/preCompetition")
public class preCompetitionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setInitialCode(req);
        setStringToDecrypt(req);
        setGameLevel(req);
        resp.sendRedirect("/theGameUboat.html");
    }

    private void setInitialCode(HttpServletRequest req) {
        Competition competition = Utils.CookieUtils.getCompetitionFromCookie(req.getCookies(), getServletContext());
        String[] rotorIds = req.getParameter("rotorNumbers").split(",");
        String[] initialLoc = req.getParameter("initialLocation").split(",");
        String chosenRefl = req.getParameter("reflector");
        competition.getIntegrator().setInitialCode(rotorIds, initialLoc, chosenRefl);
        Utils.CookieUtils.setCompetitionFromCookie(competition, req.getCookies(), getServletContext());
    }

    private void setStringToDecrypt(HttpServletRequest req) {
        Competition competition = Utils.CookieUtils.getCompetitionFromCookie(req.getCookies(), getServletContext());
        String stringToEncrypt = req.getParameter("stringToProcess").toUpperCase();
        competition.setDecryptedString(stringToEncrypt.toUpperCase());
        competition.setEncryptedString(competition.getIntegrator().processInput(stringToEncrypt.toUpperCase()).toUpperCase());
        Utils.CookieUtils.setCompetitionFromCookie(competition, req.getCookies(), getServletContext());
    }

    private void setGameLevel(HttpServletRequest req) {
        Competition competition = Utils.CookieUtils.getCompetitionFromCookie(req.getCookies(), getServletContext());
        //String gameLevel = competition.getBattlefield().getLevel();
        //TODO::get competition from string - easy, medium etc.
        String gameLevel = "1";
        competition.setTaskLevel(Integer.parseInt(gameLevel));
        Utils.CookieUtils.setCompetitionFromCookie(competition, req.getCookies(), getServletContext());
    }
}
