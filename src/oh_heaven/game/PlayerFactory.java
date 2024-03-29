package oh_heaven.game;
import utility.InvalidPlayerException;

public class PlayerFactory {
    public Player createPlayer(Oh_Heaven game, int index,String type) {
        switch (type){
            case "human": return new HumanPlayer(game,index);
            case "random": return new NPCPlayer(game,index,new RandomSelectStrategy());
            case "legal": return new NPCPlayer(game,index,new LegalSelectStrategy());
            case "smart": return new NPCPlayer(game,index,new SmartSelectStrategy());
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
