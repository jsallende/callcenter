package almundo.callCenter.models;

public enum Rango {

	OPERADOR(0, "Operador"), SUPERVISOR(1, "Supervisor"), DIRECTOR(2, "Director");
	
	private Integer valor;
	private String descripcion;
	
	public Integer getVal() {
		return this.valor;
	}
	
	public String getDescripcion() {
		return this.descripcion;
	}
	
	Rango(Integer val, String desc){
		this.valor = val;
		this.descripcion = desc;
	}
}
