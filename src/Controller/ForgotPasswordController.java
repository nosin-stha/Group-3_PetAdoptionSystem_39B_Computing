package Controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Dell
 */

import DAO.OtpDAO;
import DAO.ForgotPasswordDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import model.SessionData;
import view.OTPWindow;
import view.NewPassword;
import utils.EmailService;
import utils.OTPGenerator;

public class ForgotPasswordController {

    private final OTPWindow otpView;
    private final OtpDAO otpDAO = new OtpDAO();
    private final ForgotPasswordDAO forgotDAO = new ForgotPasswordDAO();

    public ForgotPasswordController(OTPWindow otpView) {
    this.otpView = otpView;
    otpView.setLocationRelativeTo(null);

    otpView.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);

    otpView.getTxtEmail().setEditable(true);
    otpView.getTxtEmail().setText("");

    otpView.addSendOtpListener(new SendOtpListener());
    otpView.addVerifyOtpListener(new VerifyOtpListener());
}

    public void open() {
        otpView.setLocationRelativeTo(null);
        otpView.setVisible(true);
    }

    public void close() {
        otpView.dispose();
    }

    // ── Send OTP ─────────────────────────────────────────────────────────────
    class SendOtpListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            String email = otpView.getTxtEmail().getText().trim();

            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(otpView, "Please enter your email address.");
                return;
            }

            if (!forgotDAO.isEmailExist(email)) {
                JOptionPane.showMessageDialog(otpView, "No account found with that email address.");
                return;
            }

            String otp = OTPGenerator.generateOTP();
            boolean saved = otpDAO.saveOtp(email, otp);

            if (!saved) {
                JOptionPane.showMessageDialog(otpView, "Failed to save OTP. Please try again.");
                return;
            }

            SessionData.role = "ForgotPassword";
            EmailService.sendOTP(email, otp);
            JOptionPane.showMessageDialog(otpView, "OTP sent to " + email + ". It expires in 5 minutes.");
        }
    }

    // ── Verify OTP ────────────────────────────────────────────────────────────
    class VerifyOtpListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            String email = otpView.getTxtEmail().getText().trim();
            String otp   = otpView.getTxtOtp().getText().trim();

            if (otp.isEmpty()) {
                JOptionPane.showMessageDialog(otpView, "Please enter the OTP.");
                return;
            }

            String result = otpDAO.verifyOtp(email, otp);

            switch (result) {
                case "EXPIRED":
                    JOptionPane.showMessageDialog(otpView, "OTP has expired. Please request a new one.");
                    break;

                case "INVALID":
                    JOptionPane.showMessageDialog(otpView, "Invalid OTP. Please check and try again.");
                    break;

                case "VALID":
                    otpDAO.deleteOtp(email);
                    SessionData.role = null;
                    JOptionPane.showMessageDialog(otpView, "OTP verified! Please set your new password.");
                    close();

                    String verifiedEmail = email;
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        NewPassword newPassView = new NewPassword();
                        new NewPasswordController(newPassView, verifiedEmail, true);
                        newPassView.setLocationRelativeTo(null);
                        newPassView.setVisible(true);
                    });
                    break;

                default:
                    JOptionPane.showMessageDialog(otpView, "Verification failed. Please try again.");
            }
        }
    }
}