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
import model.AdopterData;
import model.SessionData;

import utils.EmailService;
import utils.OTPGenerator;
import view.OTPWindow;
import view.ProviderExtraWindow;

public class OTPController {
    
    
    private final OTPWindow otpView;
    private final OtpDAO otpDAO = new OtpDAO();
    private final UsersDAO usersDAO = new UsersDAO();

    // Otp window button click listener
    
    public OTPController(OTPWindow otpView) {
        this.otpView = otpView;
        
        otpView.setLocationRelativeTo(null);
        
        otpView.addSendOtpListener(new SendOtpListener());
        otpView.addVerifyOtpListener(new VerifyOtpListener());
    }

    // Open OTP Window 
    public void open() {

        otpView.setVisible(true);
    }

    // on click sent otp button actions to send otp
    class SendOtpListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String email = otpView.getTxtEmail().getText().trim();

            // generate otp
            String otp = OTPGenerator.generateOTP();

            // saveOTP 
            boolean saved = otpDAO.saveOtp(email, otp);
            if (!saved) {
                JOptionPane.showMessageDialog(otpView,"Failed to save OTP!");
                return;
            }

            // send otp to email
            EmailService.sendOTP(email, otp);          
            JOptionPane.showMessageDialog(otpView, "OTP Sent Successfully!");
        }
    }

    // on click verify email button actions to verify
    class VerifyOtpListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String email = otpView.getTxtEmail().getText().trim();
            String otp = otpView.getTxtOtp().getText().trim();

            String result = otpDAO.verifyOtp(email, otp);

            if (result.equals("EXPIRED")) {
                JOptionPane.showMessageDialog(otpView,"OTP has expired! Please request a new one.");
                return;
            }

            if (result.equals("INVALID")) {
                JOptionPane.showMessageDialog(otpView,"Invalid OTP!");
                return;
            }

            if (result.equals("VALID")) {

                if (SessionData.role.equals("Adopter")) {

                    AdopterData adopter = new AdopterData();

                    adopter.setUsername(SessionData.username);
                    adopter.setPassword(SessionData.password);
                    adopter.setEmail(SessionData.email);
                    adopter.setPfp(SessionData.imagePath);

                    boolean inserted = usersDAO.insertAdopter(adopter);

                    if (inserted) {
                        otpDAO.deleteOtp(email);
                        JOptionPane.showMessageDialog(otpView,"You successfully registered as an adopter!");
                        otpView.dispose();
                    } else {
                        JOptionPane.showMessageDialog(otpView,"Registration Failed!");
                    }
                }else if (SessionData.role.equals("Provider")) {
                    ProviderExtraWindow providerExtra = new ProviderExtraWindow();
                    SignUpController controller = new SignUpController(providerExtra);
                    providerExtra.setLocationRelativeTo(null);
                    
                    // OPEN USING CONTROLLER
                    controller.open();

                   // close otp window
                   otpView.dispose();
                }
            }
        }
    }
}
