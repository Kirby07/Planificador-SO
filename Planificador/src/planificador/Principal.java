package planificador;

/**
 *
 * @author HP
 */
import java.util.Scanner;

public class Principal {
	
	
	public static void main (String [] args) {
		//String ruta = "C:\\Users\\HP\\Documents\\NetBeansProjects\\Planificador\\profe.txt";	//ingresar ruta
                String ruta = "C:\\Users\\HP\\Documents\\NetBeansProjects\\Planificador\\EJEMPLO.txt";
		planificador pla = new planificador(ruta);
		try (Scanner entrada = new Scanner(System.in)) {
			int op1, op2,canales;
			System.out.println("programa simulador de un planificador de procesos");
			System.out.println("(1) monotarea");
			System.out.println("(2) multitarea");
			System.out.println("eleccione una opcion: ");
			op1 = entrada.nextInt();
			if(op1 == 1) {
				System.out.println("(1) tiempo llegada, tiempo exe, prioridadalta, pid menor");
				System.out.println("(2) tiempo llegada, tiempo exe, prioridadalta, pid meayor");
				System.out.println("(3) prioridad alta, llegada primero, menor ejecucion, pid menor");
				System.out.println("(4) prioridad baja, llegada primero, menor ejecucion, pid menor");
				System.out.println("elija algoritmo:");
				op2 = entrada.nextInt();
				pla.monotarea(op2);
			}
			else {
				System.out.println("numero canales:");
				canales = entrada.nextInt();
				System.out.println("(1) tiempo llegada, tiempo exe, prioridadalta, pid menor");
				System.out.println("(2) tiempo llegada, tiempo exe, prioridadalta, pid meayor");
				System.out.println("(3) prioridad alta, llegada primero, menor ejecucion, pid menor");
				System.out.println("(4) prioridad baja, llegada primero, menor ejecucion, pid menor");
				System.out.println("seleccione una opcion:");
				op2 = entrada.nextInt();
				pla.multitarea(canales, op2);
			}
		}
	}
}
