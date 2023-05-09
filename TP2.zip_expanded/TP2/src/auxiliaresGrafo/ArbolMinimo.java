package auxiliaresGrafo;

import java.util.ArrayList;
import java.util.LinkedList;

import auxiliares.Grafo;
import auxiliares.Tripla;

public class ArbolMinimo {
	private Grafo _grafo;
	
	public ArbolMinimo(Grafo grafo) {
		_grafo = grafo;
	}
	
	public Grafo obtenerArbolMinimo() {
		if(_grafo.soyConexo() == false) {
			throw new RuntimeException("No se puede aplicar arbol generador a un grafo no conexo");
		}
		
		ArrayList<Integer> lista = new ArrayList<Integer>();
		lista.add(0);
		Grafo grafoArbol = new Grafo(_grafo.getCantVertices());
		int i = 1;
		while(i < _grafo.getCantVertices()) {
			Tripla<Integer, Integer, Double> aristaAgregar = aristaMasPequena(lista);
			lista.add(aristaAgregar.getSecond());
			grafoArbol.agregarArista(aristaAgregar.getFirst(), aristaAgregar.getSecond(), aristaAgregar.getThird());
			i++;
		}
		return grafoArbol;
	}
	
	private Tripla<Integer, Integer, Double> aristaMasPequena(ArrayList<Integer> lista) {
		Tripla<Integer, Integer, Double> aux = new Tripla<Integer, Integer, Double>(null, null, null);
		//<Vertice1 (ya en la lista), Vertice2 (aún no en la lista), longitud>
		for(int i = 0; i <= lista.size()-1; i++) {
			LinkedList<Integer> vecinos = _grafo.obtenerVecinos(lista.get(i));
			for(int j = 0; j <= vecinos.size()-1; j++) {
				if(lista.contains(vecinos.get(j)) == false) {
					if(aux.getFirst() == null) { //si uno es null, todo es null
						aux.setFirst(lista.get(i));
						aux.setSecond(vecinos.get(j));
						aux.setThird(_grafo.obtenerLongitud(lista.get(i),vecinos.get(j)));
					} else {
						if(aux.getThird() > _grafo.obtenerLongitud(lista.get(i),vecinos.get(j))) { //cambiando mayor o menor es arbol generador maximo/minimo
							aux.setFirst(lista.get(i));
							aux.setSecond(vecinos.get(j));
							aux.setThird(_grafo.obtenerLongitud(lista.get(i),vecinos.get(j)));
						}
					}
				}
			}
		}
		return aux;
	}
}
