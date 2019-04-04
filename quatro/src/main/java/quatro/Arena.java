package quatro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Arena {

	private List<Fighter> fighters = new ArrayList<Fighter>();
	private List<Integer> weapons = new ArrayList<Integer>();
	private List<Fighter> chronicles = new ArrayList<Fighter>();
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
		System.out.println(fighter + " has entered.");
		addWeaponsMaybe();
		distributeWeapons(fighter);
		System.out.println(fighters);

		return true;
	}

	private void distributeWeapons(Fighter fighter) {

		if (weapons.size() > 0) {

			System.out.println(weapons);
			if (fighters.size() == 1) {
				giveWeapon(fighter);
			} else {
				System.out.println("REPLAAAACE!");
				for (int i = 0; i < fighters.size(); i++) {
					giveWeapon(fighters.get(i));
				}
			}
			System.out.println(weapons);
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
			return (int) (Math.random() * (weapons.size() - 1));
		} else {
			return -1;
		}
	}

	public void lastManStanding() {

		System.out.println("############################ UNTIL THE LAST STANDETH ! ! ############################");

		boolean done = false;
		int round = 0;

		while (!done) {

			calculateHighScore();
			if (fighters.size() <= 1) {
				done = true;
				break;
			}

			round++;
			System.out.println("---------------------- round " + round + " ---------------------- ");
			spreadTheBlood();
			removeDead();
			motivateTheLiving();

			System.out.println("Living fighters: " + fighters.toString());
			System.out.println("Available weapons: " + weapons.toString());
			letThemGatherWeapons();

			if (fighters.size() == 2) {
				fight(0, 1);
			}
			int a = (int) (Math.random() * fighters.size() - 1);
			int b = (int) (Math.random() * fighters.size() - 1);
			if (b != a) {
				fight(a, b);
			}

			removeDead();
		}
		wrapItUp();
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

		System.out.println(">---------------------------------------------------<");
		System.out.println(">---------------< LAST ONE STANDING >---------------<");
		System.out.println(">---------------------------------------------------<");

		if (fighters.size() > 0) {
			Fighter lastOne = fighters.get(0);
			lastOne.stats(highScoreHonor);
		}

		System.out.println();
		calculateBloodLevel();
	}

	private void calculateBloodLevel() {
		double blood = 0;
		if (fighters.size() > 0) {

			blood = fighters.get(0).lookAtBlood();
		}
		double wastedLife = 0;
		for (Fighter corps : chronicles) {
			blood += corps.lookAtBlood();
			wastedLife += corps.getMaxHealth();
		}
		System.out.println(chronicles.size() + " fighters lost " + (int) blood + " in blood and " + (int) wastedLife
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
		System.out.println(">---------------------------------------------------<");
		System.out.println(">-------------------< CHRONICLES >------------------<");
		System.out.println(">---------------------------------------------------<");

		boolean lastOneWasPositive = true;
		for (Fighter deadGuy : chronicles) {
			if (deadGuy.getHonor() > 0) {
				lastOneWasPositive = true;
			}
			if (deadGuy.getHonor() <= 0 && lastOneWasPositive) {
				System.out.println("------------------------------");
				lastOneWasPositive = false;
			}
			deadGuy.stats(highScoreHonor);
		}
	}

	private void removeDead() {

		List<Fighter> fightersToBeRemoved = new ArrayList<Fighter>();

		for (Fighter f : fighters) {
			if (f.getStrength() < 0) {
				try {
					f.die();
//					f.stats(highScoreHonor);
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
			System.out.println(soStrong + " was thrown into the arena");
		}
	}

	public int countWeapons() {
		return weapons.size();
	}

	public void addWeapon(Integer weapon) {
		weapons.add(weapon);
	}

	private Integer defineDanger() {
		return new Integer((int) (Math.random() * varietyOfWeapons) + minimumBluntness);
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
		System.out.println(
				s.getName() + s.getStrengthStats() + " fought " + w.getName() + w.getStrengthStats() + " and won.");

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