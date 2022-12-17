package modelo;

public class IdoneidadADN {
	private String adn;
	private float indiceSalubridadPoder;

	public IdoneidadADN(String adn, float indiceSalubridadPoder) {
		super();
		this.adn = adn;
		this.indiceSalubridadPoder = indiceSalubridadPoder;
	}

	public String getAdn() {
		return adn;
	}

	public float getIndiceSalubridadPoder() {
		return indiceSalubridadPoder;
	}

	public int isMejor(IdoneidadADN idoneo) {
		return (int) (indiceSalubridadPoder- idoneo.indiceSalubridadPoder);
	}
	@Override
	public String toString() {
		return adn+indiceSalubridadPoder;
	}

}
