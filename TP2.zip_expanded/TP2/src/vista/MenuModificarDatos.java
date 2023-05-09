package vista;

import javax.swing.table.DefaultTableModel;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import auxiliares.Localidad;
import modelo.RedTelefonica;
import vistaAuxiliares.SustituirCamino;

/**
 * La clase MenuModificarDatos es una subclase de la clase Menu. Muestra la interfaz con los componentes necesarios para
 * modificar datos y contiene la funcionalidad para trabajar con esos métodos ingresados
 * 
 * Incluye la función de agregar nueva localidad, eliminar una localidad existente, borrar un camino y sustituir un camino.
 * Además muestra el costo de la conexión, permite guardar datos y crear una nueva conexión
 * 
 * @author Mauro Ricceli
 * @author Jazmin Quinteros
 * */
@SuppressWarnings("serial")
public class MenuModificarDatos extends Menu {
	private DefaultTableModel tableModel;
	/**
	 *  La variable _agregarElemento funciona para indicar que objetos tiene que añadir al panel dependiendo de la funcionalidad
	 * que se le quiera dar: 0 agregarElemento, 1 eliminar elemento, 2 eliminar camino
	 * */
	private Integer _agregarElemento; 
	private JButton btnModificar;
	private JTable tabla;
	private RedTelefonica _red;
	/**
	 * Variable _realiceCambios utilizada para no volver a ejecutar la generación de un nuevo grafo en caso de que se hayan
	 * hecho cambios
	 * */
	protected boolean _realiceCambios;
	/**Variable _contDeshacer utiliazada para activar/desactivar el botón de deshacer camino borrado*/
	private int _contDeshacer;
	
	/**El contructor recibe por parametro un objeto de tipo Interfaz que se utilizará como frame.
	 *  Además recibe un objeto de tipo RedTelefonica y una variable de tipo Integer "agregarElemento"
	 *  @param */
	public MenuModificarDatos(Interfaz frame, RedTelefonica red, Integer agregarElemento) {
		super(frame);
				
		_agregarElemento = agregarElemento;
		_red = red;
		_realiceCambios = false;
		
		iniciar();
		establecerPosiciones();
		configuracion();
		mostrarDatos();
		accionListener();
	}

	/**MostrarDatos() Carga la información de localidades actuales en una tabla*/
	protected void mostrarDatos() {
		String informacion[] = {"Localidad", "Provincia", "Latitud", "Longitud"};
		
		//DefaultTableModel contiene m�todos para agregar filas
		tableModel = new DefaultTableModel(null,informacion);
		
		//posiciona datos en la tabla
		for (int loc = 0; loc < _red.getLocalidades().size(); loc++) {
				agregarLocalidadEnTabla(_red.getLocalidades().get(loc));
		}

		//tabla con datos
		tabla = new JTable(tableModel);
		tabla.setAutoCreateColumnsFromModel(false);
		tabla.setRowSelectionAllowed(false);
		tabla.setBounds(0, 180, 429, 82);
		
		//barra de scroll
		JScrollPane scrollPane = new JScrollPane(tabla);
		scrollPane.setBounds(10, 300, 428, 55);
		add(scrollPane, BorderLayout.CENTER);
		tabla.setEnabled(true);
		
	}

	/**agregarLocalidadEnTabla() agrega una fila en la tabla de datos con los datos de la localidad
	 * @param Localidad*/ 
	private void agregarLocalidadEnTabla(Localidad ingresada) {
		tableModel.addRow(new String[] {ingresada.getNombre(),ingresada.getProvincia(),
										ingresada.toStringLatitud(),ingresada.toStringLongitud()});
	}
	
