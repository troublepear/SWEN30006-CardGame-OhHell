package oh_heaven.game;

import ch.aplu.jcardgame.Card;

public class NPCPlayer extends Player {
    private final int thinkingTime = 2000;
    private SelectStrategy selectStrategy;
    private Information myInfo;

    // Constructor
    public NPCPlayer(Oh_Heaven game, int index, SelectStrategy selectStrategy){
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
