package quatro;

public class TheSomilutar {

	private static Arena arena;

	public static void main(String[] args) {
		defineParameters();
		initializeArena();
		awaitResults();

	}

	private static void awaitResults() {
		arena.lastManStanding();
	}

	private static void initializeArena() {
		arena = new Arena();
		for (int i = 0; i < 10; i++) {
			arena.requestEnterance(new Fighter());
		}
	}

	private static void defineParameters() {
		// TODO Auto-generated method stub

	}

}