	@Override
	protected void accionListener() {
		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				if(_realiceCambios) {
					_red.planificar();
				}
				_frame.crearMapa(_red);
			}
		});
	}
	
	/**eliminarElementoDelGrafo() crea y posiciona el botón eliminar en el panel y activa la opción de eliminar en caso de que
	 * existan más de dos localidades, esto es para que el grafo se pueda crear. Una vez ingresada una localidad, 
	 * verifica si es válida  y pasa a elminarla de la tabla de datos*/
	public void eliminarElementoDelGrafo() {
		JButton btnEliminar = new JButton("Eliminar");
		btnEliminar.setBounds(305, 185, 140, 22);
		configurarBotones(btnEliminar);
		add(btnEliminar);
		JButton btnDeshacer = new JButton("Deshacer cambio");
		btnDeshacer.setBounds(305, 240, 140,22);
		configurarBotones(btnDeshacer);
		add(btnDeshacer);
		if(_red.getLocalidades().size() <= 2) {
			btnEliminar.setEnabled(false);
			JOptionPane.showMessageDialog(null, "No puede dejar una red con menos de 2 localidades, considerar comenzar una conexión nueva");
		}
		
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(_red.getLocalidades().size() <= 2) {
					btnEliminar.setEnabled(false);
					JOptionPane.showMessageDialog(null, "No puede dejar una red con menos de 2 localidades, considerar comenzar una conexión nueva");
					return;
				}
				
				String localidad = textFieldLocalidad.getText();
				String provincia = textFieldProvincia.getText();
				
				if(localidad.equals("") || provincia.equals("")) {
					JOptionPane.showMessageDialog(null, "Olvido de completar una casilla");
					return;
				}
				
				if(_red.eliminarLocalidad(localidad, provincia) != false) {
					textFieldLocalidad.setText("");
					textFieldProvincia.setText("");
					_realiceCambios = true;
					mostrarDatos();
				} else {
					JOptionPane.showMessageDialog(null, "Esa localidad no existe en la red: "+localidad+" , "+provincia);
				}
			}
		});
		
		btnDeshacer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(_red.deshacerEliminarLocalidad()) {
					_realiceCambios = true;
					agregarLocalidadEnTabla(_red.getLocalidades().get(_red.getLocalidades().size()-1));
					JOptionPane.showMessageDialog(null,"Localidad reestablecida: "+_red.getLocalidades().get(_red.getLocalidades().size()-1));
				} else {
					JOptionPane.showMessageDialog(null, "No hay datos por deshacer");
				}
			}
		});
	}

	/**agregarElementoDelGrafo() crea y posiciona el botón agregar en el panel. Una vez ingresada una localidad, 
	 * verifica si es válida y pasa a agregarla en la tabla de datos*/
	public void agregarElementoDelGrafo() {
		JButton btnAgregar = new JButton("Agregar");
		btnAgregar.setBounds(305, 184, 140, 22);
		configurarBotones(btnAgregar);
		add(btnAgregar);
		btnAgregar.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent e) {				
				Localidad ingresada = obtenerDatosIngresados();
				if(ingresada != null) {
					if(_red.getLocalidades().contains(ingresada)) {
						JOptionPane.showMessageDialog(null, "Esa localidad ya esta ingresada: "+ingresada.getNombre()+" , "+ingresada.getProvincia());
					} else {
						if (_red.tengoLocalidad(ingresada.getNombre(),ingresada.getProvincia()) != null) {
							JOptionPane.showMessageDialog(null,"El nombre de la localidad o provincia ya está agregada:"+ingresada.getNombre()+" , "+ingresada.getProvincia());
						} else {
							if(_red.tengoCoordenadas(ingresada.getLatitud(), ingresada.getLongitud())) {
								JOptionPane.showMessageDialog(null,"Las coordenadas que ingresó ya las posee otra localidad/provincia con distinto nombre:"+ingresada.getLatitud()+" ,"+ingresada.getLongitud());
							} else {
								_red.agregarLocalidad(ingresada);
								agregarLocalidadEnTabla(ingresada);
								_realiceCambios = true;
						    
								textFieldLocalidad.setText("");
								textFieldProvincia.setText("");
								textFieldLongitud.setText("");
								textFieldLatitud.setText("");
							}
						}	
					}
				}	
			}
		});
	}
	
	/**borrarCaminoDelGrafo() crea y posiciona el botón eliminar camino, resetear y deshacer en el panel.
	 * Activa la opción de eliminar en caso de que existan más de dos localidades, esto es para que
	 * se pueda eliminar un camino existente. Al igual que resetear, que verifica que al menos se haya
	 * eliminado un camino para activar la opción*/
	public void borrarCaminoDelGrafo() {
		_contDeshacer = 0;
		
		JButton btnEliminarCamino = new JButton("Eliminar camino");
		JButton btnResetear = new JButton("Reiniciar cambios");
		JButton btnDeshacer = new JButton("Deshacer cambio");
		btnEliminarCamino.setBounds(305, 184, 140,22);
		btnResetear.setBounds(305, 267, 140, 22);
		btnDeshacer.setBounds(305, 240, 140,22);
		add(btnEliminarCamino);
		add(btnResetear);
		add(btnDeshacer);
		
		btnDeshacer.setEnabled(false);
		
		configurarBotones(btnDeshacer);
		configurarBotones(btnResetear);
		configurarBotones(btnEliminarCamino);
		
		if(_red.getLocalidades().size() <= 2) {
			JOptionPane.showMessageDialog(null, "No se puede borrar caminos con 2 localidades o menos");
			btnEliminarCamino.setEnabled(false);
			btnResetear.setEnabled(false);
		} else {
			btnEliminarCamino.setEnabled(true);
			btnResetear.setEnabled(true);
		}
		
		eliminarCamino(btnEliminarCamino, btnDeshacer);
		resetearCaminoBorrado(btnResetear);
		deshacerCambios(btnDeshacer);
	}

	/**eliminarCamino() verifica que las localidades a eliminar conexión sean válidas, de ser así, las elimina
	 * @param*/
	private void eliminarCamino(JButton btnEliminarCamino, JButton btnDeshacer) {
		btnEliminarCamino.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String localidadOrigen = textFieldLocalidad.getText();
				String provinciaOrigen = textFieldProvincia.getText();
				String localidadDestino = textFieldLatitud.getText();
				String provinciaDestino = textFieldLongitud.getText();
				
				if (verificarInput(localidadOrigen, provinciaOrigen, localidadDestino, provinciaDestino)){
					_contDeshacer++;
					_red.borrarCamino(localidadOrigen, provinciaOrigen, localidadDestino, provinciaDestino);
					_realiceCambios = true;
					JOptionPane.showMessageDialog(null,"Camino borrado : Origen: <"+localidadOrigen+" ,"+provinciaOrigen+">"+" Destino: <"+localidadDestino+" ,"+provinciaDestino+">");
					
					textFieldLocalidad.setText("");
					textFieldProvincia.setText("");
					textFieldLongitud.setText("");
					textFieldLatitud.setText("");
				}
				
				if(_contDeshacer > 0) {
					btnDeshacer.setEnabled(true);
				}	
			}	
		});
	}

	/**el método deshacerCambios() contiene el accionListener del botón deshacer, el cual se activa dentro del panel
	 * borrarCamino una vez eliminada una conexión. Si está activado, vuelve a restaurar el último camino eliminado
	 * @param*/
	private void deshacerCambios(JButton btnDeshacer) {
		btnDeshacer.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				_contDeshacer--;
				
				if(_contDeshacer == 0) {
					btnDeshacer.setEnabled(false);
					_realiceCambios = false;
				}
				
				JOptionPane.showMessageDialog(null,"Camino reestablecido");
				
				_red.deshacerBorrarCamino();
			}
		});
	}

	/**el método resetearCaminoBorrado() contiene el accionListener del botón resetear, si existen caminos borrados,
	 * estos dejan de ser tomados en cuenta y se restaura el grafo de todo camino borrado
	 * @param*/
	private void resetearCaminoBorrado(JButton btnResetear) {
		//debe existir un camino eliminado para resetear
		btnResetear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (_red.get_caminosProhibidos().size() == 0) {
					JOptionPane.showMessageDialog(null,"No hay caminos por resetear");
				} else {
					_red.resetearCaminosEliminados();
					_realiceCambios = true;
					JOptionPane.showMessageDialog(null,"Caminos reseteados");
				}
			}
		});
	}

	
	/**el método verificarInput verifica los datos ingresados por el usuario:
	 * Verifica que no haya JTextFields vacios, que las localidades de origen y destino no sean las mismas,
	 * si la localidad ingresada es existente y si es posible borrar camino, es decir, que ambas tengan más de un vecino
	 * para seguir manteniendo la red
	 * @param
	 * @return true si es posible borrar el camino y las localidades son validas, de lo contrario retorna false*/
	protected boolean verificarInput(String localidadOrigen, String provinciaOrigen, String localidadDestino,String provinciaDestino) {
		
		if(localidadOrigen.length() == 0 || provinciaOrigen.length() == 0 || localidadDestino.length() == 0 || provinciaDestino.length() == 0) {
			JOptionPane.showMessageDialog(null,"Olvidó completar una casilla");
			return false;
		}
		
		if (localidadOrigen.equals(localidadDestino) && provinciaOrigen.equals(provinciaDestino)) {
			JOptionPane.showMessageDialog(null,"No se permite ingresar una misma localidad más de una vez");
			return false;
		}
		
		if(_red.tengoLocalidad(localidadOrigen, provinciaOrigen) == null) {
			JOptionPane.showMessageDialog(null,"Esa localidad de origen no existe: "+localidadOrigen+" ,"+provinciaOrigen);
			return false;
			
		}
		if(_red.tengoLocalidad(localidadDestino, provinciaDestino) == null) {
			JOptionPane.showMessageDialog(null,"Esa localidad de destino no existe: "+localidadDestino+" ,"+provinciaDestino);
			return false;
		}
		
		if(!_red.esPosibleBorrarCamino(localidadOrigen, provinciaOrigen)) {
			JOptionPane.showMessageDialog(null,"No es posible borrar el último camino posible para: "+localidadOrigen+" ,"+provinciaOrigen);
			return false;
		}
		
		if(!_red.esPosibleBorrarCamino(localidadDestino, provinciaDestino)) {
			JOptionPane.showMessageDialog(null,"No es posible borrar el último camino posible para: "+localidadDestino+" ,"+provinciaDestino);
			return false;
		}
		return true;
	}
	
	/**El método sustituirCaminoDelGrafo() crea un nuevo objeto de la clase SusituirCamino, el cual posee la interfaz
	 * para ingresar los datos a sustituir y la funcionalidad del mismo*/
	public void sustituirCaminoDelGrafo() {
		new SustituirCamino(_frame,_red,this);
	}

	@Override
	protected void establecerPosiciones() {
		setBounds(450,150, 450, 450);
		btnModificar.setBounds(305, 212, 140, 22);
		
		if(_agregarElemento.equals(0)  || _agregarElemento.equals(2)) {
		
			textFieldLocalidad.setBounds(118, 184, 183, 20);
			textFieldProvincia.setBounds(118, 210, 183, 20);
			textFieldLatitud.setBounds(118, 244, 183, 20);
			textFieldLongitud.setBounds(118, 269, 183, 20);
			
			labelLocalidad.setBounds(10, 190, 102, 22);
			labelProvincia.setBounds(10, 215, 98, 22);
			labelLatitud.setBounds(10, 247, 102, 22);
			labelLongitud.setBounds(10, 273, 102, 22);
			
			separator.setBounds(118, 206, 180, 3);
			separator_1.setBounds(118, 230, 180, 3);
			separator_2.setBounds(118, 264, 180, 3);
			separator_3.setBounds(118, 289, 180, 3);
			
		} else {
			textFieldLocalidad.setBounds(108, 185, 183, 20);
			textFieldProvincia.setBounds(108, 210, 183, 20);
			labelLocalidad.setBounds(15,190, 102, 22);
			labelProvincia.setBounds(15, 215, 98, 22);
			separator.setBounds(110, 205, 183, 3);
			separator_1.setBounds(110, 230, 183, 3);
		}
	}
	
	@Override
	protected Localidad obtenerDatosIngresados() {
		return super.obtenerDatosIngresados();
	}
	
	/**El método crear() crea un nuevo objeto MenuModificarDatos
	 * @param recibe por parámetro un objeto de la clase Interfaz para inicalizar el objeto MenuModificarDatos
	 * @return nuevo objeto MenuModificarDatos*/
	public static MenuModificarDatos crear(Interfaz frame, RedTelefonica red, Integer agregarElemento) {
		return new MenuModificarDatos(frame, red, agregarElemento);	
	}
	
	@Override
	protected void mostrarMenu(boolean mostrar) {
		setVisible(mostrar);
	}

	@Override
	protected void configuracion() {
		add(ubicarImagen());
		configurarBotones(btnModificar);
		configurarLabel();
		configurarSeparador();
		configuracionTextField();
	}
	
	@Override
	protected void iniciar() {
		btnModificar = new JButton("Finalizar");
		if(_agregarElemento.equals(2)) {
			labelLocalidad.setText("Localidad origen");
			labelProvincia.setText("provincia origen");
		}
		
		add(btnModificar);	
		add(textFieldLocalidad);
		add(textFieldProvincia);
		add(labelLocalidad);
		add(labelProvincia);
		add(separator);
		add(separator_1);
		
		if(!_agregarElemento.equals(1)) {
			if(_agregarElemento.equals(2)) {
				labelLatitud.setText("Localidad destino");
				labelLongitud.setText("Provincia destino");
			}
			add(textFieldLatitud);
			add(textFieldLongitud);
			add(labelLatitud);
			add(labelLongitud);
			add(separator_2);
			add(separator_3);
		}
	}

}