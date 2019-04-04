package quatro;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class TheSomilutar {

	private static Arena arena;
	private static int numberOfFighters;
	private static int minimumBluntness;
	private static int varietyOfWeapons;

	public static void main(String[] args) {
		defineParameters();
		initializeArena();
		awaitResults();

	}

	private static void awaitResults() {
		arena.lastManStanding();
	}

	private static void initializeArena() {
		arena = new Arena(minimumBluntness, varietyOfWeapons);
		for (int i = 0; i < numberOfFighters; i++) {
			arena.requestEnterance(new Fighter());
		}
	}

	private static void defineParameters() {
		Scanner reader = new Scanner(System.in); // Reading from System.in
		System.out.println("How many fighters should there be?");
		numberOfFighters = reader.nextInt();
		System.out.println("What's the minimum acceptable weapon strength?");
		minimumBluntness = reader.nextInt();
		System.out.println("What's the allowed difference between a weak and a strong weapon?");
		varietyOfWeapons = reader.nextInt();
		reader.close();
		countDown();
	}

	private static void countDown() {
		for (int i = 10; i > 0; i--) {
			System.out.println("Somilutar starting in " + i + ".");
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
