package com.ychen3156;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionDB extends JFrame{
    // create a function to connect with mysql database

    public static Connection getConnection(){
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/goldminerdb", "root", "root1234");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return con;
    }


}
