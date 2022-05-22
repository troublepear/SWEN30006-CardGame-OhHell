package oh_heaven.game;

public class NPC extends Player {
    private String type;
    private SelectStrategy strategy;

    // Constructor
    public NPC(int index,String type){
        super(index);
        this.type = type;
    }

    // Getter and Setter
    public String getType() {
        return type;
    }
    public SelectStrategy getStrategy() {
        return strategy;
    }
    public void setStrategy(SelectStrategy strategy) {
        this.strategy = strategy;
    }

}
