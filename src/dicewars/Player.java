/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dicewars;

import dicewars.states.GameState;
import gameTools.state.InputManager;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.InputMap;

/**
 *
 * @author ganter
 */
public abstract class Player {
    
// --- static fields and methods ---
    
    public static final Color[] COLORS = new Color[]{
        new Color(155,  5,  0),
        new Color(160, 90, 20),
        new Color(255,216,  0),
        new Color(  5,120,  0),
        new Color( 75,  0,125),
        new Color(255,120,140),
        new Color(0,0,0)
    };
    public static final ArrayList<Player> PLAYERS = new ArrayList<>();
    
    public static int getNumOfPlayers(){
        return PLAYERS.size();
    }
    

    
// --- END OF static fields and methods ---
    
    private int team;
    private Color color;
    public ArrayList<Territory> territories;
    
    public Player(){
        this(Color.YELLOW);
    };
    public Player(Color color) {
        this(color,0);
        this.team = getId();
    }
    public Player(Color color, int team) {
        this.color = color;
        this.team = team;
        territories = new ArrayList<>();
        PLAYERS.add(this);
    }

    public Color getColor() {
        return color;
    }

    public int getId() {
        return PLAYERS.indexOf(this);
    }
    
    public int getTerritoryNum(){
        return territories.size();
    }
    
    public int getTeam(){
        return this.team;
    }
    
    public void addTerritory(Territory t){
        territories.add(t);
    }
    public void removeTerritory(Territory t){
        territories.remove(t);
    }
    
    public void dispose(){
        PLAYERS.remove(this);
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
    public abstract void selectBase(GameBoard board, InputManager input)  throws EndOfTurnException;
    
    public abstract void selectTarget(GameBoard board, InputManager input)  throws EndOfTurnException;
    
    public void play(GameBoard board, InputManager input){
        boolean endOfTurn=false;
        while(!endOfTurn){
            try{
                selectBase(board, input);
                selectTarget(board, input);
                board.evaluateMove();
            }catch(EndOfTurnException e){
                endOfTurn=true;
            }finally{
            }
        }
    }
    
}
