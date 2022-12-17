package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Callable;

import repositorios.AdnOM;
import repositorios.AlimentoOM;

public class Hormiga implements Callable<IdoneidadADN> {

	private int vidaMax = 100, vidaMin = 80;
	private int vida = getVidaAleatoria(vidaMin, vidaMax);
	private int edad = 0;
	private String adn;
	public static final int cantidadPoderNacimiento = 1000;
	private List<Alimento> recogidos;
	private Hormiguero hormiguero;
	public int id;

	public Hormiga(int id, String adnProgenitor, Hormiguero hormiguero) {
		super();
		this.adn = AdnOM.getNuevoAdn(adnProgenitor);
		this.hormiguero = hormiguero;
		recogidos = new ArrayList<Alimento>();
		this.id = id;
	}

	/**
	 * Compara si el alimento encontrado merece la pena ser recogido o no
	 * 
	 * @return
	 */
	// Optional
	private Optional<Alimento> recoger() {
		Alimento encontrado = AlimentoOM.getAlimentoOM();
		float indiceSaludYPoderActual = getIndiceSalubridadPoderMedio();
		if (encontrado.getIndiceSalubridadPoder() >= indiceSaludYPoderActual / 2) {
			return Optional.of(encontrado);
		}
		return Optional.ofNullable(null);
	}

	/**
	 * calcula el indice actual de salubridad/poder que le permite recoger el
	 * alimento, o no y al final de su vida, crear un objeto de tipo IdoneidadADN
	 * 
	 * @return
	 */
	private float getIndiceSalubridadPoderMedio() {
		// java 8
		return (float) recogidos.stream().mapToDouble((alimento)->{return alimento.getIndiceSalubridadPoder();}).average().getAsDouble();
		//java 6
//		float sumatorio = 0;
//		for (Alimento alimento : recogidos) {
//			sumatorio += alimento.getIndiceSalubridadPoder();
//		}
//		if (recogidos.isEmpty())
//			return 0;
//		return sumatorio / recogidos.size();
	}

	

	private void deambular() {
		int tiempoBusquedaMax = 2;
		try {
			Thread.sleep(new Random().nextInt(tiempoBusquedaMax));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//Optional
		recoger().ifPresent((alimento)->{
			recogidos.add(alimento);
			acarrear(alimento);
		});
	}

	/**
	 * lleva el alimento recogido a la despensa
	 * @param alimento
	 */
	private void acarrear(Alimento alimento) {
		synchronized (hormiguero) {
			hormiguero.descargar(alimento);
		}
	}

	@Override
	public IdoneidadADN call() throws Exception {
		do {
			deambular();
			edad++;
		} while (isAlive());
		return new IdoneidadADN(adn, getIndiceSalubridadPoderMedio());
	}

	public boolean isAlive() {
		return vida > edad;
	}

	public List<Alimento> getRecogidos() {
		return recogidos;
	}

	public int getEdad() {
		return edad;
	}
	private int getVidaAleatoria(int vidaMin, int vidaMax) {
		return new Random().nextInt(vidaMax - vidaMin) + vidaMin;
	}
}
