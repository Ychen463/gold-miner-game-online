package com.ychen3156;

import java.awt.*;
public class Line {
    // start coordinate
    int x =523;
    int y =100;
    // final coordinate
    int endx=800;
    int endy=500;
    double length = 50;// line length
    double min_length = 50;
    double max_length = 530;
    double n = 0 ;
    int dir = 1;// direction: if line reach to horizon, go backwards
    int state;
    Image hook = Toolkit.getDefaultToolkit().getImage("imgs/hook.png"); // hook image
    GameWin frame;

    Line(GameWin frame){this.frame=frame;}

    // check if obj is catchable
    void logic(){
        for (Object obj:this.frame.objectList){
            if(endx>obj.x
                    && endx<obj.x+obj.width
                    && endy>obj.y
                    && endy<obj.y+obj.height){
                state =3;
                obj.movable = true;
            }
        }
    }

    void lines(Graphics g){
        // draw end line
        endx = (int) (x+length*Math.cos(n*Math.PI));
        endy = (int) (y+length*Math.sin(n*Math.PI));
        g.setColor(Color.red);
        // makes the line thinker
        g.drawLine(x-1,y-1,endx-1,endy-1);
        g.drawLine(x,y,endx,endy);
        g.drawLine(x+1,y+1,endx+1,endy+1);
        // - 20 = hook width/2
        g.drawImage(hook,endx-20,endy,null);
    }
    void paintSelf(Graphics g) throws InterruptedException {
        logic();
        switch(state){
            // state: 0-swing; 1-catch; 2-pull back
            case 0: //0-swing;
                if (n<0.1){dir =1;}
                else if (n>0.9){ dir = -1;}
                n=n+0.005*dir;
                lines(g);
                break;
            case 1: //1-catch;
                if (length<max_length){
                    length += 2.5;
                    // draw end line
                    lines(g);
                } else{state =2;}
                break;
            case 2: // 2-pull back
                if (length >min_length){
                    length = length-2.5;
                    // draw end line
                    lines(g);
                } else {state=0;}
            case 3: // 3- got item;
                int material=0; // default material
                if (length >min_length){
                    length = length-2.5;
                    // draw end line
                    lines(g);
                    for (Object obj:this.frame.objectList){
                        if (obj.movable){
                            obj.x=endx- obj.getWidth()/2; // half width of gold.gif
                            obj.y=endy;
                            material = obj.getMaterial();

                            if (length<=min_length){
                                // remove gold from window
                                obj.x=-150;
                                obj.y=-150;
                                obj.movable = false;
                                Bg.waterIsBeingUsed=false;
                                Bg.points += obj.points; // add points and display in bg
                                state=0;
                        }
                            if (Bg.waterIsBeingUsed&& Bg.waterNum>=0){
                                if (obj.type==1){material=0;}// if catch gold,  water can lower the sleep time which allows catch quickly
                                if (obj.type==2){ // if catch rock
                                    // Water can cause explosion, remove rock from window
                                    obj.x=-150;
                                    obj.y=-150;
                                    obj.movable = false;
                                    Bg.waterIsBeingUsed=false;
                                    state =2;
                                }
                            }

                        }
                    }
                }
                try{
                    Thread.sleep(material);
                } catch (InterruptedException e){
                    e.printStackTrace();
            }
                break;
            default:
        }
    }
    // Restart line
    void reGame(){
        n=0;
        length=50;
    }
}
