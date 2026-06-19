package Controller;
import DAO.AdopterProfileDAO;
import model.SessionData;
import view.AdopterProfileUpdate;
import javax.swing.JOptionPane;

public class AdopterProfileUpdateController {
    private final AdopterProfileUpdate view;
    private final AdopterProfileDAO dao;
    
    public AdopterProfileUpdateController(AdopterProfileUpdate view){
        this.view = view;
        this.dao = new AdopterProfileDAO();
        attachListeners();
    }
    
    private void attachListeners(){
        for (java.awt.event.ActionListener al : view.getBtnSave().getActionListeners()) {
            view.getBtnSave().removeActionListener(al);
        }
        view.addSaveListener(e -> handleSave());
    }
    
    private void handleSave(){
        String username = view.getUsername().trim();
        String email = view.getEmail().trim();
        
        if(username.isEmpty() || email.isEmpty()){
            JOptionPane.showMessageDialog(view, "All fields are required.");
            return;
        }
        
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";        
        if(!email.matches(emailRegex)){
            JOptionPane.showMessageDialog(view,
                "Invalid email format.\nExample: example@gmail.com");
            return;
        }
        
        boolean success = dao.updateAdopterProfile(
                SessionData.userID,
                username,
                email
        );
        
        if(success){
            JOptionPane.showMessageDialog(view, "Profile updated successfully.");
            view.dispose();
            SessionData.username = username;
        } else {
            JOptionPane.showMessageDialog(view, "Error updating profile.");
        }
    }
}