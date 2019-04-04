package quatro;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ArenaTest {

	@Test
	public void arenaHasNoFightersAtStart() {
		System.out.println("+++++arenaIsEmptyAtStart++++++");
		Arena arena = new Arena();
		assertTrue(arena.isEmpty());
	}

	@Test
	public void arenaLetsEnterSomeone() {
		System.out.println("+++++arenaLetsEnterSomeone++++++");
		Arena arena = new Arena();
		assertTrue(arena.requestEnterance(new Fighter()));
		assertFalse(arena.isEmpty());
	}

	@Test
	public void arenaForbidsSoloFight() {
		System.out.println("+++++arenaForbidsSoloFight++++++");
		Arena arena = new Arena();
		assertTrue(arena.requestEnterance(new Fighter()));
		assertFalse(arena.readyToFight());
		assertTrue(arena.requestEnterance(new Fighter()));
		assertTrue(arena.readyToFight());
	}

	@Test
	public void thereIsOnlyOneWinner() {
		System.out.println("+++++1 Winner++++++");
		Arena arena = new Arena();
		assertTrue(arena.requestEnterance(new Fighter()));
		assertTrue(arena.requestEnterance(new Fighter()));
		assertTrue(arena.requestEnterance(new Fighter()));
		assertTrue(arena.requestEnterance(new Fighter()));
		assertTrue(arena.requestEnterance(new Fighter()));
		assertTrue(arena.requestEnterance(new Fighter()));
		assertTrue(arena.requestEnterance(new Fighter()));
		assertTrue(arena.requestEnterance(new Fighter()));
		arena.lastManStanding();
		assertFalse(arena.readyToFight());
	}

	@Test
	public void thereIsOnlyOneWeakWinner() {
		System.out.println("+++++WeakWinner++++++");
		Arena arena = new Arena();
		for (int i = 0; i < 5; i++) {
			assertTrue(arena.requestEnterance(new Fighter()));
		}
		arena.lastManStanding();
		assertFalse(arena.readyToFight());
	}

	@Test
	public void newArenaHasAtLeastOneWeapon() {
		System.out.println("+++++newArenaHasAtLeastOneWeapon++++++");
		Arena arena = new Arena();
		assertTrue(arena.weapons.size() >= 1);
	}

	@Test
	public void droppedWeaponsCanBePickedUp() {
		System.out.println("+++++droppedWeaponsCanBePickedUp++++++");
		Arena arena = new Arena();
		Fighter klauer = new Fighter();
		arena.requestEnterance(klauer);
		assertTrue(klauer.getWeaponStrength() > 0);
	}

	@Test
	public void weaponsArePickedUpDuringFight() {
		System.out.println("+++++droppedWeaponsCanBePickedUp++++++");
		Arena arena = new Arena();
		Fighter klauer = new Fighter();
		assertTrue(arena.requestEnterance(klauer));
		assertTrue(klauer.getWeaponStrength() > 0);
		try {
			klauer.dropWeapon();
		} catch (DroppedWeaponException e) {
			e.printStackTrace();
		}
		assertEquals(0, klauer.getWeaponStrength());
		assertTrue(arena.requestEnterance(new Fighter()));
	}

	@Test
	public void beingDefetedDoesNotAddWinnerToWonAgainst() {
		System.out.println("+++++droppedWeaponsCanBePickedUp++++++");
		Arena arena = new Arena();
		Fighter opfer = new Fighter();
		assertTrue(arena.requestEnterance(opfer));
		try {
			opfer.dropWeapon();
		} catch (DroppedWeaponException e) {
			e.printStackTrace();
		}
		assertEquals(0, opfer.getWeaponStrength());
		Fighter stronger = new Fighter();
		stronger.pickUpWeapon(new Integer(1000));

		boolean tooWeak = true;
		while (tooWeak) {
			tooWeak = stronger.getOverallStrength() < opfer.getOverallStrength();
		}
		arena.requestEnterance(stronger);
		arena.lastManStanding();
		assertEquals(0, opfer.chickenDinner());
		assertEquals(1, stronger.chickenDinner());

	}

}
