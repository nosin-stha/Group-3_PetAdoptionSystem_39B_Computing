/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import model.SessionData;
/**
 *
 * @author Dell
 */

public class EmailService {

    private static final String SENDER_EMAIL    = "pawsclawsfinder@gmail.com";
    private static final String SENDER_PASSWORD = "fcev uxpm feer esro";


    private static Session buildSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host",            "smtp.gmail.com");
        props.put("mail.smtp.port",            "587");
        props.put("mail.smtp.auth",            "true");
        props.put("mail.smtp.starttls.enable", "true");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });
    }

    // ── Shared send helper ───────────────────────────────────────────────────
    private static void send(String to, String subject, String body) {
        try {
            Message message = new MimeMessage(buildSession());
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
            System.out.println("Email sent to: " + to);
        } catch (MessagingException e) {
            System.out.println("Email send error: " + e.getMessage());
        }
    }

    // 1. OTP

    public static void sendOTP(String receiverEmail, String otp) {
        String body;

        if (SessionData.role == null || SessionData.role.equalsIgnoreCase("ForgotPassword")) {
            body =
                "Hi,\n\n" +
                "You requested a password reset for your Paws & Claws Finder account.\n\n" +
                "Your OTP is: " + otp + "\n\n" +
                "This OTP expires in 5 minutes.\n" +
                "If you did not request this, please ignore this email.\n\n" +
                "— Paws & Claws Finder Team";

        } else if (SessionData.role.equalsIgnoreCase("Provider")) {
            body =
                "Hi Provider,\n\n" +
                "Thank you for joining Paws & Claws Finder.\n" +
                "We appreciate your support in helping pets find loving homes.\n\n" +
                "Your OTP is: " + otp + "\n\n" +
                "— Paws & Claws Finder Team";

        } else {
            body =
                "Hi Adopter,\n\n" +
                "Thank you for using Paws & Claws Finder.\n" +
                "We look forward to helping you find a reliable pet companion.\n\n" +
                "Your OTP is: " + otp + "\n\n" +
                "— Paws & Claws Finder Team";
        }

        send(receiverEmail, "Paws & Claws Finder — OTP Verification", body);
    }

   
    // 2. Adoption request ACCEPTED — notify adopter
    
    public static void sendAdoptionAccepted(
            String adopterEmail,
            String adopterName,
            String petName,
            String petType,
            String petGender,
            String petAge,
            String shelterName) {

        String subject = "Your Adoption Request for " + petName + " has been Accepted!";

        String body =
            "Hi " + adopterName + ",\n\n" +
            "Great news! Your adoption request has been ACCEPTED by " + shelterName + ".\n\n" +
            "────────────────────────\n" +
            "Pet Details\n" +
            "────────────────────────\n" +
            "Name   : " + petName   + "\n" +
            "Type   : " + petType   + "\n" +
            "Gender : " + petGender + "\n" +
            "Age    : " + petAge    + "\n" +
            "────────────────────────\n\n" +
            "Please contact " + shelterName + " to arrange the next steps.\n\n" +
            "Thank you for choosing to adopt!\n\n" +
            "— Paws & Claws Finder Team";

        send(adopterEmail, subject, body);
    }

    
    // 3. Adoption request DECLINED — notify adopter
   
    public static void sendAdoptionDeclined(
            String adopterEmail,
            String adopterName,
            String petName,
            String petType,
            String petGender,
            String petAge,
            String shelterName) {

        String subject = "Update on Your Adoption Request for " + petName;

        String body =
            "Hi " + adopterName + ",\n\n" +
            "We're sorry to inform you that your adoption request for the pet below " +
            "has been DECLINED by " + shelterName + ".\n\n" +
            "────────────────────────\n" +
            "Pet Details\n" +
            "────────────────────────\n" +
            "Name   : " + petName   + "\n" +
            "Type   : " + petType   + "\n" +
            "Gender : " + petGender + "\n" +
            "Age    : " + petAge    + "\n" +
            "────────────────────────\n\n" +
            "Don't be discouraged — there are many pets still looking for a loving home.\n\n" +
            "— Paws & Claws Finder Team";

        send(adopterEmail, subject, body);
    }

    
    // 4. Provider REPORTED — notify provider

    public static void sendProviderReported(
            String providerEmail,
            String shelterName,
            String reportReason) {

        String subject = "You Have Received a Report — Paws & Claws Finder";

        String body =
            "Hi " + shelterName + ",\n\n" +
            "A report has been submitted against your shelter on Paws & Claws Finder.\n\n" +
            "────────────────────────\n" +
            "Report Reason\n" +
            "────────────────────────\n" +
            reportReason + "\n" +
            "────────────────────────\n\n" +
            "Our admin team will review this report. If you believe this is a mistake, " +
            "please ensure your listings and conduct comply with our community guidelines.\n\n" +
            "— Paws & Claws Finder Team";

        send(providerEmail, subject, body);
    }


    // Admin DISABLED provider account — notify provider
    
    public static void sendAccountDisabled(
            String providerEmail,
            String shelterName) {

        String subject = "Your Account Has Been Disabled — Paws & Claws Finder";

        String body =
            "Hi " + shelterName + ",\n\n" +
            "After reviewing reports submitted against your account, " +
            "our admin team has DISABLED your Paws & Claws Finder account.\n\n" +
            "This means you will no longer be able to log in or manage listings.\n\n" +
            "If you believe this was a mistake, please submit an Unfreeze Request " +
            "through the login page.\n\n" +
            "— Paws & Claws Finder Team";

        send(providerEmail, subject, body);
    }

    
    // Admin ACCEPTED unfreeze request — notify provider
    
    public static void sendUnfreezeAccepted(
            String providerEmail,
            String shelterName) {

        String subject = "Your Unfreeze Request Has Been Accepted — Paws & Claws Finder";

        String body =
            "Hi " + shelterName + ",\n\n" +
            "Good news! Our admin team has ACCEPTED your unfreeze request.\n\n" +
            "Your account has been reactivated. You can now log in and manage " +
            "your listings again.\n\n" +
            "Please ensure your future activity complies with our community guidelines " +
            "to avoid further action.\n\n" +
            "— Paws & Claws Finder Team";

        send(providerEmail, subject, body);
    }

   
    // Admin DENIED unfreeze request — notify provider
   
    public static void sendUnfreezeDenied(
            String providerEmail,
            String shelterName) {

        String subject = "Your Unfreeze Request Has Been Denied — Paws & Claws Finder";

        String body =
            "Hi " + shelterName + ",\n\n" +
            "We regret to inform you that your unfreeze request has been DENIED " +
            "by our admin team.\n\n" +
            "Your account will remain disabled at this time.\n\n" +
            "If you have further questions, please contact our support team.\n\n" +
            "— Paws & Claws Finder Team";

        send(providerEmail, subject, body);
    }
}