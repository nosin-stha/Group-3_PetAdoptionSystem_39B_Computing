package Controller;

import DAO.ProviderDetailsDAO;
import DAO.ReportDAO;
import model.ProviderData;
import model.SessionData;
import view.ReportPetProvider;
import view.ShelterCard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ReportPetProviderController {

    private final ReportPetProvider view;
    private final int providerID;
    private ButtonGroup radioGroup;

    // ✅ Constructor used when opening directly with a known providerID
    public ReportPetProviderController(ReportPetProvider view, int providerID) {
        this.view = view;
        this.providerID = providerID;

        setupRadioGroup();
        loadProviderDetails();
        attachListeners();
    }

    
    public static void attachToShelterCard(ShelterCard card, int providerID) {
        card.addReportListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openReportPage(providerID);
            }
        });
    }

    
    private static void openReportPage(int providerID) {
        ReportPetProvider reportView = new ReportPetProvider();
        new ReportPetProviderController(reportView, providerID);
        reportView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        reportView.setAlwaysOnTop(true);
        reportView.setLocationRelativeTo(null);
        reportView.setVisible(true);
    }

    private void setupRadioGroup() {
        radioGroup = new ButtonGroup();
        radioGroup.add(view.getRadioButton1());
        radioGroup.add(view.getRadioButton2());
        radioGroup.add(view.getRadioButton3());
        radioGroup.add(view.getRadioButton4());
        radioGroup.add(view.getRadioButton5());
    }

    private String getSelectedReason() {
        if (view.getRadioButton1().isSelected()) return view.getRadioButton1().getText().trim();
        if (view.getRadioButton2().isSelected()) return view.getRadioButton2().getText().trim();
        if (view.getRadioButton3().isSelected()) return view.getRadioButton3().getText().trim();
        if (view.getRadioButton4().isSelected()) return view.getRadioButton4().getText().trim();
        if (view.getRadioButton5().isSelected()) return view.getRadioButton5().getText().trim();
        return null;
    }

    private void clearRadioSelection() {
        radioGroup.clearSelection();
    }

    private void loadProviderDetails() {
        ProviderDetailsDAO dao = new ProviderDetailsDAO();
        ProviderData provider = dao.getProviderById(providerID);

        if (provider != null) {
            view.getlblShelterName().setText(provider.getShelterName());
            view.getlblPhoneNo_fill().setText(provider.getPhoneNumber());
            view.getlblEmail_fill().setText(provider.getEmail());
            view.getlblAddress_fill().setText(provider.getAddress());
            loadShelterProfilePicture(provider.getPfp());
        }
    }

    private void loadShelterProfilePicture(String imagePath) {
        JLabel profileLabel = view.getShelterPfp();

        if (imagePath != null && !imagePath.trim().isEmpty()) {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                ImageIcon icon = new ImageIcon(imagePath);
                Image scaled = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                profileLabel.setIcon(new ImageIcon(scaled));
                profileLabel.setText("");
            } else {
                setDefaultShelterIcon(profileLabel);
            }
        } else {
            setDefaultShelterIcon(profileLabel);
        }
    }

    private void setDefaultShelterIcon(JLabel profileLabel) {
        java.net.URL imgURL = getClass().getResource("/Images/default_shelter.png");
        if (imgURL != null) {
            ImageIcon defaultIcon = new ImageIcon(imgURL);
            Image scaled = defaultIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
            profileLabel.setIcon(new ImageIcon(scaled));
        } else {
            profileLabel.setText("No Image");
        }
    }

    private void attachListeners() {
        view.addSubmitListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
            }
        });
    }

    private void handleSubmit() {
    String reason = getSelectedReason();

    if (reason == null) {
        JOptionPane.showMessageDialog(view, "Please select a reason before submitting.");
        return;
    }

    int adopterID = SessionData.userID;

    ReportDAO dao = new ReportDAO();
    boolean success = dao.insertReport(adopterID, providerID, reason);

    if (success) {
        JOptionPane.showMessageDialog(view, "Report submitted successfully.");
        clearRadioSelection();

        // ── EMAIL: notify provider they have been reported ──
        ProviderDetailsDAO pDao = new ProviderDetailsDAO();
        ProviderData provider   = pDao.getProviderById(providerID);
        if (provider != null) {
            new Thread(() ->
                utils.EmailService.sendProviderReported(
                    provider.getEmail(),
                    provider.getShelterName(),
                    reason
                )
            ).start();
        }

        view.dispose();
    } else {
        JOptionPane.showMessageDialog(view, "Error submitting report. Please try again.");
    }
}
}