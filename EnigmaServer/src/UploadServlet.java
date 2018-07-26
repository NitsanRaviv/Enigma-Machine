import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import EnigmaCompetition.Competition;
import EnigmaCompetition.Ubout;
import LogicManager.*;
import XmlParsing.DictionaryXmlParser;
import XmlParsing.Ex3XmlParser;
import XmlParsing.JaxbClasses.Battlefield;
import agentUtilities.EnigmaDictionary;


@WebServlet("/UploadServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50)   // 50MB
public class UploadServlet extends HttpServlet {
    /**
     * Name of the directory where uploaded files will be saved, relative to
     * the web application directory.
     */
    private static final String SAVE_DIR = "uploadFiles";

    /**
     * handles file upload
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        // gets absolute path of the web application
        String appPath = request.getServletContext().getRealPath("");
        // constructs path of the directory to save uploaded file
        String savePath = appPath + File.separator + SAVE_DIR;

        // creates the save directory if it does not exists
        File fileSaveDir = new File(savePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }

        String pathToNewFile = null;

        //XML files only, so only one part
        for (Part part : request.getParts()) {
            String fileName = extractFileName(part);
            // refines the fileName in case it is an absolute path
            fileName = new File(fileName).getName();
            pathToNewFile = savePath + File.separator + fileName;
            part.write(pathToNewFile);

        }

        //TODO:: check xml is valid
        setCompetitionFromFile(pathToNewFile, request);
        response.sendRedirect("/defineCompetition.html");
    }

    private void setCompetitionFromFile(String pathToNewFile, HttpServletRequest request) {
        createNewUboat(request);
        setIntegratorForCompetition(pathToNewFile, request);
        setBattlefieldForCompetition(pathToNewFile, request);
        setEnigmaDictionaryForCompetition(pathToNewFile, request);
    }

    private void createNewUboat(HttpServletRequest req) {
        Ubout ubout = new Ubout(Utils.CookieUtils.getUserCookie(req.getCookies()).getValue());
        Competition competition = Utils.CookieUtils.getCompetitionFromCookie(req.getCookies(), getServletContext());
        competition.setUboat(ubout);
        Utils.CookieUtils.setCompetitionFromCookie(competition, req.getCookies(), getServletContext());
    }

    private void setIntegratorForCompetition(String xmlPath, HttpServletRequest req) {
        Competition competition = Utils.CookieUtils.getCompetitionFromCookie(req.getCookies(), getServletContext());
        Integrator integrator = new Integrator();
        integrator.loadMachineFromXml(xmlPath);
        competition.setIntegrator(integrator);
        Utils.CookieUtils.setCompetitionFromCookie(competition, req.getCookies(), getServletContext());
    }

    private void setBattlefieldForCompetition(String xmlPath, HttpServletRequest req){
        Competition competition = Utils.CookieUtils.getCompetitionFromCookie(req.getCookies(), getServletContext());
        Battlefield battlefield = null;
        try {
            battlefield = Ex3XmlParser.parseXmltoJaxbBattlefield(xmlPath);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        competition.setBattlefield(battlefield);
        Utils.CookieUtils.setCompetitionFromCookie(competition, req.getCookies(), getServletContext());
    }


    private void setEnigmaDictionaryForCompetition(String xmlPath, HttpServletRequest req){
        Competition competition = Utils.CookieUtils.getCompetitionFromCookie(req.getCookies(), getServletContext());
        EnigmaDictionary dictionary = null;
        try {
            dictionary = DictionaryXmlParser.getDictionaryXmlParser().getDictionary(xmlPath);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        competition.setDictionary(dictionary);
        Utils.CookieUtils.setCompetitionFromCookie(competition, req.getCookies(), getServletContext());
    }

    /**
     * Extracts file name from HTTP header content-disposition
     */
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";
    }
}