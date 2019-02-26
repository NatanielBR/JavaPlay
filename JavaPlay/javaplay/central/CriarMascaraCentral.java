package javaplay.central;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.AbstractListModel;
import java.awt.FlowLayout;
import javax.swing.SwingConstants;

public class CriarMascaraCentral extends JPanel {
	private JTextField textoRegex;
	private JList<JLabel> Original;
	private JList<JLabel> Nova;

	/**
	 * Create the panel.
	 */
	public CriarMascaraCentral() {
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		textoRegex = new JTextField();

		textoRegex.setToolTipText("Escreva a Regex (Oque deve remover em forma de Regex)");
		panel.add(textoRegex, BorderLayout.CENTER);
		textoRegex.setColumns(10);

		JButton RegexAplicar = new JButton("Aplicar");
		RegexAplicar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultListModel<JLabel> lista = new DefaultListModel<>();
				ListModel<JLabel> lis = Original.getModel();
				if (Original.getSelectionModel().isSelectionEmpty()) {
					for (int i = 0; i < lis.getSize(); i++) {
						JLabel ant = lis.getElementAt(i);
						lista.addElement(new JLabel(ant.getText().replaceAll(textoRegex.getText(), "")));
					}
					Nova.setModel(lista);
				} else {
					for (JLabel ant : Original.getSelectedValuesList()) {
						lista.addElement(new JLabel(ant.getText().replaceAll(textoRegex.getText(), "")));
					}
				}
				Nova.setModel(lista);
			}
		});
		textoRegex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (ActionListener i : RegexAplicar.getActionListeners()) {
					i.actionPerformed(e);
				}
			}
		});
		panel.add(RegexAplicar, BorderLayout.EAST);

		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JButton btnNewButton_1 = new JButton("Aplicar");
		panel_1.add(btnNewButton_1);

		JPanel panel_2 = new JPanel();
		add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new GridLayout(0, 2, 0, 0));

		Original = new JList<JLabel>();
		Original.setToolTipText("Arquivos Originais\n(É possivel filtrar arquivos)");
		Original.setValueIsAdjusting(true);
		panel_2.add(Original);

		Nova = new JList<JLabel>();
		Nova.setToolTipText("Texto após a Regex");
		panel_2.add(Nova);

	}

	public CriarMascaraCentral(List<File> orig) {
		this();
		Original.setCellRenderer(new ListCellRenderer<JLabel>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends JLabel> list, JLabel value, int index,
					boolean isSelected, boolean cellHasFocus) {
				if (isSelected) {
					value.setBackground(Color.CYAN);
				} else {
					value.setBackground(Color.WHITE);
				}
				return value;
			}
		});
		Nova.setCellRenderer(new ListCellRenderer<JLabel>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends JLabel> list, JLabel value, int index,
					boolean isSelected, boolean cellHasFocus) {
				return value;
			}
		});
		adicionarOriginal(orig);
	}

	private void adicionarOriginal(List<File> orig) {
		DefaultListModel<JLabel> lista = new DefaultListModel<>();
		orig.forEach((a) -> lista.addElement(arquivoParaLabel(a)));
		Original.setModel(lista);
	}

	private JLabel arquivoParaLabel(File f) {
		JLabel lb = new JLabel(f.getName());
		lb.setOpaque(true);
		return lb;
	}
}
