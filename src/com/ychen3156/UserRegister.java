package com.ychen3156;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserRegister extends JFrame{

    Bg bg =new Bg();

    private static final long serialVersionUID = 1L;
    private JButton jButton_CANCEL;
    private JButton jButton_REG;
    private JLabel label;
    private JPanel contentPane;
    public void UserRegister() {
        //Creating the Frame
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//<-- destroy only this frame
        setBounds(400, 190, 600, 270);

        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Register");
        lblNewLabel.setForeground(Color.BLACK);
        lblNewLabel.setHorizontalAlignment((JLabel.CENTER));
        lblNewLabel.setSize(100,300);
        lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        lblNewLabel.setBounds(200, 13, 200, 70);
        contentPane.add(lblNewLabel);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setBackground(Color.BLACK);
        lblUsername.setForeground(Color.BLACK);
        lblUsername.setBounds(100, 80, 100, 20);
        contentPane.add(lblUsername);

        final JTextField jTextField_acct = new JTextField();
        jTextField_acct.setBounds(260, 80, 100, 20);
        contentPane.add(jTextField_acct);
        jTextField_acct.setColumns(10);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setForeground(Color.BLACK);
        lblPassword.setBackground(Color.CYAN);
        lblPassword.setBounds(100, 120, 100, 20);
        contentPane.add(lblPassword);

        final JPasswordField jPasswordField_pw = new JPasswordField();
        jPasswordField_pw.setBounds(260, 120, 100, 20);
        contentPane.add(jPasswordField_pw);

        JLabel lblRePassword = new JLabel("Confirm Password");
        lblRePassword.setForeground(Color.BLACK);
        lblRePassword.setBackground(Color.CYAN);
        lblRePassword.setBounds(100, 160, 140, 20);
        contentPane.add(lblRePassword);

        final JPasswordField jPasswordField_rePw = new JPasswordField();
        jPasswordField_rePw.setBounds(260, 160, 100, 20);
        contentPane.add(jPasswordField_rePw);


        jButton_REG = new JButton("Register");
        jButton_REG.setBounds(410, 120, 100, 20);
        jButton_REG.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String acct = jTextField_acct.getText();
                String pw = String.valueOf(jPasswordField_pw.getPassword());
                String rePw = String.valueOf(jPasswordField_rePw.getPassword());
                if(acct.equals(""))
                {
                    JOptionPane.showMessageDialog(null, "Add An UserName");
                }
                else if(pw.equals(""))
                {
                    JOptionPane.showMessageDialog(null, "Add A Password");
                }
                else if(!pw.equals(rePw))
                {
                    JOptionPane.showMessageDialog(null, "Retype The Password Again");
                }

                else if(checkUsername(acct))
                {
                    JOptionPane.showMessageDialog(null, "This Username Already Exist");
                }

                else{
                    PreparedStatement ps;
                    String query = "INSERT INTO `USERS`(`acct`, `password`) VALUES (?,?)";

                    try {
                        ps = ConnectionDB.getConnection().prepareStatement(query);
                        ps.setString(1, acct);
                        ps.setString(2, pw);
                        if(ps.executeUpdate() > 0)
                        {
                            JOptionPane.showMessageDialog(null, "New User Added");

                            //
                            GameWin.usrName=acct;
                            GameWin.gameState=1;
                            bg.startTime = System.currentTimeMillis();
                            System.out.println("UL: Welcome "+ acct);
                            dispose();
                        }

                    } catch (SQLException ex) {
                        Logger.getLogger(UserRegister.class.getName()).log(Level.SEVERE, null, ex);

                    }
                }
            }
            });

        jButton_CANCEL = new JButton("Cancel");
        jButton_CANCEL.setBounds(410, 160, 100, 20);
        jButton_CANCEL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        contentPane.add(jButton_REG);
        contentPane.add(jButton_CANCEL);

        label = new JLabel("");
        label.setBounds(0, 0, 1008, 562);
        contentPane.add(label);

    }
    // ----------- REGISTERATION FORM
    // function to check if the username already exist in database table
    public boolean checkUsername(String username)
    {
        PreparedStatement ps;
        ResultSet rs;
        boolean checkUser = false;
        String query = "SELECT * FROM `Users` WHERE `acct` =?";

        try {
            ps = ConnectionDB.getConnection().prepareStatement(query);
            ps.setString(1, username);

            rs = ps.executeQuery();

            if(rs.next())
            {
                checkUser = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserRegister.class.getName()).log(Level.SEVERE, null, ex);
        }
        return checkUser;
    }


}
