/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dicewars;

import gameTools.Graphical;
import gameTools.state.State;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ganter
 */
public class GameBoard1 implements Graphical{
    
    private class Territory {
        final private int id=++numOfTerritories;
        public Player owner;
        public byte strength;
        private ArrayList<Cell> cells;

        public Territory() {
            strength = 0;
            cells = new ArrayList<>();
        }
        
        void add(Cell c){
            cells.add(c);
            c.owner=this;
        }
        
    }
    
    
    private class Cell implements Graphical{
        final short X,Y; //id
         int renderX, renderY;
        Territory owner;
        
        Cell(short x, short y){
            X=x;
            Y=y;
            x++;y++;
            renderX = (int) ((y%2==0) ? ((2.5 + (x-1)*1.5)*x0) : ((1+(x-1)*1.5)*x0));
            renderY = (int)((GYOK3/2) * y * y0);
        }
        
        Cell getNeighbor(Dir dir){
            short x=this.X;
            short y=this.Y;
            switch(dir){
                    case NORTH:
                        y--;
                        break;
                    case SOUTH:
                        y++;
                        break;
                    case NORTH_WEST:
                        x--;
                        if(x%2!=0)y--;
                        break;
                    case SOUTH_WEST:
                        x--;
                        if(x%2==0) y++;
                        break;
                    case NORTH_EAST:
                        x++;
                        if(x%2!=0){
                            x++;
                            y--;
                        }
                        break;
                    case SOUTH_EAST:
                        x++;
                        if(x%2==0) y++;
                        break;
            }
            if(x<0 || x>=matrix[0].length || y<0 || y>=matrix.length) return null;
            return matrix[y][x];
        }
        
        ArrayList<Cell> getNeighbors(){
            ArrayList<Cell> r = new ArrayList<>();
            for(Dir d : Dir.values()){
                Cell c = getNeighbor(d);
                if(c != null) r.add(c);
            }
            return r;
        }
        
        ArrayList<Cell> getEmptyNeighbors(){
            ArrayList<Cell> r = new ArrayList<>();
            for(Dir d : Dir.values()){
                Cell c = getNeighbor(d);
                if(c != null && c.owner == null) r.add(c);
            }
            return r;
        }
        
        int getRenderX(){
//            System.out.println("Y+1 : "+ (Y+1) +"; ((Y+1)%2==0) : "+((Y+1)%2==0));
            return (int) (( ((X%2==0) ? 1 : 2.5) + X*1.5)*x0);
        }
        int getRenderY(){
            return (int) (( ((Y%2==0) ? 2 : 1) + 2*Y)*y0);
        }
        
        boolean equals(Cell b){
            return (X == b.X && Y == b.Y);
        }
        
        @Override
        public void render(Graphics2D g) {
//            System.out.printf("[%d,%d] \t",renderX,renderY);
            renderX=getRenderX();
            renderY=getRenderY();
            g.translate(renderX, renderY);
                g.drawArc(0, 0, (int)x0*2, (int)x0*2, 0, 360);
                g.drawString(""+owner.id,0,0);
            g.translate(-renderX, -renderY);
            
        }
        @Override
        public void update(State s) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
    
    //tool variables
    static int numOfTerritories=0;
    static enum Dir{NORTH, SOUTH, NORTH_EAST, SOUTH_EAST, NORTH_WEST, SOUTH_WEST};
    private static Cell[][] matrix;
    final static double GYOK3 = Math.sqrt(3);
    double x0, y0;
    
    Territory[] territory;
    
    //play-variables
    Territory selectedBase;
    Territory selectedTarget;
    Player currentPlayer;
    
    GameBoard(int x, int y, int averageSize, int w, int h){
        x0= w/(2+(x-1)*1.5);
        y0= h/(1.5*GYOK3+((y-1) * GYOK3));
        generate(x, y, averageSize);
        System.out.println(x0);
        System.out.println(y0);
    }
    
