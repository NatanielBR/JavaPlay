package javaplay.central;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import javaplay.central.mascara.MascaraCentral;
import javaplay.outros.BancoComunicador;
import javaplay.outros.Propriedades;
import javaplay.thread.DiretorioMonitor;

public class AnimeLista {

	private JFrame frame;
	private JTabbedPane Coluna;
	private AtomicBoolean encerrar;
	private Consumer<List<File>> novidade;
	private DiretorioMonitor monitor;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		new Propriedades();
		new BancoComunicador();
		AnimeLista window = new AnimeLista();
		window.frame.setVisible(true);
		
	}
	
	public void dividirPorMascara(List<File> arq) {
		HashMap<String,List<File>> listas = new HashMap<>();
		Coluna.removeAll();
		for (File f : arq) {
			MascaraCentral masc = MascaraCentral.obterMascaraCompativel(f);
			if (masc == null) continue;
			if (listas.get(masc.getFantasiaSemEp())== null) {
				List<File> arqs = new ArrayList<>();
				arqs.add(f);
				listas.put(masc.getFantasiaSemEp(), arqs);
			}else {
				listas.get(masc.getFantasiaSemEp()).add(f);
			}
		}
		listas.keySet().forEach((mascara)->{
			JLista lis = new JLista(frame);
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
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				encerrar.set(true);
				super.windowClosing(e);
			}
		});
		novidade = (a)->{
			dividirPorMascara(a);
			frame.repaint();
			frame.pack();
		};
		monitor = new DiretorioMonitor(encerrar,novidade);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("JavaPlay");
		frame.setBounds(100, 100, 760, 510);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		Coluna = new JTabbedPane(JTabbedPane.LEFT);
		frame.getContentPane().add(Coluna, BorderLayout.CENTER);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnJavaplay = new JMenu("JavaPlay");
		menuBar.add(mnJavaplay);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Mudar Diretorio");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Propriedades.instancia.removerDir();
				monitor.setDiretorio();
			}
		});
		mnJavaplay.add(mntmNewMenuItem);
		
		JMenuItem Sair = new JMenuItem("Sair");
		Sair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				encerrar.set(true);
			}
		});
		mnJavaplay.add(Sair);

		JMenu mnNewMenu = new JMenu("Player");
		menuBar.add(mnNewMenu);
		
		JCheckBoxMenuItem ProximoBoolean = new JCheckBoxMenuItem("Proximo Arquivo");
		ProximoBoolean.setSelected(Propriedades.instancia.isProximoArquivo());
		ProximoBoolean.addActionListener((a)->{
			Propriedades.instancia.setProximoArquivo(ProximoBoolean.isSelected());
		});
		mnNewMenu.add(ProximoBoolean);
		
		JCheckBoxMenuItem PularBoolean = new JCheckBoxMenuItem("Pular Abertura");
		PularBoolean.setSelected(Propriedades.instancia.isPularAbertura());
		PularBoolean.addActionListener((a)->{
			Propriedades.instancia.setPularAbertura(PularBoolean.isSelected());
		});
		mnNewMenu.add(PularBoolean);

		JMenu mnSobre = new JMenu("Ajuda");
		menuBar.add(mnSobre);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Versão");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Sobre(frame);
			}
		});
		mnSobre.add(mntmNewMenuItem_1);
	}

}
