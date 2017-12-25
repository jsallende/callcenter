package almundo.callCenter;

import java.util.concurrent.PriorityBlockingQueue;

import almundo.callCenter.models.Empleado;
import almundo.callCenter.models.Llamada;
import almundo.callCenter.models.Rango;

public class App 
{
    public static void main( String[] args ) throws InterruptedException
    {
        System.out.println("================CALL CENTER SIMULATOR================");
        
        //Instanciando estructuras
        
        PriorityBlockingQueue<Empleado> lista = crearListaDeEmpleados();
        Dispatcher dis = new Dispatcher(10, lista);
        
        for(int i = 0; i<10 ; i++) {
        		dis.dispatchCall(new Llamada());
        }
        
        dis.clockOut(15);
    }
    
    private static PriorityBlockingQueue<Empleado> crearListaDeEmpleados(){
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
