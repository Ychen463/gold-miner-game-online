package com.ychen3156;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Shop {
    Bg bg =new Bg();
    int numWater;
    int gameState;

    void shopWater() {
        //Creating the Frame
        final JFrame frame = new JFrame("Shop");
        frame.setSize(600, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//<-- destroy only this frame

        JPanel panel = new JPanel(null);
        frame.add(panel);

        JLabel lab1 = new JLabel("You have " +bg.points+" points.");
        lab1.setHorizontalAlignment((JLabel.CENTER));
        lab1.setSize(400,300);

        JLabel lab2 = new JLabel("You can purchase ");
        lab2.setHorizontalAlignment((JLabel.CENTER));
        lab2.setSize(400,300);

        JLabel lab3 = new JLabel("water at price of "+ bg.price);
        lab3.setHorizontalAlignment((JLabel.CENTER));
        lab3.setSize(400,300);

        JButton purchaseBtn = new JButton("Purchase");
        lab1.setBounds(48,28,200,30);
        purchaseBtn.setBounds(150,110,100,30);

        JButton cancelBtn = new JButton("Cancel");
        lab2.setBounds(48,65,200,30);
        lab3.setBounds(300,65,200,30);
        cancelBtn.setBounds(280,110,100,30);

        numWater = bg.points/bg.price;
        List<String> selections = new ArrayList<String>();
        for (int i = 0; i <= numWater; i++) {
            selections.add(String.valueOf(i));
        }

        final JComboBox  box = new JComboBox(selections.toArray());
        box.setBounds(220,65,90,30);

        purchaseBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                bg.purchase=true;
                frame.dispose();
                String response = String.valueOf(box.getSelectedItem());
                bg.numWaterPurchase = Integer.parseInt(response);
            }
        });

        cancelBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                bg.purchase=true;
                frame.dispose();
                bg.numWaterPurchase = 0;
            }
        });
        panel.add(lab1);
        panel.add(lab2);
        panel.add(lab3);
        panel.add(purchaseBtn);
        panel.add(cancelBtn);
        panel.add(box);

    }
}
