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
        createNewUboat(request);
        setIntegratorForCompetition(pathToNewFile);
        //TODO:: check xml is valid
        request.getSession().setAttribute("stam", "stamt");
        response.sendRedirect("/defineCompetition.html");
    }

    private void createNewUboat(HttpServletRequest req) {
        Ubout ubout = new Ubout("roy");
        Competition competition = (Competition) getServletContext().getAttribute("roy");
        competition.setUboat(ubout);
    }

    private void setIntegratorForCompetition(String xmlPath) {
        Competition competition = (Competition) getServletContext().getAttribute("roy");
        Integrator integrator = new Integrator();
        integrator.loadMachineFromXml(xmlPath);
        competition.setIntegrator(integrator);
        getServletContext().setAttribute("roy", competition);
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