/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dicewars;

import gameTools.Graphical;
import gameTools.map.Layout;
import gameTools.map.Map;
import gameTools.map.Tester;
import gameTools.map.generators.MapGenerator;
import gameTools.map.generators.MapGeneratorHexRectangleFlat;
import gameTools.state.State;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ganter
 */
public class GameBoard extends Map<Cell> implements Graphical{
    
    //tool variables   
//    public static final double GYOK3 = Math.sqrt(3);
    
    Territory[] territories;
    
    //play-variables
    public Cell mouseOver;
    public Territory selectedBase;
    public Territory selectedTarget;
    public Player currentPlayer;
    
    GameBoard(MapGenerator<Cell> g, Layout layout){
        super(g, layout);
    }
    GameBoard(MapGenerator<Cell> g, Layout layout, int territoryNum){
        super(g, layout);
        generateTerritories(territoryNum);
    }
    
    public void generateTerritories(int territoryNum){
        
        territories = new Territory[territoryNum];
        Cell[] cells = new Cell[0];
        cells = (Cell[]) values().toArray(cells);
        
        //generate random territory bases/origins
        for(short i = 0; i < territoryNum; i++){
            boolean ok;     
            int index;
            Cell f;
            do{
                ok=true;
                index = (int) Math.floor(Math.random()*size());
                f = cells[index];
                for(int j = 0; j < i; j++){
                    if(f.equals(territories[j].cells.get(0))) ok = false;
                }
            }while(!ok);
            territories[i] = new Territory();
            territories[i].add(f);
        }
        
        for(int i = 0; i< territories.length; i++){
            territories[i].owner = new Player(""+i, new Color((int)((256 / (double)territories.length) * i), 255-(int)((256 / (double)territories.length) * i), 255));
        }
        
        //expand territories


        boolean emptyNeighborFound;
        final Random RANDOM = new Random();
        do{
            emptyNeighborFound = false;
            ArrayList<Cell> unownedNeighborsOfCell; // unowned neighboring cells of random cell in territory
            
            for(Territory territory : territories){ //at the end of this loop every territory(that can) gets a new member
                ArrayList<Cell> unownedNeighbors = new ArrayList<>(); //unowned neighboring cells of the territory
                //collect the cells that have empty neighbors
                for(Cell cell: territory.cells){
                    unownedNeighborsOfCell = getSpecNeighborTiles(Cell.CELL_NOT_OWNED, cell.x, cell.y);
                    if (! unownedNeighborsOfCell.isEmpty()){
                        emptyNeighborFound = true;
                        for(Cell c : unownedNeighborsOfCell){
                            if(!unownedNeighbors.contains(c)) {
                                unownedNeighbors.add(c);
                            }
                        }
                    }
                }

                if(!unownedNeighbors.isEmpty()){
                    //get random cell from the collected ownerless cells
                    Cell c = unownedNeighbors.get(RANDOM.nextInt(unownedNeighbors.size()));

                    territory.add(c);
                }
                //go on to the next territory    
            }
        }while(emptyNeighborFound);

    }
    
    public void evaluateMove(){
        
    }
    
    public void finishRound(){
        
    }
    
    @Override
    public void render(Graphics2D g) {
        
        for(Cell i : values()){
            i.render(g, layout);
        }
        
//        for(Cell i : getSpecTiles(Cell.CELL_SELECTED)){
//            i.render(g, layout);
//        }
        
        try{
            mouseOver.select();
            mouseOver.render(g, layout);
            mouseOver.deselect();
        }catch(NullPointerException ex){};
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


    
}
