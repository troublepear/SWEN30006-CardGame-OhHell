package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public interface SelectStrategy {
    public Card selectCard(Suit lead, Hand hand);
}
