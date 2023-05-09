package modelo;

import java.util.ArrayList;
import java.util.HashSet;

import auxiliares.Grafo;
import auxiliares.Localidad;
import auxiliares.Tripla;
import auxiliares.Tupla;
import auxiliaresGrafo.ArbolMinimo;


/**
 * Clase que crea trabaja utilizando un grafo, donde cada nodo representa un objeto Localidad con sus propias ubicaciones, siendo sus aristas
 * el precio que costaría construir cada camino entre ellas. No tiene límite de Localidades para trabajar, pero si posee un mínimo que es de 2
 * Localidades, para mantener el sentido de la búsqueda de la red óptima (al menos debería tener un camino entre dos Localidades).
 * Esta clase utiliza un AGM para obtener la ruta óptima. Es modificable una vez creado utilizando distintos metodos tales como 
 * agregarLocalidad, eliminarLocalidad, borrarCamino y sustituirCamino.
 * 
 * @param ArrayList<Localidad> con dos o mas objetos dentro del conjunto.
 * 
 * @author Mauro Ricceli
 * @author Jazmin Quinteros
 * 
 *
 *
 */

public class RedTelefonica {
	/**
	 *  Conjunto de localidades representadas actualmente en el grafo.
	 */
	ArrayList<Localidad> _localidades;
	
	/**
	 *  Conjunto de localidades eliminadas del grafo durante su ejecución. Permite deshacer cambios al momento de eliminar una localidad
	 *  del grafo. 
	 */	
	
	ArrayList<Localidad> _localidadesEliminadas;
	
	/**
	 * Conjunto de Tuplas que guarda la ubicación de la localidad <Origen, Destino> de nuevos caminos sustituidos, fuera de lo óptimo. 
	 */
	
	ArrayList<Tupla<Integer, Integer>> _nuevosCaminos;
	
	/**
	 * Conjunto de Tuplas que guarda la ubicacion de la localidad <Origen, Destino> de caminos sustituidos por el usuario. 
	 * Permite deshacer cambios.
	 */
	
	ArrayList<Tupla<Integer, Integer>> _caminosSustituidos;
	
	/**
	 * Conjunto de Tuplas que guarda la ubicacion de la localidad <Origen, Destino> de caminos borrados por el usuario. No se utilizan en próximas
	 * planificaciones sobre el mismo objeto, al menos hasta que el usuario decide reiniciar los cambios. Permite deshacer cambios parciales.
	 */
	
	ArrayList<Tupla<Integer, Integer>> _caminosProhibidos;
	
	/**
	 * Grafo original, es un grafo completo a no ser que se tenga caminos prohibidos por el usuario, en ese caso, ese camino en específico no
	 * existirá.
	 */
	
	Grafo _redTelefonica;
	
	/**
	 * Es el grafo original con el AGM aplicado, ignora los caminos prohibidos.
	 */
	
	Grafo _redOptimizada;
	
	Integer _costoPorKilometro;
	Integer _porcentajeCostoPorSuperarLimiteKm;
	Integer _costoDistintasProvincias;
	
	
	/**
	 * Construye un nueva red utilizando un conjunto de localidades.
	 * 
	 * @param localidades Conjunto localidades con tamaño mayor o igual a 2.
	 */
	
	public RedTelefonica(ArrayList<Localidad> localidades) {
		if(localidades.size() < 2) {
			throw new IllegalArgumentException("Una red necesita al menos dos localidades para trabajar");
		}
		
		_nuevosCaminos = new ArrayList<Tupla<Integer, Integer>>();	//caminos sustituidos agregados
		_caminosSustituidos = new ArrayList<Tupla<Integer, Integer>>(); //caminos sustituidos eliminados
		
		_localidadesEliminadas = new ArrayList<Localidad>();	
		_caminosProhibidos = new ArrayList<Tupla<Integer, Integer>>();
		
		_localidades = localidades;
		asignarCostos();
		planificar();
	}

	/**
	 * Setea los costos que se utilzarán para definir los pesos de las aristas sobre el grafo.
	 */
	
	public void asignarCostos() {
		_costoPorKilometro = 10;
		_porcentajeCostoPorSuperarLimiteKm = 20;
		_costoDistintasProvincias = 300;
	}
	
	/**
	 * Devuelve el valor que nos costaría construir nuestra red completa actualmente.
	 * @return costoTotal
	 */
	
