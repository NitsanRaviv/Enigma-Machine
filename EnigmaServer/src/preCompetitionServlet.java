import EnigmaCompetition.Competition;
import LogicManager.InitialCode.InitialCodeParser;

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
        resp.sendRedirect("/theGameUboat.html");
    }

    private void setInitialCode(HttpServletRequest req) {
        Competition competition = (Competition) getServletContext().getAttribute("roy");
        String[] rotorIds = req.getParameter("rotorNumbers").split(",");
        String[] initialLoc = req.getParameter("initialLocation").split(",");
        String chosenRefl = req.getParameter("reflector");
        competition.getIntegrator().setInitialCode(rotorIds, initialLoc, chosenRefl);
        getServletContext().setAttribute("roy", competition);
    }

    private void setStringToDecrypt(HttpServletRequest req) {
        Competition competition = (Competition) getServletContext().getAttribute("roy");
        String stringToEncrypt = req.getParameter("stringToProcess");
        competition.setDecryptedString(stringToEncrypt);
        competition.setEncryptedString(competition.getIntegrator().getMachine().encryptCodeToString(stringToEncrypt));
        getServletContext().setAttribute("roy", competition);

    }
}
