package vistaAuxiliares;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.openstreetmap.gui.jmapviewer.JMapViewer;

import modelo.RedTelefonica;
import vista.Historial;
import vista.Interfaz;
import vista.MenuModificarDatos;
/**
 * La clase OpcionesMapa contiene la funcionalidad del menú de barra que se crea en Mapa, además de mostrarla en
 * pantalla junto con el mapa, permite ingresar a las distintas interfaces de modificar datos
 * @author Mauro Ricceli
 * @author Jazmin Quinteros
 * */
public class OpcionesMapa {
	private  JMapViewer _mapa;
	
	private JMenuBar menuBarra;
	
	private JMenu menuOpciones;
	private JMenu ModificarOpciones;
	
	private JMenuItem menuCostos;
	private JMenuItem menuNuevaConexion;
	private JMenuItem eliminar;
	private JMenuItem agregar;
	private JMenuItem borrarCamino;
	private JMenuItem sustituirCamino;
	private JMenuItem menuGuardar;
	
	private JPanel  panel;
	
	private RedTelefonica _red;
	private Interfaz _frame;
	private Historial _historial;
	
	/**
	 * El constructor de la clase OpcionesMenu inicializa la interfaz de la barra de menú y los accionListener para poder
	 * seleccionar las opciones
	 * */
	public OpcionesMapa(Interfaz frame, JMapViewer mapa,RedTelefonica red) {
		_mapa = mapa;
		_red = red;
		_frame = frame;
		_historial = _frame.getHistorial();
		_historial.inicializarJSON(_red.getLocalidades());
		
		iniciar();
		establecerPosiciones();
		accionListener();
	}
	
	/**El método opcionesMenu() crea accionListeners de las pestañas ubicadas en la barra de menú en el mapa*/
	protected void opcionesMenu() {
		menuCostos();
		menuGuardar();
		menuNuevaConexion();
		menuEliminarLocalidad();
		menuAgregarLocalidad();
		menuBorrarCamino();
		menuSustituirCamino();
		
	}
	
	/**El método menuSustituirCamino() genera la interfaz sustituir camino utilizando el
	 * método estático de la clase MenuModificarDatos "crear" y llamando al método sustituirCaminoDelGrafo() */
	private void menuSustituirCamino() {
		sustituirCamino.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarMenu(false);
				_mapa.setVisible(false);
				MenuModificarDatos.crear(_frame, _red, 0).sustituirCaminoDelGrafo();
			}
		});
	}
	
	/**El método borrarCamino() genera la interfaz de borrar camino mediante el
	 * método estático de la clase MenuModificarDatos "crear" y llamando al método borrarCaminoDelGrafo() */
	private void menuBorrarCamino() {
		borrarCamino.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarMenu(false);
				_mapa.setVisible(false);
				MenuModificarDatos.crear(_frame, _red, 2).borrarCaminoDelGrafo();
			}
		});
	}
	
	/**El método menuAgregarLocalidad() genera la interfaz de agregar localidad mediante el
	 * método estático de la clase MenuModificarDatos "crear" y llamando al método agregarElementoDelGrafo() */
	private void menuAgregarLocalidad() {
		agregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarMenu(false);
				_mapa.setVisible(false);
				MenuModificarDatos.crear(_frame, _red, 0).agregarElementoDelGrafo();
			}
		});
	}
	
	/**El método menuEliminarLocalidad() genera la interfaz de eliminar localidad mediante el
	 * método estático de la clase MenuModificarDatos "crear" y llamando al método eliminarElementoDelGrafo() */
	private void menuEliminarLocalidad() {
		eliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarMenu(false);
				_mapa.setVisible(false);	
				MenuModificarDatos.crear(_frame, _red, 1).eliminarElementoDelGrafo();
			}
		});
	}
	
	/**El método menuNuevaConexion() genera la interfaz de menuIngresarDatos mediante el método de la
	 * clase Interfaz nuevaConexion()*/
	private void menuNuevaConexion() {
		menuNuevaConexion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_mapa.setVisible(false);	
				_frame.nuevaConexion();
			}
		});
	}
	
	/**El método menuGuardar() guarda los datos actuales llamando al método de la clase Historial "guardar" */
	private void menuGuardar() {
		menuGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    _historial.guardar(_red.getLocalidades());
				JOptionPane.showMessageDialog(null, "Red guardada");
			}
		});
	}
	
	/**El método menuCostos() llama al método mostrarCostoTotal() */
	private void menuCostos() {
		menuCostos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarCostoTotal(_red);
			}
		});
	}
	
	/**El método mostrarCostoTotal() crea un panel con los datos del costo generado por la conexión*/
	private void mostrarCostoTotal(RedTelefonica _red) {
		JOptionPane.showMessageDialog(null, _red.toStringCostos());
	}	
	
	protected void iniciar() {
		panel = new JPanel();
		
		menuBarra = new JMenuBar();
		menuOpciones = new JMenu("Menu");
		menuCostos = new JMenuItem("Costo total");
		menuNuevaConexion = new JMenuItem("Nueva conexion");
		eliminar = new JMenuItem("Eliminar localidad");
		agregar = new JMenuItem("Agregar localidad");
		borrarCamino = new JMenuItem("Borrar camino");
		sustituirCamino = new JMenuItem("Sustituir camino");
		ModificarOpciones = new JMenu("Modificar");
		menuGuardar = new JMenuItem("Guardar");
		
		menuOpciones.setBackground(new Color(64, 0, 64));
		menuBarra.add(menuOpciones);
		menuOpciones.add(menuCostos);
		menuOpciones.add(menuNuevaConexion);
		
		menuOpciones.add(menuGuardar);
		menuOpciones.add(ModificarOpciones);
		ModificarOpciones.add(eliminar);
		ModificarOpciones.add(borrarCamino);
		ModificarOpciones.add(agregar);
		ModificarOpciones.add(sustituirCamino);
		
		panel.add(menuBarra);
		_mapa.add(panel);
		panel.setLayout(null);
	}

	protected void accionListener() {
		opcionesMenu();
	}

	protected void establecerPosiciones() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		panel.setBounds(0, 0, screenSize.width, 22);
		menuBarra.setBounds(0, 0, screenSize.width, 22);
	}


	protected void mostrarMenu(boolean mostrar) {
		panel.setVisible(mostrar);
	}
	
	public static OpcionesMapa crearMenuMapa(Interfaz frame,JMapViewer mapa,RedTelefonica red) {
		return new OpcionesMapa(frame,mapa,red);
	}
}