package auxiliares;

import static org.junit.Assert.*;

import org.junit.Test;

@SuppressWarnings("unused")
public class TestsLocalidades {
	
		@Test
		public void crearLocalidad() {
			Localidad loc = new Localidad("Concepción del Uruguay", "Entre Rios", "51", "4");
		}

		@Test (expected = IllegalArgumentException.class)
		public void localilidadLatitudInvalida() {
			Localidad loc = new Localidad("Concepción del Uruguay", "Entre Rios", "-91", "4");
		}
			
		@Test (expected = IllegalArgumentException.class)
		public void localidadLongitudInvalida() {
			Localidad loc = new Localidad("Concepción del Uruguay", "Entre Rios", "23", "91");
		}
			
		@Test
		public void localidadesIguales() {
			Localidad loc1 = new Localidad("Concepción del Uruguay", "Entre Rios", "23", "89");
			Localidad loc2 = new Localidad("Concepción del Uruguay", "Entre Rios", "23", "89");
			assertTrue(loc1.equals(loc2));
		}
			
		@Test
		public void localidadesDistintas() {
			Localidad loc1 = new Localidad("Concepción del Uruguay", "Entre Rios", "23", "90");
			Localidad loc2 = new Localidad("Concepción del Uruguay", "Entre Rios", "22", "89");
			assertFalse(loc1.equals(loc2));
		}
}
