package vista;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import auxiliares.Localidad;
import manejoDeArchivos.LocalidadesJSON;
import modelo.RedTelefonica;
/** La clase Historial es subclase de la superclase Menu, la finalidad de la clase Historial es mostrar la interfaz que contiene
 * una tabla que contiene los datos qye han sido guardados anteriormente. A partir de eso presenta la opción de cargar datos, permitiendo
 * utilizar los ya almacenados. De lo contrario, también posee  la opción de volver al menú principal de ingresar datos
 * @author Mauro Ricceli
 * @author Jazmin Quinteros
 * */

@SuppressWarnings("serial")
public class Historial extends Menu {
	/**
	 * Variable de clase de tipo LocalidadesJSON, la cual contiene los métodos para almacenar datos
	 * */
	private LocalidadesJSON _localidadesJSON;
	private JButton btnVolver;
	private JButton btnCargar;
	private JTable table;
	private DefaultTableModel tableModel;
	/**
	 *  El ArrayList que recibe el nombre  de "guardadas", almacena temporalmente objetos de tipo Localidad, es utilizado para obtener los datos que se
	 *  cargarán en la tabla    
	 * */
	private ArrayList<Localidad> guardadas;
	
	/** 
	 * El constructor de Historial se encarga de delegar a la clase Menu el frame la tarea de establecer la configuración del JFrame 
	 * y añadir el panel de la clase Historial al mismo,    
	 * */
	public Historial(Interfaz frame) {
		super(frame);
		
		iniciar();
		establecerPosiciones();
		accionListener();
		configuracion();
		//para cargar datos guardados previamente 
		guardadas = new ArrayList<Localidad>();
	}
	
	/**El método cargarHistorial() se encarga de inicializar la tabla colocarla en un  ScrollPane, este último le agrega la barra de desplazamiento
	 * a la tabla. Además, agrega en filas y columnas los datos almacenados en el archivo "historial"*/
	public void cargarHistorial() {
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(32, 210, 385, 124);
		add(scrollPane);
		
		actualizarTabla();
		
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(tableModel);
		table.setEnabled(false);
		
	}

	/**
	 * El método actualizarTabla() agrega los datos de las columnas y las filas a la tabla
	 * */
	private void actualizarTabla() {
		String informacion[] = {"Localidad", "Provincia", "Latitud", "Longitud"};
		tableModel = new DefaultTableModel(null,informacion);
		agregarFila();
	}


	/**
	 * El método carga datos guardados del archivo "historial" mediante el método estático "leerJSON" de la clase LocalidadesJSON, creando nuevas filas en la tabla
	 * */
	private void agregarFila() {
		//agrega datos que se encuentran guardados en JSON
		LocalidadesJSON localidades =  LocalidadesJSON.leerJSON("historial");
		if(localidades!=null) {
			for (int i = 0; i < localidades.tamanio(); i++) {
				tableModel.addRow(new String[] {localidades.dameLocalidad(i),localidades.dameProvincia(i),
						""+localidades.dameLatitud(i),""+localidades.dameLongitud(i)});
			}
		}
	}
	
	/**
	 * El método utilizarDatosGuardados() toma los datos guardados guardandolos en el ArrayList "guardadas"
	 * */
	private void utilizarDatosGuardados() {
		LocalidadesJSON localidades =  LocalidadesJSON.leerJSON("historial");
		if(localidades!=null) {
			for (int i = 0; i < localidades.tamanio(); i++) {
				guardadas.add(new Localidad(localidades.dameLocalidad(i),localidades.dameProvincia(i),
						""+localidades.dameLatitud(i),""+localidades.dameLongitud(i)));
			}
		}
	}

	/**
	 * Muestra el panel del historial mediante el método setVisible y añade el panel al JFrame 
	 * */
	public void mostrarHistorial() {
		mostrarMenu(true);
		_frame.getContentPane().add(this);
	}

	@Override
	protected void iniciar() {
		btnCargar = new JButton("Cargar datos");
		btnVolver = new JButton("Volver");
		add(btnVolver);
		add(btnCargar);
	}

	@Override
	protected void accionListener() {
		btnCargar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				utilizarDatosGuardados();
				if (guardadas.size() > 1) {
					mostrarMenu(false);
					RedTelefonica red = new RedTelefonica(guardadas);
					_frame.crearMapa(red);
				} else {
					JOptionPane.showMessageDialog(null,"Error al cargar datos");
				}
			}
		});
		
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					mostrarMenu(false);
					_frame.getIngresarDatos().mostrarMenu(true);
			}
		});
	}

	@Override
	protected void establecerPosiciones() {
		setBounds(450,150, 490, 450);
		btnCargar.setBounds(32, 350, 111, 23);
		btnVolver.setBounds(32, 175, 89, 23);
	}

	@Override
	protected void mostrarMenu(boolean mostrar) {
		setVisible(mostrar);
	}
	
	@Override
	protected void configuracion() {
		add(ubicarImagen());	
		configurarBotones();
	}

	/**El método configurar botones estiliza el diseño de los mismos delegandole el trabajo al métódo de la superclase Menu*/
	private void configurarBotones() {
		configurarBotones(btnCargar);
		configurarBotones(btnVolver);
	}

	/**El método guardar() llama al metodo guardar de la clase LocalidadJSON
	 * @param localidades ingresadas, recibe por parámetro un ArrayList de tipo Localidad con las localidades que se desea guardar*/
	public void guardar(ArrayList<Localidad> localidades) {
		_localidadesJSON.guardar(localidades);
		
	}

	/**El método inicializarJSON genera una nueva instancia de la clase LocalidadesJSON con las localidades nuevas
	 * @param localidades*/
	public void inicializarJSON(ArrayList<Localidad> localidades) {
		_localidadesJSON = new LocalidadesJSON(localidades);	
	}

}
