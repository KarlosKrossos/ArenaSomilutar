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
	private boolean startWithVowel = false;
	// TODO first not fully functional, double
	// letters can occur even if !multiple
	private boolean first = false;
	private String doubleChar = "";
	private String trippleChar = "";
	private String ret = "";
	private String ch;
	private String vowelStart = "";
	private String vowelEnd = "";
	private String consonantStart = "";
	private String consonantEnd = "";
	private double extentionChance = 0;
	private int wordLength = 0;

	public String generateNameAdv() {
		ret = "";
		for (int i = 0; i < wordLength; i++) {
			if (i == 0) {
				first = true;
				setDoubleTripple();
				if (startWithVowel) {
					ch = generateVowel(true);
				} else {
					ch = generateConsonant(true);
				}
				first = false;
			} else {
				setDoubleTripple();
				if (i % 2 == 0) {
					if (startWithVowel) {
						ch = generateVowel(false);
					} else {
						ch = generateConsonant(false);
					}
				} else {
					if (!startWithVowel) {
						ch = generateVowel(false);
					} else {
						ch = generateConsonant(false);
					}
				}
			}
			ret += ch;

			if (Math.random() <= extentionChance) {
				wordLength++;
			}
		}
		return ret;
	}

	private String generateVowel(boolean caps) {
		if (caps) {
			if (doubleBool) {
				if (trippleBool) {
					trippleChar = "" + vowels.charAt(r.nextInt(vowels.length()));
				}
				doubleChar = "" + vowels.charAt(r.nextInt(vowels.length())) + trippleChar;
			}
			return vowelStart + vowelsCAP.charAt(r.nextInt(vowelsCAP.length())) + doubleChar + vowelEnd;
		} else {
			if (doubleBool) {
				if (trippleBool) {
					trippleChar = "" + vowels.charAt(r.nextInt(vowels.length()));
				}
				doubleChar = "" + vowels.charAt(r.nextInt(vowels.length())) + trippleChar;
			}
			return vowelStart + vowels.charAt(r.nextInt(vowels.length())) + doubleChar + vowelEnd;
		}
	}

	private String generateConsonant(boolean caps) {
		if (caps) {
			if (doubleBool) {
				if (trippleBool) {
					trippleChar = "" + consonants.charAt(r.nextInt(consonants.length()));
				}
				doubleChar = "" + consonants.charAt(r.nextInt(consonants.length())) + trippleChar;
			}
			return consonantStart + consonantsCAP.charAt(r.nextInt(consonantsCAP.length())) + doubleChar + consonantEnd;
		} else {
			if (doubleBool) {
				if (trippleBool) {
					trippleChar = "" + consonants.charAt(r.nextInt(consonants.length()));
				}
				doubleChar = "" + consonants.charAt(r.nextInt(consonants.length())) + trippleChar;

			}
			return consonantStart + consonants.charAt(r.nextInt(consonants.length())) + doubleChar + consonantEnd;
		}
	}

	private void setDoubleTripple() {
		doubleBool = false;
		trippleBool = false;
		double dbl = r.nextDouble();
		double trpl = r.nextDouble();
		double dblCh = 0.15;
		double trplCh = 0.15;
		doubleChar = "";
		trippleChar = "";
		if (first) {
			startWithVowel = r.nextBoolean();
		}

		if (multiple) {
			if (dbl < dblCh) {
				doubleBool = true;
				if (trpl < trplCh) {
					trippleBool = true;
				}
			}
		}
	}

	@Deprecated
	public String nameNumber() {
		int ran = (int) (Math.random() * 10000);
		return ran + "";
	}

	@Deprecated
	public String generateName() {
		String ret = "";
		String ch;
		int nLength = (int) (Math.random() * 8) + 2;
		boolean startWithVowel = false;
		if (Math.random() > 0.5) {
			startWithVowel = true;
		}
		for (int i = 0; i < nLength; i++) {
			if (i == 0) {
				if (startWithVowel) {
					ch = generateVowel(true);
				} else {
					ch = generateConsonant(true);
				}
			} else {
				if (i % 2 == 0) {
					if (startWithVowel) {
						ch = generateVowel(false);
					} else {
						ch = generateConsonant(false);
					}
				} else {
					if (startWithVowel) {
						ch = generateConsonant(false);
					} else {
						ch = generateVowel(false);
					}
				}
			}
			String chStr = "" + ch;
			ret += chStr;
		}
		return ret;
	}

	public boolean compareToWantedName(String name, List<String> nameList) {
		for (String str : nameList) {
			if (name.equalsIgnoreCase(str))
				return true;
			// if (name.compareTo(str) == str.length()) {
			// System.err.print(name.compareTo(str));
			// }
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
