package quatro;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

public class Fighter {

	private static final Logger LOG = Logger.getLogger(Fighter.class);

	private static final int W20 = 20;
	private static final int W6 = 6;
	private String name;
	private double maxHealth;
	private double talent;
	private double adjustedTalent;
	private double strength;
	private Integer weaponStrength;
	private boolean dead = false;
	private List<Fighter> wonAgainst = new ArrayList<>();
	private List<Fighter> wonAgainstCleanedUp;
	private int loosers;
	private int woundsSuperficial;
	private int woundsDeep;
	private int woundsCritical;
	private int woundsBrutal;
	private int woundsDisastrous;
	private int honor;
	private int motivators = 1;
	private int lazyRounds;
	private int survivedRounds;
	private int roundsFought;

	private double bloodLostSoFar;
	private boolean readyForComma = false;

	public Fighter() {
		setName();
		setStrength();
	}

	public Fighter(double health) {
		setName();
		strength = health;
		maxHealth = strength;
	}

	public Integer pickUpWeapon(Integer dangerLevel) {

		if (null != dangerLevel && 0 < dangerLevel) {
			if (null == weaponStrength) {
				weaponStrength = dangerLevel;
				LOG.debug(name + " equipped " + dangerLevel + ".");
				return null;
			} else if (dangerLevel.intValue() > weaponStrength.intValue()) {
				Integer dropped = weaponStrength;
				weaponStrength = dangerLevel;
				LOG.debug(name + " dropped " + dropped + " and replaced it with " + dangerLevel + ".");
				return dropped;
			} else {
				LOG.debug(name + " ignored " + dangerLevel);
				return dangerLevel;
			}
		} else {
			LOG.debug(name + " cannot see any useful weapons lying about. (" + dangerLevel + ")");
			return dangerLevel;
		}
	}

	public void dropWeapon() throws DroppedWeaponException {
		Integer dangerousThing = weaponStrength;
		weaponStrength = null;

		if (null != dangerousThing) {
			if (dead) {
				LOG.debug(name + "'s" + "\t" + "dead hand dropped " + dangerousThing + ".");
			} else {
				LOG.debug(name + "\t" + "fumbled and dropped " + dangerousThing + ".");
			}
			throw new DroppedWeaponException(dangerousThing);
		} else {
			LOG.debug(name + "\thas no weapon.");
		}

	}

	public void die() throws DroppedWeaponException {
		dead = true;
		situationStats();
		dropWeapon();
	}

	private void setStrength() {
		talent = 1 + Math.random() * W20;
		strength = 1 + 10 * Math.random() * W6;
		maxHealth = strength;
	}

	@Override
	public String toString() {
//		String detailedName = name + "{H" + honor + "," + Math.round(strength) + "}";
		String detailedName = name + getStrengthStats();
		return detailedName;
	}

	private void setName() {
		NameGenerator ng = new NameGenerator();
		ng.setWordLength(4);
		ng.setMultiple(true);
		ng.setExtentionChance(0.1);
		name = ng.generateNameAdv();
	}

	public void weakenDueToBattle(double opponentStrength) throws DroppedWeaponException {

		double before = strength;
		if (opponentStrength > 0) {
			lazyRounds = 0;
			roundsFought++;

			double damageTaken = calculateDamage(opponentStrength);
			strength -= damageTaken;
			honor += calculateGainOfHonor(damageTaken);
			takeWound(damageTaken);
		}

		suffer();
		healthStats(before, strength);
	}

	private double calculateDamage(double opponentStrength) throws DroppedWeaponException {
		double danger = opponentStrength / getOverallStrength();
//		if (danger > 0.5 && Math.random() > 0.5) {
//			dropWeapon();
//		}
		return danger * opponentStrength / getOverallStrength();
	}

	private double calculateGainOfHonor(double damageTaken) {
		int gainOfHonor;
		if (damageTaken > 1) {
			gainOfHonor = (int) (decideImportance() * damageTaken);
		} else {
			gainOfHonor = (int) (decideImportance());
		}

		if (gainOfHonor <= 0)
			gainOfHonor = 1;

		LOG.debug(name + "\t" + "has gained " + gainOfHonor + " honor - winner of " + chickenDinner() + "/"
				+ roundsFought + " rounds.");
		return gainOfHonor;
	}

	void suffer() {
		double bleeding = woundsSuperficial * 0.1;
		bleeding += woundsDeep * 0.3;
		bleeding += woundsCritical * 1;
		bleeding += woundsBrutal * 3;
		bleeding += woundsDisastrous * 10;
		bloodLostSoFar += bleeding;
		strength -= bleeding;
		countWounds();
	}

	public void bleedingStats(double bleeding) {
		if (bleeding == 0) {
			// LOG.debug(this.name + " has flawless skin.");
		} else if (bleeding < 0.3 && bleeding > 0) {
			LOG.debug(this.name + "\t" + "has some scratches.");
		} else if (bleeding < 1) {
			LOG.debug(this.name + "\t" + "is bleeding.");
		} else if (bleeding < 2) {
			LOG.debug(this.name + "\t" + "is covered in blood.");
		} else if (bleeding < 0) {
			LOG.debug(this.name + "\t" + "is RENEGERATING??!");
		} else {
			LOG.debug(this.name + "\t" + "is SPRAYING blood.");
		}
	}

