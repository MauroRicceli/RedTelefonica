package auxiliares;

/**
 * Clase que permite representar un objeto ordenado con dos argumentos, siendo estos cualquier objeto existente.
 * @author Mauro Ricceli
 * @author Jazmin Quinteros
 *
 * @param <A> objeto 1
 * @param <B> objeto 2
 */

public class Tupla<A, B> {
	private A _objeto1;
	private B _objeto2;
	
	/**
	 * Crea una tupla con los dos objetos ingresados como argumento
	 * @param objeto1 primer objeto
	 * @param objeto2 segundo objeto
	 */
	
	public Tupla(A objeto1, B objeto2) {
		_objeto1 = objeto1;
		_objeto2 = objeto2;
	}
	
	/**
	 * Analiza si dos tuplas son exactamente iguales
	 * @return true si son iguales // false si son distintas
	 */
	
	public boolean equals(Object object) {
		if(object instanceof Tupla) {
			@SuppressWarnings("unchecked")
			Tupla<A, B> aux = (Tupla<A, B>) object;
			if(_objeto1.equals(aux.getFirst()) && _objeto2.equals(aux.getSecond())) {
				return true;
			} else {
				return false;
			}	
		} else {
			return false;
		}
	}
	
	public A getFirst() {
		return _objeto1;
	}
	
	public B getSecond() {
		return _objeto2;
	}
	
	public void setFirst(A objeto) {
		_objeto1 = objeto;
	}
	
	public void setSecond(B objeto) {
		_objeto2 = objeto;
	}
}
