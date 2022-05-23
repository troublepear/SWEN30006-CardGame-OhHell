package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

public class LegalSelectStrategy implements SelectStrategy {
    @Override
    public Card selectCard(Hand hand, Information myInfo) {
        Suit lead = myInfo.getLead();
        ArrayList<Card> sameSuit = hand.getCardsWithSuit(lead);
        if(sameSuit.size() != 0){
            return Helper.randomCard(sameSuit);
        }
        else {
            return Helper.randomCard(hand);
        }
    }

    @Override
    public Card selectLeadCard(Hand hand, Information myInfo){
        return Helper.randomCard(hand);
    }

}