	private void healthStats(double before, double after) {
		StringBuilder healthStatus = new StringBuilder();
		if (strength > 0.9 * maxHealth) {
			healthStatus.append(this.name + "\t" + "is feeling fine.");
		} else if (strength > 0.6 * maxHealth) {
			healthStatus.append(this.name + "\t" + "might wanna check with a doctor.");
		} else if (strength > 0.3 * maxHealth) {
			healthStatus.append(this.name + "\t" + "looks really, really unwell.");
		} else if (strength > 0) {
			healthStatus.append(this.name + "\t" + "is on the brink of death.");
		} else {
			healthStatus.append(this.name + "\t" + "has stopped breathing.");
		}
		LOG.debug(healthStatus);
	}

	private void takeWound(double damage) {
		StringBuilder damageTaken = new StringBuilder();
		damageTaken.append(name + "\t" + "has taken ");
		if (damage == 0) {
			damageTaken.append("no");
		} else if (damage < (0.05 * maxHealth)) {
			woundsSuperficial++;
			damageTaken.append("small");
		} else if (damage < (0.15 * maxHealth)) {
			woundsDeep++;
			damageTaken.append("some");
		} else if (damage < (0.45 * maxHealth)) {
			woundsCritical++;
			damageTaken.append("lots of");
		} else if (damage < maxHealth) {
			woundsBrutal++;
			damageTaken.append("brutal");
		} else if (damage >= maxHealth) {
			woundsDisastrous++;
			damageTaken.append("potentially leathal");
		} else {
			damageTaken.append(" WEIRD ");
		}
		damageTaken.append(" damage.");
		LOG.debug(damageTaken);
	}

	public double getOverallStrength() {
		adjustedTalent = talent / 5;
		return strength + getWeaponStrength() * adjustedTalent;
	}

	public double getStrength() {
		return strength;
	}

	public int getWeaponStrength() {
		if (null != weaponStrength) {
			return weaponStrength.intValue();
		}
		return 0;
	}

	public String getName() {
		return name;
	}

	public void praise(Fighter opponent) {
		wonAgainst.add(opponent);
		honor += Math.abs(opponent.getHonor());
	}

	void doNotBeLazy(int foes) {
		survivedRounds++;
		motivators = foes;
		lazyRounds++;
		double breakDown = 1;
		if (motivators != 0) {
			breakDown = 1 - 1 / (motivators + 1);
			honor *= breakDown;
		}

		double minusLazy = lazyRounds;
		honor -= minusLazy;
	}

	private double decideImportance() {
		return motivators * (chickenDinner() * +1) * roundsFought;
	}

	public int getHonor() {
		return honor;
	}

	public void stats(int highScoreHonor) {
		finalStats(highScoreHonor);
		winnerStats();
	}

	private void situationStats() {
		StringBuilder situation = new StringBuilder();
		situation.append(name + "\t");
		if (dead) {
			situation.append("had ");
		} else {
			situation.append("has ");
		}

		situation.append((int) strength + " of " + (int) maxHealth + " left: ");
		situation.append(getStrengthStats());
		LOG.debug(situation);
	}

	private void winnerStats() {
		sortChickens();
		if (wonAgainstCleanedUp.size() == 1) {
			LOG.debug(detailSituation() + " opponent: " + wonAgainstCleanedUp);
		} else if (wonAgainstCleanedUp.size() > 1) {
			LOG.debug(detailSituation() + " opponents: " + wonAgainstCleanedUp);
		} else if (roundsFought > 1) {
			LOG.debug("In " + howManyFightsThough() + ", " + name + "\t" + "has never won.");
		} else {
			LOG.debug(name + "\t" + "has lost the first and only fight.");
		}
	}

	private String detailSituation() {
		if (chickenDinner() > 1) {
			return name + "\thas won " + chickenDinner() + " times in " + howManyFightsThough() + " against " + loosers;
		} else {
			return name + "\thas won " + chickenDinner() + " time in " + howManyFightsThough() + " against " + loosers;
		}
	}

	private void sortChickens() {
		wonAgainstCleanedUp = new ArrayList<>(new HashSet<Fighter>(wonAgainst));
		loosers = wonAgainstCleanedUp.size();
	}

	private String howManyFightsThough() {
		if (roundsFought == 0) {
			return ", ever.";
		}
		String thisMany = roundsFought + " round";
		if (roundsFought != 1) {
			thisMany += "s";
		}
		return thisMany;
	}

	public int chickenDinner() {
		return wonAgainst.size();
	}

