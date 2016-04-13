/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dicewars.states;

import dicewars.Cell;
import dicewars.GameBoard;
import dicewars.Main;
import dicewars.Player;
import dicewars.PlayerAI;
import dicewars.PlayerHuman;
import dicewars.Territory;
import gameTools.map.Layout;
import gameTools.map.Orientation;
import gameTools.map.generators.MapGenerator;
import gameTools.map.generators.MapGeneratorHexRectangleFlat;
import gameTools.state.State;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

/**
 *
 * @author ganter
 */
public class GameState extends State{
    private GameState THIS = this;
    public boolean gameOver;
    int volume;
    GameBoard gameboard;
    Player[] players = new Player[0];
    Cell mouseOver;
    Thread playerThread;
    
    public GameState() {
        super("GameState", Main.GAME_WIDTH, Main.GAME_HEIGHT);
        MapGenerator<Cell> MapGenerator = new MapGeneratorHexRectangleFlat<>(new Cell(0, 0), 50, 50);
        Layout layout = new Layout(Orientation.LAYOUT_FLAT, new Point(6,3), new Point(10,10));
        
        inputManager.addKeyMapping("ESC", KeyEvent.VK_ESCAPE);
        inputManager.addKeyMapping("Enter", KeyEvent.VK_ENTER);
        
        inputManager.addClickMapping("ButtonLeft", MouseEvent.BUTTON1);

        gameboard = new GameBoard(MapGenerator , layout);
        
                       
        
        
        
//        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//        g.setRenderingHints(rh);
    }
    
    public void create(){
        gameboard.generateTerritories(5);
        players = new Player[Player.PLAYERS.size()];
        for(int i = 0; i< Player.PLAYERS.size(); i++){
            players[i] = Player.PLAYERS.get(i);
            players[i].reincarnate();
        }
        playerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!THIS.gameOver){
                    THIS.gameOver = true;
                    int teamAlive = -1;
                    for(Player p : players){
                        if(p.isAlive()){
                            p.play(gameboard, inputManager);
                            if(teamAlive == -1){
                                teamAlive = p.getTeam();
                            } else if (teamAlive != p.getTeam()){
                                THIS.gameOver = false;
                            }
                        }
                    }
                }
                gameOver();
            }
        });
        playerThread.start();
    }
    
    private void gameOver(){
        System.out.println("GAME OVER!");
    }
    
    @Override
    public void start(){
        gameOver=false;
        create();
        super.start();
    }
    
    @Override
    public void stop(){
        gameOver=true;
        super.stop();
    }
    
    @Override
    public void render(){
        if(ticks%10 == 0) redraw();
//        g.fillRect(0, 0, 300, 300);
        gameboard.render(g);
        
        //fps
        g.setColor(Color.gray);
        g.fillRect(width-80, height-15, width, height);
        
        String s = fpsCounter.fps() + " fps";
        int rightJustifiedBase = width-3;
        g.setFont(new Font("Courier New", Font.PLAIN, 13));
        int stringWidth = g.getFontMetrics().stringWidth(s);
        int x = rightJustifiedBase - stringWidth;
        
        g.setColor(Color.WHITE);
        g.drawString(s, x, height-3);
//        g.drawString(s, width-100, height-3);
    }
    
    private void redraw(){
        //clear screen
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Main.GAME_WIDTH, Main.GAME_HEIGHT);
        //mark all cells to be repaired
        for(Territory t : gameboard.territories){
            t.touch();
        }
    }
    
    @Override
    public void update(State s){
        if (inputManager.isKeyTyped("ESC")){
            Main.getInstance().setState("MenuState");
        }
        try{
            gameboard.setHighlitCell(gameboard.fromPixel(inputManager.getMousePos().x, inputManager.getMousePos().y));
        } catch(NullPointerException ignore){}

    }
    
}
