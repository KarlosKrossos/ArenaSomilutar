package quatro;

public class DroppedWeaponException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9189868388869922509L;

	Integer weapon;

	public DroppedWeaponException(Integer dropped) {
//		if (null != dropped)
			weapon = dropped;
	}

	public Integer getWeapon() {
		return weapon;
	}

}
