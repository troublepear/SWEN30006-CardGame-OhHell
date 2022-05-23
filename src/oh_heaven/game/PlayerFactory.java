package oh_heaven.game;
import utility.InvalidPlayerException;

public class PlayerFactory {

    public Player createPlayer(int index,String type) {
        switch (type){
            case "human": return new Interactive(index);
            case "random": return new NPC(index,new RandomSelectStrategy());
            case "legal": return new NPC(index,new LegalSelectStrategy());
            case "smart": return new NPC(index,new SmartSelectStrategy());
            default:
                try {
                    throw(new InvalidPlayerException(type));
                } catch (InvalidPlayerException e) {
                    e.printStackTrace();
                    System.out.println("[players." + index + "=" + type + "] is not a valid type. Check property file!");
                    System.exit(0);
                }
        }
        return null;
    }
}
