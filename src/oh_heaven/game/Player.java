package oh_heaven.game;

import ch.aplu.jcardgame.*;

public abstract class Player {
    private int index;
    private int bid;
    private int score;
    private int trick;
    private Hand hand;
    private SelectStrategy strategy;

    // Constructor
    public Player(int index){
        this.index = index;
    }

    // Getter and Setter
    private int getIndex(){
        return index;
    }
    private void setIndex(int index){
        this.index = index;
    }
    private int getBid(){
        return bid;
    }
    private void setBid(int bid){
        this.bid = bid;
    }
    private int getScore(){
        return score;
    }
    private void setScore(int score){
        this.score = score;
    }
    private int getTrick(){return trick;}
    private void setTrick(int trick){this.trick=trick;}
    private Hand getHand(){
        return hand;
    }
    private void setHand(Hand hand){
        this.hand = hand;
    }


}
