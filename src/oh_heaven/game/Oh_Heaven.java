package oh_heaven.game;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import utility.BrokeRuleException;
import utility.InvalidPlayerException;

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
	public final int madeBidBonus = 10;
	private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover");

	// Location
	private final Location[] handLocations = {new Location(350, 625), new Location(75, 350), new Location(350, 75), new Location(625, 350)};
	private final Location[] scoreLocations = {new Location(575, 675), new Location(25, 575), new Location(575, 25), new Location(575, 575)};
	private final Location trickLocation = new Location(350, 350);
	private final Location textLocation = new Location(350, 450);
	private Location hideLocation = new Location(-500, - 500);
	private Location trumpsActorLocation = new Location(50, 50);
	// Actors
	private Actor[] scoreActors = {null, null, null, null };
	// Other
	private final int handWidth = 400;
	private final int trickWidth = 40;
	private final int thinkingTime = 2000;
	Font bigFont = new Font("Serif", Font.BOLD, 36);

	public Oh_Heaven(Properties properties) {
		super(700, 700, 30);
		setTitle("Oh_Heaven (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
		setStatusText("Initializing...");

		// Initialize game from property file
		nbStartCards = Integer.parseInt(properties.getProperty("nbStartCards"));
		enforceRules = Boolean.parseBoolean(properties.getProperty("enforceRules"));
		nbRounds = Integer.parseInt(properties.getProperty("rounds"));
		for(int i=0;i<nbPlayers;i++){
			String currentType = properties.getProperty("players."+i);
			if(currentType.equals("human")){
				players.add(new Interactive(i));
			}
			else if(currentType.equals("random")){
				players.add(new NPC(i,new RandomSelectStrategy()));
			}
			else if(currentType.equals("legal")){
				players.add(new NPC(i,new LegalSelectStrategy()));
			}
			else if(currentType.equals("smart")){
				players.add(new NPC(i,new SmartSelectStrategy()));
			}
			else{
				try {
					throw(new InvalidPlayerException(currentType));
				} catch (InvalidPlayerException e) {
					e.printStackTrace();
					System.out.println("[players."+ i +"=" + currentType+"] is not a valid type. Check property file!");
					System.exit(0);
				}
			}
		}

		// Initialize scores
		initScore();

		// For each round
		for (int i=0; i <nbRounds; i++) {
			initTricks();
			initRound();
			playRound();
			updateScores();
		};

		for (Player player:players) updateScore(player);

		// Find the winner
		int maxScore = 0;
		for(Player player:players){
			if(player.getScore() > maxScore){
				maxScore = player.getScore();
			}
		}
		Set <Player> winners = new HashSet<>();
		for(Player player:players){
			if(player.getScore() == maxScore){
				winners.add(player);
			}
		}
		String winText;
		if(winners.size() == 1){
			winText = "Game over. Winner is player: " + winners.iterator().next().getIndex();
		}
		else{
			winText = "Game Over. Drawn winners are players: " +
					String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toSet()));
		}
		addActor(new Actor("sprites/gameover.gif"), textLocation);
		setStatusText(winText);
		refresh();
	}

	/** Graphics - Display Methods */
	private void initScore() {
		for(Player player:players){
			int index = player.getIndex();
			String text = "[" + String.valueOf(player.getScore()) + "]" + String.valueOf(player.getTrick())
						+ "/" + String.valueOf(player.getBid());
			scoreActors[index] = new TextActor(text,Color.WHITE,bgColor,bigFont);
			addActor(scoreActors[index],scoreLocations[index]);
		}
	}

	private void updateScore(Player player) {
		int index = player.getIndex();
		removeActor(scoreActors[index]);
		String text = "[" + String.valueOf(player.getScore()) + "]" + String.valueOf(player.getTrick())
					+ "/" + String.valueOf(player.getBid());
		scoreActors[index] = new TextActor(text, Color.WHITE, bgColor, bigFont);
		addActor(scoreActors[index], scoreLocations[index]);
	}

	public void setStatus(String string) {
		setStatusText(string);
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
		for(Player player:players) {
			if(player instanceof Interactive){
				CardListener cardListener = new CardAdapter(){
					public void leftDoubleClicked(Card card){
						selected = card;
						player.getHand().setTouchEnabled(false);
					}
				};
				player.getHand().addCardListener(cardListener);
			}
		}
		// graphics
		for(Player player:players){
			int curIndex = player.getIndex();
			Hand curHand = player.getHand();
			player.setRowLayout(new RowLayout(handLocations[curIndex], handWidth));
			player.getRowLayout().setRotationAngle(90 * curIndex);
			curHand.setView(this,player.getRowLayout());
			curHand.setTargetArea(new TargetArea(trickLocation));
			curHand.draw();
		}
	}

	/** Initialize Methods */
	private void initTricks() {
		for(Player player:players){
			player.setTrick(0);
		}
	}

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

	private void playRound(){
		// Select and display trump suit
		final Suit trumps = randomEnum(Suit.class);
		final Actor trumpsActor = new Actor("sprites/"+trumpImage[trumps.ordinal()]);
		addActor(trumpsActor, trumpsActorLocation);

		for(Player player:players){
			if(player instanceof NPC){
				((NPC) player).getMyInfo().setTrump(trumps);
			}
		}


		Hand trick;
		Player winner;
		Card winningCard;
		Suit lead;

		// Randomly select a player to lead
		Player nextPlayer = players.get(random.nextInt(nbPlayers));

		// Initialize the bids for each player
		initBids(trumps, nextPlayer);

		// Update score display on the screen
		for(Player player:players){
			updateScore(player);
		}

		// Lead
		for (int i=0; i<nbStartCards; i++){
			trick = new Hand(deck);
			selected = null;

			// Select lead depending on player type
			if(nextPlayer instanceof Interactive){
				nextPlayer.getHand().setTouchEnabled(true);
				setStatus("Player " + nextPlayer.getIndex() + " double-click on card to lead.");
				while (selected == null) delay(100);
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

			// Set lead as winner and winning card for now
			winner = nextPlayer;
			winningCard = selected;

			// Record information
			for(Player player:players){
				if(player instanceof NPC){
					((NPC) player).getMyInfo().setLead(lead);
					((NPC) player).getMyInfo().updateCardList(selected);
				}
			}

			for(int j = 1; j < nbPlayers; j++ ){
				nextPlayer = players.get(nextPlayer.getNextIndex());
				selected = null;
				if(nextPlayer instanceof Interactive){
					nextPlayer.getHand().setTouchEnabled(true);
					setStatus("Player " + nextPlayer.getIndex() + " double-click on card to follow.");
					while (null == selected) delay(100);
				}
				else{
					setStatusText("Player " + nextPlayer.getIndex() + " thinking...");
					delay(thinkingTime);
					selected = ((NPC) nextPlayer).selectCard();
				}
				// Record information
				for(Player player:players){
					if(player instanceof NPC){
						((NPC) player).getMyInfo().updateCardList(selected);
					}
				}
				// Follow with selected card
				trick.setView(this,new RowLayout(trickLocation,(trick.getNumberOfCards()+2)*trickWidth));
				trick.draw();
				selected.setVerso(false);

				// Check: Following card must follow suit if possible
				checkViolation(lead,nextPlayer);
				// Check: Winner and winning card
				selected.transfer(trick, true); // transfer to trick (includes graphic effect)
				System.out.println("winning: " + winningCard);
				System.out.println(" played: " + selected);
				if ( // beat current winner with higher card
						(selected.getSuit() == winningCard.getSuit() && Rank.rankGreater(selected, winningCard)) ||
								// trumped when non-trump was winning
								(selected.getSuit() == trumps && winningCard.getSuit() != trumps)) {
					System.out.println("NEW WINNER");
					winner = nextPlayer;
					winningCard = selected;
				}
				// End Follow

			}
			// Clear information
			for(Player player:players){
				if(player instanceof NPC){
					((NPC) player).getMyInfo().clearMyInfo();
				}
			}
			delay(600);
			trick.setView(this, new RowLayout(hideLocation, 0));
			trick.draw();
			nextPlayer = winner;
			setStatusText("Player " + nextPlayer.getIndex() + " wins trick.");
			nextPlayer.addTrick(1);
			updateScore(nextPlayer);
		}
		removeActor(trumpsActor);
	}

	private void checkViolation(Suit lead, Player nextPlayer){
		if (selected.getSuit() != lead && nextPlayer.getHand().getNumberOfCardsWithSuit(lead) > 0) {
			// Rule violation
			String violation = "Follow rule broken by player " + nextPlayer.getIndex() + " attempting to play " + selected;
			System.out.println(violation);
			if (enforceRules)
				try {
					throw(new BrokeRuleException(violation));
				} catch (BrokeRuleException e) {
					e.printStackTrace();
					System.out.println("A cheating player spoiled the game!");
					System.exit(0);
				}
		}
	}

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
				players.get(iP).setBid(players.get(iP).getBid() + (random.nextBoolean() ? -1:1));
			}
		}
	}

	private void updateScores() {
		for(Player player:players){
			player.setScore(player.getScore()+player.getTrick());
			if(player.getTrick() == player.getBid()){
				player.setScore(player.getScore()+madeBidBonus);
			}
		}
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


	// Getter and Setter
	public ArrayList<Player> getPlayers(){
		return players;
	}

}