	public Double obtenerCostoTotal() {
		double costoTotal = 0.0;
		HashSet<Double> costosNoRepetidos = new HashSet<Double>();
		for(int fila = 0; fila <= _redOptimizada.getMatAdy().length-1; fila++) {
			for(int columna = 0; columna <= _redOptimizada.getMatAdy()[fila].length-1; columna++) {
				if(_redOptimizada.getMatAdy()[fila][columna] != null) {
					costosNoRepetidos.add(_redOptimizada.getMatAdy()[fila][columna]);
				}
			}
		}
		for(Double costo : costosNoRepetidos) {
			costoTotal = costoTotal + costo;
		}
		
		costoTotal = costoTotal * 100;
		costoTotal = (double)((int) costoTotal);
		costoTotal = costoTotal / 100;
		
		return costoTotal;
	}
	
	
	/**
	 * Aplica el AGM al grafo completo siempre que se produzca un cambio sobre el actual.
	 */
	
	public void planificar() {
		_redTelefonica = new Grafo(_localidades.size());
		generarGrafoCompleto();
		ArbolMinimo optimizarRed = new ArbolMinimo(_redTelefonica);
		_redOptimizada = optimizarRed.obtenerArbolMinimo();
		
	}
	
	/**
	 * Genera un grafo completo entre todas las localidades existentes en nuestra red. Si hay caminos prohibidos no será un grafo completo.
	 */
	
	protected void generarGrafoCompleto() {
		for(int fila = 0; fila <= _redTelefonica.getMatAdy().length-1; fila++) {
			for(int columna = 0; columna <= _redTelefonica.getMatAdy()[fila].length-1; columna++) {
				if(fila != columna) {
					Tupla<Integer, Integer> aux = new Tupla<Integer, Integer>(fila, columna);
					if(!_caminosProhibidos.contains(aux)) {
						_redTelefonica.agregarArista(fila, columna, calcularCosto(_localidades.get(fila), _localidades.get(columna)));
					}
				}
			}
		}
	}

	/**
	 * Agrega una localidad al grafo actual.
	 * @param localidad localidad deseada a ingresar.
	 */
	
	public void agregarLocalidad(Localidad localidad) {
		_localidades.add(localidad);
	}
	
	/**
	 * Elimina una localidad del grafo actual siempre y cuando se posean mas de dos localidades, y la localidad que estemos buscando
	 * realmente exista dentro de nuestra red.
	 * 
	 * @param localidad nombre de la localidad
	 * @param provincia nombre de la provincia donde se encuentra la localidad
	 * @return true si remueve la localidad
	 * @return false si no existe la localidad dentro de nuestra red.
	 */
	
	public boolean eliminarLocalidad(String localidad, String provincia) {
		if(_localidades.size() <= 2) {
			throw new RuntimeException("No se puede eliminar una localidad de un grafo con 2 localidades debido a que rompería el objeto");
		}
		Integer ubiLocalidad = tengoLocalidad(localidad, provincia);
		if(ubiLocalidad == null) {
			return false;
		}
		
		_localidadesEliminadas.add(_localidades.get(ubiLocalidad));
		_localidades.remove((int) ubiLocalidad);
		return true;
	}
	
	/**
	 * Analiza si la localidad que estamos buscando existe dentro de nuestra red.
	 * @param localidad nombre de la localidad
	 * @param provincia nombre de la provincia donde se encuentra la localidad
	 * @return Integer si la red posee la localidad (la ubicacion de la Localidad en el ArrayList)
	 * 
	 */
	
	public Integer tengoLocalidad(String localidad, String provincia) {
		for(int i = 0; i <= _localidades.size()-1; i++) {
			if(_localidades.get(i).getNombre().toLowerCase().equals(localidad.toLowerCase()) && _localidades.get(i).getProvincia().toLowerCase().equals(provincia.toLowerCase())) {
				return i;
			}
		}
		return null;
	}
	
	/**
	 * Analiza si existe alguna localidad en nuestra red con las mismas coordenadas.
	 * @param latitud la latitud de nuestra Localidad
	 * @param longitud la longitud de nuestra Localidad
	 * @return true si existe alguna Localidad con esa coordenada.
	 */
	
	public boolean tengoCoordenadas(Double latitud, Double longitud) {
		for(int i = 0; i <= _localidades.size()-1; i++) {
			if(_localidades.get(i).getLatitud().equals(latitud) && _localidades.get(i).getLongitud().equals(longitud)) {
				return true;
			}		
		}
		return false;
	}
	
	/**
	 * Analiza si la Localidad deseada a borrar un camino posee mas de 1 posible camino en nuestra red.
	 * @param localidad nombre de la localidad.
	 * @param provincia nombre de la provincia donde se encuentra nuestra localidad.
	 * @return true si la localidad posee mas de un camino posible // false si posee solo uno.
	 */
	
	public boolean esPosibleBorrarCamino(String localidad, String provincia) {
		Integer ubicacion = tengoLocalidad(localidad, provincia);
		if(_redTelefonica.obtenerVecinos(ubicacion).size() <= 1) {
			return false;
		}
		return true;
	}
	
