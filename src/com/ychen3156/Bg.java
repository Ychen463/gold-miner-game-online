package com.ychen3156;

import javax.swing.*;
import java.awt.*;

public class Bg extends JFrame{
    static int level =1; //game level
    int goalPoints = level*5; // for level up
    static int points=0;// total points
    static int waterNum = 3; // water number
    static boolean waterIsBeingUsed =false; // water state, default:false. True = is using

    // Timer
    long startTime;
    long endTime;

    // water price;
    int price =  (int) (1+Math.random() *9) ;
    boolean purchase = false;// Shop but not buy
    int numWaterPurchase;



    Image bg = Toolkit.getDefaultToolkit().getImage("imgs/bg.jpg");
    Image peo = Toolkit.getDefaultToolkit().getImage("imgs/peo.png");
    Image water = Toolkit.getDefaultToolkit().getImage("imgs/waterSmall.png");



    void paintSelf(Graphics g){

        g.drawImage(bg,0,0, null);


        switch(GameWin.gameState){
            case 0:
                drawWord(g,80,Color.black,"Ready?",410,220);
                drawWord(g,20,Color.black,"(Click to log in or register)",440,260);
                break; // Ready
            case 1: // Running
                g.drawImage(peo,465,20,null);
                // points display
                drawWord(g,30,Color.black,"Points: "+points,15,110);
                // water display
                g.drawImage(water, 900,40,null);
                drawWord(g,20,Color.black,"*"+waterNum,930,70);
                // Level display
                drawWord(g,20,Color.black,"<Level "+level+">",15,45);
                drawWord(g,20,Color.black,"Goal Points: "+goalPoints,105,45);


                // Display Timer
                endTime = System.currentTimeMillis();
                long timeInGame = 20-(endTime-startTime)/1000;
                // if time>0, display time, if not, display 0
                drawWord(g,30,Color.red,"Time: "+(timeInGame>0?timeInGame:0) + "s",900,110);
                break;
            case 2://2-Shop;
                // water display
                g.drawImage(water, 620,270,null);
//                drawWord(g,20,Color.black,"You got a chance to purchase in Shop"+price,300,300);
                drawWord(g,20,Color.black,"Congratulations! You'll go to next Level!",300,200);
                drawWord(g,20,Color.black,"You got a chance to purchase in Shop",300,300);
                drawWord(g,20,Color.black,"(Click into the shop)",300,400);

                if (purchase){
                    points = points-price*numWaterPurchase;
                    waterNum += numWaterPurchase;
                    purchase = false;
                    GameWin.gameState=1;
                    startTime=System.currentTimeMillis();
                }
                break;
            case 3: //3-Fail;
                drawWord(g,80,Color.red,"Game Over",340,220);
                // points display
                drawWord(g,50,Color.black,"Points: "+points,380,300);
                break;
            case 4: // 4-Win
                drawWord(g,80,Color.green,"You Win",340,220);
                // points display
                drawWord(g,50,Color.black,"Points: "+points,380,300);
                break;
            default:
        }
    }


    //true-Time's up; false- Timing
    boolean gameTimeEnd(){
        long gameTime = (endTime -startTime)/1000;
        if (gameTime>20){return true;}
        return false;
    }
    // Restart game
    void reGame(){
        level =1; //game level
        goalPoints = level*5; // for level up
        points=0;// total points
        waterNum = 3; // water number
        waterIsBeingUsed =false; // water state, default:false. True = is using

    }
    // display words
    public static void drawWord(Graphics g, int size, Color color, String str, int x, int y){
        g.setColor(color);
        g.setFont(new Font("Serif", Font.BOLD, size));
        g.drawString(str , x, y);
    }

}
