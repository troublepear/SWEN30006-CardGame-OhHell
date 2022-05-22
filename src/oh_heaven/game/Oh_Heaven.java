package oh_heaven.game;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import utility.BrokeRuleException;

import java.awt.Color;
import java.awt.Font;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class Oh_Heaven extends CardGame {
	final String trumpImage[] = {"bigspade.gif","bigheart.gif","bigdiamond.gif","bigclub.gif"}; // 理解：游戏左上角本轮的主导花色
	static public final int seed = 30006;
	static final Random random = new Random(seed);
	private final String version = "1.0";
	public final int nbStartCards;
	private boolean enforceRules;
	public final int nbRounds;
	public final int nbPlayers = 4;
	private ArrayList<Player> players = new ArrayList<>();
	private Hand[] hands;

	public final int madeBidBonus = 10;
	private final int handWidth = 400;
	private final int trickWidth = 40;
	private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
	private final Location[] handLocations = {new Location(350, 625), new Location(75, 350), new Location(350, 75), new Location(625, 350)};
	private final Location[] scoreLocations = {new Location(575, 675), new Location(25, 575), new Location(575, 25), new Location(575, 575)};
	private Actor[] scoreActors = {null, null, null, null };
	private final Location trickLocation = new Location(350, 350);
	private final Location textLocation = new Location(350, 450);
	private final int thinkingTime = 2000;

	private Location hideLocation = new Location(-500, - 500);
	private Location trumpsActorLocation = new Location(50, 50);
	private int[] scores = new int[nbPlayers];
	private int[] tricks = new int[nbPlayers];
	private int[] bids = new int[nbPlayers];

	Font bigFont = new Font("Serif", Font.BOLD, 36);

	public Oh_Heaven(Properties properties) {
		super(700, 700, 30);
		setTitle("Oh_Heaven (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
		setStatusText("Initializing...");

		// Initialize properties
		nbStartCards = Integer.parseInt(properties.getProperty("nbStartCards"));
		enforceRules = Boolean.parseBoolean(properties.getProperty("enforceRules"));
		nbRounds = Integer.parseInt(properties.getProperty("rounds"));
		// Initialize players
		for(int i=0;i<nbPlayers;i++){
			String currentType = properties.getProperty("players."+i);
			if(currentType == "human"){
				players.add(new Interactive(i));
			}
			else{
				players.add(new NPC(i,currentType));
			}
		}
		// Initialize scores
		initScores();
		initScore();

		// For each round
		for (int i=0; i <nbRounds; i++) {
			initTricks();
			initRound();
			playRound();
			updateScores();
		};
		for (int i=0; i <nbPlayers; i++) updateScore(i);
		int maxScore = 0;
		for (int i = 0; i <nbPlayers; i++) if (scores[i] > maxScore) maxScore = scores[i];
		Set <Integer> winners = new HashSet<Integer>();
		for (int i = 0; i <nbPlayers; i++) if (scores[i] == maxScore) winners.add(i);
		String winText;
		if (winners.size() == 1) {
			winText = "Game over. Winner is player: " +
					winners.iterator().next();
		}
		else {
			winText = "Game Over. Drawn winners are players: " +
					String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toSet()));
		}
		addActor(new Actor("sprites/gameover.gif"), textLocation);
		setStatusText(winText);
		refresh();
	}

	/** Initialize Methods */
	private void initScores() {
		for (Player player:players){
			player.setScore(0);
		}

//		for (int i = 0; i < nbPlayers; i++) {
//			scores[i] = 0;
//		}
	}

	private void initScore() {
		for(Player player:players){
			int index = player.getIndex();
			String text = "[" + String.valueOf(player.getScore()) + "]" + String.valueOf(player.getTrick()) + "/" + String.valueOf(player.getBid());
			scoreActors[index] = new TextActor(text,Color.WHITE,bgColor,bigFont);
			addActor(scoreActors[index],scoreLocations[index]);
		}

//		// 理解：初始化分数 -> 屏幕显示
//		for (int i = 0; i < nbPlayers; i++) {
//			String text = "[" + String.valueOf(scores[i]) + "]" + String.valueOf(tricks[i]) + "/" + String.valueOf(bids[i]);
//			scoreActors[i] = new TextActor(text, Color.WHITE, bgColor, bigFont);
//			addActor(scoreActors[i], scoreLocations[i]);
//		}
	}

	private void initTricks() {
		for(Player player:players){
			player.setTrick(0);
		}
//		// 理解：初始化所有玩家trick值为0
//		for (int i = 0; i < nbPlayers; i++) {
//			tricks[i] = 0;
//		}
	}

	private Card selected;
	private void initRound() {
		for(Player player:players){
			player.setHand(new Hand(deck));
		}
		dealingOut(players,nbStartCards);
		for(Player player:players){
			player.getHand().sort(Hand.SortType.SUITPRIORITY, true);
		}
		// Set up human player for interaction
		// Human Player plays card
		CardListener cardListener = new CardAdapter() {
			public void leftDoubleClicked(Card card) {
				selected = card;
				for(Player player:players){
					if(player.getClass().getSimpleName() == "Interactive"){
						player.getHand().setTouchEnabled(false);
					}
				}
			}
		};
		for(Player player:players){
			if(player.getClass().getSimpleName() == "Interactive"){
				player.getHand().addCardListener(cardListener);
			}
		}
		// graphics
		for(Player player:players){
			int index = player.getIndex();
			Hand currentHand = player.getHand();
			player.setRowLayout(new RowLayout(handLocations[index], handWidth));
			player.getRowLayout().setRotationAngle(90 * index);
			currentHand.setView(this,player.getRowLayout());
			currentHand.setTargetArea(new TargetArea(trickLocation));
			currentHand.draw();
		}
	}
