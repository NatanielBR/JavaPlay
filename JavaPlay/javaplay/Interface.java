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

import java.awt.Dialog.ModalityType;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

import org.apache.commons.io.FileUtils;

import javaplay.outros.Propriedades;
import javaplay.player.Player;
import javaplay.thread.BancoComunicador;
import javaplay.thread.ObterResultado;
import javaplay.thread.Resultado;
import uk.co.caprica.vlcj.filefilters.VideoFileFilter;

/**
 * Interface do programa
 * @author natan
 */
public class Interface extends javax.swing.JFrame {
	private Player play;
	/**
	 * Metodo que retorna uma lista de JProgressBar
	 * @return
	 */
	public JProgressBar[] addConteudo() {
		Propriedades prod = Propriedades.instancia;
		String dir = prod.getDir();
		if (dir == null) {
			dir = escolherDiretorio();
			prod.setDir(dir);
		}
		List<File> it = Arrays.asList(parser(dir).toArray(new File[0]));
		Collections.sort(it);
		List<JProgressBar> barras = new ArrayList<>();
		it.forEach((info)->{
			JProgressBar barr = new JProgressBar(0, 100);
			barr.setString(info.getName());
			barr.setStringPainted(true);
			Resultado res = BancoComunicador.instancia.obterResultado(new ObterResultado(info));
			barr.setValue(res == null?0:res.getUltimoTempo());
			barr.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (play != null && play.isVisible()) {
						return;
					}
					Resultado res = BancoComunicador.instancia.obterResultado(new ObterResultado(info));
					Consumer<Integer> ina = (a)->{
						barr.setValue(a);
					};
					play = barr.getValue()>0?new Player(info,ina,res.getUltimoTempo()):new Player(info,ina);
					play.setModal(true);
					play.setModalityType(ModalityType.DOCUMENT_MODAL);
					play.setVisible(true);
				}

			});
			barr.setBorderPainted(false);
//            bt.setBorderPainted(false);
//            bt.setContentAreaFilled(false);
//            bt.setFocusPainted(false);
//            bt.setOpaque(false);
			barras.add(barr);
		});
		return barras.toArray(new JProgressBar[0]);
	}
	/**
	 * Metodo que retorna o diretorio da String
	 * 
	 * Ele é executado na primeira vez que é executado
	 * @return
	 */
	public String escolherDiretorio() {
		String dir = null;
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Escolha o diretorio");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			dir = chooser.getSelectedFile().getAbsolutePath();
		} else {
			System.exit(0);
		}
		return dir;
	}
	private Collection<File> parser(String dir) {
		Collection<File> files = FileUtils.listFiles(new File(dir), new VideoFileFilter().getExtensions(), true);
		return files;
	}
	/**
	 * onde tudo irá rodar
	 */
	public Interface() {
		setTitle("JavaPlay");
		JPanel pan = new JPanel();
		JScrollPane pane = new JScrollPane(pan);
		pan.setAutoscrolls(true);
		GridLayout lay = new GridLayout(0, 1);
		lay.setVgap(20);
		pan.setLayout(lay);
		JProgressBar[] obs = addConteudo();
		for (JProgressBar o : obs) {
			JProgressBar bar = o;
			pan.add(bar);
		}
		add(pane);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
			}
		});
		setSize(600, 480);
		setVisible(true);
	}
}
