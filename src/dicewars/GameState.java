/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dicewars;

import gameTools.map.Layout;
import gameTools.map.Map;
import gameTools.map.Orientation;
import gameTools.map.generators.MapGenerator;
import gameTools.map.generators.MapGeneratorHexHexagonPointy;
import gameTools.map.generators.MapGeneratorHexRectangleFlat;
import gameTools.state.State;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.invoke.util.ValueConversions;

/**
 *
 * @author ganter
 */
public class GameState extends State
        implements MouseListener, MouseMotionListener
{
    int volume;
    GameBoard gameboard;
    Player[] players;
    Thread gameBoardGenerator;
    Point mousePointer;
    Cell mouseOver;
    
    public GameState() {
        super("GameState", Main.GAME_WIDTH, Main.GAME_HEIGHT);
        addMouseListener(this);
        addMouseMotionListener(this);
        MapGenerator<Cell> MapGenerator = new MapGeneratorHexRectangleFlat<>(new Cell(0, 0), 50, 30);
        Layout layout = new Layout(Orientation.LAYOUT_FLAT, new Point(8,6), new Point(10,10));

        gameboard = new GameBoard(MapGenerator , layout);
        gameboard.generateTerritories(25);

        players = new Player[3];
                       
        
        
        
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHints(rh);
    }
    
    @Override
    public void render(){
        if(ticks%1000 == 0) redraw();
//        g.fillRect(0, 0, 300, 300);
        gameboard.render(g);
        
        g.setColor(Color.gray);
        g.fillRect(width-80, height-15, width, height);
        
        String s = fpsCounter.fps() + " fps";
        int rightJustifiedBase = width-3;
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
        for(Cell c : gameboard.values()){
            c.touch();
        }
    }
    
    @Override
    public void update(State s){

        try{
            gameboard.mouseOver = gameboard.fromPixel(mousePointer.x, mousePointer.y);
        } catch(NullPointerException ignore){}

    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePointer = new Point(e.getX(), e.getY());
//        mouseOver = gameboard.fromPixel(e.getX(), e.getY());
//        gameboard.mouseOver = gameboard.fromPixel(e.getX(), e.getY());
        
    }
}