//	private void initRound(){
//		hands = new Hand[nbPlayers];
//		// 理解：为每个玩家分牌
//		for (int i = 0; i < nbPlayers; i++) {
//			hands[i] = new Hand(deck);
//		}
//		dealingOut(hands, nbPlayers, nbStartCards);
//		for (int i = 0; i < nbPlayers; i++) {
//			hands[i].sort(Hand.SortType.SUITPRIORITY, true); // 理解：SUITPRIORITY -> 手中牌按照花色排序
//		}
//		// Set up human player for interaction
//		CardListener cardListener = new CardAdapter()  // Human Player plays card
//		{
//			public void leftDoubleClicked(Card card) { selected = card; hands[0].setTouchEnabled(false); }
//		};
//		hands[0].addCardListener(cardListener);
//		// graphics
//		RowLayout[] layouts = new RowLayout[nbPlayers];
//		for (int i = 0; i < nbPlayers; i++) {
//			layouts[i] = new RowLayout(handLocations[i], handWidth);
//			layouts[i].setRotationAngle(90 * i);
//			// layouts[i].setStepDelay(10);
//			hands[i].setView(this, layouts[i]);
//			hands[i].setTargetArea(new TargetArea(trickLocation));
//			hands[i].draw();
//		}
//		// for (int i = 1; i < nbPlayers; i++) // This code can be used to visually hide the cards in a hand (make them face down)
//		// hands[i].setVerso(true);			// You do not need to use or change this code.
//		// End graphics
//	}

	private void dealingOut(ArrayList<Player> players, int nbCardsPerPlayer){
		Hand pack = deck.toHand(false);
		for (int i = 0; i < nbCardsPerPlayer; i++) {
			for(Player player:players){
				if(pack.isEmpty()) return;
				Card dealt = randomCard(pack);
				dealt.removeFromHand(false);
				player.getHand().insert(dealt,false);
			}
		}
	}
//	private void dealingOut(Hand[] hands, int nbPlayers, int nbCardsPerPlayer) {
//		Hand pack = deck.toHand(false);
//		// pack.setView(Oh_Heaven.this, new RowLayout(hideLocation, 0));
//		for (int i = 0; i < nbCardsPerPlayer; i++) {
//			for (int j=0; j < nbPlayers; j++) {
//				if (pack.isEmpty()) return; // 理解：先检查牌是否发完，牌发完了则结束这个method
//				Card dealt = randomCard(pack); // 理解：随机从pack里取一张牌
//				// System.out.println("Cards = " + dealt);
//				dealt.removeFromHand(false); // 理解：将这张牌从pack里删掉
//				hands[j].insert(dealt, false); // 理解：将从pack里随机取的牌发给hands[j]（i.e. 第j个玩家手里）
//				// dealt.transfer(hands[j], true);
//			}
//		}
//	}

	private void playRound(){
		// Select and display trump suit
		final Suit trumps = randomEnum(Suit.class);
		final Actor trumpsActor = new Actor("sprites/"+trumpImage[trumps.ordinal()]);
		addActor(trumpsActor, trumpsActorLocation);

		// End trump suit
		Hand trick;
		Player winner;
		Card winningCard;
		Suit lead;
		Player nextPlayer = players.get(random.nextInt(nbPlayers));
		initBids(trumps, nextPlayer);

		// Display on screen
		for(Player player:players){
			updateScore(player);
		}
		// Select lead depending on player type
		for (int i=0; i<nbStartCards; i++){
			trick = new Hand(deck);
			selected = null;
			if(nextPlayer.getClass().getSimpleName() == "Interactive"){
				nextPlayer.getHand().setTouchEnabled(true);
				setStatus("Player " + nextPlayer.getIndex() + " double-click on card to lead.");
				while (null == selected) delay(100);
			}
			else {
				setStatusText("Player " + nextPlayer.getIndex() + " thinking ...");
				delay(thinkingTime);
				selected = randomCard(nextPlayer.getHand());
			}

			// Lead with selected card
			trick.setView(this,new RowLayout(trickLocation,(trick.getNumberOfCards()+2)*trickWidth));
			trick.draw();
			selected.setVerso(false);
			// No restriction on the card being lead
			lead = (Suit) selected.getSuit();
			selected.transfer(trick, true); // transfer to trick (includes graphic effect)
			winner = nextPlayer;
			winningCard = selected;
			// End lead
			for(int j = 0; j < nbPlayers; j++ ){
				if ( nextPlayer.getIndex() == nbPlayers-1 ){
					nextPlayer = players.get(0);
				}
			}



			}




	}
