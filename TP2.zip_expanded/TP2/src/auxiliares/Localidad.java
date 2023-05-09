package auxiliares;

import org.openstreetmap.gui.jmapviewer.Coordinate;

/**
 * Clase que representa una localidad en una parte del mundo, con su propio nombre, provincia donde se encuentra y sus respectivas coordenadas
 * latitud y longitud para representarla en el mapa.
 * 
 * @author Mauro Ricceli
 * @author Jazmin Quinteros
 *
 */

public class Localidad {
	private String _nombre;
	private String _provincia;
	private Double _latitud;
	private Double _longitud;
	
	/**
	 * Crea una Localidad utilizando strings para facilidad del usuario y hace las conversiones necesarias.
	 * @param nombre nombre de la localidad
	 * @param provincia provincia donde se encuentra la localidad
	 * @param latitud latitud de la localidad en el mapa
	 * @param longitud longitud de la localidad en el mapa
	 */
	
	public Localidad(String nombre, String provincia, String latitud, String longitud) {
		argumentoValido(nombre, provincia, Double.parseDouble(latitud), Double.parseDouble(longitud));
		_nombre = nombre;
		_provincia = provincia;
		_latitud = Double.parseDouble(latitud);
		_longitud = Double.parseDouble(longitud);
	}
	
	/**
	 * Analiza si el argumento ingresado por el usuario es válido.
	 * 
	 * @throws IllegalArgumentException
	 * 
	 * @param nombre nombre de la localidad
	 * @param provincia provincia donde se encuentra la localidad
	 * @param latitud latitud de la localidad en el mapa
	 * @param longitud longitud de la localidad en el mapa
	 */
	
	private void argumentoValido(String nombre, String provincia, Double latitud, Double longitud) {
		if(nombre.length() < 3) {
			throw new IllegalArgumentException("El nombre de la localidad debe ser válido "+nombre);
		} else {
			if(provincia.length() < 3) {
				throw new IllegalArgumentException("El nombre de la provincia debe ser válido "+provincia);
			}
		}
		
		if(latitud < -90 || latitud > 90) {
			throw new IllegalArgumentException("La latitud máxima es 90 para el polo Norte, y -90 para el polo Sur, ingresar latitud válida "+latitud);
		} else {
			if(longitud < -90 || longitud > 90) {
				throw new IllegalArgumentException("La longitud máxima es 90 para el Este y -90 para el Oeste ");
			}
		}
		
	}
	
	/**
	 * Obtiene la distancia entre la localidad actual y una localidad destino.
	 * @param destino localidad a analizar su distancia con respecto al punto actual
	 * @return distancia distancia hacia la localidad ingresada
	 */
	
	public double obtenerDistancia(Localidad destino) {
		double distancia = Math.pow(_latitud - destino.getLatitud(),2) + Math.pow(_longitud - destino.getLongitud(),2);
		distancia = Math.sqrt(distancia);
		return distancia;
	}
	
	/**
	 * Analiza si dos localidades son exactamente iguales
	 * @return true si son iguales // false si no son iguales.
	 */
	
	public boolean equals(Object object) {
		if(object instanceof Localidad) {
			Localidad aux = (Localidad) object;
			if(aux.getLatitud().equals(_latitud) && aux.getLongitud().equals(_longitud) && aux.getNombre().toLowerCase().equals(_nombre.toLowerCase()) && aux.getProvincia().toLowerCase().equals(_provincia.toLowerCase())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public String toString() {
		return "La localidad "+_nombre+" ubicada en la provincia "+_provincia+" con latitud "+_latitud+" y longitud "+_longitud;
	}
	
	public String getNombre() {
		return _nombre;
	}
	
	public String getProvincia() {
		return _provincia;
	}
	
	public String toStringLatitud() {
		return ""+_latitud;
	}
	
	public String toStringLongitud() {
		return ""+_longitud;
	}
	
	public Double getLatitud() {
		return _latitud;
	}
	
	public Double getLongitud() {
		return _longitud;
	}

	public Coordinate getCoordenada() {
		return new Coordinate(_latitud,_longitud);
	}
}
