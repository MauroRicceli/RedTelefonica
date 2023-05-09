package vista;

import javax.swing.JFrame;

import modelo.RedTelefonica;
import java.awt.Color;
/**
 * La clase Interfaz es subclase de la clase JFrame, por lo que los métodos pertenecientes a JFrame
 * son heredados, por lo que la clase Interfaz también actúa como un objeto JFrame
 * @author Mauro Ricceli
 * @author Jazmin Quinteros
 * */


@SuppressWarnings("serial")
public class Interfaz extends JFrame {
	/**
	 * Objeto de tipo MenuIngresarDatos, carga una nueva interfaz destinada para la funcionalidad relacionada
	 * con los datos que ingrese el usuario
	 * */
	protected MenuIngresarDatos ingresarDatos;
	/**
	 * Objeto de tipo Mapa, carga una nueva interfaz con la funcionalidad de un mapa
	 * */
	protected Mapa _mapa;
	/**
	 * Objeto de tipo Historiol, carga una nueva interfaz con la funcionalidad para mostrar datos guardados en pantalla
	 * */
	protected Historial historial;
	
	
	/**El contructor configura la estilización el JFrame y llama al método nuevaConexión()*/
	public Interfaz() {
		getContentPane().setBackground(new Color(255, 255, 255));
		setBackground(new Color(64, 0, 64));
		nuevaConexion();
	}

	private void initialize() {
		new JFrame();
		setBounds(450,150, 469, 450);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**El método crearMapa() crea un nuevo objeto Mapa y a su vez remueve del JFrame el contenido de la interfaz de
	 * Historial e IngresarDatos
	 * @param recibe por parámetro un objeto de la clase RedTelefonica para inicalizar el objeto Mapa*/
	public void crearMapa(RedTelefonica redTelefonica) {
		removerHistorial();
		removerIngresarDatos();
		_mapa  = Mapa.crearMapa(this, redTelefonica);
	}
	
	/**El método nuevaConexión() resetea valores para generar una nueva conexión, crea un nuevo objeto MenuIngresarDatos
	 * para mostrarlo en pantalla y también inicializa el historial*/
	public void nuevaConexion() {
		initialize();
		historial = new Historial(this);
		ingresarDatos = MenuIngresarDatos.crear(this);
		ingresarDatos.set_historial(historial);
	}

	/**El método removerHistorial remueve el panel Historial del Frame*/
	public void removerHistorial() {
		this.remove(historial);
		this.repaint();
	}

	/**El método removerIngresarDatos remueve el panel ingresarDatos del Frame*/
	public void removerIngresarDatos() {
		this.remove(ingresarDatos);
		this.repaint();
		
	}
	
	public JFrame getFrame() {
		return this;
	}

	public Historial getHistorial() {
		return historial;
	}

	public MenuIngresarDatos getIngresarDatos() {
		return ingresarDatos;
	}	
}
