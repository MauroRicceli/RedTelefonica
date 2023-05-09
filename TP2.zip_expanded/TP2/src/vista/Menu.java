package vista;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Image;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import auxiliares.Localidad;
/**
 * La clase Menu es de tipo abstracta, define y posee métodos que van a ser utilizados por las otras clases relacionadas
 * a un menú. A su vez, es una subclase de la clase JLayeredPane, por lo que todas sus clases heredadas también van a 
 * heredar el comportamiento de un objeto JLayeredPane
 * @author Mauro Ricceli
 * @author Jazmin Quinteros
 * 
 * */
@SuppressWarnings("serial")
public abstract class Menu extends JLayeredPane {
	protected Interfaz _frame;
	
	protected JTextField textFieldLocalidad;
	protected JTextField textFieldProvincia;
	protected JTextField textFieldLatitud;
	protected JTextField textFieldLongitud;
	
	protected JLabel labelLocalidad;
	protected JLabel labelProvincia;
	protected JLabel labelLatitud;
	protected JLabel labelLongitud;
	
	protected JSeparator separator;
	protected JSeparator separator_1;
	protected JSeparator separator_3;
	protected JSeparator separator_2;	
	
	protected Historial _historial;
	/**
	 * Un HashMap de tipo <String,String> que va a almacenar las localidades ya guardadas,  utilizada para evitar la repetición 
	 * de localidades con el mismo nombre y provincia
	 * */
	
	private HashMap <String,String> posiblesRepetidas;
	
	/** 
	 * El constructor de Menu se encarga de establecer la configuración del JFrame y añadir el panel de la clase al mismo
	 * @param Interfaz frame   
	 * */

	public Menu(Interfaz frame) {
		_frame = frame;
		posiblesRepetidas = new HashMap<String,String>();

		_frame.setExtendedState(Frame.NORMAL);
		_frame.setResizable(false);
		_frame.getContentPane().add(this);
		_frame.setTitle("Optimizador de redes");
		inicializarComponentes();
	}
	
	/**Inicializa las variables de JTextField, JLabel y JSeparator, además de agregar el icono de la aplicación
	 * en la barra de tareas*/
	private void inicializarComponentes() {
		textFieldLocalidad = new JTextField();
		textFieldProvincia = new JTextField();
		textFieldLatitud = new JTextField();
		textFieldLongitud = new JTextField();
		
		labelLocalidad = new JLabel("LOCALIDAD");
		labelProvincia = new JLabel("PROVINCIA");
		labelLatitud = new JLabel("LATITUD");
		labelLongitud = new JLabel("LONGITUD");
		
		separator = new JSeparator();
		separator_1 = new JSeparator();
		separator_2 = new JSeparator();
		separator_3 = new JSeparator();
		
		icono();
	}
	
	/**Agrega el icono de la barra de tareas*/
	private void icono() {
		Image iconImagen = new ImageIcon(this.getClass().getResource("/logo/titlebarIcon.png")).getImage();
		Image iconResize = iconImagen.getScaledInstance(120, 120,  java.awt.Image.SCALE_SMOOTH);
		_frame.setIconImage(iconResize);
	}
	
	/*El método verificarRepetidas() recibe una localidad ingresada con el fin de verificar si la localidad ya está
	 * en el HashMap de posiblesRepetidas
	 * @param Localidad ingresada
	 * @return true si la localidad ingresada está repetida, de lo contrario retorna false
	 * */
	protected boolean verificarRepetidas(Localidad ingresada) {
		String localidad = ingresada.getNombre();
		String provincia = ingresada.getProvincia();
		if (posiblesRepetidas.containsKey(localidad) && posiblesRepetidas.get(localidad).equals(provincia)) {
			return true;
		} else {
			posiblesRepetidas.put(localidad,provincia);
		}
		return false;
	}
	
