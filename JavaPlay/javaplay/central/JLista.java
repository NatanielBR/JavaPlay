package javaplay.central;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

import javaplay.central.mascara.MascaraCentral;
import javaplay.outros.BancoComunicador;
import javaplay.outros.Resultado;
import javaplay.player.NovoPlayer;
import javaplay.player.Player;

public class JLista extends JList<JBarra> {
	private JPopupMenu menu;
	private JMenuItem inicio, parou;
	private Player play;
	private JLista eu;
	private JFrame pai;
	private JTabbedPane coluna;
	private String nome;
	private JBarra selecionado;
	public JLista(JFrame p,JTabbedPane c, String s) {
		super();
		pai = p;
		coluna = c;
		nome = s;
		menu = new JPopupMenu();
		inicio = new JMenuItem("Abrir no inicio");
		parou = new JMenuItem("Abrir onde parou");
		eu = this;
		setCellRenderer(new ListCellRenderer<JBarra>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends JBarra> list, JBarra value,
					int index, boolean isSelected, boolean cellHasFocus) {
				if (isSelected) {
					value.setBackground(Color.LIGHT_GRAY);
				}else {
					value.setBackground(Color.WHITE);
				}
				return value;
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					if (!isSelectionEmpty()) {
						JProgressBar barra = getSelectedValue();
						menu.removeAll();
						if (barra.getValue() > 0) {
							menu.add(inicio);
							menu.add(parou);
						}else {
							menu.add(inicio);
						}
						selecionado = getSelectedValue();
						menu.show(eu,e.getX(),e.getY());
					}
					
				}else {
					menu.setVisible(false);
				}
			}
		});
		inicio.addActionListener((a)->{
			if (play != null && play.isVisible()) {
				return;
			}
			Consumer<Integer> ina = (b)->{
				selecionado.setValue(b);
				selecionado = null;
			};
			play.setVisible(true);
			play.play(selecionado.getArquivo(), 0);
		});
		parou.addActionListener((a)->{
			if (play != null && play.isVisible()) {
				return;
			}
			Consumer<Integer> ina = (b)->{
				selecionado.setValue(b);
				selecionado = null;
			};
			play.play(selecionado.getArquivo(), selecionado.getValue());
//			play = new Player(selecionado.getArquivo(),ina,selecionado.getValue(),this);
			play.setVisible(true);
		});
	}
	/**
	 * @return the pai
	 */
	public JFrame getPai() {
		return pai;
	}
	public void devolver() {
//		coluna.removeTabAt(coluna.indexOfTab(nome));
		coluna.addTab(nome, this);
	}
	public void adicionarConteudo(HashMap<String, List<Path>> arq) {
		trocarLista(criarBarra(arq.get(nome)));
	}
	private JBarra criarBarra(Path info) {
		MascaraCentral masc = MascaraCentral.obterMascaraCompativel(info);
		JBarra barr = masc == null ? new JBarra(info):new JBarra(masc);
		barr.setMaximum(100);
		barr.setMinimum(0);
		barr.setStringPainted(true);
		Resultado res = BancoComunicador.instancia.obterResultado(info);
		barr.setValue(res == null?0:res.getUltimoTempo());
		barr.setToolTipText("Progresso: "+barr.getValue()+ "%");
		barr.setBorderPainted(false);
		return barr;
	}
	private List<JBarra> criarBarra(List<Path> info) {
		List<JBarra> barras = new ArrayList<JBarra>();
		info.forEach((a)->barras.add(criarBarra(a)));
		play = new Player(info, this);
		return barras;
	}
	private void trocarLista(List<JBarra> novo) {
		DefaultListModel<JBarra> barras = new DefaultListModel<>();
		novo.forEach(barras::addElement);
		setModel(barras);
	}
}
