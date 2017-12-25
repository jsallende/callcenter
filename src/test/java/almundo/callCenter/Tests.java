package almundo.callCenter;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.PriorityBlockingQueue;

import org.junit.Test;

import almundo.callCenter.models.Empleado;
import almundo.callCenter.models.Llamada;
import almundo.callCenter.models.Rango;

public class Tests{
	
	
	//Main Test Cases
	@Test
	public void test10LlamadosConcurrentes() throws InterruptedException {
		PriorityBlockingQueue<Empleado> lista = crearListaDeEmpleados();
		Dispatcher dispatcher = new Dispatcher(10,lista);
		
		for(int i = 0; i<10; i++) {
			dispatcher.dispatchCall(new Llamada());
		}
		
		int llamadasAtendidas = dispatcher.clockOut(15);
		
		assertEquals("Se perdieron llamadas!",10,llamadasAtendidas);
	}
	
	@Test
	public void test20LlamadosConcurrentes() throws InterruptedException {
		PriorityBlockingQueue<Empleado> lista = crearListaDeEmpleados();
		Dispatcher dispatcher = new Dispatcher(10,lista);
		
		for(int i = 0; i<20; i++) {
			dispatcher.dispatchCall(new Llamada());
		}
		
		int llamadasAtendidas = dispatcher.clockOut(25);
		
		assertEquals("Se perdieron llamadas!",10,llamadasAtendidas);
	}
	
	@Test
	public void testMasEmpleadosQueThreads() throws InterruptedException {
		PriorityBlockingQueue<Empleado> lista = crearListaDeEmpleados();
		Dispatcher dispatcher = new Dispatcher(5,lista);
		
		for(int i = 0; i<10; i++) {
			dispatcher.dispatchCall(new Llamada());
		}
		
		int llamadasAtendidas = dispatcher.clockOut(30);
		
		assertEquals("Se perdieron llamadas!",10,llamadasAtendidas);
	}
	
	@Test
	public void testLlamadaAbandonada() throws InterruptedException {
		PriorityBlockingQueue<Empleado> lista = crearListaDeEmpleados();
		Dispatcher dispatcher = new Dispatcher(5,lista);
		
		for(int i = 0; i<5; i++) {
			dispatcher.dispatchCall(new Llamada());
		}
		dispatcher.dispatchCall(new Llamada(20));
		
		int llamadasAtendidas = dispatcher.clockOut(10);
		
		assertEquals("Se perdieron llamadas!",6,llamadasAtendidas);
	}
	
	//empleado tests
	@Test
	public void testEmpleadoComparable() {
		Empleado director = new Empleado(Rango.DIRECTOR);
		Empleado operador = new Empleado(Rango.OPERADOR);
		
		int resultado = director.compareTo(operador);
		assertEquals("Comparable no funciona",1,resultado);
	}
	
	@Test
	public void testColaEmpleadosPrioridad() throws InterruptedException {
		Empleado director = new Empleado(Rango.DIRECTOR);
		Empleado operador = new Empleado(Rango.OPERADOR);
		
		PriorityBlockingQueue<Empleado> lista = new PriorityBlockingQueue<Empleado>();
		lista.add(director);
		lista.add(operador);
		
		Empleado elqueagarre = lista.take();
		System.out.println(elqueagarre);
		assertEquals("Agarraste el que no es!",new Integer(0),elqueagarre.getRango().getVal());
	}
	
	@Test
	public void testEmpleadoRecibeLlamada() {
		Empleado emp = new Empleado(Rango.DIRECTOR);
		emp.atender(new Llamada(2));
	}
	
	@Test
	public void testConDispatcher() throws InterruptedException {
		PriorityBlockingQueue<Empleado> lista = new PriorityBlockingQueue<Empleado>();
		lista.add(new Empleado(Rango.DIRECTOR));
		lista.add(new Empleado(Rango.OPERADOR));
		Dispatcher dis = new Dispatcher(10,lista);
		dis.dispatchCall(new Llamada(3));
		dis.dispatchCall(new Llamada(5));
		dis.dispatchCall(new Llamada(2));
		dis.dispatchCall(new Llamada(4));
		dis.dispatchCall(new Llamada(7));
		dis.dispatchCall(new Llamada(2));
		dis.dispatchCall(new Llamada(1));
		dis.dispatchCall(new Llamada(4));
		dis.dispatchCall(new Llamada(3));
		dis.clockOut(25);
	}
	
	private PriorityBlockingQueue<Empleado> crearListaDeEmpleados(){
		PriorityBlockingQueue<Empleado> lista = new PriorityBlockingQueue<Empleado>();

		//7 operadores
		lista.add(new Empleado(Rango.OPERADOR,"Wedge Antilles"));
		lista.add(new Empleado(Rango.OPERADOR,"Lando Calrissian"));
		lista.add(new Empleado(Rango.OPERADOR,"Biggs Darklighter"));
		lista.add(new Empleado(Rango.OPERADOR,"Nien Nunb"));
		lista.add(new Empleado(Rango.OPERADOR,"Chewbacca"));
		lista.add(new Empleado(Rango.OPERADOR,"C3PO"));
		lista.add(new Empleado(Rango.OPERADOR,"Jek Porkins"));
		
		//2 supervisores
		lista.add(new Empleado(Rango.SUPERVISOR,"Gial Ackbar"));
		lista.add(new Empleado(Rango.SUPERVISOR,"Jan Dodonna"));
		
		//1 director
		lista.add(new Empleado(Rango.DIRECTOR,"Mon Mothma"));
		
		return lista;
	}
}
