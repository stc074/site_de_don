/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package classes;

import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.naming.NamingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author pj
 */
public class Membre extends Objet {

    private String pseudo;
    private String email;
    private String motDePasse;
    private String motDePasse2;
    private String idRegion;
    private String idDepartement;
    private int idCommune;
    private String captcha;
    private long id;
    private String pseudoConnecte;
    private long timestamp;
    private boolean flagMdp;

    public Membre() {
        super();
        this.pseudo="";
        this.email="";
        this.idRegion="0";
        this.idDepartement="0";
        this.idCommune=0;
    }

    public void getPostsInsc(HttpServletRequest request) {
        this.pseudo=request.getParameter("pseudo");
        this.pseudo=Objet.codeHTML(this.pseudo);
        this.email=request.getParameter("email");
        this.email=Objet.codeHTML(this.email);
        this.motDePasse=request.getParameter("motDePasse");
        this.motDePasse=Objet.codeHTML(this.motDePasse);
        this.motDePasse2=request.getParameter("motDePasse2");
        this.motDePasse2=Objet.codeHTML(this.motDePasse2);
        this.setIdRegion(request.getParameter("idRegion"));
        this.setIdRegion(Objet.codeHTML(this.idRegion));
        this.idDepartement=request.getParameter("idDepartement");
        this.idDepartement=Objet.codeHTML(this.idDepartement);
        this.idCommune=Integer.parseInt(request.getParameter("idCommune"));
        this.captcha=request.getParameter("captcha").toLowerCase();
        this.captcha=Objet.codeHTML(this.captcha);
    }

