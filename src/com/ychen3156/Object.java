package com.ychen3156;

import java.awt.*;

public class Object {
    // for gold and rock because they share lots of common attributes

    // cooridinate
    int x;
    int y;
    // width height
    int width;
    int height;
    Image img;//picture
    boolean movable;
    int material; // material weight
    int points;
    int type; // 1 = gold, 2= rock

    //draw
    void paintSelf(Graphics g){
        g.drawImage(img, x,y,null);
    }

    public int getWidth(){
        return width;
    }
    public int getMaterial(){
        return material;
    }

    // get rectangle: for checking if object is overlapped in GameWin.java
    public Rectangle getRec(){
        return new Rectangle(x,y,width,height);
    }
}