//	private void playRound() {
//		// Select and display trump suit
//		final Suit trumps = randomEnum(Suit.class);
//		final Actor trumpsActor = new Actor("sprites/"+trumpImage[trumps.ordinal()]);
//		addActor(trumpsActor, trumpsActorLocation);
//		// End trump suit
//		Hand trick;
//		int winner;
//		Card winningCard;
//		Suit lead;
//		int nextPlayer = random.nextInt(nbPlayers); // randomly select player to lead for this round
//		initBids(trumps, nextPlayer);
//		// initScore();
//		for (int i = 0; i < nbPlayers; i++) updateScore(i);
//		for (int i = 0; i < nbStartCards; i++) {
//			trick = new Hand(deck);
//			selected = null;
//			// if (false) {
//			if (0 == nextPlayer) {  // Select lead depending on player type
//				// 理解：手动玩家没出牌前，要一直等着
//				hands[0].setTouchEnabled(true);
//				setStatus("Player 0 double-click on card to lead.");
//				while (null == selected) delay(100);
//			} else {
//				// 理解：legal player，给2000time，直接出牌
//				setStatusText("Player " + nextPlayer + " thinking...");
//				delay(thinkingTime);
//				selected = randomCard(hands[nextPlayer]);
//			}
//			// Lead with selected card
//			trick.setView(this, new RowLayout(trickLocation, (trick.getNumberOfCards()+2)*trickWidth));
//			trick.draw();
//			selected.setVerso(false);
//			// No restrictions on the card being lead
//			lead = (Suit) selected.getSuit();
//			selected.transfer(trick, true); // transfer to trick (includes graphic effect)
//			winner = nextPlayer;
//			winningCard = selected;
//			// End Lead
//			for (int j = 1; j < nbPlayers; j++) {
//				if (++nextPlayer >= nbPlayers) nextPlayer = 0;  // From last back to first
//				selected = null;
//				// if (false) {
//				if (0 == nextPlayer) {
//					hands[0].setTouchEnabled(true);
//					setStatus("Player 0 double-click on card to follow.");
//					while (null == selected) delay(100);
//				} else {
//					setStatusText("Player " + nextPlayer + " thinking...");
//					delay(thinkingTime);
//					selected = randomCard(hands[nextPlayer]);
//				}
//				// Follow with selected card
//				trick.setView(this, new RowLayout(trickLocation, (trick.getNumberOfCards()+2)*trickWidth));
//				trick.draw();
//				selected.setVerso(false);  // In case it is upside down
//				// Check: Following card must follow suit if possible
//				if (selected.getSuit() != lead && hands[nextPlayer].getNumberOfCardsWithSuit(lead) > 0) {
//					// Rule violation
//					String violation = "Follow rule broken by player " + nextPlayer + " attempting to play " + selected;
//					System.out.println(violation);
//					if (enforceRules)
//						try {
//							throw(new BrokeRuleException(violation));
//						} catch (BrokeRuleException e) {
//							e.printStackTrace();
//							System.out.println("A cheating player spoiled the game!");
//							System.exit(0);
//						}
//				}
//				// End Check
//				selected.transfer(trick, true); // transfer to trick (includes graphic effect)
//				System.out.println("winning: " + winningCard);
//				System.out.println(" played: " + selected);
//				// System.out.println("winning: suit = " + winningCard.getSuit() + ", rank = " + (13 - winningCard.getRankId()));
//				// System.out.println(" played: suit = " +    selected.getSuit() + ", rank = " + (13 -    selected.getRankId()));
//				if ( // beat current winner with higher card
//						(selected.getSuit() == winningCard.getSuit() && rankGreater(selected, winningCard)) ||
//								// trumped when non-trump was winning
//								(selected.getSuit() == trumps && winningCard.getSuit() != trumps)) {
//					System.out.println("NEW WINNER");
//					winner = nextPlayer;
//					winningCard = selected;
//				}
//				// End Follow
//			}
//			delay(600);
//			trick.setView(this, new RowLayout(hideLocation, 0));
//			trick.draw();
//			nextPlayer = winner;
//			setStatusText("Player " + nextPlayer + " wins trick.");
//			tricks[nextPlayer]++;
//			updateScore(nextPlayer);
//		}
//		removeActor(trumpsActor);
//	}

	private void initBids(Suit trumps, Player nextPlayer) {
		int total = 0;
		int nextPlayerIndex = nextPlayer.getIndex();
		for(int i=nextPlayerIndex;i<nextPlayerIndex+nbPlayers;i++){
			int iP = i % nbPlayers;
			players.get(iP).setBid(nbStartCards / 4 + random.nextInt(2));
			total += players.get(iP).getBid();
		}
		if(total == nbStartCards){
			int iP = (nextPlayerIndex + nbPlayers) % nbPlayers;
			if(players.get(iP).getBid() == 0){
				players.get(iP).setBid(1);
			}
			else{
				int newBid = players.get(iP).getBid();
				newBid += random.nextBoolean() ? -1:1;
				players.get(iP).setBid(newBid);
			}
		}
	}
