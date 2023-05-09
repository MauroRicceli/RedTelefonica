package auxiliares;

import java.util.ArrayList;
import java.util.LinkedList;

public class Grafo {
	private Integer _cantVertices;
	private Double[][] _matAdyacencia;
	
	public Grafo(int cantVertices) {
		_cantVertices = cantVertices;
		_matAdyacencia = new Double[cantVertices][cantVertices];
	}
	
	public void verticeValido(int verticeInicial, int verticeFinal) {
		if(verticeInicial < 0 || verticeInicial >= _cantVertices || verticeFinal < 0 || verticeFinal >= _cantVertices) {
			throw new IllegalArgumentException("Los vertices ingresados deben ser mayores a 0 y menores a "+_cantVertices+1);
		} else {
			if(verticeInicial == verticeFinal) {
				throw new IllegalArgumentException("Los vertices ingresados no pueden ser iguales");
			}
		}
	}
	
	public void agregarArista(int verticeInicial, int verticeFinal, double longitud) {
		verticeValido(verticeInicial, verticeFinal);
		if(longitud <= 0) {
			throw new IllegalArgumentException("La longitud de una arista no puede ser menor o igual a 0");
		}
		
		_matAdyacencia[verticeInicial][verticeFinal] = longitud;
		_matAdyacencia[verticeFinal][verticeInicial] = longitud;
	}
	
	public void eliminarArista(int verticeInicial, int verticeFinal) {
		verticeValido(verticeInicial, verticeFinal);
		
		_matAdyacencia[verticeInicial][verticeFinal] = null;
		_matAdyacencia[verticeFinal][verticeInicial] = null;
		
	}
	
	public boolean sonVecinos(int verticeInicial, int verticeFinal) {
		verticeValido(verticeInicial, verticeFinal);
		
		if(_matAdyacencia[verticeInicial][verticeFinal] != null) {
			return true;
		}
		return false;
	}
	
	public void agregarVertice() {
		_cantVertices++;
		Double[][] nuevaMatriz = new Double[_cantVertices][_cantVertices];
		
		for(int fila = 0; fila <= _matAdyacencia.length-1; fila++) {
			for(int columna = 0; columna <= _matAdyacencia[fila].length-1; columna++) {
				if(_matAdyacencia[fila][columna] != null) {
					nuevaMatriz[fila][columna] = _matAdyacencia[fila][columna];
				}
			}
		}
		
		_matAdyacencia = nuevaMatriz;
	}
	
	public void eliminarVertice(int vertice) {
		if(vertice < 0 || vertice >= _cantVertices) {
			throw new IllegalArgumentException("El vertice a eliminar debe estar entre el rango manejable <0,"+ _cantVertices+"> :"+vertice);
		}
		
		_cantVertices--;
		Double[][] nuevaMatriz = new Double[_cantVertices][_cantVertices];
		boolean llegueAlVerticeColumna = false;
		boolean llegueAlVerticeFila = false;
		
		
		for(int fila = 0; fila <= _matAdyacencia.length-1; fila++) {
			if(fila == vertice && llegueAlVerticeFila == false) {
				llegueAlVerticeFila = true;
			}
			for(int columna = 0; columna <= _matAdyacencia[fila].length-1; columna++) {
				if(fila != vertice) {
					if(columna == vertice && llegueAlVerticeColumna == false) {
						llegueAlVerticeColumna = true;
					}
					if(llegueAlVerticeColumna == true && llegueAlVerticeFila == true && columna != vertice) {
						nuevaMatriz[fila-1][columna-1] = _matAdyacencia[fila][columna];
					} else {
						if(llegueAlVerticeColumna == false && llegueAlVerticeFila == true && columna != vertice) {
							nuevaMatriz[fila-1][columna] = _matAdyacencia[fila][columna];
						} else {	
							if(llegueAlVerticeColumna == true && columna != vertice) {
								nuevaMatriz[fila][columna-1] = _matAdyacencia[fila][columna];
							} else {
								if(llegueAlVerticeColumna == false && columna != vertice) {
									nuevaMatriz[fila][columna] = _matAdyacencia[fila][columna];
								}
							}	
						}
					}
				}					
			}
			llegueAlVerticeColumna = false;
		}
		
		_matAdyacencia = nuevaMatriz;
		
	}
	
	public Double obtenerLongitud(int verticeSalida, int verticeLlegada) {
		if(sonVecinos(verticeSalida, verticeLlegada)) {
			return _matAdyacencia[verticeSalida][verticeLlegada];
		}
		throw new NullPointerException("La longitud entre esos vertices no esta declarada: "+verticeSalida+" , "+verticeLlegada);
	}
	
	public LinkedList<Integer> obtenerVecinos(int vertice) {
		if(vertice < 0 || vertice >= _cantVertices) {
			throw new IllegalArgumentException("El vertice ingresado debe ser mayor a 0 y menor a "+_cantVertices+1);
		}
		LinkedList<Integer> ret = new LinkedList<Integer>();
		for(int columna = 0; columna <= _matAdyacencia[vertice].length-1; columna++) {
			if(_matAdyacencia[vertice][columna] != null) {
				ret.add(columna);
			}
		}
		
		return ret;
		
	}
	
	public boolean soyConexo() {
		ArrayList<Integer> lista = new ArrayList<Integer>();
		boolean[] marcados = new boolean[_cantVertices];
		lista.add(0); //origen random
		
		while(lista.size() > 0) {
			int i = lista.get(0);
			marcados[i] = true;
			soyConexoAgregarVecinosPendientes(marcados, i, lista);
			lista.remove(0);
		}
		
		boolean aux = true;
		for(int i = 0; i <= marcados.length-1; i++) {
			aux = aux && marcados[i];
		}
		return aux;
		
	}
	
	public void soyConexoAgregarVecinosPendientes(boolean[] marcados, int i, ArrayList<Integer> lista) {
		for(int vertice : obtenerVecinos(i)) {
			if(marcados[vertice] == false && lista.contains(vertice) == false) { //si no esta marcado aun y no está en la lista (aun no esta en la cola para probarse)
				lista.add(vertice);
			}
		}
	}
	
	public Double[][] getMatAdy(){
		return _matAdyacencia;
	}
	
	public Integer getCantVertices() {
		return _cantVertices;
	}
	
	public boolean equals(Object object) {
		if(object instanceof Grafo) {
			Grafo aux = (Grafo) object;
			if(matAdyIgual(aux.getMatAdy()) == true) {
				return true;
			}
			return false;
		} else {
			return false;
		}
	}
	
	private boolean matAdyIgual(Double[][] matAdyAux) {
		boolean resultado = true;
		for(int fila = 0; fila <= _matAdyacencia.length-1; fila++) {
			for(int columna = 0; columna <= _matAdyacencia[fila].length-1; columna++) {
				if(_matAdyacencia[fila][columna] != null) {
					resultado = resultado && (_matAdyacencia[fila][columna].equals(matAdyAux[fila][columna]));
				} else {
					resultado = resultado && (_matAdyacencia[fila][columna] == matAdyAux[fila][columna]);
				}
				
			}
		}
		return resultado;
	}
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int fila = 0; fila <= _matAdyacencia.length-1; fila++) {
			sb.append("[");
			for(int columna = 0; columna <= _matAdyacencia[fila].length-1; columna++) {
				if(_matAdyacencia[fila][columna] == null){
					sb.append(" null");
				} else {
					sb.append(" "+_matAdyacencia[fila][columna]);
				}
			}
			sb.append(" ] \n");
		}
		return sb.toString();
	}
}	

