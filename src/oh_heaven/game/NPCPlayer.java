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
    public  SelectStrategy getSelectStrategy(){return this.selectStrategy;}
    public void setSelectStrategy(SelectStrategy newSelectStrategy){
        this.selectStrategy = newSelectStrategy;
    }

    @Override
    public void playLead(){
        getGame().setStatusText("Player " + getIndex() + " thinking ...");
        getGame().delay(thinkingTime);
        setSelected(selectStrategy.selectLeadCard(getHand(),myInfo));
    }

    @Override
    public void playFollow(){
        getGame().setStatusText("Player " + getIndex() + " thinking ...");
        getGame().delay(thinkingTime);
        setSelected(selectStrategy.selectCard(getHand(),myInfo));
    }

}
