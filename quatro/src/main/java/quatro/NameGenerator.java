package quatro;

import java.util.List;
import java.util.Random;

public class NameGenerator {

	public int getWordLength() {
		return wordLength;
	}

	public void setWordLength(int wordLength) {
		this.wordLength = wordLength;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	private boolean multiple = false;

	private String vowelsCAP = "AAAAAÁÂÀÄÄEEEEEÉÊÈIIIIIÍÎÌOOOOOÓÔÒÖÖUUUUUÚÛÙÜÜYYYYÝ";
	private String vowels = "aaaaaáâàääeeeeeéêèiiiiiíîìoooooóôòööuuuuuúûùüüyyyyý''";
	private String consonantsCAP = "BCDFGHJKLMNPQRSTVWXZ";
	private String consonants = "bcdfghjklmnp'qrstvwxz";
	private Random r = new Random();
	private boolean doubleBool = false;
	private boolean trippleBool = false;
	private String vowelEnd = "";
	private double extentionChance = 0;
	private int wordLength = 0;

	public String generateNameAdv() {
		StringBuilder ret = new StringBuilder();
		String ch = "";

		for (int i = 0; i < wordLength; i++) {
			setDoubleTripple();
			if (i == 0) {
				if (startWithVowel()) {
					ch = generateVowel(true);
				} else {
					ch = generateConsonant(true);
				}
			} else {
				ch = addSmallLetter(i % 2 == 0);
			}
			ret.append(ch);

			if (Math.random() <= extentionChance) {
				wordLength++;
			}
		}
		return ret.toString();
	}

	private String addSmallLetter(boolean decision) {
		if (decision) {
			return generateVowel(false);
		} else {
			return generateConsonant(false);
		}
	}

	private boolean startWithVowel() {
		return r.nextBoolean();
	}

	private String generateVowel(boolean caps) {
		String doubleChar = "";
		String trippleChar = "";
		if (caps) {
			if (doubleBool) {
				if (trippleBool) {
					trippleChar = "" + vowels.charAt(r.nextInt(vowels.length()));
				}
				doubleChar = "" + vowels.charAt(r.nextInt(vowels.length())) + trippleChar;
			}
			return vowelsCAP.charAt(r.nextInt(vowelsCAP.length())) + doubleChar + vowelEnd;
		} else {
			if (doubleBool) {
				if (trippleBool) {
					trippleChar = "" + vowels.charAt(r.nextInt(vowels.length()));
				}
				doubleChar = "" + vowels.charAt(r.nextInt(vowels.length())) + trippleChar;
			}
			return vowels.charAt(r.nextInt(vowels.length())) + doubleChar + vowelEnd;
		}
	}

	private String generateConsonant(boolean caps) {
		String doubleChar = "";
		String trippleChar = "";
		if (caps) {
			if (doubleBool) {
				if (trippleBool) {
					trippleChar = "" + consonants.charAt(r.nextInt(consonants.length()));
				}
				doubleChar = "" + consonants.charAt(r.nextInt(consonants.length())) + trippleChar;
			}
			return consonantsCAP.charAt(r.nextInt(consonantsCAP.length())) + doubleChar;
		} else {
			if (doubleBool) {
				if (trippleBool) {
					trippleChar = "" + consonants.charAt(r.nextInt(consonants.length()));
				}
				doubleChar = "" + consonants.charAt(r.nextInt(consonants.length())) + trippleChar;

			}
			return consonants.charAt(r.nextInt(consonants.length())) + doubleChar;
		}
	}

	private void setDoubleTripple() {
		doubleBool = false;
		trippleBool = false;
		double dblCh = 0.15;
		double trplCh = 0.15;

		if (multiple && r.nextDouble() < dblCh) {
			doubleBool = true;
			if (r.nextDouble() < trplCh) {
				trippleBool = true;
			}

		}
	}

	public boolean compareToWantedName(String name, List<String> nameList) {
		for (String str : nameList) {
			if (name.equalsIgnoreCase(str))
				return true;
		}
		return false;
	}

	public double getExtentionChance() {
		return extentionChance;
	}

	public void setExtentionChance(double extentionChance) {
		this.extentionChance = extentionChance;
	}

}
