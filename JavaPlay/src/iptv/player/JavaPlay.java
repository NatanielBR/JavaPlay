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
package iptv.player;

import javax.swing.JOptionPane;

import iptv.player.thread.BancoComunicador;
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery;

/**
 *
 * @author Nataniel
 */
public class JavaPlay {
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		new Propriedades();
		new NativeDiscovery().discover();
		new BancoComunicador();
		new Thread(new Interface()).start();
		
	}
	public static void error(Exception er) {
		JOptionPane.showMessageDialog(null, er);
		System.exit(3);
	}
}
