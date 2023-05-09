package auxiliares;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("unused")
public class TestsGrafo {
	private Grafo grafoPrueba1;
	
	@Before
	public void declararGrafos() {
		grafoPrueba1 = new Grafo(5);
	}
	@Test
	public void crearGrafo() {
		Grafo grafo = new Grafo(2);
	}
	
	@Test (expected = NegativeArraySizeException.class)
	public void crearGrafoError() {
		Grafo grafo = new Grafo(-1);
	}
	
	@Test
	public void agregarAristaTrue() {
		grafoPrueba1.agregarArista(2, 4, 1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void agregarAristaFueraTamanio() {
		grafoPrueba1.agregarArista(2, 5, 1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void agregarAristaLongitudMenorIgualA_0() {
		grafoPrueba1.agregarArista(2, 4, 0);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void agregarAristaVerticesIguales() {
		grafoPrueba1.agregarArista(2, 2, 0);
	}
	
	@Test
	public void eliminarAristaNoExiste() {
		grafoPrueba1.eliminarArista(2, 4);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void eliminarAristaVerticesIguales() {
		grafoPrueba1.eliminarArista(2, 2);
	}
	
	@Test
	public void eliminarAristaExistente() {
		grafoPrueba1.agregarArista(2, 4, 1);
		grafoPrueba1.eliminarArista(2, 4);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void eliminarAristaFueraTamanio() {
		grafoPrueba1.eliminarArista(2,5);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void sonVecinosVerticesFueraTamanio() {
		grafoPrueba1.sonVecinos(5, 4);
	}
	
	@Test
	public void sonVecinosAristaNoExiste() {
		assertFalse(grafoPrueba1.sonVecinos(0, 1));
	}
	
	@Test
	public void sonVecinosFalse() {
		grafoPrueba1.agregarArista(2, 4, 4);
		assertFalse(grafoPrueba1.sonVecinos(2, 1));
	}
	
	@Test
	public void sonVecinosTrue() {
		grafoPrueba1.agregarArista(2, 4, 4);
		assertTrue(grafoPrueba1.sonVecinos(2, 4));
	}
	
	@Test
	public void agregarVertice() {
		Grafo grafo = new Grafo(4);
		Grafo grafo2 = new Grafo(5);
		
		grafo.agregarVertice();
		assertEquals(grafo, grafo2);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void eliminarVerticeFueraTamanio() {
		grafoPrueba1.eliminarVertice(5);
	}
	
	@Test
	public void eliminarVertice() {
		grafoPrueba1.agregarArista(0, 2, 3);
		grafoPrueba1.agregarArista(0, 3, 1);
		grafoPrueba1.agregarArista(0, 4, 2.1);
		grafoPrueba1.eliminarVertice(0);
		
		Grafo grafo = new Grafo(4);
		
		assertEquals(grafoPrueba1, grafo);
	}
	
	@Test
	public void obtenerVecinosTrue() {
		grafoPrueba1.agregarArista(0, 1, 2);
		grafoPrueba1.agregarArista(0, 2, 2);
		grafoPrueba1.agregarArista(0, 3, 2);
		grafoPrueba1.agregarArista(0, 4, 2);
		
		LinkedList<Integer> vecinos = new LinkedList<Integer>();
		vecinos.add(1);
		vecinos.add(2);
		vecinos.add(3);
		vecinos.add(4);
		
		assertEquals(grafoPrueba1.obtenerVecinos(0), vecinos);
	}
	
	@Test
	public void obtenerVecinosFalse() {
		grafoPrueba1.agregarArista(0, 1, 2);
		grafoPrueba1.agregarArista(0, 2, 2);
		grafoPrueba1.agregarArista(0, 3, 2);
		grafoPrueba1.agregarArista(0, 4, 2);
		
		LinkedList<Integer> vecinos = new LinkedList<Integer>();
		vecinos.add(1);
		vecinos.add(2);
		vecinos.add(3);
		vecinos.add(5);
		
		assertNotEquals(grafoPrueba1.obtenerVecinos(0), vecinos);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void obtenerVecinosFueraTamanio() {
		grafoPrueba1.obtenerVecinos(5);
	}
	
	@Test
	public void obtenerLongitudAristaDeclarada() {
		grafoPrueba1.agregarArista(3, 4, 150);
		assertEquals((Double) 150.0, grafoPrueba1.obtenerLongitud(4, 3));
	}
	
	@Test (expected = NullPointerException.class)
	public void obtenerLongitudAristaNoDeclarada() {
		grafoPrueba1.agregarArista(2, 4, 150);
		grafoPrueba1.obtenerLongitud(4, 3);
	}
	
	@Test
	public void soyConexoTrue() {
		grafoPrueba1.agregarArista(0, 1, 1);
		grafoPrueba1.agregarArista(1, 2, 1);
		grafoPrueba1.agregarArista(2, 3, 1);
		grafoPrueba1.agregarArista(3, 4, 1);
		
		assertTrue(grafoPrueba1.soyConexo());
	}
	
	@Test
	public void soyConexoFalse() {
		grafoPrueba1.agregarArista(0, 1, 1);
		grafoPrueba1.agregarArista(1, 2, 1);
		grafoPrueba1.agregarArista(3, 4, 1);
		
		assertFalse(grafoPrueba1.soyConexo());
	}
	
}
