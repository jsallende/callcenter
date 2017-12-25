# Almundo Call Center

## Consigna

Exite un call center donde hay 3 tipos de empleados: operador, supervisor y director. El proceso de la atención de una llamada telefónica en primera instancia debe ser atenida por un operador, si no hay ninguno libre debe ser atendida por un supervisor, y de no haber tampoco supervisores libres debe ser atendida por un director.

### Requerimientos

- Diseñar el modelado de clases y diagramas UML necesarios para documentar y comunicar el diseño.
- Debe existir una clase *Dispatcher* encargada de manejar las llamadas, y debe contener el método *dispatchCall* para que las asigne a los empleados disponibles.
- La clase Dispatcher debe tener la capacidad de poder procesar 10 llamadas al mismo tiempo (de modo concurrente).
- Cada llamada puede durar un tiempo aleatorio entre 5 y 10 segundos.
- Debe tener un test unitario donde lleguen 10 llamadas.

### Extras/Plus

- Dar alguna solución sobre qué pasa con una llamada cuando no hay ningún empleado libre.
- Dar alguna solución sobre qué pasa con una llamada cuando entran más de 10 llamadas concurrentes.
- Agregar los tests unitarios que se crean convenientes.
- Agregar documentación de código.

### Tener en cuenta

- El proyecto debe ser creado con Maven
- De ser necesario, anexar un documento con la explicación de cómo. por qué resolvió los puntos extras, o comentarlo en las clases donde se encuentran sus tests unitarios respectivos.

## Solución

El primer objetivo a la hora de encarar la solución fue buscar estructuras y clases preexistentes en las librerías de Java que pudieran solucionar la mayor parte de los problemas en cuestión. De este modo di con la estrategia de utilizar [ExecutorService](https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ExecutorService.html) en combinación con [PriorityBlockingQueue](https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/PriorityBlockingQueue.html).

Por un lado el uso de **ExecutorService** me permitiría manejar debidamente la concurrencia (requerimiento de manejar 10 llamadas concurrentes), y por el otro, utilizar **PriorityBlockingQueue** me permitiría tanto resolver el extra de llamadas en espera sin empleados disponibles, como manejar la priorización en la elección del empleado que tomaría la llamada.

Las clases utilizadas son bastante sencillas:

#### Empleado

La clase Empleado sirve para modelar a todos los empleados del Call Center. Cada empleado tiene un nombre (String) y un rango. Si bien hubiera tenido sentido crear una superclase Empleado y que los distintos tipos de empleados heredaran de esta, la complejidad del ejercicio no dio a la necesidad de hacerlo, dado que no hay diferencias intrínsecas entre los distintos tipos de empleados, más que el nivel de prioridad que estos suponen. Cada empleado tiene únicamente un método *atenderLlamada* que recibe como parámetro una llamada, y la forma de simular que el empleado toma la llamada es ejecutar un Thread.sleep() por la duración de la llamada. Para que los Empleados pudieran ser utilizados en la PriorityBlockingQueue, se los hizo extender de Comparable, y se les incluyó un método compareTo(Empleado e) que los compara por el valor numérico de la prioridad de su rango.

#### Llamada

La llamada tiene únicamente el atributo de duración en segundos, pero para poder hacer distintas pruebas se desarrollaron dos constructores. En uno, sin parámetros, se ejecuta un llamado a calcularDuracionRandom(), que toma las cotas inferior y superior de duración de llamada definidas por la consigna, y devuelve cualquier número entre ellas. En el otro, podemos especificar cualquier duración en enteros. *Nota: en un caso de mundo real correspondería agregar una validación de números positivos mayores que 0*.

#### Dispatcher

Esta es la clase que orquesta la operatoria del callCenter. Por un lado, posee un constructor en el que enviamos por parámetro la cantidad de llamados concurrentes que queremos que pueda procesar, y la lista de empleados. Luego encontramos el método dispatchCall(Llamada ll). Este método es donde se encuentra el grueso del manejo de la concurrencia y priorización de empleados, en 2 líneas de código muy importantes.

```java
Empleado emp = lista.take();
```
*El método take() de la priorityBlockingQueue en principio va a tomar en cuenta el resultado del compareTo de los empleados para elegir a cuál tomar. Por otra parte, también interrumpirá el curso del hilo hasta que pueda encontrar a un empleado disponible (frena la ejecución si la lista está vacía).*

```java
ex.submit(asignarLlamada(emp,ll));
```
*Al ExecutorService se le envía la tarea de asignarLlamada, enviándole como parámetro el empleado obtenido en la línea anterior, y la llamada recibida. La tarea asignarLlamada es un Runnable, que llamará al método atenderLlamada del Empleado en cuestión, y luego de finalizada la llamada volverá a insertarlo en la PriorityBlockingQueue.*

Por último, el Dispatcher cuenta con otro método igualmente importante, que es el clockOut(long timeout). Este método es la señal de finalización del programa, que sirve para manejar correctamente la finalización de los hilos ocupados. Primero se envía una señal de *shutDown()* al ExecutorService. Esto hará que el servicio no tome más tareas. Luego se ingresa en un bucle while(true) donde se llama a otro método de ExecutorService, que es *awaitTermination(timeout,timeunit)*, mediante el cual esperamos una cantidad definida de tiempo a que los hilos ocupados terminen su ejecución. Finalmente pasado ese tiempo, o cuando finalicen todos los procesos (lo que ocurra primero), el programa finaliza su ejecución imprimiendo por log la cantidad de llamadas atendidas.

#### App.java

Se incluye para aislarlo de los casos de Test, un ejecutable App.java que instanciará y resolverá el caso base de operatoria del callCenter: 10 llamadas de duración aleatoria entre las cotas, atendidas por 10 empleados del callCenter.

### TESTS

Además de ciertos tests incluidos para verificar el correcto funcionamiento por ejemplo de la priorización de los empleados, o del método atenderLlamada, se incluyen 4 tests particularmente importantes para el análisis de un par de escenarios característicos, a saber:

- **test10LlamadosConcurrentes**: Es el caso base solicitado en la consigna, y réplica de la ejecución de App.java.
- **test20LlamadosConcurrentes**: En este caso el tamaño del pool de Threads del ExecutorService es igual al tamaño de la lista de empleados, por lo que se toman 10 llamados instantáneamente al iniciar el test, y los otros 10 se van tomando a medida que se liberan tanto empleados como threads.
- **testMasEmpleadosQueThreads**: En este test la lista de empleados sigue siendo de 10, pero el callCenter tiene capacidad para tomar 5 llamados concurrentes. Es importante en este caso notar 2 comportamientos clave. Por un lado, que por más que haya empleados disponibles, el ExecutorService no puede atender más de 5 requests a la vez. Por otro lado, dada esta restricción, nunca un Supervisor ni el Director tomarán llamados (en el ejemplo hay 7 Operadores).
- **testLlamadaAbandonada**: En este test se envían en total 6 llamadas a un ExecutorService con capacidad para 5, siendo la última de una duración superior al timeout especificado en el clockOut. De este modo se puede verificar que esta llamada nunca llegará a completarse.
