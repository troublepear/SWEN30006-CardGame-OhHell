package oh_heaven.game;

import ch.aplu.jcardgame.Card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Information {
    private int index;
    private Suit lead;
    private Suit trump;
    private List<Card> cardList;

    // Constructor
    public Information(int index){
        this.index = index;
        this.cardList = new ArrayList<>();
    }

    // Getter and Setter
    public Suit getLead(){
        return lead;
    }
    public void setLead(Suit lead){
        this.lead = lead;
    }
    public Suit getTrump(){
        return trump;
    }
    public void setTrump(Suit trump){
        this.trump = trump;
    }
    public List<Card> getCardList(){
        return cardList;
    }
    public void updateCardList(Card card){
        this.cardList.add(card);
    }

    public void clearMyInfo(){
        lead = null;
        cardList.clear();
    }

}
