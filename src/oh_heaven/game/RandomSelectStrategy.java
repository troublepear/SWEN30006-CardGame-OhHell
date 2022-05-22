package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.List;

import static oh_heaven.game.Oh_Heaven.random;

public class RandomSelectStrategy implements SelectStrategy {

    @Override
    public Card selectCard(Hand hand, Information myInfo){
        int x = random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }


}
