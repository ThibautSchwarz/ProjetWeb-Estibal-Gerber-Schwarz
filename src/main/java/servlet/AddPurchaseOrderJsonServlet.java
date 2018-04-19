package servlet;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cgerber
 */
@WebServlet(name = "addPurchaseOrder", urlPatterns = {"/protected/addOrder"})
public class AddPurchaseOrderJsonServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.text.ParseException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        DAO dao = new DAO(DataSourceFactory.getDataSource());

        String num = request.getParameter("num");
        String qt = request.getParameter("qt");
        String prodid = request.getParameter("prodid");
        String username = request.getParameter("username");
        String comp = request.getParameter("comp");
        String ship_cost = request.getParameter("shipcost");
        String message = null;
        LocalDate todayLocalDate = LocalDate.now(ZoneId.of("Europe/Paris"));// On récupère la date actuelle
        Date sysdate = Date.valueOf(todayLocalDate);
        Float cost;

        try {
            // cost = dao.cost(parseInt(qt), parseInt(prodid)); amélioration éventuelle où le coût de la commande serait affiché
            dao.addPurchaseOrder(parseInt(num), parseInt(qt), Float.valueOf(ship_cost), username, parseInt(prodid), sysdate, sysdate, comp);
            // méthode dao pour ajouter une commande dans la BDD

            message = String.format("Bon %s ajouté", num);
        } catch (SQLException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            message = ex.getMessage();
        }

        Properties resultat = new Properties();
        resultat.put("message", message);

        try (PrintWriter out = response.getWriter()) {
            // On spécifie que la servlet va générer du JSON
            response.setContentType("application/json;charset=UTF-8");
            // Générer du JSON
            Gson gson = new Gson();
            out.println(gson.toJson(resultat));
        }
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
        processRequest(request, response);
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
        processRequest(request, response);
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

}
