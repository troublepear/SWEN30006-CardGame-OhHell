package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardListener;

public class HumanPlayer extends Player{

    // Constructor
    public HumanPlayer(Oh_Heaven game, int index){
        super(game,index);
    }

    @Override
    public void playLead(){
        this.getHand().setTouchEnabled(true);
        getGame().setStatus("Player " + getIndex() + " double-click on card to lead.");
        while(getSelected() == null) getGame().delay(100);
        getHand().setTouchEnabled(false);
    }

    @Override
    public void playFollow(){
        this.getHand().setTouchEnabled(true);
        getGame().setStatus("Player " + getIndex() + " double-click on card to follow.");
        while(getSelected() == null) getGame().delay(100);
        getHand().setTouchEnabled(false);
    }


    public void setupCardListener() {
        // Set up human player for interaction
        CardListener cardListener = new CardAdapter() // Human Player plays card
        {
            public void leftDoubleClicked(Card card) {
                setSelected(card);
                getHand().setTouchEnabled(false);
            }
        };
        getHand().addCardListener(cardListener);
    }


}
