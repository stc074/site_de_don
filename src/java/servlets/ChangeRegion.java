/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package servlets;

import classes.Objet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author pj
 */
public class ChangeRegion extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            if(request.getParameter("idRegion")!=null) {
                String idRegion=request.getParameter("idRegion");
                try {
                    Objet.getConnection();
                    String query="SELECT id_departement,departement FROM table_departements WHERE id_region=? ORDER BY id_departement ASC";
                    PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                    prepare.setString(1, idRegion);
                    ResultSet result=prepare.executeQuery();
                    out.println("<span>Votre département :</span>");
                    out.println("<select name=\"idDepartement\" onchange=\"javascript:changeDepartement(this.value);\">");
                    out.println("<option value=\"0\" selected=\"selected\">Choisissez</option>");
                    while(result.next()) {
                        String idDepartement=result.getString("id_departement");
                        String departement=result.getString("departement");
                        out.println("<option value=\""+idDepartement+"\">"+idDepartement+" &rarr; "+departement+"</option>");
                    }
                    out.println("</select>");
                    out.println("<div id=\"innerCommunes\">");
                    out.println("<span>Votre commune :</span>");
                    out.println("<select name=\"idCommune\">");
                    out.println("<option value=\"0\" selected=\"selected\">Choisissez</option>");
                    out.println("</select>");
                    out.println("</div>");
                    result.close();
                    prepare.close();
                    Objet.closeConnection();
                } catch (NamingException ex) {
                    Logger.getLogger(ChangeRegion.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(ChangeRegion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            /* TODO output your page here
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ChangeRegion</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ChangeRegion at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
            */
        } finally { 
            out.close();
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
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
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
