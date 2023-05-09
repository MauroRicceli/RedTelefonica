package auxiliares;

/**
 * Clase que permite representar un objeto ordenado con tres argumentos, siendo estos cualquier objeto existente.
 * 
 * 
 * @author Mauro Ricceli
 * @author Jazmin Quinteros
 *
 * @param <A> objeto 1
 * @param <B> objeto 2
 * @param <C> objeto 3
 */

public class Tripla<A, B, C> {
	private A _objeto1;
	private B _objeto2;
	private C _objeto3;
	
	/**
	 * Crea una tripla de tres objetos ingresados.
	 * @param objeto1 primer objeto de la tripla
	 * @param objeto2 segundo objeto de la tripla
	 * @param objeto3 tercer objeto de la tripla
	 */
	
	public Tripla(A objeto1, B objeto2, C objeto3) {
		_objeto1 = objeto1;
		_objeto2 = objeto2;
		_objeto3 = objeto3;
	}
	
	/**
	 * Analiza si dos triplas son exactamente iguales
	 * @return true si son iguales // false si son distintas
	 */
	public boolean equals(Object object) {
		if(object instanceof Tripla) {
			@SuppressWarnings("unchecked")
			Tripla<A,B,C> aux = (Tripla<A,B,C>) object;
			if(_objeto1.equals(aux.getFirst()) && _objeto2.equals(aux.getSecond()) && _objeto3.equals(aux.getThird())) {
				return true;
			}
			return false;
		} else {
			return false;
		}
	}
	
	
	public String toString() {
		return "< "+_objeto1.toString()+", "+_objeto2.toString()+", "+_objeto3.toString()+" >";
	}
	
	public void setFirst(A objeto) {
		_objeto1 = objeto;
	}
	
	public void setSecond(B objeto) {
		_objeto2 = objeto;
	}
	
	public void setThird(C objeto) {
		_objeto3 = objeto;
	}
	
	public A getFirst() {
		return _objeto1;
	}
	
	public B getSecond() {
		return _objeto2;
	}
	
	public C getThird() {
		return _objeto3;
	}
}
