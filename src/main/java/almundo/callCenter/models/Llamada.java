package almundo.callCenter.models;

import java.util.Random;

public class Llamada {

	private static final Integer minimaDuracion = 5;
	private static final Integer maximaDuracion = 10;
	
	private Integer duracion;
	
	public Llamada(Integer duracion) {
		this.duracion = duracion;
	}
	
	public Llamada() {
		this.duracion = calcularDuracionRandom();
	}
	
	public Integer getDuracion() {
		return this.duracion;
	}
	
	private Integer calcularDuracionRandom() {
		Random rn = new Random();
		int range = maximaDuracion - minimaDuracion + 1;
		int randomNum =  rn.nextInt(range) + minimaDuracion;
		return randomNum;
	}
}
