package com.ychen3156;

import java.awt.*;

public class Rock extends Object{
    Rock() {
        this.x = (int) (Math.random()*950);
        this.y=(int) (Math.random()*320+180);
        this.width=71;
        this.height=71;
        this.movable = false;
        this.material=30;
        this.points=1;
        this.type=2;
        this.img= Toolkit.getDefaultToolkit().getImage("imgs/rock1.png");
    }
}