//	private void initBids(Suit trumps, int nextPlayer) {
//		int total = 0;
//		// 理解：nextPlayer初始时为开始玩家，例如：开始为2号玩家，则循环 i=2;i<6（2+4）;i++，正好循环4次
//		for (int i = nextPlayer; i < nextPlayer + nbPlayers; i++) {
//			// 理解：iP=nextPlayer是几号玩家。i % nbPlayers，例如：i=5，则5 % 4 = 1，说明nextPlayer=5为1号玩家
//			int iP = i % nbPlayers;
//			// 理解：nbStartCards=13，13/4=3，再随机加0或1，则玩家的bid随机等于3或4
//			bids[iP] = nbStartCards / 4 + random.nextInt(2);
//			total += bids[iP]; // 理解：计算所有人bids总数
//		}
//		// 理解： 假设total bids等于13，则需要让最后一个玩家bid变大/变小（每round出一张牌，一共13把在一个round里，因此total=13意味着每个人都可能达到bid）
//		if (total == nbStartCards) {  // Force last bid so not every bid possible
//			int iP = (nextPlayer + nbPlayers) % nbPlayers; // 理解：计算最后一个玩家是几号玩家
//			if (bids[iP] == 0) {
//				bids[iP] = 1;
//			} else {
//				bids[iP] += random.nextBoolean() ? -1 : 1;
//			}
//		}
//	}


	private void updateScores() {
		for (int i = 0; i < nbPlayers; i++) {
			scores[i] += tricks[i];
			if (tricks[i] == bids[i]) scores[i] += madeBidBonus; // 理解：如果回合结束，trick数=bid数，额外加10分
		}
	}

	private void updateScore(Player player) {
		int index = player.getIndex();
		removeActor(scoreActors[index]);
		String text = "[" + String.valueOf(players.get(index).getScore()) + "]"
				+ String.valueOf(players.get(index).getTrick()) + "/"
				+ String.valueOf(players.get(index).getBid());
		scoreActors[index] = new TextActor(text, Color.WHITE, bgColor, bigFont);
		addActor(scoreActors[index], scoreLocations[index]);
	}
//	private void updateScore(int player) {
//		// 理解：更新分数 -> 屏幕显示
//		removeActor(scoreActors[player]);
//		String text = "[" + String.valueOf(scores[player]) + "]" + String.valueOf(tricks[player]) + "/" + String.valueOf(bids[player]);
//		scoreActors[player] = new TextActor(text, Color.WHITE, bgColor, bigFont);
//		addActor(scoreActors[player], scoreLocations[player]);
//	}




	/** Other Methods */
	public void setStatus(String string) {
		setStatusText(string);
	}

	/** Other Methods */
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


	// 理解: 比较两张牌的大小（只看数字）
	public boolean rankGreater(Card card1, Card card2) {
	  return card1.getRankId() < card2.getRankId();
	}

}
