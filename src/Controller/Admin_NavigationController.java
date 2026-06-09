/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author Dell
 */

import view.AdminAllAccountStatus;
import view.AdminReportedAccountManagement;
// import view.AdminUnfreezeRequestManagement;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class Admin_NavigationController {

    private JFrame currentFrame;

    public Admin_NavigationController(JFrame currentFrame) {
        this.currentFrame = currentFrame;
        attachListeners();
    }

    private void attachListeners() {

        if (currentFrame instanceof AdminAllAccountStatus allAccounts) {

            allAccounts.addAllAccountsListener(new AllAccountsListener());
            allAccounts.addReportsListener(new ReportsListener());

            // allAccounts.addUnfreezeRequestsListener(new UnfreezeRequestsListener());

            allAccounts.addLogoutListener(new LogoutListener());

        } else if (currentFrame instanceof AdminReportedAccountManagement reports) {

            reports.addAllAccountsListener(new AllAccountsListener());
            reports.addReportsListener(new ReportsListener());

            // reports.addUnfreezeRequestsListener(new UnfreezeRequestsListener());

            reports.addLogoutListener(new LogoutListener());

        }

        /*
        else if (currentFrame instanceof AdminUnfreezeRequestManagement unfreeze) {

            unfreeze.addAllAccountsListener(new AllAccountsListener());
            unfreeze.addReportsListener(new ReportsListener());
            unfreeze.addUnfreezeRequestsListener(new UnfreezeRequestsListener());
            unfreeze.addLogoutListener(new LogoutListener());
        }
        */
    }

    public class AllAccountsListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (currentFrame instanceof AdminAllAccountStatus)
                return;

            AdminAllAccountStatus page = new AdminAllAccountStatus();

            new Admin_NavigationController(page);
            new AdminAllAccountsController(page, new database.MySqlConnector().openConnection());

            page.setLocationRelativeTo(null);
            page.setVisible(true);

            currentFrame.dispose();
        }
    }

    public class ReportsListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (currentFrame instanceof AdminReportedAccountManagement)
                return;

            AdminReportedAccountManagement page =
                    new AdminReportedAccountManagement();

            new Admin_NavigationController(page);

            page.setLocationRelativeTo(null);
            page.setVisible(true);

            currentFrame.dispose();
        }
    }

    /*
    public class UnfreezeRequestsListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (currentFrame instanceof AdminUnfreezeRequestManagement)
                return;

            AdminUnfreezeRequestManagement page =
                    new AdminUnfreezeRequestManagement();

            new Admin_NavigationController(page);

            page.setLocationRelativeTo(null);
            page.setVisible(true);

            currentFrame.dispose();
        }
    }
    */

    public class LogoutListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            int confirm = javax.swing.JOptionPane.showConfirmDialog(
                    currentFrame,
                    "Are you sure you want to logout?",
                    "Logout",
                    javax.swing.JOptionPane.YES_NO_OPTION
            );

            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                new SessionController().logout(currentFrame);
            }
        }
    }
}
