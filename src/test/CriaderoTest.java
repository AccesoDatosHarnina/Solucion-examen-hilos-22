package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import modelo.Alimento;
import modelo.Criadero;
import modelo.Hormiguero;
import modelo.IdoneidadADN;

class CriaderoTest {

	@Test
	void testCriadero() throws InterruptedException, ExecutionException {
		Hormiguero hormiguero=new Hormiguero();
		//pata, poder de cien, cantidad para criar hormiga 1000
		//con esto puede crear solo una hormiga
//		System.out.println("hormigas "+hormiguero.getHormigas().size());
		int anterior=hormiguero.getHormigas().size();
		int seres=10;
		int cantidad=10;
		hormiguero.getDespensa().put(Alimento.pata, cantidad*seres);	
		List<IdoneidadADN> idoneidades=new ArrayList<IdoneidadADN>();
		idoneidades.add(new IdoneidadADN("BB", 100f));
		hormiguero.setIdoneidades(idoneidades);
		Criadero criadero=new Criadero(hormiguero);
		ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
		hormiguero.getHormigas().addAll(newCachedThreadPool.submit(criadero).get());
		newCachedThreadPool.awaitTermination(1, TimeUnit.SECONDS);
		newCachedThreadPool.shutdown();
		int posterior=hormiguero.getHormigas().size();
		assertEquals(anterior+seres, posterior);
		assertEquals(0,hormiguero.getDespensa().get(Alimento.pata));
	}

}
