package planificador;

/**
 *
 * @author HP
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class planificador {
	
	private lista lista;
	private cola_d cola = new cola_d();
	private lista sublista;
	private ArrayList<proceso> nucleo;
	private int num_pro;
	private boolean finalizar;
	private int tiempopromedio = 0;

	public planificador (String ruta) {
		this.num_pro = 0;
		this.finalizar = false;
		
		int contador = 0;
		int [] procesos = null;
		try {
			BufferedReader bf = null;
			String temporal = "";
			String numero = "";
			int numero_pids = 0;
			
			bf = new BufferedReader(new FileReader(ruta));		//INSTANCIAMOS EL BUFFER PARA LEER EL ARCHIVO
			bf.readLine();
			while(bf.readLine() != null)	numero_pids ++;		//OBTENEMOS EL NUMERO DE PROCESOS EXISTENTES
			bf.close();
			bf = new BufferedReader(new FileReader(ruta));
			bf.readLine();
			
			procesos = new int [numero_pids*4];
			
			while((temporal = bf.readLine()) != null) {
				temporal = temporal.replace("	", "/");
				for(int i=0; i<temporal.length(); i++) {
					if(temporal.charAt(i) != '/') {
						numero += temporal.charAt(i);
					}
					else{
						if(numero != "") {
							procesos [contador] = Integer.parseInt(numero);
							contador++;
						}
						numero = "";
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Archivo no encontrado, verefique ruta");
		} catch (IOException e) {
			System.out.println("No se puede leer el archivo.");
		}
		
		contador = 0;
		lista = new lista();
		for(int i=0; i<procesos.length; i+=4) {
			lista.Agregar_Proceso(procesos[i], procesos[i+1], procesos[i+2], procesos[i+3]);
			num_pro++;
			contador ++;
		}
	}
	
	public void monotarea (int algoritmo) {
		proceso core = null;
		int  procesos_ejecutados = 0;
		int tiempo = 0;
		while(true) {
			if(algoritmo != 3 && algoritmo !=4)	{
				sublista = new lista();
				sublista(tiempo);
				core = ej_mono(algoritmo);
			}
			else {
				if(algoritmo == 3) {
					core = al3();
				}
				else {
					core = al4();
				}
			}
			if(core != null) {
				core.setExe(core.getExe()-1);
				if(core.getExe() == 0)	{
					if(algoritmo == 3 || algoritmo == 4)	core.setSeleccionado(true);
					procesos_ejecutados ++;
					core.setFinalizado(true);
					core.setTiempofinalizado(tiempo+1);
					core.setTiempoespera(core.getTiempofinalizado() - (core.getTiempollegada() + core.getTiempoeje()));
					tiempopromedio += core.getTiempoespera();
				}
				else {
					if(algoritmo != 3 && algoritmo != 4)	cola.encolar(core.getPid(), core.getLlegada(), core.getExe(), core.getPrioridad());
				}
				if(core.isFinalizado()) {
					System.out.println("procesador: \t\ttiempo:"+tiempo+"  \t\tproceso:"+core.getPid()+"\t tiempo finalizado:"+core.getTiempofinalizado());
				}
				else {
					System.out.println("procesador: \t\ttiempo:"+tiempo+"  \t\tproceso:"+core.getPid());
				}
			}
			else {
				core = cola.Desencolar();
				if(core != null) {
					core.setExe(core.getExe()-1);
					if(core.getExe() == 0)	{
						procesos_ejecutados ++;
						core.setFinalizado(true);
						core.setTiempofinalizado(tiempo+1);
						core.setTiempoespera(core.getTiempofinalizado() - (core.getTiempollegada() + core.getTiempoeje()));
						tiempopromedio += core.getTiempoespera();
					}
					else {
						cola.encolar(core.getPid(), core.getLlegada(), core.getExe(), core.getPrioridad());
					}
					if(core.isFinalizado()) {
						System.out.println("procesador: \t\ttiempo:"+tiempo+"  \t\tproceso:"+core.getPid()+"\t tiempo finalizado:"+core.getTiempofinalizado());
					}
					else {
						System.out.println("procesador: \t\ttiempo:"+tiempo+"  \t\tproceso:"+core.getPid());
					}
				}
				else {
					if (procesos_ejecutados == num_pro) 	break;
					System.out.println("procesador:  \ttiempo:"+tiempo);
				}
			}
			tiempo++;
			sublista = null;
		}
		System.out.println("Tiempo: "+ tiempopromedio);
		System.out.println("Tiempo promedio de espera: "+(tiempopromedio/num_pro));
	}
	
	private void sublista(int tiempo) {
		proceso aux = lista.Lista();
		while(aux!= null) {
			if(aux.getLlegada() == tiempo) {
				sublista.Agregar_Proceso(aux.getPid(), aux.getLlegada(), aux.getExe(), aux.getPrioridad());
			}
			aux= aux.get_Siguiente();
		}	
	}
	
	private proceso ej_mono (int algoritmo) {
		proceso proceso = null, aux2=null,  aux = null;
		aux = sublista.Lista();
		if(sublista.Num_Procesos() > 1) {
			for(int i=0; i<sublista.Num_Procesos(); i++) {
				if(i == 0) {
					if(algoritmo == 1)			proceso = alg1();		//llega, exe, pri, pid menor
					else if(algoritmo == 2)		proceso = al2();		//lle, exe, pri, pid mayor
				}
				else {
					if(algoritmo == 1)			aux2 = alg1();
					else if(algoritmo == 2)		aux2 = al2();
					cola.encolar(aux2.getPid(), aux2.getLlegada(), aux2.getExe(), aux2.getPrioridad());
				}
			}
		}
		else {
			if(sublista.Num_Procesos() == 1)	{
				proceso = aux;
			}
			else {
				sublista = null;
				return null;
			}
		}
		sublista = null;
		return proceso;
	}

	public void multitarea(int nucleos, int algoritmo) {
		proceso cpu = null;
		int pro_eje = 0;
		int tiempo = 0;
		while(true) {
			if(algoritmo != 3 && algoritmo != 4) {
				sublista= new lista();
				sublista(tiempo);
			}
			nucleo = new ArrayList<proceso>();
			
			for(int j=0; j<nucleos; j++) {
				if(algoritmo !=4 && algoritmo !=3)	{
					cpu = ejemult(algoritmo);
					if(tiempo != 1 && cpu == null) {
						cpu = cola.Desencolar();
						if(cpu != null && cpu.getExe() == 1)	pro_eje++;
						nucleo.add(cpu);
					}
					else {
						if(cpu != null && cpu.getExe() == 1)	pro_eje ++;
						nucleo.add(cpu);
					}
				}
				else{
					if(algoritmo==3) {
						cpu = al3();
					}
					else if(algoritmo == 4){
						cpu = al4();
					}
					if (cpu != null)	cpu.setSeleccionado(true);
					nucleo.add(cpu);
				}
			}
			if(algoritmo != 3 && algoritmo != 4) {
				cpu = ejemult(algoritmo);
				while(cpu != null) {
					cola.encolar(cpu.getPid(), cpu.getLlegada(), cpu.getExe(), cpu.getPrioridad());
					cpu = ejemult(algoritmo);
				}
			}
			for(int j=0; j<nucleos; j++) {
				cpu = nucleo.get(j);
				if(cpu != null) {
					
					cpu.setExe(cpu.getExe() - 1);
					if(cpu.getExe() == 0) {
						cpu.setFinalizado(true);
						cpu.setTiempofinalizado(tiempo+1);
						cpu.setTiempoespera(cpu.getTiempofinalizado() - (cpu.getTiempollegada() + cpu.getTiempoeje()));
						tiempopromedio += cpu.getTiempoespera();
						if(algoritmo ==3 || algoritmo == 4)	{
							pro_eje++;
							cpu.setSeleccionado(true);
							cpu.setFinalizado(true);
							cpu.setTiempofinalizado(tiempo+1);
							cpu.setTiempoespera(cpu.getTiempofinalizado() - (cpu.getTiempollegada() + cpu.getTiempoeje()));
							tiempopromedio += cpu.getTiempoespera();
						}
					}
					else {
						if (algoritmo != 3 && algoritmo != 4)	cola.encolar(cpu.getPid(), cpu.getLlegada(), cpu.getExe(), cpu.getPrioridad());
						else	cpu.setSeleccionado(false);
					}
					if(cpu.isFinalizado()) {
						System.out.println("procesador: "+(j+1)+" \t\ttiempo:"+tiempo+"  \t\tproceso:"+cpu.getPid()+"\t tiempo finalizado:"+cpu.getTiempofinalizado());
					}
					else {
						System.out.println("procesador: "+(j+1)+" \t\ttiempo:"+tiempo+"  \t\tproceso:"+cpu.getPid());
					}
					
				}
				else {
					if(pro_eje == num_pro)	{
						finalizar = true;
						break;
					}
					System.out.println("procesador: "+(j+1)+"  \ttiempo:"+tiempo);
				}
			}
			if(finalizar == true)	break;
			tiempo++;
			sublista = null;
			nucleo = null;
		}
		System.out.println("Tiempo: "+tiempopromedio);
		System.out.println("Tiempo promedio de espera : "+(tiempopromedio/num_pro));
	}
		
	private proceso ejemult (int algoritmo) {
		proceso proceso = null, aux = null;
		aux = sublista.Lista();
		if(sublista.Num_Procesos() > 1) {
			if(algoritmo == 1)			proceso = almu1();		//LLEGADA, EXECUCION, PRIORIDAD, PID MENOR
			else if(algoritmo == 2)		proceso = almul2();		//LLEGADA, EXECUCION, PRIORIDAD, PID MAYOR
			else if(algoritmo == 3)		proceso = al3();			//prior alta, LLEGADA PRIMERO, EXECUCION BAJA, PID MENOR
			else if(algoritmo == 4)		proceso = al4();			//priorida baja, LLEGADA PRIMERO, EXECUCION BAJA, PID MENOR
		}
		else {
			if(sublista.Num_Procesos() == 1)	{
				proceso = aux;
				if(proceso.isSeleccionado())	return null;
				else	proceso.setSeleccionado(true);
			}
			else {
				return null;
			}
		}
		return proceso;
	}

	private proceso alg1 () {
		proceso aux, proceso;
		proceso = sublista.Lista();
		aux = sublista.Lista().get_Siguiente();
		
		while(proceso.isSeleccionado()) {
			proceso = proceso.get_Siguiente();
		}
		
		while(aux != null) {
			if(!aux.isSeleccionado()) {
				if(aux.getExe() < proceso.getExe()) {
					proceso = aux;
				}
				else if(aux.getExe() == proceso.getExe()) {	
					if(aux.getPrioridad() > proceso.getPrioridad()) {
						proceso = aux;
					}
					else if(aux.getPrioridad() == proceso.getPrioridad()) {	
						if(aux.getPid() < proceso.getPid()) {		//pid menor
							proceso = aux;
						}
					}
				}
			}
			aux = aux.get_Siguiente();
		}
		proceso.setSeleccionado(true);
		return proceso;
	}
	
	private proceso al2 () {
		proceso aux, proceso;
		proceso = sublista.Lista();
		aux = sublista.Lista().get_Siguiente();
		
		while(proceso.isSeleccionado()) {
			proceso = proceso.get_Siguiente();
		}
		
		while(aux != null) {
			if(!aux.isSeleccionado()) {
				if(aux.getExe() < proceso.getExe()) {
					proceso = aux;
				}
				else if(aux.getExe() == proceso.getExe()) {	
					if(aux.getPrioridad() > proceso.getPrioridad()) {
						proceso = aux;
					}
					else if(aux.getPrioridad() == proceso.getPrioridad()) {	
						if(aux.getPid() > proceso.getPid()) {		//pid mayor
							proceso = aux;
						}
					}
				}
			}
			aux = aux.get_Siguiente();
		}
		proceso.setSeleccionado(true);
		return proceso;
	}
	
	private proceso almu1 () {
		proceso aux, proceso;
		proceso = sublista.Lista();
		aux = sublista.Lista().get_Siguiente();
		
		while(proceso != null) {
			if(proceso.isSeleccionado())	proceso = proceso.get_Siguiente();
			else break;
		}
		
		if(proceso == null) {
			return null;
		}
		else {
			while(aux != null) {
				if(!aux.isSeleccionado()) {
					if(aux.getExe() < proceso.getExe()) {
						proceso = aux;
					}
					else if(aux.getExe() == proceso.getExe()) {	
						if(aux.getPrioridad() > proceso.getPrioridad()) {
							proceso = aux;
						}
						else if(aux.getPrioridad() == proceso.getPrioridad()) {	
							if(aux.getPid() < proceso.getPid()) {		//pid menor
								proceso = aux;
							}
						}
					}
				}
				aux = aux.get_Siguiente();
			}
			proceso.setSeleccionado(true);
			return proceso;
		}
	}
	
	
	private proceso almul2 () {
		proceso aux, proceso;
		proceso = sublista.Lista();
		aux = sublista.Lista().get_Siguiente();
		
		while(proceso != null) {
			if(proceso.isSeleccionado())	proceso = proceso.get_Siguiente();
			else break;
		}
		
		if(proceso == null) {
			return null;
		}
		else {
			while(aux != null) {
				if(!aux.isSeleccionado()) {
					if(aux.getExe() < proceso.getExe()) {
						proceso = aux;
					}
					else if(aux.getExe() == proceso.getExe()) {	
						if(aux.getPrioridad() > proceso.getPrioridad()) {
							proceso = aux;
						}
						else if(aux.getPrioridad() == proceso.getPrioridad()) {	
							if(aux.getPid() > proceso.getPid()) {		//pid mayor
								proceso = aux;
							}
						}
					}
				}
				aux = aux.get_Siguiente();
			}
			proceso.setSeleccionado(true);
			return proceso;
		}
	}
	
	
	private proceso al3 () {
		proceso aux, proceso;
		proceso = lista.Lista();
		aux = lista.Lista().get_Siguiente();
		while(proceso.isSeleccionado()) {
			proceso = proceso.get_Siguiente();
			if (proceso == null)	return null;
		}
		
		while(aux != null) {
			if(!aux.isSeleccionado()) {
				if(aux.getPrioridad() > proceso.getPrioridad()) {
					proceso = aux;
				}
				else if(aux.getPrioridad() == proceso.getPrioridad()) {
					if(aux.getLlegada() < proceso.getLlegada()) {
						proceso = aux;
					}
					else if(aux.getLlegada() == proceso.getLlegada()) {	
						if(	aux.getExe() < proceso.getExe()) {
							proceso = aux;
						}
						else if(aux.getExe() == proceso.getExe()) {	
							if(aux.getPid() < proceso.getPid()) {
								proceso = aux;
							}
						}
					}
				}
			}
			aux = aux.get_Siguiente();
		}
		return proceso;
	}
	
	
	private proceso al4 () {
	proceso aux, proceso;
	proceso = lista.Lista();
	aux = lista.Lista().get_Siguiente();
	while(proceso.isSeleccionado()) {
		proceso = proceso.get_Siguiente();
		if (proceso == null)	return null;
	}
	
	while(aux != null) {
		if(!aux.isSeleccionado()) {
			if(aux.getPrioridad() < proceso.getPrioridad()) {
				proceso = aux;
			}
			else if(aux.getPrioridad() == proceso.getPrioridad()) {
				if(aux.getLlegada() < proceso.getLlegada()) {
					proceso = aux;
				}
				else if(aux.getLlegada() == proceso.getLlegada()) {	
					if(	aux.getExe() < proceso.getExe()) {
						proceso = aux;
					}
					else if(aux.getExe() == proceso.getExe()) {	
						if(aux.getPid() < proceso.getPid()) {
							proceso = aux;
						}
					}
				}
			}
		}
		aux = aux.get_Siguiente();
	}
	return proceso;
	}
}
