/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package servlets;

import classes.Datas;
import classes.Objet;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author pj
 */
public class DisplayPhoto extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            if(request.getParameter("idAnnonce")!=null&&request.getParameter("index")!=null) {
                long idAnnonce=Long.parseLong(request.getParameter("idAnnonce"));
                String index=request.getParameter("index");
                String mini="";
                if(request.getParameter("mini")!=null)
                    mini="mini"+request.getParameter("mini")+"_";
                Objet.getConnection();
                String query="SELECT extension"+index+" FROM table_annonces WHERE id=? LIMIT 0,1";
                PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                prepare.setLong(1, idAnnonce);
                ResultSet result=prepare.executeQuery();
                result.next();
                String extension=result.getString("extension"+index);
                result.close();
                prepare.close();
                String filename=Datas.DIR+"/photos/"+mini+idAnnonce+"_"+index+extension;
                File file=new File(filename);
                if(file.exists()) {
                    String extMime=extension.replaceAll("\\.", "");
                    String mime="";
                    String format="";
                    if(extMime.equals("jpg")) {
                        mime="image/jpeg";
                        format="JPEG";
                    } else {
                        mime="image/"+extMime;
                        format=extMime.toUpperCase();
                    }
                    BufferedImage buffer = ImageIO.read(file);
                    response.setContentType(mime);
                    OutputStream os = response.getOutputStream();
                    ImageIO.write(buffer, format, os);
                    os.close();
                    }
                Objet.closeConnection();
                }
        } catch (NamingException ex) {
            Logger.getLogger(DisplayPhoto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DisplayPhoto.class.getName()).log(Level.SEVERE, null, ex);
        } finally { 
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
