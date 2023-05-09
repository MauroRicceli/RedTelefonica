package auxiliaresGrafo;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import auxiliares.Grafo;


@SuppressWarnings("unused")
public class TestsArbolMinimo {
	private Grafo grafoPrueba1;
	private Grafo grafoPrueba2;
	
	@Before
	public void declararGrafos() {
		grafoPrueba1 = new Grafo(5);
		grafoPrueba2 = new Grafo(5);
	}
	
	public Grafo generarAGM(Grafo grafoObjetivo) {
		ArbolMinimo AGM = new ArbolMinimo(grafoObjetivo);
		return AGM.obtenerArbolMinimo();
	}
	
	@Test (expected = RuntimeException.class)
	public void arbolGeneradorNoConexo() {
		grafoPrueba1.agregarArista(0, 1, 3);
		grafoPrueba1.agregarArista(0, 2, 3);
		grafoPrueba1.agregarArista(0, 3, 3);
		grafoPrueba1.agregarArista(1, 2, 3);
		grafoPrueba1.agregarArista(2, 3, 3);
		grafoPrueba1.agregarArista(3, 1, 3);
		//vertice 4 no conectado con nadie
		
		Grafo nuevo = generarAGM(grafoPrueba1);	
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void arbolGeneradorMinimoTrue() {
		grafoPrueba1.agregarArista(0, 1, 3);
		grafoPrueba1.agregarArista(0, 2, 3);
		grafoPrueba1.agregarArista(0, 4, 1);
		grafoPrueba1.agregarArista(1, 2, 1);
		grafoPrueba1.agregarArista(1, 3, 3);
		grafoPrueba1.agregarArista(3, 0, 1);
		grafoPrueba1.agregarArista(1, 4, 3);
		grafoPrueba1.agregarArista(2, 3, 3);
		
		Grafo nuevo = generarAGM(grafoPrueba1);
		
		Double[][] aux = {
				{null, 3.0, null, 1.0, 1.0},
				{3.0, null, 1.0, null, null},
				{null, 1.0, null, null, null},
				{1.0, null, null, null, null},
				{1.0, null, null, null, null},
		}; //ESTO DEBERIA QUEDAR
		
		assertEquals(nuevo.getMatAdy(), aux);
	}
	
	@Test
	public void arbolGeneradorMinimoFalse() {
		grafoPrueba1.agregarArista(0, 1, 3);
		grafoPrueba1.agregarArista(0, 2, 3);
		grafoPrueba1.agregarArista(0, 4, 1);
		grafoPrueba1.agregarArista(1, 2, 1);
		grafoPrueba1.agregarArista(1, 3, 3);
		grafoPrueba1.agregarArista(3, 0, 1);
		grafoPrueba1.agregarArista(1, 4, 3);
		grafoPrueba1.agregarArista(2, 3, 3);
		
		Grafo nuevo = generarAGM(grafoPrueba1);
		
		Double[][] aux = {
				{3.0, null, null, 1.0, 1.0},
				{null, null, 1.0, null, null},
				{null, 1.0, null, null, null},
				{1.0, null, null, 3.0, null},
				{1.0, null, null, null, null},
		}; //ESTO DEBERIA QUEDAR
		
		assertNotEquals(nuevo.getMatAdy(), aux);
	}
	
	@Test
	public void arbolGeneradorMinimoEquals() {
		grafoPrueba1.agregarArista(0, 1, 3);
		grafoPrueba1.agregarArista(0, 2, 3);
		grafoPrueba1.agregarArista(0, 4, 1);
		grafoPrueba1.agregarArista(1, 2, 1);
		grafoPrueba1.agregarArista(1, 3, 3);
		grafoPrueba1.agregarArista(3, 0, 1);
		grafoPrueba1.agregarArista(1, 4, 3);
		grafoPrueba1.agregarArista(2, 3, 3);
		
		grafoPrueba2.agregarArista(0, 1, 3);
		grafoPrueba2.agregarArista(0, 2, 3);
		grafoPrueba2.agregarArista(0, 4, 1);
		grafoPrueba2.agregarArista(1, 2, 1);
		grafoPrueba2.agregarArista(1, 3, 3);
		grafoPrueba2.agregarArista(3, 0, 1);
		grafoPrueba2.agregarArista(1, 4, 3);
		grafoPrueba2.agregarArista(2, 3, 3);
		
		ArbolMinimo AGM1 = new ArbolMinimo(grafoPrueba1);
		ArbolMinimo AGM2 = new ArbolMinimo(grafoPrueba2);
		
		assertEquals(AGM1.obtenerArbolMinimo(), AGM2.obtenerArbolMinimo());
	}
	
	@Test
	public void arbolGeneradorMinimoNotEquals() {
		grafoPrueba1.agregarArista(0, 1, 3);
		grafoPrueba1.agregarArista(0, 2, 3);
		grafoPrueba1.agregarArista(0, 4, 1);
		grafoPrueba1.agregarArista(1, 2, 1);
		grafoPrueba1.agregarArista(1, 3, 3);
		grafoPrueba1.agregarArista(3, 0, 1);
		grafoPrueba1.agregarArista(1, 4, 3);
		grafoPrueba1.agregarArista(2, 3, 3);
		
		grafoPrueba2.agregarArista(0, 1, 3);
		grafoPrueba2.agregarArista(0, 2, 3);
		grafoPrueba2.agregarArista(0, 4, 1);
		grafoPrueba2.agregarArista(1, 2, 2); //cambio pesos
		grafoPrueba2.agregarArista(1, 3, 1); //
		grafoPrueba2.agregarArista(3, 0, 1);
		grafoPrueba2.agregarArista(1, 4, 3);
		grafoPrueba2.agregarArista(2, 3, 3);
		
		ArbolMinimo AGM1 = new ArbolMinimo(grafoPrueba1);
		ArbolMinimo AGM2 = new ArbolMinimo(grafoPrueba2);
		
		assertNotEquals(AGM1.obtenerArbolMinimo(), AGM2.obtenerArbolMinimo());
	}
	
	
}
