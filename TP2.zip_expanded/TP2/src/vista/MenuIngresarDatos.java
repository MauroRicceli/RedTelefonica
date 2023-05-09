package vista;

import java.awt.Font;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;

import javax.swing.JOptionPane;

import auxiliares.Localidad;
import modelo.RedTelefonica;
/**
 * La clase MenuIngresarDatos es una subclase de la clase Menu. Muestra la interfaz con los componentes necesarios para
 * ingresar datos y contiene la funcionalidad para trabajar con esos métodos ingresados
 * 
 * @author Mauro Ricceli
 * @author Jazmin Quinteros
 * */
@SuppressWarnings("serial")
public class MenuIngresarDatos extends Menu {
	private JButton botonAgregar;
	private JButton botonPlanificar;
	private JButton botonHistorial;
	
	private JLabel labelCrearRed;
	/**ArrayList de tipo Localidad que almacena las localidades válidas ingresadas por el usuario*/
	private ArrayList <Localidad> localidades;
	
	/** 
	 * El constructor de MenuIngresarDatos se encarga de delegar a la clase Menu el frame la tarea de establecer la configuración del JFrame 
	 * y añadir el panel de la clase MenuIngresarDatos al mismo,    
	 * */
	public MenuIngresarDatos(Interfaz frame) {
		super(frame);
		
		iniciar();
		configuracion();
		establecerPosiciones();
		accionListener();

		localidades = new ArrayList<Localidad>();
	}
	
	@Override
	protected void accionListener() {
		//agrega a la lista de localidades segun los datos ingresados
		botonAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				Localidad ingresada = obtenerDatosIngresados();
				if(verificarInput(ingresada)) {
					localidades.add(ingresada);
					textFieldLocalidad.setText("");
					textFieldProvincia.setText("");
					textFieldLongitud.setText("");
					textFieldLatitud.setText("");
				}
				
				if(localidades.size() > 1) {
					botonPlanificar.setEnabled(true);
				}
				
			}
		});
		
		//muestra el mapa
		botonPlanificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarMapa();	
			}
		});
		
		botonHistorial.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarMenu(false);
				_historial.cargarHistorial();
				_historial.mostrarHistorial();
			}
		});
	}
	
	/**El método verificarInput() se encarga de verificar que el dato ingresado no sea null, que no se repita con una localidad
	 * ya ingresada tanto sus coordenadas como la localidad y provincia
	 * @param
	 * @return true si la localidad ingresada es válida, de lo contrario retorna false*/
	private boolean verificarInput(Localidad ingresada) {
		if(ingresada != null) {
			if(localidades.contains(ingresada)) {
				JOptionPane.showMessageDialog(null, "Esa localidad ya esta ingresada: "+ingresada.getNombre()+" , "+ingresada.getProvincia());
				return false;
			}
			if(verificarRepetidas(ingresada)) {
				JOptionPane.showMessageDialog(null,"El nombre de la localidad o provincia ya está agregado:"+ingresada.getNombre()+" , "+ingresada.getProvincia());
				return false;
			}
			if(tengoCoordenadas(ingresada.getLatitud(), ingresada.getLongitud())) {
				JOptionPane.showMessageDialog(null,"Esas coordenadas ya estan agregadas en otra localidad de distinto nombre:"+ingresada.getLatitud()+" ,"+ingresada.getLongitud());
				return false;
			}
			
		} else {
			return false;
		}
		return true;		
	}
	
	/**el método tengoCoordenadas verifica si existe otra localidad en el ArrayList de localidades que posea las mismas
	 * coordenadas que la localidad ingresada
	 * @param
	 * @return true si las coordenadas de la localidad ingresada coinciden con una que ya está en el ArrayList de localidades,
	 * de lo contrario retorna false*/
	private boolean tengoCoordenadas(Double latitud, Double longitud) {
		for(Localidad loc : localidades) {
			if(loc.getLatitud().equals(latitud) && loc.getLongitud().equals(longitud)) {
				return true;
			}
		}
		return false;
	}
	
	/**Oculta el panel de MenuIngresarDatos y crea una nueva clase RedTelefonica con las localidades ingresadas, además llama 
	 * al método crearMapa() de la clase Interfaz para que genere un nuevo mapa con los datos ingresados
	 * */
	protected void mostrarMapa() {
		mostrarMenu(false);
		RedTelefonica red = new RedTelefonica(localidades);
		_frame.crearMapa(red);
	
	}
	
	@Override
	protected void mostrarMenu(boolean mostrar) {
		setVisible(mostrar);
	}
	/**El método crear() crea un nuevo objeto MenuIngresarDatos
	 * @param recibe por parámetro un objeto de la clase Interfaz para inicalizar el objeto MenuIngresarDatos
	 * @return nuevo objeto MenuIngresarDatos*/
	protected static MenuIngresarDatos crear(Interfaz frame) {
		return new MenuIngresarDatos(frame);	
	}
	
	//ubicacion de textField, botones y labels en pantalla
	@Override
	protected void establecerPosiciones() {	
		setBounds(450,150, 490, 450);

		textFieldLocalidad.setBounds(130, 189, 250, 20);
		textFieldProvincia.setBounds(130, 220, 250, 20);
		textFieldLatitud.setBounds(130, 248, 250, 20);
		textFieldLongitud.setBounds(130, 280, 250, 20);
		
		labelLocalidad.setBounds(46, 192, 72, 14);
		labelProvincia.setBounds(45, 223, 72, 14);	
		labelLatitud.setBounds(45, 251, 72, 14);
		labelLongitud.setBounds(45, 283, 72, 14);
		
		botonAgregar.setBounds(45, 311, 118, 23);
		botonPlanificar.setBounds(45, 337, 118, 23);
		botonHistorial.setBounds(45, 365, 118, 23);
		
		labelCrearRed.setBounds(46, 165, 183, 20);
		
		separator.setBounds(130, 299, 250, 3);
		separator_1.setBounds(130, 269, 250, 3);
		separator_2.setBounds(130, 240, 250, 3);
		separator_3.setBounds(130, 209, 250, 3);
	
	}
	
	@Override
	protected void configuracion() {
		add(ubicarImagen());
		labelCrearRed.setFont(new Font("Arial", labelCrearRed.getFont().getStyle() | Font.BOLD, labelCrearRed.getFont().getSize() + 2));
		configurarLabel();
		configuracionTextField();
		configurarSeparador();
		
		configurarBotones(botonAgregar);
		configurarBotones(botonHistorial);
		configurarBotones(botonPlanificar);
	}

	@Override
	protected void iniciar() {
		labelCrearRed = new JLabel("CREAR RED TELEFONICA");
		
		botonAgregar = new JButton("AGREGAR");
		botonPlanificar = new JButton("PLANIFICAR");
		botonHistorial = new JButton("HISTORIAL");
		
		add(textFieldLocalidad);
		add(textFieldProvincia);
		add(textFieldLatitud);
		add(textFieldLongitud);

		add(botonHistorial);
		add(botonAgregar);
		add(botonPlanificar);
		botonPlanificar.setEnabled(false); //no se puede iniciar sin localidades
		
		add(labelCrearRed);
		
		add(separator);
		add(separator_1);
		add(separator_2);
		add(separator_3);
		
		add(labelLocalidad);
		add(labelProvincia);
		add(labelLatitud);
		add(labelLongitud);
	}
}

