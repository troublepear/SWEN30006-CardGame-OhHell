package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.List;

import static oh_heaven.game.Oh_Heaven.random;

public class LegalSelectStrategy implements SelectStrategy {
    @Override
    public Card selectCard(Hand hand, Information myInfo) {
        Suit lead = myInfo.getLead();
        List<Card> sameSuit = hand.getCardsWithSuit(lead);
        if(sameSuit.size() != 0){
            Card randomCard = sameSuit.get(random.nextInt(sameSuit.size()));
            return randomCard;
        }
        else {
            int x = random.nextInt(hand.getNumberOfCards());
            return hand.get(x);
        }
    }
}