    public void verifPostsInsc(HttpServletRequest request, HttpServletResponse response) {
        try {
            Objet.getConnection();
            HttpSession session = request.getSession(true);
            Pattern p = Pattern.compile("[a-zA-Z0-9]+");
            Matcher m = p.matcher(this.pseudo);
            if (this.pseudo.length() == 0) {
                this.setErrorMsg("Champ PSEUDONYME vide.<br/>");
            }
            else if(this.pseudo.length()<2)
                this.setErrorMsg("Champ PSEUDONYME trop court.<br/>");
            else if (this.pseudo.length() > 20) {
                this.setErrorMsg("Champ PSEUDONYME trop long.<br/>");
            } else if (m.matches() == false) {
                this.setErrorMsg("Champ PSEUDONYME non valide (Caractères alphanumériques uniquement).<br/>");
            } else {
                String query="SELECT COUNT(id) AS nb FROM table_membres WHERE pseudo=?";
                PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                prepare.setString(1, this.pseudo);
                ResultSet result=prepare.executeQuery();
                result.next();
                int nb=result.getInt("nb");
                if(nb!=0)
                    this.setErrorMsg("Désolé, ce PSEUDONYME est déjà pris.<br/>");
                result.close();
                prepare.close();
            }
            p = Pattern.compile("^[a-z0-9._-]+@[a-z0-9._-]{2,}\\.[a-z]{2,4}$");
            m = p.matcher(this.email);
            if(this.email.length()==0)
                this.setErrorMsg("Champ EMAIL vide.<br/>");
            else if(this.email.length()>200)
                this.setErrorMsg("Champ EMAIL trop long.<br/>");
            else if(m.matches()==false)
                this.setErrorMsg("Champ EMAIL non valide.<br/>");
            else {
                String query="SELECT COUNT(id) AS nb FROM table_membres WHERE email=?";
                PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                prepare.setString(1, this.email);
                ResultSet result=prepare.executeQuery();
                result.next();
                int nb=result.getInt("nb");
                if(nb!=0)
                    this.setErrorMsg("Désolé, cette ADRESSE EMAIL est déjà associée à un compte.<br/>");
                result.close();
                prepare.close();
            }
            p = Pattern.compile("[a-zA-Z0-9]+");
            m = p.matcher(this.motDePasse);
            Matcher m2 = p.matcher(this.motDePasse2);
            if(this.motDePasse.length()==0)
                this.setErrorMsg("Champ MOT DE PASSE vide.<br/>");
            else if(this.motDePasse.length()<3)
                this.setErrorMsg("Champ MOT DE PASSE trop court.<br/>");
            else if(this.motDePasse.length()>15)
                this.setErrorMsg("Champ MOT DE PASSE trop long.<br/>");
            else if(m.matches()==false)
                this.setErrorMsg("Champ MOT DE PASSE non-valide (Caractères alphanumériques uniquement).<br/>");
            else if(this.motDePasse2.length() == 0)
                this.setErrorMsg("Champ CONFIRMATION vide.<br/>");
            else if(this.motDePasse2.length()<3)
                this.setErrorMsg("Champ CONFIRMATION trop court.<br/>");
            else if(this.motDePasse2.length()>15)
                this.setErrorMsg("Champ CONFIRMATION trop long.<br/>");
            else if(m2.matches()==false)
                this.setErrorMsg("Champ CONFIRMATION non-valide (Caractères alphanumériques uniquement).<br/>");
            else if(!this.motDePasse.equals(this.motDePasse2))
                this.setErrorMsg("Vos 2 MOTS DE PASSE sont différents.<br/>");
            if(this.idRegion.equals("0"))
                this.setErrorMsg("Veuiller choisir votre RÉGION SVP.<br/>");
            if(this.idDepartement.equals("0"))
                this.setErrorMsg("Veuillez choisir votre DÉPARTEMENT SVP.<br/>");
            if(this.idCommune==0)
                this.setErrorMsg("Veuillez choisir votre COMMUNE SVP.<br/>");
            if(this.captcha.length()==0)
                this.setErrorMsg("Champ CODE ANTI-ROBOT vide.<br/>");
            else if(this.captcha.length()>5)
                this.setErrorMsg("Champ CODE ANTI-ROBOT trop long.<br/>");
            else if(session.getAttribute("captcha")==null)
                this.setErrorMsg("Session CODE ANTI-ROBOT Dépassée.<br/>");
            else if(!session.getAttribute("captcha").toString().equals(Objet.getEncoded(this.captcha)))
                this.setErrorMsg("Mauvais CODE ANTI-ROBOT.<br/>");
            if(this.getErrorMsg().length()==0) {
                String query="INSERT INTO table_membres (pseudo,email,mot_de_passe,id_region,id_departement,id_commune,timestamp) VALUES (?,?,?,?,?,?,?)";
                PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                prepare.setString(1, this.pseudo);
                prepare.setString(2, this.email);
                prepare.setString(3, Objet.getEncoded(this.motDePasse));
                prepare.setString(4, this.idRegion);
                prepare.setString(5, this.idDepartement);
                prepare.setInt(6, this.idCommune);
                Calendar cal=Calendar.getInstance();
                long ts=cal.getTimeInMillis();
                prepare.setLong(7, ts);
                prepare.executeUpdate();
                prepare.close();
                query="SELECT LAST_INSERT_ID() AS idMembre FROM table_membres WHERE pseudo=? AND email=? AND mot_de_passe=?";
                prepare=Objet.getConn().prepareStatement(query);
                prepare.setString(1, this.pseudo);
                prepare.setString(2, this.email);
                prepare.setString(3, Objet.getEncoded(this.motDePasse));
                ResultSet result=prepare.executeQuery();
                boolean flag=result.next();
                if(!flag) {
                    prepare.close();
                    result.close();
                    this.setErrorMsg("ERREUR INTERNE.<br/>");
                } else if(flag) {
                    this.id=result.getLong("idMembre");
                    session.setAttribute("idMembre", this.getId());
                    session.setAttribute("pseudo", this.pseudo);
                    result.close();
                    prepare.close();
                    Objet.setCookie(this.getId(), response);
                    Mail mail=new Mail(this.email, this.pseudo, "Inscription !");
                    mail.initMailInscription(this.pseudo, this.email, this.motDePasse);
                    mail.send();
                    session.setAttribute("captcha", null);
                    this.setTest(1);
                }
            }
            Objet.closeConnection();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Membre.class.getName()).log(Level.SEVERE, null, ex);
            this.setErrorMsg("ERREUR INTERNE : "+ex.getMessage());
        } catch (NamingException ex) {
            Logger.getLogger(Membre.class.getName()).log(Level.SEVERE, null, ex);
            this.setErrorMsg("ERREUR INTERNE : "+ex.getMessage());
        } catch (SQLException ex) {
            Logger.getLogger(Membre.class.getName()).log(Level.SEVERE, null, ex);
            this.setErrorMsg("ERREUR INTERNE : "+ex.getMessage());
        }
    }
    public void testConnecte(HttpServletRequest request, HttpServletResponse response) {
        this.id=0;
        //Objet.initSession(request, response);
        HttpSession session=request.getSession(true);
        String cookieValue="";
        if(session.getAttribute("idMembre")!=null&&session.getAttribute("pseudo")!=null) {
            this.id=Long.parseLong(session.getAttribute("idMembre").toString());
            this.pseudo=session.getAttribute("pseudo").toString();
            this.pseudoConnecte=this.pseudo;
            session.setAttribute("idMembre", this.getId());
            session.setAttribute("pseudo", this.pseudo);
        } else {
            Cookie[] cookies=request.getCookies();
            if(cookies!=null) {
            for(int i=0;i<cookies.length;i++) {
                if(cookies[i].getName().equals("doncook"))
                    cookieValue=cookies[i].getValue();
            }
            if(cookieValue.length()>0) {
                try {
                    Objet.getConnection();
                    String query="SELECT id,pseudo FROM table_membres WHERE cookie_code=? LIMIT 0,1";
                    PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                    prepare.setString(1, cookieValue);
                    ResultSet result=prepare.executeQuery();
                    boolean flag=result.next();
                    if(flag) {
                        this.id=result.getLong("id");
                        session.setAttribute("idMembre", this.getId());
                        this.pseudo=result.getString("pseudo");
                        this.pseudoConnecte=this.pseudo;
                        session.setAttribute("pseudo", this.pseudo);
                    }
                    else
                        this.id=0;
                    result.close();
                    prepare.close();
                    Objet.closeConnection();
                } catch (NamingException ex) {
                    Logger.getLogger(Membre.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(Membre.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            }
        }
    }
    public void getPostsCnx(HttpServletRequest request) {
            this.pseudo=request.getParameter("pseudo");
            this.pseudo=Objet.codeHTML(this.pseudo);
            this.motDePasse=request.getParameter("motDePasse");
            this.motDePasse=Objet.codeHTML(this.motDePasse);
    }

    public void verifPostsCnx(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session=request.getSession(true);
        try {
            Objet.getConnection();
            Pattern p = Pattern.compile("[a-zA-Z0-9]+");
            Matcher m = p.matcher(this.pseudo);
            if(this.pseudo.length()==0)
                this.setErrorMsg("PSEUDONYME vide.<br/>");
            else if(this.pseudo.length() < 2)
                this.setErrorMsg("PSEUDONYME trop court.<br/>");
            else if(this.pseudo.length()>20)
                this.setErrorMsg("PSEUDONYME trop long.<br./>");
            else if(m.matches()==false)
                this.setErrorMsg("PSEUDONYME non-valide.<br/>");
            else {
                String query="SELECT COUNT(id) AS nb FROM table_membres WHERE pseudo=?";
                PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                prepare.setString(1, this.pseudo);
                ResultSet result=prepare.executeQuery();
                result.next();
                int nb=result.getInt("nb");
                if(nb==0)
                    this.setErrorMsg("PSEUDO inconnu.<br/>");
                result.close();
                prepare.close();
            }
            m = p.matcher(this.motDePasse);
            if(this.motDePasse.length()==0)
                this.setErrorMsg("MOT DE PASSE vide.<br/>");
            else if(this.motDePasse.length()<3)
                this.setErrorMsg("MOT DE PASSE trop court.<br/>");
            else if(this.motDePasse.length()>15)
                this.setErrorMsg("MOT DE PASSE trop long.<br/>");
            else if(m.matches()==false)
                this.setErrorMsg("MOT DE PASSE non-valide.<br/>");
            else if(this.getErrorMsg().length()==0) {
                String query="SELECT COUNT(id) AS nb FROM table_membres WHERE pseudo=? AND mot_de_passe=?";
                PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                prepare.setString(1, this.pseudo);
                prepare.setString(2, Objet.getEncoded(this.motDePasse));
                ResultSet result=prepare.executeQuery();
                result.next();
                int nb=result.getInt("nb");
                if(nb==0)
                    this.setErrorMsg("Mauvais MOT DE PASSE.<br/>");
                result.close();
                prepare.close();
            }
            if(this.getErrorMsg().length()==0) {
                String query="SELECT id FROM table_membres WHERE pseudo=? AND mot_de_passe=? LIMIT 0,1";
                PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                prepare.setString(1, this.pseudo);
                prepare.setString(2, Objet.getEncoded(this.motDePasse));
                ResultSet result=prepare.executeQuery();
                boolean flag=result.next();
                if(!flag) {
                    this.id=0;
                } else if(flag) {
                    this.id=result.getLong("id");
                    session.setAttribute("idMembre", this.getId());
                    session.setAttribute("pseudo", this.pseudo);
                    Objet.setCookie(this.getId(), response);
                    this.setTest(1);
                }
                result.close();
                prepare.close();
            }
            Objet.closeConnection();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Membre.class.getName()).log(Level.SEVERE, null, ex);
            this.setErrorMsg("ERREUR INTENE : "+ex.getMessage());
        } catch (NamingException ex) {
            Logger.getLogger(Membre.class.getName()).log(Level.SEVERE, null, ex);
            this.setErrorMsg("ERREUR INTENE : "+ex.getMessage());
        } catch (SQLException ex) {
            Logger.getLogger(Membre.class.getName()).log(Level.SEVERE, null, ex);
            this.setErrorMsg("ERREUR INTENE : "+ex.getMessage());
        }
    }

    public void initInfos(long idMembre) {
        this.id=idMembre;
        try {
            Objet.getConnection();
            String query="SELECT pseudo,email,id_region,id_departement,id_commune,timestamp FROM table_membres WHERE id=? LIMIT 0,1";
            PreparedStatement prepare=Objet.getConn().prepareStatement(query);
            prepare.setLong(1, this.id);
            ResultSet result=prepare.executeQuery();
            result.next();
            this.pseudo=result.getString("pseudo");
            this.email=result.getString("email");
            this.setIdRegion(result.getString("id_region"));
            this.idDepartement=result.getString("id_departement");
            this.idCommune=result.getInt("id_commune");
            this.timestamp=result.getLong("timestamp");
            result.close();
            prepare.close();
            Objet.closeConnection();
        } catch (NamingException ex) {
            Logger.getLogger(Membre.class.getName()).log(Level.SEVERE, null, ex);
            this.id=0;
        } catch (SQLException ex) {
            Logger.getLogger(Membre.class.getName()).log(Level.SEVERE, null, ex);
            this.id=0;
        }
    }

    public void getPostsInfos(HttpServletRequest request) {
        this.motDePasse=request.getParameter("motDePasse");
        this.motDePasse=Objet.codeHTML(this.motDePasse);
        this.motDePasse2=request.getParameter("motDePasse2");
        this.motDePasse2=Objet.codeHTML(this.motDePasse2);
        this.setIdRegion(request.getParameter("idRegion"));
        this.setIdRegion(Objet.codeHTML(this.idRegion));
        this.idDepartement=request.getParameter("idDepartement");
        this.idDepartement=Objet.codeHTML(this.idDepartement);
        this.idCommune=Integer.parseInt(request.getParameter("idCommune"));
        this.captcha=request.getParameter("captcha").toLowerCase();
        this.captcha=Objet.codeHTML(this.captcha);
    }

    public void verifPostsInfos(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(true);
            this.flagMdp = false;
            if (this.motDePasse.length() > 0) {
                this.flagMdp = true;
                Pattern p = Pattern.compile("[a-zA-Z0-9]+");
                Matcher m = p.matcher(this.motDePasse);
                Matcher m2 = p.matcher(this.motDePasse2);
                if (this.motDePasse.length() < 3) {
                    this.setErrorMsg("Champ NOUVEAU MOT DE PASSE trop court.<br/>");
                } else if (this.motDePasse.length() > 15) {
                    this.setErrorMsg("Champ NOUVEAU MOT DE PASSE trop long.<br/>");
                } else if (m.matches() == false) {
                    this.setErrorMsg("Champ NOUVEAU MOT DE PASSE non-valide (Caractères alphanumériques uniquement).<br/>");
                } else if (this.motDePasse2.length() == 0) {
                    this.setErrorMsg("Champ CONFIRMATION vide.<br/>");
                } else if (this.motDePasse2.length() < 3) {
                    this.setErrorMsg("Champ CONFIRMATION trop court.<br/>");
                } else if (this.motDePasse2.length() > 15) {
                    this.setErrorMsg("Champ CONFIRMATION trop long.<br/>");
                } else if (m2.matches() == false) {
                    this.setErrorMsg("Champ CONFIRMATION non-valide (Caractères alphanumériques uniquement).<br/>");
                }
            }
            if (this.idRegion.equals("0")) {
                this.setErrorMsg("Veuillez spécifier votre RÉGION SVP.<br/>");
            }
            if (this.idDepartement.equals("0")) {
                this.setErrorMsg("Veuillez spécifier votre DÉPARTEMENT SVP.<br/>");
            }
            if (this.idCommune == 0) {
                this.setErrorMsg("Veuillez spécifier votre COMMUNE SVP.<br/>");
            }
            if (this.captcha.length() == 0) {
                this.setErrorMsg("Champ CODE ANTI-ROBOT vide.<br/>");
            } else if (this.captcha.length() > 5) {
                this.setErrorMsg("Champ CODE ANTI-ROBOT trop long.<br/>");
            } else if (session.getAttribute("captcha") == null) {
                this.setErrorMsg("Session CODE ANTI-ROBOT dépassée.<br/>");
            } else if (!session.getAttribute("captcha").toString().equals(Objet.getEncoded(this.captcha))) {
                this.setErrorMsg("Mauvais CODE ANTI-ROBOT.<br/>");
            }
            if (this.getErrorMsg().length() == 0) {
                try {
                    Objet.getConnection();
                    String query = "";
                    PreparedStatement prepare = null;
                    if (this.flagMdp == true) {
                        query = "UPDATE table_membres SET mot_de_passe=?,id_region=?,id_departement=?,id_commune=? WHERE id=?";
                        prepare = Objet.getConn().prepareStatement(query);
                        prepare.setString(1, Objet.getEncoded(this.motDePasse));
                        prepare.setString(2, this.idRegion);
                        prepare.setString(3, this.idDepartement);
                        prepare.setInt(4, this.idCommune);
                        prepare.setLong(5, this.id);
                    } else {
                        query = "UPDATE table_membres SET id_region=?,id_departement=?,id_commune=? WHERE id=?";
                        prepare = Objet.getConn().prepareStatement(query);
                        prepare.setString(1, this.idRegion);
                        prepare.setString(2, this.idDepartement);
                        prepare.setInt(3, this.idCommune);
                        prepare.setLong(4, this.id);
                    }
                    prepare.executeUpdate();
                    prepare.close();
                    Objet.closeConnection();
                    if (this.flagMdp == true) {
                        Mail mail = new Mail(this.email, this.pseudo, "Nouveau mot de passe !");
                        mail.initMailNouveauMdp2(this.pseudo, this.email, this.motDePasse);
                        mail.send();
                    }
                    this.setTest(1);
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(Membre.class.getName()).log(Level.SEVERE, null, ex);
                    this.setErrorMsg("ERREUR INTENE : " + ex.getMessage());
                } catch (NamingException ex) {
                    Logger.getLogger(Membre.class.getName()).log(Level.SEVERE, null, ex);
                    this.setErrorMsg("ERREUR INTENE : " + ex.getMessage());
                } catch (SQLException ex) {
                    Logger.getLogger(Membre.class.getName()).log(Level.SEVERE, null, ex);
                    this.setErrorMsg("ERREUR INTENE : " + ex.getMessage());
                }
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Membre.class.getName()).log(Level.SEVERE, null, ex);
            this.setErrorMsg("ERREUR INTENE : " + ex.getMessage());
        }
    }

    public void getPostsMdpOublie(HttpServletRequest request) {
        this.email=request.getParameter("email");
        this.email=Objet.codeHTML(this.email);
        this.captcha=request.getParameter("captcha");
        this.captcha=Objet.codeHTML(this.captcha).toLowerCase();
    }


    public void verifPostsMdpOublie(HttpServletRequest request) {
        HttpSession session=request.getSession(true);
        try {
            Objet.getConnection();
            Pattern p = Pattern.compile("^[a-z0-9._-]+@[a-z0-9._-]{2,}\\.[a-z]{2,4}$");
            Matcher m = p.matcher(this.email);
            if(this.email.length()==0)
                this.setErrorMsg("Champ ADRESSE EMAIL vide.<br/>");
            else if(this.email.length()>200)
                this.setErrorMsg("Champ ADRESSE EMAIL trop long.<br/>");
            else if(m.matches()==false)
                this.setErrorMsg("Champ ADRESSE EMAIL non-valide.<br/>");
            else if(this.getErrorMsg().length()==0) {
                String query="SELECT COUNT(id) AS nb FROM table_membres WHERE email=?";
                PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                prepare.setString(1, this.email);
                ResultSet result=prepare.executeQuery();
                result.next();
                int nb=result.getInt("nb");
                if(nb==0)
                    this.setErrorMsg("Désolé, cette ADRESSE EMAIL n'est associée à aucun compte.<br/>");
                result.close();
                prepare.close();
            }
            if(this.captcha.length()==0)
                this.setErrorMsg("Champ CODE ANTI-ROBOT vide.<br/>");
            else if(this.captcha.length()!=5)
                this.setErrorMsg("Mauvaise longueur du CODE ANTI-ROBOT.<br/>");
            else if(session.getAttribute("captcha")==null)
                this.setErrorMsg("Session du CODE ANTI-ROBOT dépassée.<br/>");
            else if(!session.getAttribute("captcha").toString().equals(Objet.getEncoded(this.captcha)))
                this.setErrorMsg("Mauvais CODE ANTI-ROBOT.<br/>");
            if(this.getErrorMsg().length()==0) {
                String query="SELECT id,pseudo FROM table_membres WHERE email=? LIMIT 0,1";
                PreparedStatement prepare=Objet.getConn().prepareStatement(query);
                prepare.setString(1, this.email);
                ResultSet result=prepare.executeQuery();
                result.next();
                this.id=result.getLong("id");
                this.pseudo=result.getString("pseudo");
                result.close();
                prepare.close();
                this.motDePasse="";
                for(int i=1;i<=5;i++)
                    this.motDePasse+=Datas.arrayChars[(int)(Math.random()*(double)(Datas.arrayChars.length))];
                query="UPDATE table_membres SET mot_de_passe=? WHERE id=?";
                prepare=Objet.getConn().prepareStatement(query);
                prepare.setString(1, Objet.getEncoded(this.motDePasse));
                prepare.setLong(2, this.id);
                prepare.executeUpdate();
                prepare.close();
                Mail mail=new Mail(this.email, this.pseudo, "Nouveau mot de passe !");
                mail.initMailNouveauMdp(this.pseudo, this.email, this.motDePasse);
                mail.send();
                this.setTest(1);
            }
            Objet.closeConnection();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Membre.class.getName()).log(Level.SEVERE, null, ex);
            this.setErrorMsg("ERREUR INTERNE : "+ex.getMessage()+".<br/>");
        } catch (NamingException ex) {
            Logger.getLogger(Membre.class.getName()).log(Level.SEVERE, null, ex);
            this.setErrorMsg("ERREUR INTERNE : "+ex.getMessage()+".<br/>");
        } catch (SQLException ex) {
            Logger.getLogger(Membre.class.getName()).log(Level.SEVERE, null, ex);
            this.setErrorMsg("ERREUR INTERNE : "+ex.getMessage()+".<br/>");
        }
    }

    void initMembre() throws NamingException, SQLException {
            Objet.getConnection();
            String query="SELECT pseudo,email FROM table_membres WHERE id=? LIMIT 0,1";
            PreparedStatement prepare=Objet.getConn().prepareStatement(query);
            prepare.setLong(1, this.id);
            ResultSet result=prepare.executeQuery();
            result.next();
            this.pseudo=result.getString("pseudo");
            this.email=result.getString("email");
            result.close();
            prepare.close();
            Objet.closeConnection();
     }

    public void supprimeCompte(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession(true);
            Objet.getConnection();
            String query="SELECT id FROM table_annonces WHERE id_membre=?";
            PreparedStatement prepare=Objet.getConn().prepareStatement(query);
            prepare.setLong(1, this.id);
            ResultSet result=prepare.executeQuery();
            while(result.next()) {
                long idAnnonce=result.getLong("id");
                Annonce annonce=new Annonce(idAnnonce);
                annonce.effaceAnnonce2(idAnnonce, this.id);
            }
            result.close();
            prepare.close();
            query="DELETE FROM table_membres WHERE id=?";
            prepare=Objet.getConn().prepareStatement(query);
            prepare.setLong(1, this.id);
            prepare.executeUpdate();
            prepare.close();
            Objet.closeConnection();
            this.setTest(1);
            session.setAttribute("idMembre", null);
            session.setAttribute("pseudo", null);
            Cookie cookie=new Cookie("doncook", "");
            response.addCookie(cookie);
        } catch (NamingException ex) {
            Logger.getLogger(Membre.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Membre.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void blank() {
       super.blank();
        this.pseudo="";
        this.email="";
        this.setIdRegion("0");
        this.idDepartement="0";
        this.idCommune=0;
    }
    /**
     * @return the pseudo
     */
    public String getPseudo() {
        return pseudo;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return the idRegion
     */
    public String getIdRegion() {
        return idRegion;
    }

    /**
     * @return the idDepartement
     */
    public String getIdDepartement() {
        return idDepartement;
    }

    /**
     * @return the idCommune
     */
    public int getIdCommune() {
        return idCommune;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @return the pseudoConnecte
     */
    public String getPseudoConnecte() {
        return pseudoConnecte;
    }

    /**
     * @param idRegion the idRegion to set
     */
    public void setIdRegion(String idRegion) {
        this.idRegion = idRegion;
    }

}
