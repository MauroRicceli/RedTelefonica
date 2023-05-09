package modelo;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import auxiliares.Grafo;
import auxiliares.Localidad;
import auxiliares.Tripla;
import auxiliares.Tupla;
import auxiliaresGrafo.ArbolMinimo;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class Tests {
	private ArrayList<Localidad> _localidades;
	
		@Before
		public void generarConjuntoLocalidades() {
			_localidades = new ArrayList<Localidad>();
		}
		
		
		@Test
		public void crearNegocio() {
			Localidad loc1 = new Localidad("Gualeguay", "Entre Rios", "40", "21");
			Localidad loc2 = new Localidad("Concepción del Uruguay", "Entre Rios", "45", "30");
			_localidades.add(loc1);
			_localidades.add(loc2);
			
			RedTelefonica red = new RedTelefonica(_localidades);
		}
		
		@Test (expected = IllegalArgumentException.class)
		public void crearNegocioError() {
			RedTelefonica red = new RedTelefonica(_localidades);
		}
		
		@Test
		public void agregarLocalidad() {
			Localidad loc1 = new Localidad("Gualeguay", "Entre Rios", "40", "21");
			Localidad loc2 = new Localidad("Concepción del Uruguay", "Entre Rios", "45", "30");
			Localidad loc3 = new Localidad("CABA", "Buenos Aires","-34","-58");
			_localidades.add(loc1);
			_localidades.add(loc2);
			
			RedTelefonica red = new RedTelefonica(_localidades);
			
			red.agregarLocalidad(loc3);
			assertTrue(red.tengoLocalidad("CaBa", "buenos AIRES") != null);
		}
		
		@Test
		public void eliminarLocalidadTrue() {
			Localidad loc1 = new Localidad("Gualeguay", "Entre Rios", "40", "21");
			Localidad loc2 = new Localidad("Concepción del Uruguay", "Entre Rios", "45", "30");
			Localidad loc3 = new Localidad("CABA", "Buenos Aires","-34","-58");
			_localidades.add(loc1);
			_localidades.add(loc2);
			_localidades.add(loc3);
			
			RedTelefonica red = new RedTelefonica(_localidades);
			red.eliminarLocalidad("CABA", "buenos aireS");
			assertFalse(red.tengoLocalidad("CaBa", "buenos AIRES") != null);
		}
		
		@Test
		public void sustituirCaminoAgregar() {
			Localidad loc1 = new Localidad("Gualeguay", "Entre Rios", "40", "21");
			Localidad loc2 = new Localidad("Concepción del Uruguay", "Entre Rios", "45", "30");
			Localidad loc3 = new Localidad("CABA", "Buenos Aires","-34","-58");
			
			_localidades.add(loc1);
			_localidades.add(loc2);
			_localidades.add(loc3);
			
			RedTelefonica red = new RedTelefonica(_localidades);
			
			Tupla<String, String> aux = new Tupla<String, String>("Gualeguay", "Entre Rios");
			Tupla<String, String> aux2 = new Tupla<String, String>("CABA", "Buenos Aires");
			Tupla<String, String> aux3 = new Tupla<String, String>("Concepción del Uruguay", "Entre Rios");

			Tripla<Tupla<String,String>,Tupla<String,String>,Tupla<String,String>> tripla = new Tripla<Tupla<String,String>,Tupla<String,String>,Tupla<String,String>>(aux2,aux,aux3);
			red.sustituirCamino(tripla);
			
			assertTrue(red.get_nuevosCaminos().size()>0);
		}
		
		@Test
		public void sustituirCaminoEliminar() {
			Localidad loc1 = new Localidad("Gualeguay", "Entre Rios", "40", "21");
			Localidad loc2 = new Localidad("Concepción del Uruguay", "Entre Rios", "45", "30");
			Localidad loc3 = new Localidad("CABA", "Buenos Aires","-34","-58");
			
			_localidades.add(loc1);
			_localidades.add(loc2);
			_localidades.add(loc3);
			
			RedTelefonica red = new RedTelefonica(_localidades);
			
			Tupla<String, String> aux = new Tupla<String, String>("Gualeguay", "Entre Rios");
			Tupla<String, String> aux2 = new Tupla<String, String>("CABA", "Buenos Aires");
			Tupla<String, String> aux3 = new Tupla<String, String>("Concepción del Uruguay", "Entre Rios");

			Tripla<Tupla<String,String>,Tupla<String,String>,Tupla<String,String>> tripla = new Tripla<Tupla<String,String>,Tupla<String,String>,Tupla<String,String>>(aux2,aux,aux3);
			red.sustituirCamino(tripla);
			
			assertTrue(red.get_caminosSustituidos().size()>0);
		}
		
		@Test
		public void resetearSustitucionCamino() {
			Localidad loc1 = new Localidad("Gualeguay", "Entre Rios", "40", "21");
			Localidad loc2 = new Localidad("Concepción del Uruguay", "Entre Rios", "45", "30");
			Localidad loc3 = new Localidad("CABA", "Buenos Aires","-34","-58");
			
			_localidades.add(loc1);
			_localidades.add(loc2);
			_localidades.add(loc3);
			
			RedTelefonica red = new RedTelefonica(_localidades);
			
			Tupla<String, String> aux = new Tupla<String, String>("Gualeguay", "Entre Rios");
			Tupla<String, String> aux2 = new Tupla<String, String>("CABA", "Buenos Aires");
			Tupla<String, String> aux3 = new Tupla<String, String>("Concepción del Uruguay", "Entre Rios");
			Tripla<Tupla<String,String>,Tupla<String,String>,Tupla<String,String>> tripla = new Tripla<Tupla<String,String>,Tupla<String,String>,Tupla<String,String>>(aux2,aux,aux3);
			
			red.sustituirCamino(tripla);
			red.resetearSustitucionCamino();
			
			assertTrue(red.get_caminosSustituidos().size()==0 && red.get_nuevosCaminos().size()==0);
		}
		
		@Test
		public void esPosibleBorrarCaminoTrue() {
			Localidad loc1 = new Localidad("Gualeguay", "Entre Rios", "40", "21");
			Localidad loc2 = new Localidad("Concepción del Uruguay", "Entre Rios", "45", "30");
			Localidad loc3 = new Localidad("CABA", "Buenos Aires","-34","-58");
			
			_localidades.add(loc1);
			_localidades.add(loc2);
			_localidades.add(loc3);
			
			RedTelefonica red = new RedTelefonica(_localidades);
			assertTrue(red.esPosibleBorrarCamino("CABA", "Buenos Aires"));				
		}
		
		@Test 
		public void esPosibleBorrarCaminoFalse() {
			Localidad loc1 = new Localidad("Gualeguay", "Entre Rios", "40", "21");
			Localidad loc2 = new Localidad("Concepción del Uruguay", "Entre Rios", "45", "30");
			
			_localidades.add(loc1);
			_localidades.add(loc2);
			
			RedTelefonica red = new RedTelefonica(_localidades);
			assertFalse(red.esPosibleBorrarCamino("Gualeguay", "Entre Rios"));
		}
		
		@Test
		public void borrarCamino() {
			Localidad loc1 = new Localidad("Gualeguay", "Entre Rios", "40", "21");
			Localidad loc2 = new Localidad("Concepción del Uruguay", "Entre Rios", "45", "30");
			Localidad loc3 = new Localidad("CABA", "Buenos Aires","-34","-58");
			
			_localidades.add(loc1);
			_localidades.add(loc2);
			_localidades.add(loc3);
			
			RedTelefonica red = new RedTelefonica(_localidades);
			
			red.borrarCamino("Gualeguay", "Entre Rios", "Concepción del Uruguay", "Entre Rios");
			Tupla<Integer, Integer> aux = new Tupla<Integer, Integer>(red.tengoLocalidad("Gualeguay", "Entre Rios"), red.tengoLocalidad("Concepción del Uruguay", "Entre Rios"));
			
			assertTrue(red.get_caminosProhibidos().contains(aux));
		}
		
		@Test
		public void deshacerBorrarCamino() {
			Localidad loc1 = new Localidad("Gualeguay", "Entre Rios", "40", "21");
			Localidad loc2 = new Localidad("Concepción del Uruguay", "Entre Rios", "45", "30");
			Localidad loc3 = new Localidad("CABA", "Buenos Aires","-34","-58");
			
			_localidades.add(loc1);
			_localidades.add(loc2);
			_localidades.add(loc3);
			
			RedTelefonica red = new RedTelefonica(_localidades);
			
			red.borrarCamino("Gualeguay", "Entre Rios", "Concepción del Uruguay", "Entre Rios");
			Tupla<Integer, Integer> aux = new Tupla<Integer, Integer>(red.tengoLocalidad("Gualeguay", "Entre Rios"), red.tengoLocalidad("Concepción del Uruguay", "Entre Rios"));
			
			red.deshacerBorrarCamino();
			assertFalse(red.get_caminosProhibidos().contains(aux));
		}
		
		@Test
		public void localidadesVecinasTrue() {
			Localidad loc1 = new Localidad("Gualeguay", "Entre Rios", "40", "21");
			Localidad loc2 = new Localidad("Concepción del Uruguay", "Entre Rios", "45", "30");
			
			_localidades.add(loc1);
			_localidades.add(loc2);
			
			RedTelefonica red = new RedTelefonica(_localidades);
			
			assertTrue(red.localidadesVecinas("Gualeguay", "Entre Rios","Concepción del Uruguay", "Entre Rios"));
		}
		
		@Test
		public void localidadesVecinasFalse() {
			Localidad loc1 = new Localidad("Gualeguay", "Entre Rios", "40", "21");
			Localidad loc2 = new Localidad("Concepción del Uruguay", "Entre Rios", "45", "30");
			
			_localidades.add(loc1);
			_localidades.add(loc2);
			
			RedTelefonica red = new RedTelefonica(_localidades);
			red.sustituirCaminoEliminar("Gualeguay", "Entre Rios","Concepción del Uruguay", "Entre Rios");
			
			assertFalse(red.localidadesVecinas("Gualeguay", "Entre Rios","Concepción del Uruguay", "Entre Rios"));
		}
		
		@Test
		public void deshacerLocalidadEliminadaTest() {
			Localidad loc1 = new Localidad("Gualeguay", "Entre Rios", "40", "21");
			Localidad loc2 = new Localidad("Concepción del Uruguay", "Entre Rios", "45", "30");
			Localidad loc3 = new Localidad("CABA", "Buenos Aires","-34","-58");
			
			_localidades.add(loc1);
			_localidades.add(loc2);
			_localidades.add(loc3);
			
			RedTelefonica red = new RedTelefonica(_localidades);
			
			red.eliminarLocalidad("CABA", "Buenos Aires");
			red.deshacerEliminarLocalidad();
			
			assertTrue(red.getLocalidades().contains(loc3));
		}

		
		@Test
		public void costoLocalidadesMismaProvinciaCercaTrue() {
			Localidad loc1 = new Localidad("Gualeguay", "Entre Rios", "40", "21");
			Localidad loc2 = new Localidad("Concepción del Uruguay", "Entre Rios", "45", "30");
			
			_localidades.add(loc1);
			_localidades.add(loc2);
			
			RedTelefonica red = new RedTelefonica(_localidades);
			
			assertEquals(Math.round(red.calcularCosto(loc1, loc2)), 103);
		}
		
		@Test
		public void costoLocalidadesMismaProvinciaCercaFalse() {
			Localidad loc1 = new Localidad("Gualeguay", "Entre Rios", "40", "21");
			Localidad loc2 = new Localidad("Concepción del Uruguay", "Entre Rios", "46", "30");
			
			_localidades.add(loc1);
			_localidades.add(loc2);
			
			RedTelefonica red = new RedTelefonica(_localidades);

			assertNotEquals(Math.round(red.calcularCosto(loc1, loc2)), 103);
		}
		
		@Test
		public void costoLocalidadesMismaProvinciaLejosTrue() {
			Localidad loc1 = new Localidad("Gualeguay", "Entre Rios", "44", "72");
			Localidad loc2 = new Localidad("Concepción del Uruguay", "Entre Rios", "31", "15");
			
			_localidades.add(loc1);
			_localidades.add(loc2);
			
			RedTelefonica red = new RedTelefonica(_localidades);
			
			assertEquals(Math.round(red.calcularCosto(loc1, loc2)), 585);
		}
		
		@Test
		public void costoLocalidadesMismaProvinciaLejosFalse() {
			Localidad loc1 = new Localidad("Gualeguay", "Entre Rios", "44", "72");
			Localidad loc2 = new Localidad("Concepción del Uruguay", "Entre Rios", "31", "17");
			
			_localidades.add(loc1);
			_localidades.add(loc2);
			
			RedTelefonica red = new RedTelefonica(_localidades);

			assertNotEquals(Math.round(red.calcularCosto(loc1, loc2)), 585);
		}
		
		@Test
		public void costoLocalidadesDistintaProvinciaCercaTrue() {
			Localidad loc1 = new Localidad("Gualeguay", "Entre Rios", "40", "21");
			Localidad loc2 = new Localidad("Villa Fiorito", "Buenos Aires", "45", "30");
			
			_localidades.add(loc1);
			_localidades.add(loc2);
			
			RedTelefonica red = new RedTelefonica(_localidades);

			assertEquals(Math.round(red.calcularCosto(loc1, loc2)), 403);
		}
		
		@Test
		public void costoLocalidadesDistintaProvinciaCercaFalse() {
			Localidad loc1 = new Localidad("Gualeguay", "Entre Rios", "42", "21");
			Localidad loc2 = new Localidad("Villa Fiorito", "Buenos Aires", "45", "30");
			
			_localidades.add(loc1);
			_localidades.add(loc2);
			
			RedTelefonica red = new RedTelefonica(_localidades);
			assertNotEquals(Math.round(red.calcularCosto(loc1, loc2)), 403);
		}
		
		@Test
		public void costoLocalidadesDistintaProvinciaLejosTrue() {
			Localidad loc1 = new Localidad("Gualeguay", "Entre Rios", "90", "11");
			Localidad loc2 = new Localidad("Villa Fiorito", "Buenos Aires", "31", "15");

			_localidades.add(loc1);
			_localidades.add(loc2);
			
			RedTelefonica red = new RedTelefonica(_localidades);

			assertEquals(Math.round(red.calcularCosto(loc1, loc2)), 891);
		}
		
		@Test
		public void costoLocalidadesDistintaProvinciaLejosFalse() {
			Localidad loc1 = new Localidad("Gualeguay", "Entre Rios", "90", "11");
			Localidad loc2 = new Localidad("Villa Fiorito", "Buenos Aires", "32", "15");
			
			_localidades.add(loc1);
			_localidades.add(loc2);
			
			RedTelefonica red = new RedTelefonica(_localidades);
			assertNotEquals(Math.round(red.calcularCosto(loc1, loc2)), 891);
		}
		
		@Test
		public void costoTotalTrue() {
			
			Localidad loc1 = new Localidad("CABA", "Buenos Aires","-34","-58");
			Localidad loc2 = new Localidad("Rosario", "Santa Fe","-32","-60");
			Localidad loc3 = new Localidad("Rivadavia", "San Juan","-45","90");
			Localidad loc4 = new Localidad("Bariloche", "Neuquen","-55","2");
			
			_localidades.add(loc1);
			_localidades.add(loc2);
			_localidades.add(loc3);
			_localidades.add(loc4);
			
			RedTelefonica red = new RedTelefonica(_localidades);
			red.planificar();
			
			Double[][] arbolMinimo = {
					{null, 328.28, null, 935.68},
					{328.28, null, null, null},
					{null, null, null, 1185.08},
					{935.68, null, 1285.08, null},
			}; //ASI QUEDARIA EL GRAFO APLICANDO ARBOL GENERADOR RESOLVIENDO EL PROBLEMA A MANO -> COSTO TOTAL = 2.449,62
			
			assertEquals(red.obtenerCostoTotal(), (Double) 2449.62);	
		}
		
		@Test
		public void costoTotalFalse() {
			Localidad loc1 = new Localidad("CABA", "Buenos Aires","-34","-58");
			Localidad loc2 = new Localidad("Rosario", "Santa Fe","-32","-60");
			Localidad loc3 = new Localidad("Rivadavia", "San Juan","-45","90");
			Localidad loc4 = new Localidad("Bariloche", "Neuquen","-55","2");
			
			_localidades.add(loc1);
			_localidades.add(loc2);
			_localidades.add(loc3);
			_localidades.add(loc4);
			
			RedTelefonica red = new RedTelefonica(_localidades);
			red.planificar();

			Double[][] arbolMinimo = {
					{null, 328.28, null, 935.68},
					{328.28, null, null, null},
					{null, null, null, 1185.08},
					{935.68, null, 1285.08, null},
			};
			
			assertNotEquals(red.obtenerCostoTotal(), (Double) 2500.0);			
		}		
		
}
