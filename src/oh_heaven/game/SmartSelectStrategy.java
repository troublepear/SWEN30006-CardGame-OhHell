package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.List;

public class SmartSelectStrategy implements SelectStrategy{

    @Override
    public Card selectCard(Hand hand, Information myInfo) {
        // Information
        Suit lead = myInfo.getLead();
        Suit trump = myInfo.getTrump();
        List<Card> curCard = myInfo.getCardList();

        List<Card> sameLead = hand.getCardsWithSuit(lead);
        // Has the card to follow lead
        if(sameLead.size() != 0){
            Card randomCard = sameLead.get(Helper.random.nextInt(sameLead.size()));
            return randomCard;
        }

        // No card to follow lead
        else {
            List<Card> sameTrump = hand.getCardsWithSuit(trump);
            // Has trump suit
            if(sameTrump.size() !=0){
                Card randomCard = sameTrump.get(Helper.random.nextInt(sameTrump.size()));
                return randomCard;
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
