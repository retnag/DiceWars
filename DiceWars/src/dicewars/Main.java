/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dicewars;

import gameTools.state.StateManager;
import javax.swing.JFrame;

/**
 *
 * @author ganter
 */
public class Main extends JFrame{
    
    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;
//    public static boolean dbgHitbox = true;
//    public static boolean dbgCoord = true;
//    public static boolean dbgAll = true;
    public static boolean dbgHitbox = false;
    public static boolean dbgCoord = false;
    public static boolean dbgField = false;
    public static boolean dbgAll = false;
    public static boolean isPaused = false;
    
    public static int numOfPlayers;
    
    private StateManager sm = new StateManager(this);
    private GameState game;
    
    public Main(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setSize(GAME_WIDTH,GAME_HEIGHT);
        this.setResizable(false);
        this.setTitle("DiceWars");
        this.setLocationRelativeTo(null);
        
        numOfPlayers = 4;
        
        game = new GameState();
        
        sm.addState(game);
        
        sm.setCurrentState("GameState");
        sm.startCurrentState();
        this.setVisible(true);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        main = new Main();
    }
    static Main main;
}
