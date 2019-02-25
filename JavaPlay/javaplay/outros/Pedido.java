/*******************************************************************************
 * Copyright (C) 2019 natan
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package javaplay.outros;

import java.util.function.Consumer;
/**
 * Classe para realizar o pedido, depreceada.
 * @author natan
 * @deprecated
 * @param <T>
 */
public class Pedido<T> {
	private Object valor;
	private Consumer<Object> fim;
	public Pedido(Consumer<Object> cons) {
		this.valor = valor;
		fim = cons;
	}
	public Consumer<Object> getResposta(){
		return fim;
	}
	public T getValor() {
		return (T) valor;
	}
	public void setValor(T valor) {
		this.valor = valor;
	}
	
}
