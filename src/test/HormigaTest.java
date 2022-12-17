package test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Test;

import modelo.Alimento;
import modelo.Hormiga;
import modelo.Hormiguero;
import modelo.IdoneidadADN;

class HormigaTest {

	@Test
	void test() throws Exception {
		Hormiguero hormiguero = new Hormiguero();
		ExecutorService executorService = Executors.newCachedThreadPool();
		for (int i = 0; i < 50; i++) {
			Hormiga hormiga = new Hormiga(1, "AA", hormiguero);
			Future<IdoneidadADN> future = executorService.submit(hormiga);
			IdoneidadADN call=future.get();
			List<Alimento> recogidos = hormiga.getRecogidos();
			int edad = hormiga.getEdad();
//			System.out.println(recogidos.size() + " " + edad);
//			System.out.println(call.toString());
			int mitad = 2;
			// debido a las limitaciones impuestas las veces que recoge
			// es menos del tercio de su edad
			assertTrue(recogidos.size() < edad / mitad);
			assertTrue(call.getIndiceSalubridadPoder()>40);
		}

	}

}
