/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dicewars;

import gameTools.state.InputManager;
import java.awt.Color;

/**
 *
 * @author ganter
 */
public class PlayerHuman extends Player{
    
    public PlayerHuman(){
        super();
    };
    
    public PlayerHuman(Color color, int team) {
        super(color, team);
    }
    public PlayerHuman(Color color) {
        super(color);
    }
    
    @Override
    public void selectBase(GameBoard board, InputManager input) throws EndOfTurnException{
        while(true){
            try{
                if(input.isKeyTyped("Enter")){
                    throw new EndOfTurnException();
                }
                if(input.isClicked("ButtonLeft")){
                    Territory t = board.fromPixel(input.getMousePos().x, input.getMousePos().y).getOwner();
                    if(isOwner(t) && t.getStrength()>1){
                        board.selectBase(t);
                        System.out.printf("Player%d selecting BASE   territory %d%n",getId(), t.id);
                        return;
                    }
                }
                Thread.sleep(50);
            } catch(NullPointerException | InterruptedException ignore){}
        }
    }
    
    @Override
    public void selectTarget(GameBoard board, InputManager input) throws EndOfTurnException{
        while(true){
            try{
                if(input.isKeyTyped("Enter")){
                    throw new EndOfTurnException();
                }
                if(input.isClicked("ButtonLeft")){
                    Territory t = board.fromPixel(input.getMousePos().x, input.getMousePos().y).getOwner();
                    
                    if(board.getSelectedBase().equals(t)){
                        board.unSelectBase();
                        selectBase(board, input);
                    }else if(board.getSelectedBase().isNeighbor(t, board) && (t.getOwner().team != this.team)){
                        board.selectTarget(t);
                        System.out.printf("Player%d selecting TARGET territory %d%n",getId(), t.id);
                        return;
                    }
                }
                Thread.sleep(50);
            } catch(NullPointerException | InterruptedException ignore){}
        }
    }
}
