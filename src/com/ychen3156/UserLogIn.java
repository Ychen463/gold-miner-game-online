package com.ychen3156;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserLogIn extends JFrame{
    Bg bg =new Bg();
    int gameState;
    String usrName;

    private static final long serialVersionUID = 1L;
    private JTextField jTextField_acct;
    private JPasswordField jPasswordField_pw;
    private JButton jButton_LOGIN;
    private JButton jButton_REG;
    private JButton jButton_CANCEL;
    private JLabel label;
    private JPanel contentPane;

    UserLogIn(){
        super("Log In");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//<-- destroy only this frame
        setBounds(400, 190, 600, 270);

        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Login");
        lblNewLabel.setForeground(Color.BLACK);
        lblNewLabel.setHorizontalAlignment((JLabel.CENTER));
        lblNewLabel.setSize(100,300);
        lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        lblNewLabel.setBounds(200, 13, 200, 70);
        contentPane.add(lblNewLabel);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setBackground(Color.BLACK);
        lblUsername.setForeground(Color.BLACK);
        lblUsername.setBounds(130, 100, 100, 20);
        contentPane.add(lblUsername);

        jTextField_acct = new JTextField();
        jTextField_acct.setBounds(200, 100, 100, 20);
        contentPane.add(jTextField_acct);
        jTextField_acct.setColumns(10);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setForeground(Color.BLACK);
        lblPassword.setBackground(Color.CYAN);
        lblPassword.setBounds(130, 140, 100, 20);
        contentPane.add(lblPassword);

        jPasswordField_pw = new JPasswordField();
        jPasswordField_pw.setBounds(200, 140, 100, 20);
        contentPane.add(jPasswordField_pw);



        jButton_LOGIN = new JButton("Log In");
        jButton_LOGIN.setBounds(350, 100, 100, 20);
        jButton_LOGIN.addActionListener(new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent e) {
                PreparedStatement ps;
                ResultSet rs;
                String uname = jTextField_acct.getText();
                String pass = String.valueOf(jPasswordField_pw.getPassword());
                String query = "SELECT * FROM `Users` WHERE `acct` =? AND `password` =?";

                try {
                    ps = ConnectionDB.getConnection().prepareStatement(query);

                    ps.setString(1, uname);
                    ps.setString(2, pass);

                    rs = ps.executeQuery();

                    if(rs.next())
                    {
                        GameWin.usrName=uname;
                        GameWin.gameState=1;
                        bg.startTime = System.currentTimeMillis();
                        System.out.println("UL: Welcome "+ uname);
                        dispose();
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Incorrect Username Or Password", "Login Failed", 2);
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(UserLogIn.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        // Register
        jButton_REG = new JButton("Register");
        jButton_REG.setBounds(350, 140, 100, 20);
        jButton_REG.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserRegister userRegister = new UserRegister();
                userRegister.UserRegister();
                userRegister.setVisible(true);
                dispose();
            }

        });

        jButton_CANCEL = new JButton("Cancel");
        jButton_CANCEL.setBounds(250, 180, 100, 20);
        jButton_CANCEL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        contentPane.add(jButton_CANCEL);

        contentPane.add(jButton_LOGIN);
        contentPane.add(jButton_REG);

        label = new JLabel("");
        label.setBounds(0, 0, 1008, 562);
        contentPane.add(label);
    }

    public String getUsrName(){
        return this.usrName;
    }

}




