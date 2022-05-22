package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public class NPC extends Player {
    private SelectStrategy selectStrategy;

    // Constructor
    public NPC(int index,SelectStrategy selectStrategy){
        super(index);
        this.selectStrategy = selectStrategy;
    }

    // Getter and Setter
    public SelectStrategy getSelectStrategyStrategy() {
        return selectStrategy;
    }
    public void setStrategy(SelectStrategy strategy) {
        this.selectStrategy = selectStrategy;
    }

    public Card selectCard(Suit lead){
        return selectStrategy.selectCard(lead,getHand());
    }

}
