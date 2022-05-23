package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.List;

public class NPC extends Player {
    private SelectStrategy selectStrategy;
    private Information myInfo;

    // Constructor
    public NPC(int index,SelectStrategy selectStrategy){
        super(index);
        this.selectStrategy = selectStrategy;
        this.myInfo = new Information(index);
    }

    public Information getMyInfo(){
        return myInfo;
    }

    public Card selectCard(){
        return selectStrategy.selectCard(getHand(),myInfo);
    }

    public Card selectLeadCard(){
        return selectStrategy.selectLeadCard(getHand(),myInfo);
    }

}
