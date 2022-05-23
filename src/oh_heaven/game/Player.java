package oh_heaven.game;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;

import java.awt.Color;
import java.awt.Font;
import java.util.*;
import java.util.stream.Collectors;

public abstract class Player {
    private int index;
    private int bid;
    private int score;
    private int trick;
    private Hand hand;
    private RowLayout rowLayout;

    // Constructor
    public Player(int index){
        this.index = index;
        this.score = 0;
    }

    // Getter and Setter
    public int getIndex(){
        return index;
    }
    public int getBid(){
        return bid;
    }
    public void setBid(int bid){
        this.bid = bid;
    }
    public int getScore(){
        return score;
    }
    public void setScore(int score){
        this.score = score;
    }
    public int getTrick(){return trick;}
    public void setTrick(int trick){this.trick=trick;}
    public Hand getHand(){
        return hand;
    }
    public void setHand(Hand hand){
        this.hand = hand;
    }
    public RowLayout getRowLayout(){
        return rowLayout;
    }
    public void setRowLayout(RowLayout rowLayout) {
        this.rowLayout = rowLayout;
    }

    // Other Methods
    public String getIndexString(){
        return String.valueOf(this.index);
    }

    public int getNextIndex(){
        if(this.index == 3){
            return 0;
        }
        else{
            return this.index+1;
        }
    }



}
