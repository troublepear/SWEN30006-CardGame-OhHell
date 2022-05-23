package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.List;

public interface SelectStrategy {
    Card selectCard(Hand hand, Information myInfo);
    Card selectLeadCard(Hand hand, Information myInfo);
}
