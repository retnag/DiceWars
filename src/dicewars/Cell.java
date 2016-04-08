package dicewars;

import gameTools.Graphical;
import gameTools.state.State;
import gameTools.map.TileHex;
import gameTools.map.Layout;
import gameTools.map.Map;
import gameTools.map.Orientation;
import com.sun.xml.internal.fastinfoset.algorithm.HexadecimalEncodingAlgorithm;
import gameTools.map.Tester;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ganter
 */
public class Cell extends TileHex{
    
    public static final Tester<Cell> CELL_SELECTED = new Tester<Cell>(){
        @Override
        public boolean test(Cell c) {
            return c.selected;
        }
    };
    public static final Tester<Cell> CELL_NOT_SELECTED = new Tester<Cell>(){@Override
        public boolean test(Cell c) {
            return !c.selected;
        }
    };
    public static final Tester<Cell> CELL_NOT_OWNED = new Tester<Cell>(){
        @Override
        public boolean test(Cell c) {
            return (c.owner == null);
        }
    };
    
    private Territory owner;
    private boolean selected;
    
    private boolean updated = true;

    public Cell( int q, int r, int s) {
        super(q, r, s);
        selected = false;
    }
    
    public Cell( int q, int r) {
        super(q, r, -q-r);
    }
    
    public void touch(){
        this.updated = true;
    }

    public void select() {
        this.selected = true;
        touch();
    }
    public void deselect() {
        this.selected = false;
        touch();
    }
    public boolean isSelected() {
        return selected;
    }

    public Territory getOwner() {
        return owner;
    }
    public void setOwner(Territory owner) {
        this.owner = owner;
        touch();
    }

    
    @Override
    public Cell newTile(int... i) {
        return new Cell(i[0], i[1]);
    }
    
    @Override
    public void render(Graphics2D g, Layout layout) {
        if(!updated){
            return;
        } else {
            updated=false;
        }
        
        if (selected){
            g.setColor(Color.WHITE);
            g.fill(polygonCorners(layout));
        } else {
            if(owner != null && owner.owner != null){
                g.setColor(owner.owner.getColor());
            } else {
                g.setColor(Color.GRAY);
            }
            g.fill(polygonCorners(layout));
        }
        g.setColor(Color.black);
        g.draw(polygonCorners(layout));
        //draw boundaries
        
//        //draw info - Debug
//        g.setColor(Color.BLACK);
//        
//        Point p = toPixel(layout).toPoint();
//        double angle = 2.0 * Math.PI * (3 + layout.orientation.START_ANGLE) / 6.0;
//        int X = (int) (layout.size.x * Math.cos(angle)) + p.x +10;
//        int Y = p.y+4;
//        String s="";
////        s = String.format("[%d,%d]", x,y); //cell coordinates axial
//        if(owner != null){ //territory id
//            s = String.format("%d", owner.id); 
//        }
//        g.drawString(s, X, Y);
    }
    
}
