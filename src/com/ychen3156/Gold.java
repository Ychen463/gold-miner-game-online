package com.ychen3156;


import java.awt.*;

public class Gold  extends Object{

    Gold(){
        this.x = (int) (Math.random()*950);
        this.y=(int) (Math.random()*320+180);
        this.width=52;
        this.height=52;
        this.movable = false;
        this.material=15;
        this.points=4;
        this.type = 1;
        this.img= Toolkit.getDefaultToolkit().getImage("imgs/gold1.gif");
    }
}

class GoldMini extends Gold{  // child of Gold
    GoldMini(){
        this.width=36;
        this.height=36;
        this.material=10;
        this.points=2;
        this.img= Toolkit.getDefaultToolkit().getImage("imgs/gold0.gif");
    }
}
class GoldPlus extends Gold{
    GoldPlus(){
        this.x = (int) (Math.random()*800);
        this.y=(int) (Math.random()*320+180);
        this.width=77;
        this.height=77;
        this.material=30;
        this.points=8;
        this.img= Toolkit.getDefaultToolkit().getImage("imgs/gold2.gif");
    }
}
