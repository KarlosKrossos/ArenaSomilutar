package quatro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

public class Arena {

	private static final Logger LOG = Logger.getLogger(Arena.class);
	private final Random r = new Random();
	private static final String SIGNBOARDEMPTY = ">---------------------------------------------------<";

	private List<Fighter> fighters = new ArrayList<>();
	private List<Integer> weapons = new ArrayList<>();
	private List<Fighter> chronicles = new ArrayList<>();
	private int highScoreHonor;
	private int minimumBluntness = 1;
	private int varietyOfWeapons = 10;

	public Arena() {
		provideMinimumWeapons();
	}

	public Arena(int minimumBluntness, int varietyOfWeapons) {
		if (minimumBluntness >= 0)
			this.minimumBluntness = minimumBluntness;
		if (varietyOfWeapons >= 0)
			this.varietyOfWeapons = varietyOfWeapons;
		provideMinimumWeapons();
	}

	private void provideMinimumWeapons() {
		while (weapons.isEmpty()) {
			addWeaponsMaybe();
		}
	}

	public boolean requestEnterance(Fighter fighter) {
		fighters.add(fighter);
		LOG.debug(fighter + " has entered.");
		addWeaponsMaybe();
		distributeWeapons(fighter);
		LOG.debug(fighters);

		return true;
	}

	private void distributeWeapons(Fighter fighter) {

		if (!weapons.isEmpty()) {

			LOG.debug(weapons);
			if (fighters.size() == 1) {
				giveWeapon(fighter);
			} else {
				LOG.debug("REPLAAAACE!");
				for (int i = 0; i < fighters.size(); i++) {
					giveWeapon(fighters.get(i));
				}
			}
			LOG.debug(weapons);
		}
	}

	private void giveWeapon(Fighter fighter) {
		if (!weapons.isEmpty()) {
			int weaponInSight = identifyRandomWeapon();
			int checkedOut = weapons.get(weaponInSight);

			Integer dropped = fighter.pickUpWeapon(checkedOut);
			addAndRemoveWeapons(weaponInSight, dropped);
		}
	}

	private void addAndRemoveWeapons(int thisOne, Integer dropped) {
		int whichOneThough = weapons.get(thisOne);

		if (null == dropped) {
			weapons.remove(thisOne);
		} else if (!dropped.equals(whichOneThough)) {
			weapons.remove(thisOne);
			weapons.add(dropped);
		}
	}

	private int identifyRandomWeapon() {
		if (weapons.size() == 1) {
			return 0;
		} else if (weapons.size() > 1) {
			return r.nextInt(weapons.size() - 1);
		} else {
			return -1;
		}
	}

	public void lastManStanding() {

		LOG.debug("############################ UNTIL THE LAST STANDETH ! ! ############################");

		int round = 0;

		while (fighters.size() > 1) {

			calculateHighScore();

			round++;
			LOG.debug("---------------------- round " + round + " ---------------------- ");
			spreadTheBlood();
			removeDead();
			motivateTheLiving();

			LOG.debug("Living fighters: " + fighters);
			LOG.debug("Available weapons: " + weapons);
			letThemGatherWeapons();

			if (fighters.size() == 2) {
				fight(0, 1);
			} else {
				int a = r.nextInt(fighters.size() - 1);
				int b = findOtherFighter(a);
				fight(a, b);
			}
			removeDead();
		}
		wrapItUp();
	}

	private int findOtherFighter(int a) {
		int count = fighters.size();
		int b = r.nextInt(count - 1);
		if (count == 2) {
			if (a == 0) {
				return 1;
			} else if (count == 1) {
				return 0;
			} else {
				LOG.error("COUNT out of BOUND");
			}
		} else {
			while (a == b) {
				b = r.nextInt(count - 1);
				LOG.trace("A" + a + " against B" + b + " | " + count);
			}
		}
		return b;
	}

	private void motivateTheLiving() {
		for (Fighter f : fighters) {
			f.doNotBeLazy(fighters.size() - 1);
		}
	}

	private void wrapItUp() {
		listChronicleEntries();
	}

	private void listChronicleEntries() {
		calculateHighScore();
		showLastStanding();
		writeTheChronicleBoard();
	}

