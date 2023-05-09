package vista;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;

import auxiliares.Localidad;
import modelo.RedTelefonica;
import vistaAuxiliares.OpcionesMapa;
/**
 * La clase se encarga de interactuar con el mapa mediante métodos como dibujar arista y vertice, va a permitir la visualización
 * del grafo de camino mínimo en pantalla.
 * @author Mauro Ricceli
 * @author Jazmin Quinteros
 * */
public class Mapa {
	/*
	 *  Variable de tipo JMapViewer que permite la visualización de un mapa en la pantalla
	 *  
	 *  */
	private JMapViewer _mapa;
	/*
	 *  Variable de tipo RedTelefonica que permite obtener los datos para dibujar el grafo en el mapa
	 *  
	 *  */
	protected RedTelefonica _red;

	/**
	 * El contructor recibe por parametro un objeto de tipo Interfaz que se utilizará como frame.
	 *  Además recibe un objeto de tipo RedTelefonica
	 *  @param
	 *  
	 *  */
	public Mapa(Interfaz frame, RedTelefonica redTelefonica) {
		
		_mapa = new JMapViewer();
		_red = redTelefonica;
		
		frame.setResizable(true);
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.getContentPane().add(_mapa);	
		
		configuracionMapa(frame);
		detectarCoordenadas();
		dibujarVertice();
		dibujarAristas();
		
	}

	/**
	 * El MétodoConfiguraciónMapa() agrega extras del mapa: aproximación de la zona donde se ubica el grafo ("zoom")
	 *  y carga la barra de menú creando un objeto de la clase OpcionesMapa
	 *  @param utilizada para crear el objeto OpcionesMapa
	 *  */
	private void configuracionMapa(Interfaz frame) {
		//barra de menu
		OpcionesMapa.crearMenuMapa(frame,_mapa,_red);
		//no muestra la barra de zoom en pantalla
		_mapa.setZoomControlsVisible(false);
		
		//donde se muestra el mapa al crearlo, toma una localidad para aproximar la zona donde se encuentra el grafo
		Coordinate coordinate = _red.getLocalidades().get(0).getCoordenada();
		_mapa.setDisplayPosition(coordinate, 6);
	}
	
	/**El método estático crearMapa crea un nuevo objeto de tipo Mapa
	 * @param 
	 * @return un nuevo objeto Mapa
	 * */
	public static Mapa crearMapa(Interfaz frame, RedTelefonica redTelefonica) {
		return new Mapa(frame, redTelefonica);
	}
	
	/**El método dibujarVertice() dibuja un punto y muestra el nombre en el mapa a partir de las coordenadas dadas*/
	private void dibujarVertice() {
		for (Localidad localidad : _red.getLocalidades()) {
			MapMarker vertice = new MapMarkerDot(localidad.getNombre(),localidad.getCoordenada());
			vertice.getStyle().setBackColor(Color.blue);
			vertice.getStyle().setColor(Color.blue);
			_mapa.addMapMarker(vertice);
		}
	}
	
	/**El métódo dibujarAristas() dibuja las aristas del arbol generador minimo*/
	private void dibujarAristas() {
		Double[][] ubicacionAristas = _red.getRedOptimizada().getMatAdy();
		ArrayList<Localidad> localidades = _red.getLocalidades();
		
		for(int fila = 0; fila <= ubicacionAristas.length-1; fila++) {
			for(int columna = 0; columna <= ubicacionAristas.length-1; columna++) {
				if(ubicacionAristas[fila][columna] != null) {
					Coordinate cord1 = new Coordinate(localidades.get(fila).getLatitud(), localidades.get(fila).getLongitud());
					Coordinate cord2 = new Coordinate(localidades.get(columna).getLatitud(), localidades.get(columna).getLongitud());
					ArrayList<Coordinate> ruta = new ArrayList<Coordinate>(Arrays.asList(cord1, cord2, cord2));
					_mapa.addMapPolygon(new MapPolygonImpl(ruta));
				}
			}
		}
	}

	
	//imprime la coordenada donde se clickea (para testear con coordenadas especificas)
	private void detectarCoordenadas()	{

		_mapa.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
			if (e.getButton() == MouseEvent.BUTTON1)
			{
				Coordinate markeradd = (Coordinate) _mapa.getPosition(e.getPoint());
				System.out.println(markeradd);
				
			}}
		});
	}

}
