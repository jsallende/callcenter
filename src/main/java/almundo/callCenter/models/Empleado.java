package almundo.callCenter.models;

import java.util.logging.Logger;

public class Empleado implements Comparable<Empleado> {

	private final static Logger LOGGER = Logger.getLogger(Empleado.class.getName());
	private Rango rango;
	private String nombre;
	
	public Empleado(Rango rango) {
		this.rango = rango;
	}
	
	public Empleado(Rango rango, String nombre) {
		this.rango = rango;
		this.nombre = nombre;
	}
	
	public void setNombre(String nom) {
		this.nombre = nom;
	}
	
	public String getNombre() {
		if(nombre != null) {
			return nombre;
		}else {
			return "SinNombre";
		}
	}
	
	public Rango getRango() {
		return this.rango;
	}
	
	public int compareTo(Empleado otro) {
		return this.rango.getVal().compareTo(otro.rango.getVal());
	}
	
	public void atender(Llamada ll) {
	
		LOGGER.info(this.toString()+" va a atender una llamada de "+ll.getDuracion()+" segundos de duración");
		try {
			Thread.sleep(ll.getDuracion() * 1000);
		} catch (InterruptedException e) {
			LOGGER.warning(this.toString()+": Ey! Me apagaron el teléfono! =(");
		}
		LOGGER.info(this.toString()+" acaba de terminar su llamada");
	
	}
	
	@Override
	public String toString() {
		return this.getNombre() + " ("+this.rango.getDescripcion()+")";
	}

}
