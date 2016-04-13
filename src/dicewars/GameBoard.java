/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dicewars;

import gameTools.Graphical;
import gameTools.map.Layout;
import gameTools.map.Map;
import gameTools.map.generators.MapGenerator;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author ganter
 */
public class GameBoard extends Map<Cell> implements Graphical{
    
    //tool variables   
//    public static final double GYOK3 = Math.sqrt(3);
    
    public Territory[] territories;
    
    //play-variables
    private Territory mouseOver;
    private Territory selectedBase;
    private Territory selectedTarget;
    private int[] baseRoll = new int[0];
    private int[] targetRoll = new int[0];
    private int baseRollSum=0, targetRollSum=0;
    
    public void setHighlitCell(Cell c){
        if(c != null)
            mouseOver = c.getOwner();
        else
            mouseOver=null;
    }
    
    public GameBoard(MapGenerator<Cell> g, Layout layout){
        super(g, layout);
    }
    public GameBoard(MapGenerator<Cell> g, Layout layout, int territoryNum){
        super(g, layout);
        generateTerritories(territoryNum);
    }
    
    /**
     * 
     * @param territoryNum ammount of territories per player (Territories Per Player = tpp)
     */
    public void generateTerritories(final int TPP){
        System.out.println("generating gameboard... ");
        final int territoryNum = TPP * Player.PLAYERS.size();
        
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
                    if(f.equals(territories[j].getCells().get(0))) ok = false;
                }
            }while(!ok);
//            territories[i] = Settings.usedTerritory.getInstance();
            territories[i] = new Territory();
            territories[i].add(f);
        }
        
        for(int i = 0; i< territories.length; i++){
            territories[i].setOwner(Player.PLAYERS.get(i%Player.PLAYERS.size()));
        }
        
        
        //distribute Dices
        for(Player p : Player.PLAYERS){
            int num = 3*TPP;
            for(Territory t : p.getTerritories()){
                num -= t.addDices(1);
            }

            while(num>0){
                int rand = Main.RANDOM.nextInt(p.getTerritories().size());
                num -= p.getTerritories().get(rand).addDices(1);
            }
        }
        
        //expand territories
        boolean emptyNeighborFound;
        do{
            emptyNeighborFound = false;
            ArrayList<Cell> unownedNeighborsOfCell; // unowned neighboring cells of random cell in territory
            
            for(Territory territory : territories){ //at the end of this loop every territory(that can) gets new member(s)
                ArrayList<Cell> unownedNeighborsOfTerritory = new ArrayList<>(); //unowned neighboring cells of the territory
                //collect the cells that have empty neighbors
                for(Cell cell: territory.getCells()){
                    unownedNeighborsOfCell = getSpecNeighborTiles(Cell.CELL_NOT_OWNED, cell.x, cell.y);
                    if (! unownedNeighborsOfCell.isEmpty()){
                        emptyNeighborFound = true;
                        for(Cell c : unownedNeighborsOfCell){
                            if(!unownedNeighborsOfTerritory.contains(c)) {
                                unownedNeighborsOfTerritory.add(c);
                            }
                        }
                    }
                }

                for(int i = 0; i< 10; i++){
                    if(!unownedNeighborsOfTerritory.isEmpty()){
                        //get random cell from the collected ownerless cells
                        Cell c = unownedNeighborsOfTerritory.remove(Main.RANDOM.nextInt(unownedNeighborsOfTerritory.size()));
                        if (c == null) break;
                        territory.add(c);
                    }
                }
                //go on to the next territory    
            }
        }while(emptyNeighborFound);
        
//        for(Territory t : territories){
//            t.calcBoundary(layout);
//        }
        System.out.println("done generating");
    }
    
    public Territory getSelectedBase(){
        return selectedBase;
    }
    
    public Territory getSelectedTarget(){
        return selectedTarget;
    }
    
    public void selectBase(Territory t){
        if(t != null){
            selectedBase = t;
        }
    }
    public void unSelectBase(){
        if(selectedBase != null) selectedBase.touch();
        selectedBase = null;
    }
    
    public void selectTarget(Territory t){
        selectedTarget = t;
    }
    
    public void unSelectTarget(){
        selectedTarget = null;
    }
    
    public void evaluateMove(){
        baseRoll = Dice.roll(selectedBase.getStrength());
        targetRoll = Dice.roll(selectedTarget.getStrength());
        baseRollSum=0;
        targetRollSum=0;
        for(int i : baseRoll) baseRollSum+=i;
        for(int i : targetRoll) targetRollSum+=i;
        if(baseRollSum > targetRollSum){ //win!
            selectedTarget.setStrength(selectedBase.getStrength()-1);
            selectedTarget.setOwner(selectedBase.getOwner());
        }
        selectedBase.setStrength(1);
        
        unSelectBase();
        unSelectTarget();
    }
    
    public void finishRound(Player p){
        int num = (int) Math.round(p.getTerritoryNum()/2.0);
        
        int maxNum = 0;
        for(Territory t : p.getTerritories()){
            maxNum += 8-t.getStrength();
        }
        num = (num > maxNum)? maxNum : num;
        
        while(num>0){
            int rand = Main.RANDOM.nextInt(p.getTerritories().size());
            num -= p.getTerritories().get(rand).addDices(1);
        }

        unSelectBase();
        unSelectTarget();
        
        for(Player pp : Player.PLAYERS){
            if(pp.getTerritoryNum() == 0)
                pp.kill();
        }
    }
    
    @Override
    public void render(Graphics2D g) {
        
        for(Territory t : territories){
                t.render(g, layout);
        }
        
        try{
            selectedBase.highlight();
            selectedBase.render(g, layout);
            selectedBase.unLight();
        }catch(NullPointerException ignore){}
        
        try{
            mouseOver.highlight();
            mouseOver.render(g, layout);
            mouseOver.unLight();
        }catch(NullPointerException ex){};
        
        int i;
        int x,y;
        int rowWidth=30;
        g.setFont(new Font("Courier New", Font.PLAIN, 13));
        g.setColor(Color.WHITE);
        
        String s = "Attacker: ";
        for(i = 0; i < baseRoll.length; i++){
            s += baseRoll[i];
            if(i<baseRoll.length-1)
                s += " + ";
        }
        s += " = "+baseRollSum;
        x = 5;
        y=Main.GAME_HEIGHT-rowWidth;
        
        g.drawString(s, x, y);
        
        s = "Defender: ";
        for(i = 0; i < targetRoll.length; i++){
            s += targetRoll[i];
            if(i<targetRoll.length-1)
                s += " + ";
        }
        s += " = "+targetRollSum;
        x = 5;
        y -= rowWidth;
        
        g.drawString(s, x, y);
    }
}
