package Controller;

import DAO.RequestToAdminDAO;
import view.RequestToAdminForm;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RequestToAdminController {

    private final RequestToAdminForm view;
    private final RequestToAdminDAO  dao;

    private static final String[] REASONS = {
        "Information has been corrected and verified",
        "Behaviour was misunderstood or taken out of context",
        "I was temporarily unavailable but now responsive",
        "Listings have been updated and are now accurate",
        "No fraudulent activity; all interactions are genuine"
    };

    public RequestToAdminController(RequestToAdminForm view, RequestToAdminDAO dao) {
        this.view = view;
        this.dao  = dao;
        enforceRadioButtonGroup();
        attachListeners();
    }

    private void enforceRadioButtonGroup() {
        ButtonGroup group = new ButtonGroup();
        group.add(view.getRadioButton1());
        group.add(view.getRadioButton2());
        group.add(view.getRadioButton3());
        group.add(view.getRadioButton4());
        group.add(view.getRadioButton5());
    }

    private void attachListeners() {
        view.addSubmitListener(this::handleSubmit);
    }

    private void handleSubmit(ActionEvent e) {

        String email    = view.getEmail().trim();
        String password = new String(view.getPassword().getPassword()).trim();
        String reason   = getSelectedReason();

     
        if (email.isEmpty() && password.isEmpty() && reason == null) {
            showError("All fields are required.\nPlease enter your email, password, and select a reason.");
            return;
        }
        if (email.isEmpty()) {
            showError("Email field is empty.\nPlease enter your registered email address.");
            return;
        }
        if (password.isEmpty()) {
            showError("Password field is empty.\nPlease enter your password.");
            return;
        }
        if (reason == null) {
            showError("No reason selected.\nPlease select one reason for your recovery request.");
            return;
        }

        
        if (!email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
            showError("Invalid email format.\nPlease enter a valid email address.");
            return;
        }

       
        
        int accountStatus = dao.getAccountStatus(email, password);
        
        if (accountStatus == -1) {
            showError("Invalid credentials.\nNo account was found with the provided email and password.");
            return;
        }

        if (accountStatus == 0) {
            showError("Your account has not been disabled.\nOnly disabled accounts can submit a recovery request.");
            return;
        }


        int providerID = dao.getDisabledProviderID(email, password);

        
        if (dao.hasPendingRequest(providerID)) {
            showError("You already have a pending recovery request.\nPlease wait for the admin to review it.");
            return;
        }

    
        boolean success = dao.insertRecoverRequest(providerID, reason);
        if (success) {
            JOptionPane.showMessageDialog(
                view,
                "Your recovery request has been submitted successfully.\nThe admin will review it shortly.",
                "Request Submitted",
                JOptionPane.INFORMATION_MESSAGE
            );
            view.dispose();
        } else {
            showError("Something went wrong while submitting your request.\nPlease try again later.");
        }
    }

    private String getSelectedReason() {
        JRadioButton[] buttons = {
            view.getRadioButton1(),
            view.getRadioButton2(),
            view.getRadioButton3(),
            view.getRadioButton4(),
            view.getRadioButton5()
        };

        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].isSelected()) {
                return REASONS[i];
            }
        }
        return null;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
            view,
            message,
            "Validation Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}