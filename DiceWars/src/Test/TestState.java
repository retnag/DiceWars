package Test;


import gameTools.state.State;
import gameTools.map.Map;
import gameTools.map.Orientation;
import gameTools.map.Layout;
import gameTools.map.generators.MapGenerator;
import gameTools.map.generators.MapGeneratorHexRectanglePointy;
import gameTools.map.Tester;
import dicewars.Cell;
import java.awt.Color;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import gameTools.map.generators.MapGeneratorHexHexagonPointy;
import gameTools.map.generators.MapGeneratorHexParalelogram;
import gameTools.map.generators.MapGeneratorHexRectangleFlat;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ganter
 */
public class TestState extends State{
    
    Map<Cell> h;
    
    
    int x = 10;
    int y = 10;
    
    public TestState(String s) {
        super(s);
//        MapGenerator<MyHex> rectangularMapGenerator = new MapGeneratorHexRectanglePointyTop<>(new MyHex(0, 0), 6, 10,);
//        MapGenerator<MyHex> rectangularMapGenerator = new MapGeneratorHexRectangleFlatTop<>(new MyHex(0, 0), 6, 10,);
//        MapGenerator<MyHex> rectangularMapGenerator = new MapGeneratorHexParalelogram<>(new MyHex(0, 0), 6, 10,);
        MapGenerator<Cell> rectangularMapGenerator = new MapGeneratorHexHexagonPointy<>(new Cell(0, 0), 6);
        
        h = new Map(rectangularMapGenerator, new Layout(Orientation.LAYOUT_FLAT, new Point(30,15), new Point(100,100)));
        
        h.getTile(0,0).selected = true;
        
        for(Cell i : h.getNeighborTiles(2,2)){
            i.selected = true;
        }
        
                       
        
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHints(rh);
    }
    
    @Override
    public void render(){
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, TestGraphics.GAME_WIDTH, TestGraphics.GAME_HEIGHT);
        
        h.render(g);
        
        for(Cell i : h.getSpecTiles(new Tester<Cell>(){
            @Override
            public boolean test(Cell c) {
                return c.selected;
            }
            
        })){
            i.render(g, h.layout);
        } 
        
        super.render();
    }

    @Override
    public void update(State s) {
        try {
            Thread.sleep(20);
        } catch (InterruptedException ex) {
            Logger.getLogger(TestState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
