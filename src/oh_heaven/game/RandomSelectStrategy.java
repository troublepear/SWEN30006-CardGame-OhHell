package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import static oh_heaven.game.Oh_Heaven.random;

public class RandomSelectStrategy implements SelectStrategy {

    @Override
    public Card selectCard(Suit lead, Hand hand){
        int x = random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }


}
