package javaplay.player;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

public class NovoPlayer{
	private EmbeddedMediaPlayerComponent playerList;
	public NovoPlayer() {
		playerList = new EmbeddedMediaPlayerComponent();
		iniciar();
	}
	public void iniciar() {
		JFrame f = new JFrame("Player 2.1.3");
		f.add(playerList);
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				playerList.mediaPlayer().media().play("C:\\Users\\Nataniel\\animes\\Zetsuen_no_Tempest_01_Dollars-OF-Anbient.mkv");
			}
			@Override
			public void windowClosed(WindowEvent e) {
				
				playerList.mediaPlayer().release();
				playerList.release();
				f.dispose();
				super.windowClosed(e);
			}
		});
		f.setSize(300, 400);
		f.setVisible(true);
	}
}