	private void showLastStanding() {

		// TODO use constants and use append
		LOG.debug(SIGNBOARDEMPTY);
		LOG.debug(">---------------< LAST ONE STANDING >---------------<");
		LOG.debug(SIGNBOARDEMPTY);

		if (!fighters.isEmpty()) {
			Fighter lastOne = fighters.get(0);
			lastOne.stats(highScoreHonor);
		}

		calculateBloodLevel();
	}

	private void calculateBloodLevel() {
		double blood = 0;
		if (!fighters.isEmpty()) {
			blood = fighters.get(0).lookAtBlood();
		}
		double wastedLife = 0;
		for (Fighter corps : chronicles) {
			blood += corps.lookAtBlood();
			wastedLife += corps.getMaxHealth();
		}
		LOG.debug(chronicles.size() + " fighters lost " + (int) blood + " in blood and " + (int) wastedLife
				+ " life was wasted overall.");
	}

	private void calculateHighScore() {
		if (!chronicles.isEmpty()) {
			Collections.sort(chronicles,
					(a, b) -> a.getHonor() < b.getHonor() ? 1 : a.getHonor() == b.getHonor() ? 0 : -1);
			highScoreHonor = chronicles.get(0).getHonor();
		}
	}

	private void writeTheChronicleBoard() {
		LOG.debug(SIGNBOARDEMPTY);
		LOG.debug(">-------------------< CHRONICLES >------------------<");
		LOG.debug(SIGNBOARDEMPTY);

		boolean lastOneWasPositive = true;
		for (Fighter deadGuy : chronicles) {
			if (deadGuy.getHonor() > 0) {
				lastOneWasPositive = true;
			}
			if (deadGuy.getHonor() <= 0 && lastOneWasPositive) {
				LOG.debug("------------------------------");
				lastOneWasPositive = false;
			}
			deadGuy.stats(highScoreHonor);
		}
	}

	private void removeDead() {

		List<Fighter> fightersToBeRemoved = new ArrayList<>();

		for (Fighter f : fighters) {
			if (f.getStrength() < 0) {
				try {
					f.die();
				} catch (DroppedWeaponException e) {
					if (null != e.getWeapon())
						weapons.add(e.getWeapon());
				}
				fightersToBeRemoved.add(f);
			}
		}

		for (Fighter f : fightersToBeRemoved) {
			f.stats(highScoreHonor);
			chronicles.add(f);
			fighters.remove(f);
		}

	}

	private void spreadTheBlood() {
		for (Fighter f : fighters) {
			f.suffer();
		}
	}

	private void letThemGatherWeapons() {

		for (Fighter f : fighters) {
			giveWeapon(f);
		}
	}

	public boolean isEmpty() {
		return fighters.isEmpty();
	}

	public boolean readyToFight() {
		return fighters.size() >= 2;
	}

	private void addWeaponsMaybe() {
		while (Math.random() < 0.25) {
			Integer soStrong = defineDanger();
			addWeapon(soStrong);
			LOG.debug(soStrong + " was thrown into the arena");
		}
	}

	public int countWeapons() {
		return weapons.size();
	}

	public void addWeapon(Integer weapon) {
		weapons.add(weapon);
	}

	private Integer defineDanger() {
		return new Integer(r.nextInt(varietyOfWeapons) + minimumBluntness);
	}

	private void fight(int i, int j) {
		if (fighters.get(i).getOverallStrength() >= fighters.get(j).getOverallStrength()) {
			resolve(i, j);
		} else {
			resolve(j, i);
		}
	}

	private void resolve(int stronger, int weaker) {
		Fighter s = fighters.get(stronger);
		Fighter w = fighters.get(weaker);
		double aOverallStrength = s.getOverallStrength();
		double bOverallStrength = w.getOverallStrength();
		LOG.debug(s.getName() + s.getStrengthStats() + " fought " + w.getName() + w.getStrengthStats() + " and won.");

		s.praise(w);

		try {
			w.weakenDueToBattle(aOverallStrength);
			s.weakenDueToBattle(bOverallStrength);
		} catch (DroppedWeaponException e) {
			if (null != e.getWeapon())
				weapons.add(e.getWeapon());
		}

	}

}