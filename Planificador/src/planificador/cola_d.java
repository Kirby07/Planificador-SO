package planificador;

/**
 *
 * @author HP
 */
public class cola_d {
	
	private proceso lista;
	private int procesos;
	
	public cola_d() {
		lista = null;
		procesos = 0;
	}
	
	public void encolar(int pid, int llegada, int exe, int prioridad) {
		proceso nodo = new proceso(pid, llegada, exe, prioridad);
		if(lista == null) {
			lista = nodo;
		}
		else {
			proceso aux = lista;
			nodo.set_Siguiente(aux);
			lista = nodo;
		}
		procesos ++;
	}
	
	public proceso Desencolar() {
		if(!Vacio()) {
			proceso aux, proceso;
			aux = lista;
			if(procesos > 1) {
				int i=1;
				while(i < (procesos-1))	{
					aux = aux.get_Siguiente();
					i++;
				}
				proceso = aux.get_Siguiente();
				aux.set_Siguiente(null);
				proceso.set_Siguiente(null);
				procesos--;
				return proceso;
			}
			else {
				aux.set_Siguiente(null);
				procesos --;
				lista = null;
				return aux;
			}
		}
		return null;
	}
	
	public boolean Vacio() {
		if(lista == null) return true;
		else	return false;
	}

}