	/**El método obtenerDatosIngresados() crea un nuevo objeto Localidad a partir de los datos ingresados, 
	 * estos son creados si cumplen todas las condiciones,de lo contrario lanza un cartel emergente con el tipo de error
	 * @return Localidad devuelve un nuevo objeto Localidad con los datos ingresados, retorna null si el dato 
	 * ingresado tuvo una verificación que no cumplió*/
	protected Localidad obtenerDatosIngresados() {
		if(textFieldLocalidad.getText().equals("") || textFieldProvincia.getText().equals("") || textFieldLongitud.getText().equals("") || textFieldLatitud.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Olvidó completar una casilla");
			return null;
		}
	
		String nombreLocalidad = textFieldLocalidad.getText();
		String provincia = textFieldProvincia.getText();
		String longitud = textFieldLongitud.getText();
		String latitud = textFieldLatitud.getText();
		
		if(nombreLocalidad.length() <3 ) {
			JOptionPane.showMessageDialog(null, "Una localidad no puede tener un nombre con menos de 3 letras: "+nombreLocalidad);
		}
		if(provincia.length() <3 ) {
			JOptionPane.showMessageDialog(null, "Una provincia no puede tener un nombre con menos de 3 letras: "+provincia);
		}
	
		try {
			@SuppressWarnings("unused")
			Localidad prueba =  new Localidad(nombreLocalidad,provincia,latitud,longitud);
		} catch(Exception IllegalArgumentException) {
			Double latitudPrueba = Double.parseDouble(latitud);
			Double longitudPrueba = Double.parseDouble(longitud);
			if(latitudPrueba < -90 || latitudPrueba > 90) {
				JOptionPane.showMessageDialog(null, "La latitud debe respetar el rango entre -90 y 90: "+latitud);
			}
			if(longitudPrueba < -90 || longitudPrueba > 90) {
				JOptionPane.showMessageDialog(null, "La longitud debe respetar el rango entre -90 y 90: "+longitud);
			}
			return null;
		}
		
		return new Localidad(nombreLocalidad,provincia,latitud,longitud);
	}
	
	/**Carga la imagen utilizada para el logo creando una instancia JLabel
	 * @return  JLabel logo*/
	protected JLabel ubicarImagen() {
		Image logoImagen = new ImageIcon(this.getClass().getResource("/logo/logo.png")).getImage();
		Image logoImagenResize = logoImagen.getScaledInstance(300, 120, Image.SCALE_DEFAULT);
		
		JLabel logo = new JLabel();
		logo.setBounds(50, -20, 416, 198);
		logo.setIcon(new ImageIcon(logoImagenResize));
		return logo;
	}
	
	public void configurarBotones(JButton boton) {
		boton.setForeground(Color.LIGHT_GRAY);
		boton.setBackground(new Color(64, 128, 128));
	}
	
	protected void configurarLabel() {
		configurarLabel(labelLocalidad);
		configurarLabel(labelProvincia);
		configurarLabel(labelLatitud);
		configurarLabel(labelLongitud);
	}
	
	protected void configurarLabel(JLabel label) {
		if (label != null) 
			label.setForeground(Color.DARK_GRAY);
	}
	
	protected void configuracionTextField() {
		configurarTextField(textFieldLocalidad);
		configurarTextField(textFieldProvincia);
		configurarTextField(textFieldLatitud);
		configurarTextField(textFieldLongitud);
	}
	
	protected void configurarTextField(JTextField textField) {
		if (textField != null)
			textField.setBorder(null);
	}
	
	protected void configurarSeparador() {
		configurarSeparador(separator);
		configurarSeparador(separator_1);
		configurarSeparador(separator_2);
		configurarSeparador(separator_3);
	}
	
	protected void configurarSeparador(JSeparator separador) {
		if (separador != null) {
			separador.setForeground(Color.WHITE);
			separador.setBackground(new Color(64, 128, 128));
		}	
	}
	
	public void set_historial(Historial historial) {
		_historial = historial;
	}

	/**El método abstracto configuración() se tiene como fin la edición de botones, textfields, labels...*/
	protected abstract void configuracion();
	
	/**El método abstracto iniciar() tiene como fin la inicialización de variables de clase*/
	protected abstract void iniciar();
	
	/**El método abstracto accionListener tiene como fin la inicializacion de accionListeners de botones*/
	protected abstract void accionListener();
	
	/**El método abstracto establecerPosiciones() tiene como fin establecer la ubicacion en el panel de cada componente*/
	protected abstract void establecerPosiciones();

	/**El método mostrarMenu() Muestra/oculta el panel actual
	 * @param boolean mostrar*/
	protected abstract void mostrarMenu(boolean mostrar);
}

