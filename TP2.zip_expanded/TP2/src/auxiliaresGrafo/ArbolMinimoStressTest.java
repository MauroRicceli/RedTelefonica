package auxiliaresGrafo;

import java.util.Random;

import auxiliares.Grafo;

public class ArbolMinimoStressTest {
	
	public static void main(String[] args) {
		for(int n=2; n<200; ++n) {
			long inicio = System.currentTimeMillis();
			Grafo aleatorio = aleatorio(n);
			ArbolMinimo AGM = new ArbolMinimo(aleatorio);
			AGM.obtenerArbolMinimo();
			
			long fin = System.currentTimeMillis();
			double tiempo = (fin - inicio) / 1000.0;
			
			System.out.println("n = " + n + ": " + tiempo + " seg.");
		}
	}
	
	private static Grafo aleatorio(int n)
	{
		Grafo grafo = new Grafo(n);
		Random random = new Random(0);
		Random randomPeso = new Random(10);
		boolean yaConectado = false; //al menos un camino por nodo para cumplir con la conexidad
		
		for(int i=0; i<n; ++i) {
			for(int j=i+1; j<n; ++j) {
				if(!yaConectado) {
					grafo.agregarArista(i, j, randomPeso.nextDouble());
					yaConectado = true;
				} else {
					if( random.nextDouble() < 0.5 ) {
						grafo.agregarArista(i, j, randomPeso.nextDouble());
					}
				}
			}
			yaConectado = false;
		}	
		
		return grafo;		
	}


}
