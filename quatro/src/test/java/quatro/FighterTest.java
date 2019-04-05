package quatro;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class FighterTest {

	// TODO LOG instead of syso

	@Test
	public void newFighterHasNoWeapon() {
		System.out.println("+++++newFighterHasNoWeapon++++++");
		double strength = 10;
		Fighter noob = new Fighter(strength);
		assertEquals(strength, noob.getOverallStrength());
	}

	@Test
	public void newFighterHasName() {
		System.out.println("+++++newFighterHasName++++++");
		Fighter noob = new Fighter();
		assertFalse(noob.getName().isEmpty());
	}

	@Test
	public void fighterGetsStrongerWithWeapon() {
		System.out.println("+++++fighterGetsStrongerWithWeapon++++++");
		Fighter noob = new Fighter();
		double strengthUnarmed = noob.getOverallStrength();
		noob.pickUpWeapon(10);
		double strengthWithWeapon = noob.getOverallStrength();
		assertTrue(strengthWithWeapon > strengthUnarmed);
	}

	@Test
	public void fighterTakesBetterWeapon() {
		System.out.println("+++++fighterTakesBetterWeapon++++++");
		Fighter noob = new Fighter();
		double strengthUnarmed = noob.getOverallStrength();
		noob.pickUpWeapon(10);
		double strengthWithWeapon = noob.getOverallStrength();
		assertTrue(strengthWithWeapon > strengthUnarmed);

		Integer theDrop = noob.pickUpWeapon(20);
		assertNotNull(theDrop);
		assertEquals(10, theDrop.intValue());

		theDrop = noob.pickUpWeapon(10);
		assertNotNull(theDrop);
		assertEquals(10, theDrop.intValue());

		theDrop = noob.pickUpWeapon(20);
		assertNotNull(theDrop);
		assertEquals(20, theDrop.intValue());

		theDrop = noob.pickUpWeapon(null);
		assertNull(theDrop);
		assertEquals(20, noob.getWeaponStrength());

		Integer iih = 0;
		theDrop = noob.pickUpWeapon(0);
		assertEquals(iih, theDrop);
		assertEquals(20, noob.getWeaponStrength());

		iih = 21;
		theDrop = noob.pickUpWeapon(iih);
		assertEquals(21, noob.getWeaponStrength());
	}

	@Test
	public void zeroWeakenWithoutFight() {
		System.out.println("+++++zeroWeakenWithoutFight++++++");
		Fighter noob = new Fighter();
		double health = noob.getStrength();

		try {
			noob.weakenDueToBattle(0);
		} catch (DroppedWeaponException e1) {
		}
		assertEquals(0, noob.getHonor());

		try {
			noob.weakenDueToBattle(-1);
		} catch (DroppedWeaponException e) {
		}
		assertEquals(0, noob.getHonor());
		assertEquals(health, noob.getStrength());
	}

	@Test
	public void fightsChangeStats() {
		System.out.println("+++++fightsChangeStats++++++");
		double maxHealth = 10;
		Fighter noob = new Fighter(maxHealth);
		System.out.println(noob.getHonor());

		try {
			noob.weakenDueToBattle(1);
		} catch (DroppedWeaponException e1) {
		}
		assertNotEquals(0, noob.getHonor());
		assertNotEquals(maxHealth, noob.getStrength());

		try {
			noob.weakenDueToBattle(1000);
		} catch (DroppedWeaponException e) {
		}
		assertTrue(noob.getHonor() > 0);
		assertNotEquals(maxHealth, noob.getStrength());
	}

	@Test
	public void lazyChangeStats() {
		System.out.println("+++++lazyChangeStats++++++");
		Fighter noob = new Fighter();
		double health = noob.getStrength();

		noob.doNotBeLazy(0);
		noob.doNotBeLazy(0);
		assertEquals(health, noob.getStrength());
		System.out.println(noob.getHonor());
		assertTrue(noob.getHonor() < 0);

		System.out.println("----");
		noob = new Fighter();
		health = noob.getStrength();
		noob.doNotBeLazy(100);
		noob.doNotBeLazy(100);
		assertEquals(health, noob.getStrength());
		System.out.println(noob.getHonor());
		assertTrue(noob.getHonor() < 0);

		noob = new Fighter();
		health = noob.getStrength();
		try {
			noob.weakenDueToBattle(health * 0.8);
		} catch (DroppedWeaponException e) {
		}
		int hodor = noob.getHonor();
		System.out.println("hodor:" + hodor);
		assertTrue(hodor > 0);

		noob.doNotBeLazy(100);
		assertTrue(hodor > noob.getHonor());
		System.out.println("hodor:" + noob.getHonor());
	}

	@Test
	public void honorStaysWithinBounds() {
		System.out.println("+++++honorStaysWithinBounds++++++");
		double maxHealth = 10;
		Fighter fighter = new Fighter(maxHealth);
		double foeStrength = maxHealth * 0.9;

		while (fighter.getStrength() >= 0) {
			System.out.println(fighter.getStrength() + " <---- " + foeStrength);
			try {
				fighter.weakenDueToBattle(foeStrength);
			} catch (DroppedWeaponException e) {
			}
			fighter.praise(new Fighter(foeStrength));
			System.out.println(fighter.getStrength() + " -x-x- ");
//			fighter.heal(1);
			assertTrue(fighter.getHonor() >= 0);
		}
		System.out.println("hodor:" + fighter.getHonor());
		assertTrue(fighter.getHonor() < 100);

	}

	@Test
	public void gainJustAsMuchFromManyWeakFoes() {
		System.out.println("+++++gainSomeFromManyWeakFoes++++++");
		double maxHealth = 10;
		Fighter fighter = new Fighter(maxHealth);
		double foeStrength = maxHealth * 0.05;

		while (fighter.getStrength() >= 0) {
			System.out.println(fighter.getStrength() + " <---- " + foeStrength);
			try {
				fighter.weakenDueToBattle(foeStrength);
			} catch (DroppedWeaponException e) {
			}
			fighter.praise(new Fighter(foeStrength));
			System.out.println(fighter.getStrength() + " -x-x- ");
			assertTrue(fighter.getHonor() >= 0);
		}
		System.out.println("hodor:" + fighter.getHonor());
		assertTrue(fighter.getHonor() < 1050);
		assertTrue(fighter.getHonor() > 1000);

	}

	@Test
	public void gainJustAsMuchFromUnweakeningStrongFoes() {
		System.out.println("+++++gainJustAsMuchFromUnweakeningStrongFoes++++++");
		double maxHealth = 10;
		Fighter fighter = new Fighter(maxHealth);
		double foeStrength = maxHealth;

		while (fighter.getStrength() >= 0) {
			System.out.println(fighter.getStrength() + " <---- " + foeStrength);
			try {
				fighter.weakenDueToBattle(foeStrength);
			} catch (DroppedWeaponException e) {
			}
			fighter.praise(new Fighter(foeStrength));
			System.out.println(fighter.getStrength() + " -x-x- ");
			assertTrue(fighter.getHonor() >= 0);
		}
		System.out.println("hodor:" + fighter.getHonor());
		assertTrue(fighter.getHonor() < 200);
		assertTrue(fighter.getHonor() > 150);
	}

	@Test
	public void gainBitFromOverwhelmingFoes() {
		System.out.println("+++++gainBitFromOverwhelmingFoes++++++");
		double maxHealth = 10;
		Fighter fighter = new Fighter(maxHealth);
		double foeStrength = maxHealth * 2;

		while (fighter.getStrength() >= 0) {
			System.out.println(fighter.getStrength() + " <---- " + foeStrength);
			try {
				fighter.weakenDueToBattle(foeStrength);
			} catch (DroppedWeaponException e) {
			}
			fighter.praise(new Fighter(foeStrength));
			System.out.println(fighter.getStrength() + " -x-x- ");
			assertTrue(fighter.getHonor() >= 0);
		}
		System.out.println("hodor:" + fighter.getHonor());
		assertTrue(fighter.getHonor() < 100);
		assertTrue(fighter.getHonor() > 0);
	}

	@Test
	public void gainAtLeastOneHonor() {
		System.out.println("+++++gainAtLeastOneHonor++++++");
		double maxHealth = 20;
		Fighter winner = new Fighter(maxHealth);
		int hodor = winner.getHonor();

		try {
			winner.weakenDueToBattle(10);
		} catch (DroppedWeaponException e1) {
		}
		assertTrue(winner.getHonor() >= hodor);

		for (int i = 10; i > 0; i--) {
			hodor = winner.getHonor();
			winner.heal(200);

			try {
				winner.weakenDueToBattle(i);
			} catch (DroppedWeaponException e) {
			}
			if (i < maxHealth) {
				winner.praise(new Fighter(i));
			}
			assertTrue(winner.getHonor() > hodor);
		}
	}

	@Test
	public void moreWinsMoreHonorGain() {
		System.out.println("+++++moreWinsMoreHonorGain++++++");
		Fighter winner = new Fighter(20);
		int hodor = winner.getHonor();

		for (int i = 10; i > 0; i--) {
			hodor = winner.getHonor();
			winner.heal(200);
			winner.praise(new Fighter(0));
			try {
				winner.weakenDueToBattle(10);
			} catch (DroppedWeaponException e) {
			}
			int gained = winner.getHonor() - hodor;

			assertTrue(gained >= 0);
		}
	}

	@Test
	public void healingToMaxDoesNotBandage() {
		System.out.println("+++++healingToMaxDoesNotBandage++++++");
		Fighter cutPuppet = new Fighter(20);
		double maxHealth = cutPuppet.getStrength();
		double diff = 0;

		for (int i = 10; i > 0; i--) {
			try {
				cutPuppet.weakenDueToBattle(10);
			} catch (DroppedWeaponException e) {
			}
			diff = maxHealth - cutPuppet.getStrength();
			cutPuppet.heal(diff);
		}
	}

	@Test
	public void healingBeyondMaxBandages() {
		System.out.println("+++++healingToMaxDoesNotBandage++++++");
		double maxHealth = 20;
		double potentialBleeding = 10;
		double potentialDamage = 20.25;
		Fighter cutPuppet = new Fighter(maxHealth);

		for (int i = 1; i < 10; i++) {
			System.out.println("~~~~~~~~~~~~~~~~~~~~ round " + i);
			assertTrue(cutPuppet.isAlive());
			assertEquals(maxHealth, cutPuppet.getStrength());
			assertEquals(0, cutPuppet.countWounds());

			try {
				cutPuppet.weakenDueToBattle(i * 10);
			} catch (DroppedWeaponException e) {
			}
			assertEquals(1, cutPuppet.countWounds());
			cutPuppet.heal(maxHealth + potentialDamage + potentialBleeding);

			assertTrue(cutPuppet.isAlive());
			assertEquals(maxHealth, cutPuppet.getStrength());
			assertEquals(0, cutPuppet.countWounds());
		}
	}

	@Test
	public void bigFoesLeadToGreatHonor() {
		System.out.println("+++++healingToMaxDoesNotBandage++++++");
		double maxHealth = 1;
		Fighter cutPuppet = new Fighter(maxHealth);
		double expectedDamage = 1600;
		double expectedBleeding = 10;

		try {
			cutPuppet.weakenDueToBattle(40);
		} catch (DroppedWeaponException e) {
		}

		assertTrue(cutPuppet.countWounds() > 0);
		assertEquals(1, cutPuppet.countWounds());
		cutPuppet.heal(expectedDamage + expectedBleeding + maxHealth);
		assertEquals(maxHealth, cutPuppet.getStrength());
		assertTrue(cutPuppet.isAlive());
	}
}
