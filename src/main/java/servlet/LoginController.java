package servlet;

import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.NumberFormatException;

public class LoginController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws servlet.DAOException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DAOException {
        // Quelle action a appelé cette servlet ?
        String action = request.getParameter("action");
        if (null != action) {
            switch (action) {
                case "Se connecter":
                    checkLogin(request);
                    break;
                case "Se deconnecter":
                    doLogout(request);
                    break;
            }
        }

        // Est-ce que l'utilisateur est connecté ?
        // On cherche l'attribut userName dans la session
        String userName = findUserInSession(request);
        String jspView;
        if (null == userName) { // L'utilisateur n'est pas connecté
            // On choisit la page de login
            jspView = "login.jsp";

        } else {
            if("Administrateur".equals(userName))
                jspView = "afficheAdmin.jsp";
            else
                jspView = "afficheClient.jsp";
        }
        // On va vers la page choisie
        request.getRequestDispatcher(jspView).forward(request, response);

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (DAOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (DAOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void checkLogin(HttpServletRequest request) throws DAOException {
        // Les paramètres transmis dans la requête
        String loginParam = request.getParameter("loginParam");
        int passwordParam = 999;
        try {
            passwordParam = parseInt(request.getParameter("passwordParam"));
          
            //identifiants administrateurs
        String login = getInitParameter("login");
        int password = parseInt(getInitParameter("password"));
        String userName = getInitParameter("userName");

        if ((login.equals(loginParam)) && (password == passwordParam)) { // on test si les identifiants sont ceux d'un admin 
            // On a trouvé la combinaison login / password
            // On stocke l'information dans la session
            HttpSession session = request.getSession(true); // démarre la session
            session.setAttribute("userName", userName);
        } else { // On positionne un message d'erreur pour l'afficher dans la JSP
            DAO dao = new DAO(DataSourceFactory.getDataSource());
            CustomerEntity user = dao.findUser(loginParam, passwordParam);

            if (user != null) {
                // On a trouvé la combinaison login / password
                // On stocke l'information dans la session
                HttpSession session = request.getSession(true); // démarre la session
                session.setAttribute("userName", user.getName());
            } else { // On positionne un message d'erreur pour l'afficher dans la JSP
                request.setAttribute("errorMessage", "Identifiant / Mot de passe incorrect");
            }
        }
        } catch (NumberFormatException ex) {
            request.setAttribute("errorMessage", "Votre mot de passe doit être un nombre");
        }

    }

    private void doLogout(HttpServletRequest request) {
        // On termine la session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    private String findUserInSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session == null) ? null : (String) session.getAttribute("userName");
    }

}
