package javaplay.central;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;

import javaplay.central.mascara.MascaraCentral;
import javaplay.outros.Propriedades;
import javaplay.thread.BancoComunicador;
import javaplay.thread.DiretorioMonitor;

public class AnimeLista {

	private JFrame frame;
	private JTabbedPane Coluna;
	
	private AtomicBoolean encerrar;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		AnimeLista window = new AnimeLista();
		new BancoComunicador();
		new Propriedades();
		
		Consumer<List<File>> novidade = (a)->{
			window.dividirPorMascara(a);
			window.frame.repaint();
			window.frame.pack();
		};
		DiretorioMonitor monitor = new DiretorioMonitor(window.encerrar,novidade);
		window.frame.setVisible(true);
		
	}
	
	public void dividirPorMascara(List<File> arq) {
		HashMap<String,List<File>> listas = new HashMap<>();
		for (File f : arq) {
			MascaraCentral masc = MascaraCentral.obterMascaraCompativel(f);
			if (listas.get(masc.getFantasiaSemEp())== null) {
				List<File> arqs = new ArrayList<>();
				arqs.add(f);
				listas.put(masc.getFantasiaSemEp(), arqs);
			}else {
				listas.get(masc.getFantasiaSemEp()).add(f);
			}
		}
		listas.keySet().forEach((mascara)->{
			JLista lis = new JLista();
			lis.adicionarConteudo(listas.get(mascara));
			Coluna.addTab(mascara, lis);
		});
		
	}
	
	/**
	 * Create the application.
	 */
	public AnimeLista() {
		initialize();
		encerrar = new AtomicBoolean();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 760, 510);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Coluna = new JTabbedPane(JTabbedPane.LEFT);
		frame.getContentPane().add(Coluna, BorderLayout.CENTER);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnJavaplay = new JMenu("JavaPlay");
		menuBar.add(mnJavaplay);

		JMenu mnNewMenu = new JMenu("Player");
		menuBar.add(mnNewMenu);

		JMenu mnSobre = new JMenu("Sobre");
		menuBar.add(mnSobre);
	}

}
