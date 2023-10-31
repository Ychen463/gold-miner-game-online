package com.ychen3156;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameWin extends JFrame implements Runnable{
    static int gameState; // 0-Ready; 1-Running; 2-Shop; 3-Fail; 4-Win
    static String usrName = "visitor";
    //to save rocks, golds
    List<Object> objectList = new ArrayList<Object>();
    Bg bg =new Bg();
    Line line = new Line(this);
    int numWater;
    private Graphics g;
    Shop shop = new Shop();
    UserLogIn userLogIn = new UserLogIn();




    {
        // randomly add gold, mini gold, plus gold
        boolean placeable = true;
        for (int i = 0; i < 11; i++) {
            double random = Math.random();
            Gold gold; // current gold generated
            if (random < 0.3) {gold = new GoldMini();}
            else if (random < 0.7) {gold = new Gold();}
            else {gold = new GoldPlus();}

            for (Object obj : objectList) {
                // check if obj is overlapped
                // cannot be overlapped, we need to regenerate obj
                if (gold.getRec().intersects(obj.getRec())) {placeable = false;}
            }
            if (placeable) {objectList.add(gold);}
            else {placeable = true; i--;}
        }


        // add rock
            for (int i = 0; i < 5; i++) {
                Rock rock =new Rock();
                for (Object obj : objectList) {
                    // check if obj is overlapped
                    // cannot be overlapped, we need to regenerate obj
                    if (rock.getRec().intersects(obj.getRec())) {placeable = false;}
                }
                if (placeable) {objectList.add(rock);}
                else {placeable = true; i--;}
            }
        }
    // Canvas
    Image offScreenImage;

    public GameWin() {
        super("Gold Miner ychen3156");

        this.setVisible(true);
//        this.setSize(1030,650);
        this.setSize(1030,700);
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        createMenu();
        createMenu();

    }

    void launch() throws InterruptedException {

        createMenu();
        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                super.mouseClicked(e);
                switch (gameState){
                    case 0: //0-Ready
                        if (e.getButton()>0){ // click into game
                            if (usrName == "visitor"){
                                userLogIn = new UserLogIn();
                                userLogIn.setVisible(true);
                                gameState = userLogIn.gameState;
                                bg.startTime = System.currentTimeMillis();
                                System.out.println("GW: Welcome "+ usrName);
                            }
                            else{
                                gameState = 1;
                                bg.startTime = System.currentTimeMillis();
                            }

                        }
                        break;
                    case 1: //- Running
                        if(e.getButton()==1 && line.state==0){line.state=1;}// swing: state=0, left-click
                        if(e.getButton()==3&&line.state==3&& Bg.waterNum>0){ // Use water after catching something can faster the catch process
                                Bg.waterNum--;
                                Bg.waterIsBeingUsed= true;}//pull back: state=3
                        break;
                    case 2://2-Shop;
                        shop.bg=bg;
                        shop.numWater=numWater;
                        shop.gameState=gameState;
                        shop.shopWater();
                        break;
                    case 3: //3-Fail;
                    case 4: // 4-Win
                        if (e.getButton()==1){
                            // show ranking
                            Rank rank = null;
                            try {
                                rank = new Rank();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            } catch (PrinterException ex) {
                                ex.printStackTrace();
                            }
                            rank.setVisible(true);
//                            dispose();

                            gameState =0;
                            bg.reGame();
                            line.reGame();
                        }


                        break;
                    default:
                }
            }
        });
        run();
    }

    public void run() {
        //draw the line
        while (true){ // Threads loop
            repaint();
            try {
                nextLevel();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try{
                Thread.sleep(10);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    private void GameGUI() throws InterruptedException {
        this.launch();
    }


    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem logInItem = new JMenuItem("Log In");
        JMenuItem restartItem = new JMenuItem("Restart");
        JMenuItem rankingItem = new JMenuItem("Ranking");

        JMenuItem exitItem = new JMenuItem("Exit");
        JMenu acctMenu = new JMenu("Welcome "+usrName+"!");

        // exit
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        // ranking
        rankingItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Rank rank = new Rank();
                    rank.setVisible(true);

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        // restart game
        restartItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameState =0;
                bg.reGame();
                line.reGame();
            }
        });
        // log in
        logInItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UserLogIn userLogIn = new UserLogIn();
                    userLogIn.setVisible(true);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        menu.add(logInItem);
        menu.add(restartItem);
        menu.add(rankingItem);
        menu.add(exitItem);
        menuBar.add(acctMenu);
        menuBar.add(menu);
        menuBar.setOpaque(true);

        menuBar.setBackground(Color.RED);
        menuBar.setForeground(Color.BLUE);

        this.setJMenuBar(menuBar);
        this.setVisible(true);
    }



    public void nextLevel() throws InterruptedException {
        if (bg.gameTimeEnd() && gameState==1) {
            if (Bg.points >= bg.goalPoints) {
                if(Bg.level==5){
                    gameState=4;
                    // Store new record
                    PreparedStatement ps;
                    String query = "INSERT INTO `GameLogs`(`acct`, `gameDateTime`,`points`) VALUES (?,?,?)";
                    Calendar calendar = Calendar.getInstance();
                    java.util.Date currentTime = calendar.getTime();
                    long time = currentTime.getTime();

                    try {
                        ps = ConnectionDB.getConnection().prepareStatement(query);
                        ps.setString(1, usrName);
                        ps.setTimestamp(2, new Timestamp(time));
                        ps.setInt(3, bg.points);
                        String message = "Congratulations " + usrName +"\n"
                                + "We have stored your records in our Database!\n" +
                                "You got " + bg.points +" points on "+ new Timestamp(time);
                        JOptionPane.showMessageDialog (null, message);


                        ps.execute();
                        System.out.println("Data inserted......");
                        ps.close();

                        dispose();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                } //Win
                else{
                    gameState=2;
                    Bg.level++;
                    }
                }
            else {//fail
                gameState=3;
                }
            dispose();
            GameWin gameWin = new GameWin();
            System.out.println("Launch new game on"+ Bg.level);
            gameWin.launch();
        }
    }

    @Override
    public void paint(Graphics g) {
        offScreenImage = this.createImage(1030,650);
        Graphics gImage = offScreenImage.getGraphics();
        // draw on canvas
        bg.paintSelf(gImage);

        if (gameState ==1){
            for (Object obj:objectList){
                obj.paintSelf(gImage);
            }
            try {
                line.paintSelf(gImage);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        // Canvas on background
//        g.drawImage(offScreenImage,0,0, null);
        update(g);
    }
    @Override
    public void update(Graphics g) {
        // Canvas on background
        g.drawImage(offScreenImage, 0,  50, null);
    }

    public static void main(String[] args) throws InterruptedException {
        GameWin gameWin=new GameWin();
        gameWin.GameGUI();


    }
}
