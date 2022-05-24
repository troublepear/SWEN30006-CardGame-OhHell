package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.List;

public class NPC extends Player {
    private final int thinkingTime = 2000;
    private SelectStrategy selectStrategy;
    private Information myInfo;

    // Constructor
    public NPC(Oh_Heaven game, int index,SelectStrategy selectStrategy){
        super(game,index);
        this.selectStrategy = selectStrategy;
        this.myInfo = new Information(index);
    }

    // Getter and Setter
    public Information getMyInfo(){
        return myInfo;
    }

    @Override
    public void play(boolean isLead){
        getGame().setStatusText("Player " + getIndex() + " thinking ...");
        getGame().delay(thinkingTime);
        if(isLead){
            setSelected(selectLeadCard());
        }
        else{
            setSelected(selectCard());
        }
    }

    public Card selectCard(){
        return selectStrategy.selectCard(getHand(),myInfo);
    }

    public Card selectLeadCard(){
        return selectStrategy.selectLeadCard(getHand(),myInfo);
    }


}
