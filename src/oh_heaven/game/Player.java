package oh_heaven.game;

import ch.aplu.jcardgame.*;

public abstract class Player {
    private int index;
    private int bid;
    private int score;
    private int trick;
    private Hand hand;
    private RowLayout rowLayout;
    private String type;

    // Constructor
    public Player(int index){
        this.index = index;
    }

    // Getter and Setter
    public int getIndex(){
        return index;
    }
    public void setIndex(int index){
        this.index = index;
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
}
