package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public class RandomSelectStrategy implements SelectStrategy {

    @Override
    public Card selectCard(Hand hand,Information myInfo){
        return Helper.randomCard(hand);
    }

    @Override
    public Card selectLeadCard(Hand hand,Information myInfo){
        return Helper.randomCard(hand);
    }
}
