/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;
import Controller.AdoptionReqController;
import java.awt.event.ActionListener;
/**
 *
 * @author shank
 */

    public class AdoptionRequest extends javax.swing.JFrame {

    /**
     * Creates new form AdoptionRequest
     */
    
//Listeners
    public AdoptionRequest(int petID) {
        initComponents();
    }
    public javax.swing.JPanel getPetPanel(){
        return AdoptionPetPanel;
    }

    public void clearPetPanel(){
        AdoptionPetPanel.removeAll();
    }
    public void refreshPetPanel(){
        AdoptionPetPanel.revalidate();
        AdoptionPetPanel.repaint();
        
    }
    public void addSubmitListener(ActionListener listener){
        btnSubmit.addActionListener(listener);
    }
    
//Getter Method
    public String getFullName(){
        return txtFullName.getText();
    }
    public String getEmail(){
        return txtEmail.getText();
    }    
    public String getAddress(){
        return txtAddress.getText();
    }
    public String getWhatsappNumber(){
        return txtWhatsappNumber.getText();
    }
    public String getReason(){
        return txtReason.getText();
    }
    public javax.swing.JButton getBtnSubmit() { 
        return btnSubmit; }
    

    public javax.swing.JTextField getTxtFullName() { 
        return txtFullName; }
    public javax.swing.JTextField getTxtEmail() { 
        return txtEmail; }
    public javax.swing.JTextField getTxtAddress() { 
        return txtWhatsappNumber; }
    public javax.swing.JTextField getTxtWhatsappNumber() { 
        return txtAddress; }
    public javax.swing.JTextArea getTxtReasonForAdoption() { 
        return txtReason; }   
    public javax.swing.JLabel getLblPetImg(){
        return lblPetImg;}
    
    public javax.swing.JLabel getPetname_petinfopanal()        { return Petname_petinfopanal; }
    public javax.swing.JLabel getPetTyoe_petinfopanal_fill()   { return PetTyoe_petinfopanal_fill; }
    public javax.swing.JLabel getPetAge_petinfopanal_fill()    { return PetAge_petinfopanal_fill; }
    public javax.swing.JLabel getPetGender_petinfopanal_fill() { return PetGender_petinfopanal_fill; }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        txtFullName = new javax.swing.JTextField();
        lblApplyAdoption = new javax.swing.JLabel();
        lblFullname = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        lblEmail = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        txtWhatsappNumber = new javax.swing.JTextField();
        lblWhatsappNumber = new javax.swing.JLabel();
        lblAddress = new javax.swing.JLabel();
        AdoptionPetPanel = new javax.swing.JPanel();
        lblPetImg = new javax.swing.JLabel();
        AdoptionPetPanel_infopanal = new javax.swing.JPanel();
        Petname_petinfopanal = new javax.swing.JLabel();
        Pettype_petinfopanal = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        PetTyoe_petinfopanal_fill = new javax.swing.JLabel();
        PetAge_petinfopanal_fill = new javax.swing.JLabel();
        PetGender_petinfopanal_fill = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtReason = new javax.swing.JTextArea();
        lblReason = new javax.swing.JLabel();
        btnSubmit = new javax.swing.JButton();

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(null);

        txtFullName.addActionListener(this::txtFullNameActionPerformed);
        jPanel3.add(txtFullName);
        txtFullName.setBounds(50, 170, 300, 22);

        lblApplyAdoption.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblApplyAdoption.setText("Apply Adoption");
        jPanel3.add(lblApplyAdoption);
        lblApplyAdoption.setBounds(400, 60, 140, 25);

        lblFullname.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblFullname.setText("Full Name:");
        jPanel3.add(lblFullname);
        lblFullname.setBounds(50, 150, 60, 16);

        txtEmail.addActionListener(this::txtEmailActionPerformed);
        jPanel3.add(txtEmail);
        txtEmail.setBounds(50, 230, 300, 22);

        lblEmail.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblEmail.setText("Email:");
        jPanel3.add(lblEmail);
        lblEmail.setBounds(50, 210, 50, 16);

        txtAddress.addActionListener(this::txtAddressActionPerformed);
        jPanel3.add(txtAddress);
        txtAddress.setBounds(50, 290, 300, 22);

        txtWhatsappNumber.addActionListener(this::txtWhatsappNumberActionPerformed);
        jPanel3.add(txtWhatsappNumber);
        txtWhatsappNumber.setBounds(50, 360, 300, 22);

        lblWhatsappNumber.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblWhatsappNumber.setText(" Phone Number:");
        jPanel3.add(lblWhatsappNumber);
        lblWhatsappNumber.setBounds(50, 340, 110, 16);

        lblAddress.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblAddress.setText("Address:");
        jPanel3.add(lblAddress);
        lblAddress.setBounds(50, 270, 50, 16);

        AdoptionPetPanel.setBackground(new java.awt.Color(255, 204, 102));
        AdoptionPetPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblPetImg.setText("pet pic");

        AdoptionPetPanel_infopanal.setBackground(new java.awt.Color(255, 204, 102));

        javax.swing.GroupLayout AdoptionPetPanel_infopanalLayout = new javax.swing.GroupLayout(AdoptionPetPanel_infopanal);
        AdoptionPetPanel_infopanal.setLayout(AdoptionPetPanel_infopanalLayout);
        AdoptionPetPanel_infopanalLayout.setHorizontalGroup(
            AdoptionPetPanel_infopanalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        AdoptionPetPanel_infopanalLayout.setVerticalGroup(
            AdoptionPetPanel_infopanalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 122, Short.MAX_VALUE)
        );

        Petname_petinfopanal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        Pettype_petinfopanal.setText("Type:");

        jLabel3.setText("Age");

        jLabel1.setText("Gender");

        PetTyoe_petinfopanal_fill.setText("Pet Type");

        PetAge_petinfopanal_fill.setText("Pet Age");

        PetGender_petinfopanal_fill.setText("Pet Gender");

        javax.swing.GroupLayout AdoptionPetPanelLayout = new javax.swing.GroupLayout(AdoptionPetPanel);
        AdoptionPetPanel.setLayout(AdoptionPetPanelLayout);
        AdoptionPetPanelLayout.setHorizontalGroup(
            AdoptionPetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AdoptionPetPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblPetImg, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AdoptionPetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(AdoptionPetPanelLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(AdoptionPetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1)
                            .addComponent(Pettype_petinfopanal, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(76, 76, 76)
                        .addGroup(AdoptionPetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PetGender_petinfopanal_fill, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PetTyoe_petinfopanal_fill, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(PetAge_petinfopanal_fill, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(AdoptionPetPanelLayout.createSequentialGroup()
                        .addComponent(Petname_petinfopanal, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 178, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AdoptionPetPanel_infopanal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        AdoptionPetPanelLayout.setVerticalGroup(
            AdoptionPetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AdoptionPetPanelLayout.createSequentialGroup()
                .addGroup(AdoptionPetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(AdoptionPetPanelLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(AdoptionPetPanel_infopanal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(AdoptionPetPanelLayout.createSequentialGroup()
                        .addGroup(AdoptionPetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(AdoptionPetPanelLayout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(lblPetImg, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(AdoptionPetPanelLayout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(Petname_petinfopanal, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(AdoptionPetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(Pettype_petinfopanal)
                                    .addComponent(PetTyoe_petinfopanal_fill))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(AdoptionPetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(PetAge_petinfopanal_fill))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(AdoptionPetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(PetGender_petinfopanal_fill))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel3.add(AdoptionPetPanel);
        AdoptionPetPanel.setBounds(30, 450, 420, 140);

        txtReason.setColumns(20);
        txtReason.setRows(5);
        jScrollPane1.setViewportView(txtReason);

        jPanel3.add(jScrollPane1);
        jScrollPane1.setBounds(540, 180, 400, 180);

        lblReason.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblReason.setText("Reason for Adoption:");
        jPanel3.add(lblReason);
        lblReason.setBounds(550, 150, 120, 16);

        btnSubmit.setBackground(new java.awt.Color(255, 204, 0));
        btnSubmit.setText("Submit");
        btnSubmit.addActionListener(this::btnSubmitActionPerformed);
        jPanel3.add(btnSubmit);
        btnSubmit.setBounds(720, 480, 80, 23);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtFullNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFullNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFullNameActionPerformed

    private void txtWhatsappNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtWhatsappNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtWhatsappNumberActionPerformed

    private void txtAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAddressActionPerformed

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSubmitActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AdoptionPetPanel;
    private javax.swing.JPanel AdoptionPetPanel_infopanal;
    private javax.swing.JLabel PetAge_petinfopanal_fill;
    private javax.swing.JLabel PetGender_petinfopanal_fill;
    private javax.swing.JLabel PetTyoe_petinfopanal_fill;
    private javax.swing.JLabel Petname_petinfopanal;
    private javax.swing.JLabel Pettype_petinfopanal;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAddress;
    private javax.swing.JLabel lblApplyAdoption;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblFullname;
    private javax.swing.JLabel lblPetImg;
    private javax.swing.JLabel lblReason;
    private javax.swing.JLabel lblWhatsappNumber;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFullName;
    private javax.swing.JTextArea txtReason;
    private javax.swing.JTextField txtWhatsappNumber;
    // End of variables declaration//GEN-END:variables
    
}
