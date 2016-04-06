package Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import gameTools.Physical;
import gameTools.state.StateManager;
import dicewars.Cell;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
 * @author ganter
 */
public class TestGraphics extends JFrame{
    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
    
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
    private TestState test;
    
    public TestGraphics(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setSize(GAME_WIDTH,GAME_HEIGHT);
        this.setResizable(false);
        this.setTitle("DiceWars");
        this.setLocationRelativeTo(null);
        
        
        
        test = new TestState("TestState");
        
        sm.addState(test);
        
        sm.setCurrentState("TestState");
        sm.startCurrentState();
        this.setVisible(true);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestGraphics main = new TestGraphics();
    }
    
    
}
