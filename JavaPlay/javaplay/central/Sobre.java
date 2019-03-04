package javaplay.central;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class Sobre extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public Sobre(JFrame f) {
		super(f);
		setResizable(false);
		setBounds(100, 100, 380, 258);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblJavaplay = new JLabel("JavaPlay 2.1.3-10");
		lblJavaplay.setBounds(12, 12, 129, 15);
		contentPanel.add(lblJavaplay);
		
		JTextArea txtrJavaplayUma = new JTextArea();
		txtrJavaplayUma.setEditable(false);
		txtrJavaplayUma.setBackground(null);
		txtrJavaplayUma.setLineWrap(true);
		txtrJavaplayUma.setWrapStyleWord(true);
		txtrJavaplayUma.setText("JavaPlay é uma aplicação destinado aos fãs de animes ou series em geral.\n\nDesenvolvido em java e tendo como base o VLC, ele suporta os mais variados formatos de media e possui compatibilidade com qualquer Sistema que rode java.");
		txtrJavaplayUma.setBounds(159, 12, 213, 331);
		contentPanel.add(txtrJavaplayUma);
		
		JLabel lblCriadoPorNataniel = new JLabel("Criado por Nataniel");
		lblCriadoPorNataniel.setBounds(12, 39, 142, 26);
		contentPanel.add(lblCriadoPorNataniel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModalityType(ModalityType.DOCUMENT_MODAL);
		setVisible(true);
	}
}
