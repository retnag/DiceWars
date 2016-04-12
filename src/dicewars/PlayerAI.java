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
public class PlayerAI extends Player {
 
    public PlayerAI(){
        super();
    };
    
    public PlayerAI(Color color, int team) {
        super(color, team);
    }
    public PlayerAI(Color color) {
        super(color);
    }
    
}