    public void generate(int x, int y, int averageSize){
        
        short n = (short) Math.round(x*y/averageSize); //number of Territories
        
        matrix = new Cell[y][x];
        for(short j = 0; j<y; j++){
            for(short i = 0; i<x; i++){
                matrix[j][i] = new Cell(i,j);
            }
        }
        territory = new Territory[n];
        
        //generate random territory bases/origins
        for(short i = 0; i < n; i++){
            boolean ok;     
            short Tx, Ty;
            Cell f;
            do{
                ok=true;
                Tx = (short) Math.floor(Math.random()*x);
                Ty = (short) Math.floor(Math.random()*y);
                f = matrix[Ty][Tx];
                for(int j = 0; j < i; j++){
                    if(f.equals(territory[j].cells.get(0))) ok = false;
                }
            }while(!ok);
            territory[i] = new Territory();
            territory[i].add(f);
        }

        
        //expand territories


        boolean hasEmptyNeighbor;
        final Random RANDOM = new Random();
        do{
            hasEmptyNeighbor = false;
            loop:
            for(int i = 0; i < n; i++){
                for(Cell c : territory[i].cells){
                    ArrayList<Cell> emptyOnes = c.getEmptyNeighbors();
                    for(Cell c2 : emptyOnes){
                        hasEmptyNeighbor = true;
                        //get random empty neighbor, add it to the territory
                        territory[i].add(emptyOnes.get(RANDOM.nextInt(emptyOnes.size())));
                        continue loop; //go on to the next territory
                    }
                }
            }
        }while(hasEmptyNeighbor);
        for(Cell[] j : matrix){
            for(Cell i : j){
                if(i.owner != null){
                    System.out.printf("%3d",i.owner.id);
                } else {
                    System.out.printf("%3d",0);
                }
            }
            System.out.println("");
        } 
    }
    
    public void evaluateMove(){
        
    }
    
    public void finishRound(){
        
    }
    
    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.WHITE);        
        for(Cell[] j : matrix){
            for(Cell i : j){
                i.render(g);
            }
        }
        g.setColor(Color.BLUE);
        for(int i = 0; i<10; i++){
            for(int j = 0; j<16; j++){
                g.translate(j*x0, i*y0);
                g.drawArc(0, 0, 3, 3, 0, 360);
            g.translate(-j*x0, -i*y0);
            }
        }
//        try {
//            System.in.read();
//        } catch (IOException ex) {
//            Logger.getLogger(GameBoard.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        int tx = - ((int) Math.round((getTWidth()+1*scale)/2.0));
//        int ty = - ((int) Math.round((getTHeight()+1*scale)/2.0));
//        
//        g.translate(x, y);
//            g.rotate(this.phi);
//                g.drawImage(image, tx, ty, getIWidth(), getIHeight(), this);
//                if (Main.dbgHitbox || Main.dbgAll){
//                    g.setColor(Color.WHITE);
//                    g.fillArc(0, 0, 3, 3, 0, 360);
//                }
//            g.rotate(-this.phi);
//        g.translate(-x, -y);
//        //ez az utolsó forgatás és translate azért kell, mert a többi tank ugyanerre a Graphics2D objectre
//        //rajzol. Ezért vigyázni kell, hogy változatlanul hagyja a függvény azt, amikor visszatér.        
//        //Debugging
//        if (Main.dbgCoord || Main.dbgAll){
//            g.setColor(Color.WHITE);
//            g.drawString(String.format("Tank %d",this.id),  20 + id*100, 20);
//            g.drawString(String.format("x  : %.2f",this.x),   20 + id*100, 40);
//            g.drawString(String.format("y  : %.2f",this.y),   20 + id*100, 60);
//            g.drawString(String.format("phi: %.2f",this.phi), 20 + id*100, 80);
//            g.drawString(String.format("canFire: %b",this.weapon.getLoadedWeapon().isFireable), 20 + id*100, 100);
//        }
//
//        if (Main.dbgHitbox || Main.dbgAll){
//            g.setColor(Color.CYAN);
//            g.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.CAP_SQUARE));
//            g.draw(getBounds());
//            g.setColor(Color.BLUE);
//            Point[] points = getPointBounds();
//            for (Point p : points){
//                g.fillArc(p.getIntx(), p.getInty(), 3, 3, 0, 360);
//            }
//        }        
//        //END OF Debugging
    }

    @Override
    public void update(State s) {
        try {
            Thread.sleep(8);
        } catch (InterruptedException ex) {
            Logger.getLogger(GameBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
