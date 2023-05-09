package manejoDeArchivos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import auxiliares.Localidad;

public class LocalidadesJSON implements Runnable {
	private ArrayList<Localidad> _localidades;
	
	public LocalidadesJSON(ArrayList<Localidad> localidadesRed) {
		_localidades = localidadesRed;
	}
	
	@Override
	public void run() {
		String jsonPretty = generarJSONPretty();
		guardarJSON(jsonPretty, "historial");
	}
	
	public String dameLocalidad(int indice) {
		return _localidades.get(indice).getNombre();
	}
	
	public String dameProvincia(int indice) {
		return _localidades.get(indice).getProvincia();
	}
	
	public String dameLatitud(int indice) {
		return _localidades.get(indice).getLatitud().toString();
	}
	
	public String dameLongitud(int indice) {
		return _localidades.get(indice).getLongitud().toString();
	}
	
	public int tamanio() {
		return _localidades.size();
	}
	
	public String generarJSONPretty() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(this);
		
		return json;
	}
	
	private void guardarJSON(String jsonParaGuardar,  String archivoDestino) {
		try
		{
			FileWriter writer = new FileWriter(archivoDestino);
			writer.write(jsonParaGuardar+dameGuardado(leerJSON(archivoDestino)));
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static LocalidadesJSON leerJSON(String archivo) {
		Gson gson = new Gson();
		LocalidadesJSON ret = null;
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(archivo));
			ret = gson.fromJson(br, LocalidadesJSON.class);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	private String dameGuardado(LocalidadesJSON localidadesGuardadas) {
		if(localidadesGuardadas!=null)
			return localidadesGuardadas.generarJSONPretty();
		return "";
	}


	public void guardar(ArrayList<Localidad> localidades) {
		LocalidadesJSON localidadesJSON = new LocalidadesJSON(localidades);
		Thread guardarJSON = new Thread(localidadesJSON);
		guardarJSON.start();	
	}
}
