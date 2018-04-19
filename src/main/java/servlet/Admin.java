/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import java.io.IOException;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author
 */
@WebServlet(name = "admin", urlPatterns = {"/admin"})
public class Admin extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.text.ParseException
     * @throws servlet.DAOException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException, DAOException {
        // Créér le DAO avec sa source de données      
        // Properties est une Map<clé, valeur> pratique pour générer du JSON
        DAO dao = new DAO(DataSourceFactory.getDataSource());
        String debut1 = request.getParameter("debut1");
        String fin1 = request.getParameter("fin1");
        String debut2 = request.getParameter("debut2");
        String fin2 = request.getParameter("fin2");
        String debut3 = request.getParameter("debut3");
        String fin3 = request.getParameter("fin3");
        Map<String, Double> resultats = new HashMap<>();
        String res, clients, types, states;
        String action = request.getParameter("action");

        resultats = dao.salesByCustomer(new java.sql.Date(new SimpleDateFormat("yyyyMMdd").parse(debut1).getTime()), new java.sql.Date(new SimpleDateFormat("yyyyMMdd").parse(fin1).getTime()));
        res = resultats.values().toString();
        clients = resultats.keySet().toString();
        res = res.replace("[", "");
        res = res.replace("]", "");
        clients = clients.replace("[", "");
        clients = clients.replace("]", "");
        request.setAttribute("resultats1", res);
        request.setAttribute("clients", clients);

        resultats = dao.salesByTypes(new java.sql.Date(new SimpleDateFormat("yyyyMMdd").parse(debut2).getTime()), new java.sql.Date(new SimpleDateFormat("yyyyMMdd").parse(fin2).getTime()));
        res = resultats.values().toString();
        types = resultats.keySet().toString();
        res = res.replace("[", "");
        res = res.replace("]", "");
        types = types.replace("[", "");
        types = types.replace("]", "");
        request.setAttribute("resultats2", res);
        request.setAttribute("types", types);

        resultats = dao.salesByStates(new java.sql.Date(new SimpleDateFormat("yyyyMMdd").parse(debut3).getTime()), new java.sql.Date(new SimpleDateFormat("yyyyMMdd").parse(fin3).getTime()));
        res = resultats.values().toString();
        states = resultats.keySet().toString();
        res = res.replace("[", "");
        res = res.replace("]", "");
        states = states.replace("[", "");
        states = states.replace("]", "");
        request.setAttribute("resultats3", res);
        request.setAttribute("states", states);
        
        try {

            if (null != action) {
                switch (action) {
                    case "regler":  // On redirige vers la vue
                        request.getRequestDispatcher("protected/admin.jsp").forward(request,response);
                        break;

                    default:
                        break;
                }
            }

        } catch (IOException ex) {

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
        try {
            processRequest(request, response);
        } catch (ParseException | DAOException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (ParseException | DAOException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
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

}
