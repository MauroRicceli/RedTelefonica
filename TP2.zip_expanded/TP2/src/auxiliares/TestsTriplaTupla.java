package auxiliares;

import static org.junit.Assert.*;

import org.junit.Test;

@SuppressWarnings("unused")
public class TestsTriplaTupla {

	@Test
	public void triplaCrear() {
		Tripla<String, Integer, Integer> aux = new Tripla<String, Integer, Integer>("José", 23, 42526721);
	}
	
	@Test
	public void triplaIgualTrue() {
		Tripla<String, Integer, Integer> aux = new Tripla<String, Integer, Integer>("Ramón", 23, 42556721);
		Tripla<String, Integer, Integer> aux2 = new Tripla<String, Integer, Integer>("Ramón", 23, 42556721);
		assertEquals(aux, aux2);
	}
	
	@Test
	public void triplaIgualFalse() {
		Tripla<String, Integer, Integer> aux = new Tripla<String, Integer, Integer>("José", 23, 42526721);
		Tripla<String, Integer, Integer> aux2 = new Tripla<String, Integer, Integer>("Ramón", 23, 42556721);
		assertNotEquals(aux, aux2);
	}
	
	@Test
	public void tuplaCrear() {
		Tupla<Integer, String> aux = new Tupla<Integer, String>(1, "Hola");
	}
	
	@Test
	public void tuplaIgualTrue() {
		Tupla<Integer, String> aux = new Tupla<Integer, String>(1, "Hola");
		Tupla<Integer, String> aux2 = new Tupla<Integer, String>(1, "Hola");
		assertEquals(aux, aux2);
	}
	
	@Test
	public void tuplaIgualFalse() {
		Tupla<Integer, String> aux = new Tupla<Integer, String>(1, "Hola");
		Tupla<Integer, String> aux2 = new Tupla<Integer, String>(1, "Chau");
		assertNotEquals(aux, aux2);
	}

}
