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
package javaplay.thread;

import java.io.File;
/**
 * Classe quase depreceada para obter o resultado
 * @author natan
 *
 */
public class ObterResultado{
	private final File arq;
	public ObterResultado(File arquivo) {
		arq = arquivo;
	}
	public File getArq() {
		return arq;
	}
}