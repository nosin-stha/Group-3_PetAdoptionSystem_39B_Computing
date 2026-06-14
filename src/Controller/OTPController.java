/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author Dell
 */

import DAO.OtpDAO;
import DAO.UsersDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import model.AdopterData;
import model.SessionData;
import utils.EmailService;
import utils.OTPGenerator;
import view.Login;
import view.SignupWindow;
import view.OTPWindow;
import view.ProviderExtraWindow;

public class OTPController {

    private final OTPWindow otpView;
    private final OtpDAO otpDAO = new OtpDAO();
    private final UsersDAO usersDAO = new UsersDAO();

    public OTPController(OTPWindow otpView) {
        this.otpView = otpView;

        otpView.setLocationRelativeTo(null);
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

    // Send OTP Button - LISTENER
    class SendOtpListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            String email = otpView.getTxtEmail().getText().trim();

            String otp = OTPGenerator.generateOTP();

            boolean saved = otpDAO.saveOtp(email, otp);

            if (!saved) {
                JOptionPane.showMessageDialog(otpView, "Failed to save OTP!");
                return;
            }

            EmailService.sendOTP(email, otp);
            JOptionPane.showMessageDialog(otpView, "OTP sent successfully!");
        }
    }

    // Verify OTP Button - LISTENER
    class VerifyOtpListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            String email = otpView.getTxtEmail().getText().trim();
            String otp = otpView.getTxtOtp().getText().trim();

            String result = otpDAO.verifyOtp(email, otp);

            if ("EXPIRED".equals(result)) {
                JOptionPane.showMessageDialog(otpView, "OTP expired!");
                return;
            }

            if ("INVALID".equals(result)) {
                JOptionPane.showMessageDialog(otpView, "Invalid OTP!");
                return;
            }

            if ("VALID".equals(result)) {

                // OTP for Adopter
                if ("Adopter".equals(SessionData.role)) {

                    AdopterData adopter = new AdopterData();
                    adopter.setUsername(SessionData.username);
                    adopter.setPassword(SessionData.password);
                    adopter.setEmail(SessionData.email);
                    adopter.setPfp(SessionData.imagePath);

                    boolean inserted = usersDAO.insertAdopter(adopter);

                    if (inserted) {
                        otpDAO.deleteOtp(email);

                        JOptionPane.showMessageDialog(otpView, "Adopter registered successfully!");

                        close();
                        
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                for (java.awt.Window w : java.awt.Window.getWindows()) {
                                    if (w instanceof view.SignupWindow) w.dispose();
                                }
                            }
                        });
                        
                        Login login = new Login();
                        new LoginController(login);
                        login.setVisible(true);
                        login.setLocationRelativeTo(null);

                    } else {
                        JOptionPane.showMessageDialog(otpView, "Registration failed!");
                    }
                }

                // OTP for Provider
                else if ("Provider".equals(SessionData.role)) {

                    otpDAO.deleteOtp(email);

                    JOptionPane.showMessageDialog(otpView,
                            "OTP verified. Continue provider registration.");

                    close();

                    ProviderExtraWindow providerExtra = new ProviderExtraWindow();

                    // IMPORTANT FIX: attach controller first
                    new SignUpController(providerExtra);

                    providerExtra.setLocationRelativeTo(null);
                    providerExtra.setVisible(true);
                }
            }
        }
    }
}
