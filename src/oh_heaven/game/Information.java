package oh_heaven.game;

import ch.aplu.jcardgame.Hand;
import java.util.HashMap;

public class Information {
    // Own information (every NPC player)
    private int index;
    // Selected Information (except random player)
    private Suit lead;
    private Suit trump;
    // Further information (smart player, can be expanded)
    private Hand trickHand;

    // Constructor
    public Information(int index){
        this.index = index;
    }

    // Getter
    public Suit getLead(){
        return lead;
    }
    public Suit getTrump(){
        return trump;
    }
    public Hand getTrickHand(){return trickHand;}

    // Update methods
    public void updateTrump(Suit trump){
        this.trump = trump;
    }
    public void updateLead(Suit lead){
        this.lead = lead;
    }
    public void updateTrickHand(Hand trickHand){
        this.trickHand = trickHand;
    }

}