	/**
	 * Borra el camino entre dos localidades de la red y lo guarda en un conjunto de caminos prohibidos en forma de Tuplas<Integer, Integer>,
	 * cubriendo las dos posiblidades dentro de la matriz de adyacencia de nuestro grafo.
	 * 
	 * @param localidadOrigen nombre de la localidad origen del camino.
	 * @param provinciaOrigen nombre de la provincia donde se encuentra la localidad origen.
	 * @param localidadDestino nombre de la localidad destino del camino.
	 * @param provinciaDestino nombre de la provincia donde se encuentra la localidad destino.
	 */
	
	public void borrarCamino(String localidadOrigen, String provinciaOrigen, String localidadDestino, String provinciaDestino) {
		Integer ubicacionOrigen = tengoLocalidad(localidadOrigen, provinciaOrigen);
		Integer ubicacionDestino = tengoLocalidad(localidadDestino, provinciaDestino);		
		
		Tupla<Integer, Integer> aux1 = new Tupla<Integer, Integer>(ubicacionOrigen, ubicacionDestino);
		Tupla<Integer, Integer> aux2 = new Tupla<Integer, Integer>(ubicacionDestino, ubicacionOrigen);
		_caminosProhibidos.add(aux1);
		_caminosProhibidos.add(aux2);
	}
	
	/**
	 * Borra los dos ultimos caminos prohibidos de nuestra red, eliminando del conjunto caminos prohibidos las dos ultimas tuplas (las dos
	 * posibilidades para un camino)
	 */
	
	public void deshacerBorrarCamino() {
		_caminosProhibidos.remove(_caminosProhibidos.size()-1);
		_caminosProhibidos.remove(_caminosProhibidos.size()-1);
	}
	
	/**
	 * Analiza si la localidad deseada a sustituirle el camino posee mas de un camino posible dentro de la red.
	 * @param localidad nombre de la localidad
	 * @param provincia nombre de la provincia donde se encuentra nuestra localidad.
	 * @return true si la cantidad posible e caminos es mayor a uno.
	 */
	
	public boolean esPosibleSustituirCamino(String localidad, String provincia) {
		Integer ubicacion = tengoLocalidad(localidad, provincia);
		if(_redOptimizada.obtenerVecinos(ubicacion).size() <= 1) {
			return false;
		}
		return true;
	}
	
	/**
	 * Calcula el costo entre dos localidades de nuestra red teniendo en cuenta la distancia entre ellos y las distintas tarifas aplicadas.
	 * @param origen localidad origen de nuestro camino
	 * @param destino localidad destino de nuestro camino.
	 * @return costo
	 */
	
	public double calcularCosto(Localidad origen, Localidad destino) {
		double costo;
		double distanciaKM = origen.obtenerDistancia(destino);
		costo = distanciaKM * _costoPorKilometro;
		if(distanciaKM > 300) {
			costo = costo + (_porcentajeCostoPorSuperarLimiteKm * costo) / 100;
		}
		if(origen.getProvincia().toLowerCase().equals(destino.getProvincia().toLowerCase()) == false) {
			costo = costo + _costoDistintasProvincias; 
			
		} 
		
		//DEJAMOS SOLO 2 DECIMALES PARA MEJOR REDACCION
		costo = costo * 100;
		costo = (double)((int) costo);
		costo = costo / 100;
		
		return costo;
	}
	
	/**Elimina el último elemento de la lista de localidades eliminadas y lo agrega en la lista de localidades
	 * @return true si se pudo eliminar un elemento de la lista, false si no hay elementos para eliminar*/
	public boolean deshacerEliminarLocalidad() {
		if (_localidadesEliminadas.size()>0) {
			_localidades.add(_localidadesEliminadas.get(_localidadesEliminadas.size()-1));
			_localidadesEliminadas.remove(_localidadesEliminadas.size()-1);
			return true;
		}
		return false;
	}

