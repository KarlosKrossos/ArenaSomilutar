package quatro;

public class DroppedWeaponException extends Exception {

	private static final long serialVersionUID = 9189868388869922509L;

	private final Integer weapon;

	public DroppedWeaponException(Integer dropped) {
		weapon = dropped;
	}

	public Integer getWeapon() {
		return weapon;
	}

}
