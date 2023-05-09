package vistaAuxiliares;

import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import auxiliares.Localidad;
import auxiliares.Tripla;
import auxiliares.Tupla;
import modelo.RedTelefonica;
import vista.Interfaz;
import vista.MenuModificarDatos;
/**
 * La clase SsustituirCamino fue creada por una decisión de implementación ya que era muy extensa para que quedara en
 * MenuModificarDatos, la siguiente clase tiene la responsabilidad de generar una interfaz para sustituir datos y la
 * funcionalidad del mismo, principalmente se reciben los datos y se verifican si son válidos para susituir camino
 * @author Mauro Ricceli
 * @author Jazmin Quinteros
 * */
public class SustituirCamino {
	private JLabel [] label;
	private JLabel labelOrigen;
	private JLabel labelEliminar;
	private JLabel labelNueva;
	
	private JTextField textFieldLocOrigen;
	private JTextField textFieldProvOrigen;
	private JTextField textFieldLocEliminar;
	private JTextField textFieldProvEliminar;
	private JTextField textFieldLocNueva;
	private JTextField textFieldProvNueva;
	private JTable table;
	private DefaultTableModel tableModel;

	private Interfaz _frame;
	private RedTelefonica _red;
	private MenuModificarDatos _menuModificarDatos;
	private JLayeredPane panel;
	
	/**El contructor recibe las variables necesarias para agregar la interfaz en el JFrame y obtener datos del grafo*/

	public SustituirCamino(Interfaz frame, RedTelefonica red, MenuModificarDatos menuModificarDatos) {
		panel = new JLayeredPane();
		panel.setBounds(450,150, 469, 450);
		
		_frame = frame;
		_red = red;
		_menuModificarDatos = menuModificarDatos;
		_frame.setExtendedState(Frame.NORMAL);
		_frame.setResizable(false);
		_frame.getContentPane().add(panel);
		
		iniciar();
		configuracion();
		establecerPosiciones();
		accionListener();
		tabla();
		panel.setVisible(true);
	}
	
	/**El método SustituirCamino() tiene la finalidad para sustituir una conexion por otra a partir de los datos ingresados
	 * Los datos ingresados son: La localidad origen, la localidad nueva para crear la conexión y la localidad a eliminar para
	 * borrar la conexión
	 * 
	 * También verifica que las localidades a eliminar conexión sean vecinas y que tengan más de un vecino para 
	 * seguir con el formato de red. Por otro lado, también verifica que la localidad a agregar conexión, no sea vecina
	 * de la localidad de origen. Si pasa por esas verificaciones, se realiza la sustitución*/
	public void sustituirCamino() {
		String origenLocalidad = textFieldLocOrigen.getText();
		String origenProvincia = textFieldProvOrigen.getText();
		String nuevaLocalidad = textFieldLocNueva.getText();
		String nuevaProvincia = textFieldProvNueva.getText();
		String eliminarLocalidad = textFieldLocEliminar.getText();
		String eliminarProvincia = textFieldProvEliminar.getText();
		Tupla<String,String> origen = new Tupla<String,String>(origenLocalidad,origenProvincia);
		Tupla<String,String> nueva = new Tupla<String,String>(nuevaLocalidad,nuevaProvincia);
		Tupla<String,String> eliminada = new Tupla<String,String>(eliminarLocalidad,eliminarProvincia);

		int cont=0;
		if (verificarInput(origenLocalidad, origenProvincia) && verificarInput(eliminarLocalidad, eliminarProvincia) && verificarInput(nuevaLocalidad,nuevaProvincia)) {
			if (_red.localidadesVecinas(origenLocalidad, origenProvincia,nuevaLocalidad,nuevaProvincia)) {
				JOptionPane.showMessageDialog(null,"Ya son vecinas las localidades a agregar conexión "+"<"+origenLocalidad+","+origenProvincia+">"+" y "+"<"+nuevaLocalidad+","+nuevaProvincia+">");
				cont++;
			} 
			if (!_red.localidadesVecinas(origenLocalidad, origenProvincia,eliminarLocalidad,eliminarProvincia)) {
				JOptionPane.showMessageDialog(null,"No existe una conexión entre las localidades a eliminar conexión "+"<"+origenLocalidad+","+origenProvincia+">"+" y "+"<"+eliminarLocalidad+","+eliminarProvincia+">");
				cont++;
			}
			
			if (!_red.esPosibleSustituirCamino(eliminarLocalidad, eliminarProvincia)) {
				JOptionPane.showMessageDialog(null,"No es posible eliminar esa conexión, debe tener dos vecinos como mínimo para poder eliminar la conexión "+"<"+origenLocalidad+","+origenProvincia+">"+" y "+"<"+eliminarLocalidad+","+eliminarProvincia+">");
				cont++;
			}
			
			if (cont == 0 && _red.localidadesVecinas(origenLocalidad, origenProvincia,eliminarLocalidad,eliminarProvincia) && !_red.localidadesVecinas(origenLocalidad, origenProvincia,nuevaLocalidad,nuevaProvincia)) {
				_red.sustituirCamino(new Tripla<Tupla<String,String>,Tupla<String,String>,Tupla<String,String>>(origen,nueva,eliminada));
				JOptionPane.showMessageDialog(null, "Se ha sustituido la conexión entre "+"<"+origenLocalidad+","+eliminarLocalidad+">"+" por "+"<"+origenLocalidad+","+nuevaLocalidad+">");
			}
		}
	}
	
