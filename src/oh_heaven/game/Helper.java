package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

public class Helper{

    static int seed;
    static Random random;

    public Helper(Properties properties){
        if(properties.getProperty("seed").equals(null) || properties.getProperty("seed").equals("")){
            this.random = new Random();
        }
        else{
            this.seed = Integer.parseInt(properties.getProperty("seed"));
            this.random = new Random(seed);
        }
    }

    // return random Enum value
    public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    // return random Card from Hand
    public static Card randomCard(Hand hand){
        int x = random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }

    // return random Card from ArrayList
    public static Card randomCard(ArrayList<Card> list){
        int x = random.nextInt(list.size());
        return list.get(x);
    }

    // compare the rank of two cards
    public static boolean rankGreater(Card card1, Card card2) {
        return card1.getRankId() < card2.getRankId();
    }
}
