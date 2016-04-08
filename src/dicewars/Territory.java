/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dicewars;

import java.util.ArrayList;

/**
 *
 * @author ganter
 */
public class Territory {
        static int numOfTerritories=0;
        
        public final int id=++numOfTerritories;
        public Player owner;
        public byte strength;
        public final ArrayList<Cell> cells;

        public Territory() {
            strength = 0;
            cells = new ArrayList<>();
        }
        
        void add(Cell c){
            cells.add(c);
            c.setOwner(this);
        }
        
    }