	/**Agregar una nueva arista a la red optimizada (grafo de camino minimo)
	 * @param String Nombre de localidad y provincia de las localidades que se quiere verificar si son vecinas*/
	public void sustituirCamino(Tripla<Tupla<String,String>,Tupla<String,String>,Tupla<String,String>> localidades) {
		Tupla<String,String> localidadOrigen  = localidades.getFirst();
		Tupla<String,String> localidadAgregada  = localidades.getSecond();
		Tupla<String,String> localidadEliminada  = localidades.getThird();
		
		Integer ubicacionOrigen = tengoLocalidad(localidadOrigen.getFirst(), localidadOrigen.getSecond());
		Integer ubicacionAgregadas = tengoLocalidad(localidadAgregada.getFirst(), localidadAgregada.getSecond());	
		
		sustituirCaminoEliminar(localidadOrigen.getFirst(), localidadOrigen.getSecond(),localidadEliminada.getFirst(),localidadEliminada.getSecond());
		
		Tupla<Integer, Integer> agregadasOrigen = new Tupla<Integer, Integer>(ubicacionOrigen, ubicacionAgregadas);
		Tupla<Integer, Integer> agregadasDestino = new Tupla<Integer, Integer>(ubicacionAgregadas, ubicacionOrigen);

		_nuevosCaminos.add(agregadasOrigen);
		_nuevosCaminos.add(agregadasDestino);
		_redOptimizada.agregarArista(ubicacionOrigen, ubicacionAgregadas, calcularCosto(_localidades.get(ubicacionOrigen),_localidades.get(ubicacionAgregadas)));
	}
	
	/**Elimina una nueva arista a la red optimizada (grafo de camino minimo)
	 * @param String Nombre de localidad y provincia de las localidades que se quiere verificar si son vecinas*/
	
	public void sustituirCaminoEliminar(String localidadOrigen, String provinciaOrigen, String localidadDestino, String provinciaDestino) {
		Integer ubicacionOrigen = tengoLocalidad(localidadOrigen, provinciaOrigen);
		Integer ubicacionDestino = tengoLocalidad(localidadDestino, provinciaDestino);	
		Tupla<Integer, Integer> eliminadasOrigen = new Tupla<Integer, Integer>(ubicacionOrigen, ubicacionDestino);
		Tupla<Integer, Integer> eliminadasDestino = new Tupla<Integer, Integer>(ubicacionDestino, ubicacionOrigen);

		_caminosSustituidos.add(eliminadasOrigen);
		_caminosSustituidos.add(eliminadasDestino);
		_redOptimizada.eliminarArista(ubicacionOrigen, ubicacionDestino);
	}

	/**Verifica si los datos pasados por parametro, pertenecen a localidades vecinas
	 * @param String Nombre de localidad y provincia de las localidades que se quiere verificar si son vecinas
	 * @return true si son vecinas, de lo contrario devuelve false*/
	public boolean localidadesVecinas(String localidadOrigen, String provinciaOrigen, String localidadDestino, String provinciaDestino) {
		if (localidadOrigen.equals(localidadDestino) && provinciaOrigen.equals(provinciaDestino)) {
			return false;
		} else {
			Integer ubicacionOrigen = tengoLocalidad(localidadOrigen, provinciaOrigen);
			Integer ubicacionDestino = tengoLocalidad(localidadDestino, provinciaDestino);	
			return _redOptimizada.sonVecinos(ubicacionOrigen, ubicacionDestino);
		}
	}
	
	/**Resetea el grafo de los cambios agregados por susitucion
	 * @return true, si se resetean los caminos //
	 * 		   false si no hay caminos por resetear*/
	public boolean resetearSustitucionCamino() {
		if (_nuevosCaminos.size()>0  && _caminosSustituidos.size() > 0) {
			_nuevosCaminos.clear();
			_caminosSustituidos.clear();
			return true;
		}
		return false;
	}
	
	/**Devuelve un String con los valores de los costos*/
	public String toStringCostos() {
		return "Tarifas"+"\n\n"+
				   "Costo por Kilometro: $"+ get_costos().getFirst()+"\n"+
				   "Costo por sobrepasar 300KM: $"+ get_costos().getSecond()+"\n"+
				   "Costo por cambiar de provincia: $"+ get_costos().getThird()+"\n\n"+
				   "Total gastado: $"+ obtenerCostoTotal();
	}
	
	public void resetearCaminosEliminados() {
		_caminosProhibidos.clear();
	}
	
	public Grafo getRedTelefonica() {
		return _redTelefonica;
	}
	
	public Grafo getRedOptimizada() {
		return _redOptimizada;
	}
	
	public ArrayList<Localidad> getLocalidades(){
		return _localidades;
	}
	
	public String toString() {
		return _redTelefonica.toString();
	}
	
	public ArrayList<Tupla<Integer, Integer>> get_nuevosCaminos() {
		return _nuevosCaminos;
	}

	public ArrayList<Tupla<Integer, Integer>> get_caminosProhibidos() {
		return _caminosProhibidos;
	}

	public Tripla<Integer,Integer,Integer> get_costos() {
		Tripla<Integer,Integer,Integer> costos = new Tripla<Integer,Integer,Integer>(_costoPorKilometro, _porcentajeCostoPorSuperarLimiteKm, _costoDistintasProvincias);
		return costos;
	}

	public ArrayList<Tupla<Integer, Integer>> get_caminosSustituidos() {
		return _caminosSustituidos;
	}

}
