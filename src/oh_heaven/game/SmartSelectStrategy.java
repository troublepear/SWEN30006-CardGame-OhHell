package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

public class SmartSelectStrategy implements SelectStrategy{
    @Override
    public Card selectCard(Hand hand, Information myInfo) {
        // Information
        Suit lead = myInfo.getLead();
        Suit trump = myInfo.getTrump();
        Hand trickHand = myInfo.getTrickHand();
        trickHand.sort(Hand.SortType.SUITPRIORITY, true);

        ArrayList<Card> sameLead = hand.getCardsWithSuit(lead);
        // Has the card to follow lead
        if(sameLead.size() != 0){
            System.out.println(trickHand);
            return Helper.randomCard(sameLead);
        }

        // No card to follow lead
        else {
            ArrayList<Card> sameTrump = hand.getCardsWithSuit(trump);
            // Has trump suit
            if(sameTrump.size() !=0){
                return Helper.randomCard(sameTrump);
            }
            // No trump suit
            else{
                return Helper.randomCard(hand);
            }
        }
    }

    @Override
    public Card selectLeadCard(Hand hand, Information myInfo){
        return Helper.randomCard(hand);
    }

}
