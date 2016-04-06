/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dicewars;

import java.awt.Color;

/**
 *
 * @author ganter
 */
public class Player {
    public static final Color[] COLORS = new Color[]{
        new Color(255,  0,  0),
        new Color(  0,255,  0),
        new Color(  0,  0,255),
        new Color(255,255,  0),
        new Color(  0,255,255),
        new Color(255,  0,255),
        new Color(255,255,255)
    };
    
    private Color color;
    private String name;
    Territory[] territories;

    public Player(){};
    
    public Player(String name, Color color) {
        this.color = color;
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
    
}