	/**El método verificarInput() verifica los datos ingresados por el usuario  
	 * @param 
	 * @return true, si la localidad ingresada es válida y si existe, de lo contrario retorna false */
	private boolean verificarInput(String localidad, String provincia) {
		if(localidad.equals("") || provincia.equals("")) {
			JOptionPane.showMessageDialog(null, "Debe agregar una localidad para sustituir alguna de sus conexiones");
			return false;
		}
		if(_red.tengoLocalidad(localidad, provincia) == null) {
			JOptionPane.showMessageDialog(null,"Esa localidad de origen no existe: "+localidad+" ,"+provincia);
			return false;
		}
		return true;
	}

	protected void accionListener() {
		JButton btnSustituir = new JButton("Sustituir");
		JButton btnResetear = new JButton("Resetear grafo");
		JButton btnFinalizar = new JButton("Finalizar");
		
		btnSustituir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (_red.getLocalidades().size()<=2) {
					JOptionPane.showMessageDialog(null,"Deben existir al menos tres localidades para podes sustituir");
				} else {
					sustituirCamino();
				}
			}
		});
		
		btnFinalizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_frame.remove(_menuModificarDatos);
				_frame.remove(panel);
				_frame.crearMapa(_red);
			}
		});
		
		btnResetear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (_red.get_nuevosCaminos().size()>0) {
					JOptionPane.showMessageDialog(null,"Caminos reseteados");
					_red.planificar();
				} else {
					JOptionPane.showMessageDialog(null,"No hay caminos para resetear");
				}
			}
		});
		_menuModificarDatos.configurarBotones(btnFinalizar);
		_menuModificarDatos.configurarBotones(btnSustituir);
		_menuModificarDatos.configurarBotones(btnResetear);
		btnSustituir.setBounds(294, 61, 119, 23);
		btnFinalizar.setBounds(294, 95, 119, 23);
		btnResetear.setBounds(294,129, 119, 23);
		panel.add(btnSustituir);
		panel.add(btnFinalizar);
		panel.add(btnResetear);
		
		if(_red.getLocalidades().size() <= 2) {
			btnSustituir.setEnabled(false);
			JOptionPane.showMessageDialog(null, "No se pueden sustituir caminos si hay solo dos localidades, hay un solo camino posible");
		}		
	}
	
	/**Tabla() método que muestra la informacion de localidades que se muestran en la interfaz de usuario*/
	private void tabla() {
		String informacion[] = {"Localidad", "Provincia", "Latitud", "Longitud"};
		
		//DefaultTableModel contiene m�todos para agregar filas
		tableModel = new DefaultTableModel(null,informacion);
		
		//posiciona datos en la tabla
		for (int loc = 0; loc < _red.getLocalidades().size(); loc++) {
				agregarLocalidadEnTabla(_red.getLocalidades().get(loc));
		}
		table = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(30, 300, 390, 55);
		panel.add(scrollPane);	}
	
	private void agregarLocalidadEnTabla(Localidad ingresada) {
		tableModel.addRow(new String[] {ingresada.getNombre(),ingresada.getProvincia(),
										ingresada.toStringLatitud(),ingresada.toStringLongitud()});
	}
	
	protected void configuracion() {
		labelOrigen.setFont(new Font("Arial", labelOrigen.getFont().getStyle() | Font.BOLD | Font.ITALIC, labelOrigen.getFont().getSize()));
		labelEliminar.setFont(new Font("Arial", labelEliminar.getFont().getStyle() | Font.BOLD | Font.ITALIC, labelEliminar.getFont().getSize()));
		labelNueva.setFont(new Font("Arial", labelNueva.getFont().getStyle() | Font.BOLD | Font.ITALIC, labelNueva.getFont().getSize()));
	}
	
	protected void iniciar() {
		inicializarComponentes();
		agregarComponentes();
	}
	private void agregarComponentes() {
		panel.add(textFieldLocOrigen);
		panel.add(textFieldProvOrigen);
		panel.add(textFieldLocEliminar);
		panel.add(textFieldProvEliminar);
		panel.add(textFieldLocNueva);
		panel.add(textFieldProvNueva);
		panel.add(labelOrigen);
		panel.add(labelEliminar);
		panel.add(labelNueva);
	}
	
	private void inicializarComponentes() {
		label = new  JLabel[6];
		labelOrigen = new JLabel("INGRESAR LOCALIDAD ORIGEN");
		labelNueva = new JLabel("CONEXION NUEVA");
		labelEliminar = new JLabel("CONEXION A ELIMINAR");
		
		for (int i = 0; i < 6;i++) {
			if (i%2==0) {
				label[i] = new JLabel("Localidad");
			} else {
				label[i] = new JLabel("Provincia");
			} 
			panel.add(label[i]);
		}
		
		textFieldLocOrigen = new JTextField();
		textFieldProvOrigen = new JTextField();
		textFieldLocEliminar = new JTextField();
		textFieldProvEliminar = new JTextField();
		textFieldLocNueva = new JTextField();
		textFieldProvNueva = new JTextField();
	}

	protected void establecerPosiciones() {
		labelOrigen.setBounds(38, 40, 207, 14);
		labelEliminar.setBounds(38, 148, 207, 14);
		labelNueva.setBounds(38, 221, 207, 14);
		
		label[0].setBounds(38, 170, 83, 14);
		label[1].setBounds(38, 195, 83, 14);
		label[2].setBounds(38, 65, 83, 14);
		label[3].setBounds(38, 93, 83, 14);
		label[4].setBounds(38, 242, 83, 14);
		label[5].setBounds(38, 270, 83, 14);
		
		textFieldLocOrigen.setBounds(122, 62, 146, 20);
		textFieldProvOrigen.setBounds(122, 90, 146, 20);
		textFieldLocEliminar.setBounds(122, 167, 146, 20);
		textFieldProvEliminar.setBounds(122, 192, 146, 20);
		textFieldLocNueva.setBounds(122, 239, 146, 20);
		textFieldProvNueva.setBounds(122, 267, 146, 20);
	}
}