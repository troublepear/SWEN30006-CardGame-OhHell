package oh_heaven.game;

public class Information {
    private int index;
    private Suit lead;
    private Suit trump;

    // Constructor
    public Information(int index){
        this.index = index;
    }

    // Getter
    public Suit getLead(){
        return lead;
    }
    public Suit getTrump(){
        return trump;
    }

    // Update methods
    public void updateTrump(Suit trump){
        this.trump = trump;
    }
    public void updateLead(Suit lead){
        this.lead = lead;
    }

}
