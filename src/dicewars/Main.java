/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dicewars;

import dicewars.states.GameState;
import dicewars.states.MenuState;
import dicewars.states.SettingsState;
import gameTools.state.StateManager;
import javax.swing.JFrame;

/**
 *
 * @author ganter
 */
public class Main extends JFrame{
    
    public static final int GAME_WIDTH = 600;
    public static final int GAME_HEIGHT = 400;
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
    private MenuState menu;
    private SettingsState settings;
    
    private Main(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setSize(GAME_WIDTH,GAME_HEIGHT);
        this.setResizable(false);
        this.setTitle("DiceWars");
        this.setLocationRelativeTo(null);
        
        numOfPlayers = 4;
        
        game = new GameState();
        menu = new MenuState();
        settings = new SettingsState();
        
        sm.addState(game);
        sm.addState(menu);
        sm.addState(settings);
        
        sm.setCurrentState("MenuState");
        sm.startCurrentState();
        this.setVisible(true);
    }
    
    public void setState(String s){
        sm.stopCurrentState();
        sm.setCurrentState(s);
        sm.startCurrentState();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
    }
    private static Main main = new Main();;
    
    public static Main getInstance(){
        return main;
    }
    
    
}