	private void finalStats(int highScoreHonor) {
		StringBuilder kindOfDeath = new StringBuilder();
		String wayOfEnding = "fought";
		if (dead)
			wayOfEnding = "died";
		if (honor <= 0) {
			kindOfDeath.append(name + "\t" + "has " + wayOfEnding + " without honor");
		} else if (honor > highScoreHonor * 0.3) {
			kindOfDeath.append(name + "\t" + "has " + wayOfEnding + " like a hero");
		} else if (honor > highScoreHonor * 0.1) {
			kindOfDeath.append(name + "\t" + "has " + wayOfEnding + " like a champion");
		} else if (honor > highScoreHonor * 0.01) {
			kindOfDeath.append(name + "\t" + "has " + wayOfEnding + " bravely");
		} else if (honor > 0) {
			kindOfDeath.append(name + "\t" + "has died in vane");
		}
		kindOfDeath.append(" in round " + survivedRounds + ".\t");
		LOG.debug(kindOfDeath);
	}

	public int countWounds() {
		String pokies = this.name + "\thas lost " + countDrops() + " blood";
		int woundCount = woundsSuperficial + woundsDeep + woundsCritical + woundsBrutal + woundsDisastrous;
		readyForComma = false;
		if (woundCount == 0) {
			pokies += ".";
		} else {
			pokies += " through ";
			if (woundsSuperficial > 0) {
				pokies += woundsSuperficial + " superficial";
				readyForComma = true;
			}
			if (woundsDeep > 0) {
				pokies += doTheComma();
				pokies += woundsDeep + " deep";
				readyForComma = true;
			}
			if (woundsCritical > 0) {
				pokies += doTheComma();
				pokies += woundsCritical + " critical";
				readyForComma = true;
			}
			if (woundsBrutal > 0) {
				pokies += doTheComma();
				pokies += woundsBrutal + " brutal";
				readyForComma = true;
			}
			if (woundsDisastrous > 0) {
				pokies += doTheComma();
				pokies += woundsDisastrous + " disastrous";
			}
			pokies += " wound";
			if (woundCount != 1) {
				pokies += "s";
			}
			pokies += ".";

		}
		if (bloodLostSoFar > 0) {
			LOG.debug(pokies);
		}
		return woundCount;
	}

	public double lookAtBlood() {
		return bloodLostSoFar;
	}

	private String doTheComma() {
		if (readyForComma) {
			readyForComma = false;
			return ", ";
		} else {
			readyForComma = false;
			return "";
		}
	}

	private String countDrops() {
		if (bloodLostSoFar == 0) {
			return "not even a single drop of";
		} else if (bloodLostSoFar < 0.1 * maxHealth) {
			return "some";
		} else if (bloodLostSoFar < 0.3 * maxHealth) {
			return "plenty of";
		} else if (bloodLostSoFar < 0.5 * maxHealth) {
			return "a lot of";
		} else if (bloodLostSoFar <= maxHealth) {
			return "waaaay too much";
		} else if (bloodLostSoFar > maxHealth) {
			return "unbelievable amounts of";
		} else if (bloodLostSoFar < 0) {
			return "negative amounts? of";
		} else {
			return "WTF WTF";
		}
	}

	String getStrengthStats() {
		return "[" + (int) this.strength + "+" + this.getWeaponStrength() + "x" + (int) adjustedTalent + "]";
	}

	public void heal(double amount) {
		if (strength < maxHealth) {
			LOG.debug("Doctor Appointment!");
		}
		strength += amount;
		if (strength > maxHealth) {
			double excessAmount = strength - maxHealth;
			strength = maxHealth;
			if (excessAmount > 0) {
				LOG.debug("Attempting bandaging..." + (int) excessAmount);
				bandage(excessAmount);
			}
		} else if (strength <= 0) {
			LOG.debug(name + " couln't be helped anymore." + strength + "." + amount);
		}
	}

	private void bandage(double bandage) {
		double brutalKit = 0.45 * maxHealth;
		double criticalKit = 0.15 * maxHealth;
		double deepKit = 0.05 * maxHealth;
		double superficialKit = 0.5 * maxHealth;

		if (woundsDisastrous > 0 && bandage >= maxHealth) {
			woundsDisastrous--;
			bandage = 0;
			LOG.debug("Now it's a Frankenfighter.");
		}
		while (woundsBrutal > 0 && bandage >= brutalKit) {
			woundsBrutal--;
			bandage -= brutalKit;
			LOG.debug("Reassembling limbs.");
		}
		while (woundsCritical > 0 && bandage >= criticalKit) {
			woundsCritical--;
			bandage -= criticalKit;
			LOG.debug("Fixing some broken stuff.");
		}
		while (woundsDeep > 0 && bandage >= deepKit) {
			woundsDeep--;
			bandage -= deepKit;
			LOG.debug("Treating a cut.");
		}
		while (woundsSuperficial > 0 && bandage >= superficialKit) {
			woundsSuperficial--;
			bandage -= superficialKit;
			LOG.debug("Mending a scratch.");
		}

		if (bandage > 0) {
			LOG.debug("Bandage could not be used fully.");
		}
	}

	public boolean isAlive() {
		return !dead;
	}

	public double getMaxHealth() {
		return maxHealth;
	}

}
