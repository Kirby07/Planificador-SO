/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planificador;

/**
 *
 * @author HP
 */
public class lista {
	
	private proceso lista;
	private int num_procesos;
	
	public lista() {
		this.lista = null;
		this.num_procesos = 0;
	}
	
	public void Agregar_Proceso (int pid, int llegada, int exe, int prioridad) {
		proceso proceso = new proceso(pid, llegada, exe, prioridad);
		if (lista == null) {
			lista = proceso;
		}
		else {
			proceso aux = lista;
			proceso.set_Siguiente(aux);
			lista = proceso;
		}
		this.num_procesos ++;
	}
	
	public proceso Lista() {
		return lista;
	}
	
	public boolean Vacio() {
		if(lista == null)	return true;
		else	return false;
	}
	
	public int Num_Procesos () {
		return this.num_procesos;
	}

}

class proceso {
	
	private int pid;
	private int tiempo_llegada;
	private int tiempo_exe;
	private int prioridad;
	private boolean finalizado = false;
	private boolean seleccionado = false;
	private int tiempofinalizado = 0;
	private  int tiempoespera = 0;
	private int tiempollegada = 0;
	private int tiempoeje = 0;
	private proceso si = null;
	
	public proceso(int pid, int llegada, int exe, int prioridad) {
		this.pid = pid;
		this.tiempo_llegada = llegada;
		this.tiempo_exe = exe;
		this.prioridad = prioridad;
		this.tiempollegada= llegada;
		this.tiempoeje = exe;
	}

	public int getTiempollegada() {
		return tiempollegada;
	}

	public int getTiempoeje() {
		return tiempoeje;
	}

	public proceso get_Siguiente() {
		return si;
	}
	
	public void set_Siguiente(proceso nodo) {
		si = nodo;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getLlegada() {
		return tiempo_llegada;
	}

	public void setLlegada(int llegada) {
		this.tiempo_llegada = llegada;
	}

	public int getExe() {
		return tiempo_exe;
	}

	public void setExe(int exe) {
		this.tiempo_exe = exe;
	}

	public int getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(int prioridad) {
		this.prioridad = prioridad;
	}

	public boolean isFinalizado() {
		return finalizado;
	}

	public void setFinalizado(boolean estado) {
		this.finalizado = estado;
	}
	
	public boolean isSeleccionado() {
		return seleccionado;
	}
	
	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

	public int getTiempofinalizado() {
		return tiempofinalizado;
	}

	public void setTiempofinalizado(int tiempofinalizado) {
		this.tiempofinalizado = tiempofinalizado;
	}

	public int getTiempoespera() {
		return tiempoespera;
	}

	public void setTiempoespera(int tiempoespera) {
		this.tiempoespera = tiempoespera;
	}
}