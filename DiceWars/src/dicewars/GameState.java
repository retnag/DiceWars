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
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    public GameState() {
        super("GameState", Main.GAME_WIDTH, Main.GAME_HEIGHT);
        addMouseListener(this);
        addMouseMotionListener(this);
//        MapGenerator<Cell> MapGenerator = new MapGeneratorHexRectanglePointy<>(new Cell(0, 0), 6, 10);
        MapGenerator<Cell> MapGenerator = new MapGeneratorHexRectangleFlat<>(new Cell(0, 0), 50, 50);
//        MapGenerator<Cell> MapGenerator = new MapGeneratorHexParalelogram<>(new Cell(0, 0), 6, 10);
//        MapGenerator<Cell> MapGenerator  = new MapGeneratorHexHexagonPointy<>(new Cell(0, 0), 6);
        Layout layout = new Layout(Orientation.LAYOUT_FLAT, new Point(8,4), new Point(10,10));

        gameboard = new GameBoard(MapGenerator , layout);
        gameboard.generateTerritories(25);


//        gameboard.getTile(0,0).selected = true;
//        
//        for(Cell i : gameboard.getSpecNeighborTiles(Cell.CELL_NOT_OWNED, 2,2)){
//            i.selected = true;
//        }

        players = new Player[3];
                       
        
        
        
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHints(rh);
    }
    
    @Override
    public void render(){
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHints(rh);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Main.GAME_WIDTH, Main.GAME_HEIGHT);
//        g.fillRect(0, 0, 300, 300);
        gameboard.render(g);
        super.render();
    }
    
    @Override
    public void update(State s){
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
        }
        gameboard.update(this);

        try{
            if(gameboard.mouseOver != null) gameboard.mouseOver.selected = false;
            Cell c = gameboard.fromPixel(mousePointer.x, mousePointer.y);
            gameboard.mouseOver = c;
            gameboard.mouseOver.selected = true;
        }catch(NullPointerException x){
            gameboard.mouseOver = null;
        }
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
    }
}
