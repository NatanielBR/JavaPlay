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
package javaplay;

import java.io.File;
import java.util.Arrays;

import javax.swing.JOptionPane;

import javaplay.outros.Propriedades;
import javaplay.player.Player;
import javaplay.thread.BancoComunicador;
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery;

/**
 * O inicio de tudo :)
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
		new Interface();
//		new Player(new File("/home/natan/Animes/[EA]Log_Horizon_01_[1280x720][BDRIP][Hi10p][758A8215].mkv"), (a)->{
//			System.out.println(Arrays.toString(a));
//		}).setVisible(true);
		
	}
	/**
	 * Metodo para exibir uma mensagem de erro, nenhum será tolerado e a aplicação irá sair
	 * @param er erro
	 */
	public static void error(Exception er) {
		JOptionPane.showMessageDialog(null, er);
		System.exit(3);
	}
}