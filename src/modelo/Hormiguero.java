package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import repositorios.HormigasOM;

public class Hormiguero implements Runnable {
	private List<Hormiga> hormigas;
	private Map<Alimento, Integer> despensa;
	private List<IdoneidadADN> idoneidades;
	private ExecutorService executorService;
	private int poblacionMaxima= 10000;
	private Criadero criadero;

	public Hormiguero() {
		super();
		hormigas = HormigasOM.getHormigasInicio(5, "AA", this);
		despensa = new HashMap<Alimento, Integer>();
	}

	@Override
	public void run() {
		idoneidades = new ArrayList();
		executorService = Executors.newCachedThreadPool();
		criadero = new Criadero(this);
		try {
			arrancar();
		} catch (InterruptedException | ExecutionException e1) {
			e1.printStackTrace();
		}
		do {
			Future<List<Hormiga>> submit = executorService.submit(criadero);
			idoneidades = new ArrayList();
			try {
				this.hormigas.addAll(submit.get());
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} catch (ExecutionException e1) {
				e1.printStackTrace();
			}
			try {
				arrancar();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			System.out.println("somos " + hormigas.size() + " hormigas");
		} while (hormigas.size() < poblacionMaxima);
		System.out.println("alcanzada poblacion maxima");
		executorService.shutdown();
	}

	private void arrancar()
			throws InterruptedException, ExecutionException {
		ArrayList<Future<IdoneidadADN>> submits = new ArrayList<Future<IdoneidadADN>>();
		for (Iterator iterator = hormigas.iterator(); iterator.hasNext();) {
			Hormiga hormiga = (Hormiga) iterator.next();
			submits.add(executorService.submit(hormiga));
			
		}
		while(!allSubmitsArrived(submits)) {};
		for (Future<IdoneidadADN> future : submits) {
			idoneidades.add(future.get());
		}
	}

	private boolean allSubmitsArrived(ArrayList<Future<IdoneidadADN>> submits) {
		for (Future<IdoneidadADN> future : submits) {
			if (!future.isDone())
				return false;
		}
		return true;
	}

	public void descargar(Alimento alimento) {
		if (despensa.containsKey(alimento)) {
			Integer cantidad = despensa.get(alimento);
			despensa.put(alimento, ++cantidad);
		} else {
			despensa.put(alimento, 1);
		}
	}

	public List<Hormiga> getHormigas() {
		return hormigas;
	}

	public Map<Alimento, Integer> getDespensa() {
		return despensa;
	}

	public List<IdoneidadADN> getIdoneidades() {
		return idoneidades;
	}

	public void setIdoneidades(List<IdoneidadADN> idoneidades) {
		this.idoneidades = idoneidades;
	}

	public void setHormigas(List<Hormiga> hormigas) {
		this.hormigas = hormigas;
	}

}
