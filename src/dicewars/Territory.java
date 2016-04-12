/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dicewars;

import gameTools.map.Layout;
import gameTools.map.Tile;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author ganter
 */
public class Territory {
        static int numOfTerritories=0;
        private static final int maxStrength = 8;
        public final int id=++numOfTerritories;
        public Player owner;
        public int strength;
        public final ArrayList<Cell> cells; //inside
        private Cell[] hull;                //border, inside and border are disjunkt
        private boolean highlighted;
        private boolean updated = true;

        public Territory() {
            strength = 0;
            cells = new ArrayList<>();
            hull = new Cell[0];
        }
        
        void add(Cell c){
            cells.add(c);
            c.setOwner(this);
        }
        
        public void setOwner(Player p) {
            this.owner = p;
            p.addTerritory(this);
        }
        
//        public void calcBoundary(Layout layout){
//            ArrayList<Point> points = new ArrayList();
//            for(Cell c : cells){
//                points.add(new Point(c.x, c.y));
//            }
//            points = QuickHull.quickHull(points);
//            ArrayList<Cell> h = new ArrayList<>();
//            for(Point p: points){
//                for (Iterator<Cell> iterator = cells.iterator(); iterator.hasNext();) {
//                    Cell c = iterator.next();
//                    if(new Cell(p.x,p.y).equals(c)){
//                        h.add(c);
//                        iterator.remove();
//                    }
//                }
//            }
//            hull = h.toArray(hull);
//        }
        /**
         * adds the amount of strength(dices). The strength cannot get higher than the maximum(def.: 8).
         * @param i the amount of strength to add.
         * @return the amount of strength used up of i.
         */
        public int addStrength(int i){
            strength+=i;
            if(strength > 8){
                int ret = 8-(strength-i);
                strength = 8;
                return ret;
            } else {
                return i;
            }
        }
        
        public void touch(){
            this.updated = true;
            for(Cell c : cells){
                c.touch();
            }
        }
        
        public Point getCenter(){
            double sumX=0, sumY=0;
            double cellN = cells.size();
            for(Cell c: cells){
                sumX += c.x / cellN;
                sumY += c.y / cellN; 
            }
            return new Point((int)sumX,(int)sumY);
        }
        
        public int getStrength(){
            return this.strength;
        }

        public void highlight() {
            this.highlighted = true;
            for(Cell c : cells){
                c.highlight();
            }
            this.updated = true;
        }
        public void unLight() {
            this.highlighted = false;
            for(Cell c : cells){
                c.unLight();
            }
            touch();
        }
        public boolean isHighlighted() {
            return highlighted;
        }
        
        public void render(Graphics2D g, Layout layout){
            if(!updated){
                return;
            } else {
                updated=false;
            }
            
            if(owner != null ){
                g.setColor(owner.getColor());
            } else {
                g.setColor(Color.CYAN);
            }
            
            for(Cell c : cells){
                c.render(g, layout);
            }
            
            //draw strength
            g.setColor(Color.WHITE);
            Cell c = new Cell(getCenter().x, getCenter().y);
            Point p = c.toPixel(layout).toPoint();
            int X = p.x;
            int Y = p.y+15;
            String s = String.format("%d", strength);
            g.setFont(new Font("Courier New", Font.PLAIN, 20));
            g.drawString(s, X, Y);
        }
        
        @Override
        public boolean equals(Object o){
            if (o != null && o instanceof Territory){
                Territory t = (Territory) o;
                return (id == t.id);
            } else {
                return false;
            }
        }
        
    }
