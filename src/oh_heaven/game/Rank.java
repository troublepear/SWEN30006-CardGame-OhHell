package oh_heaven.game;

import ch.aplu.jcardgame.Card;

public enum Rank {
    // Reverse order of rank importance (see rankGreater() below)
    // Order of cards is tied to card images
    ACE, KING, QUEEN, JACK, TEN, NINE, EIGHT, SEVEN, SIX, FIVE, FOUR, THREE, TWO;

    // 理解: 比较两张牌的大小（只看数字）
    public static boolean rankGreater(Card card1, Card card2) {
        return card1.getRankId() < card2.getRankId();
    }
}


