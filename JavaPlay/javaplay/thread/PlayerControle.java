package javaplay.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Consumer;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class PlayerControle implements Runnable {
	private EmbeddedMediaPlayer player;
	private ArrayBlockingQueue<Consumer<EmbeddedMediaPlayer>> arr;
	
	public PlayerControle(EmbeddedMediaPlayer play) {
		player = play;
		arr = new ArrayBlockingQueue<Consumer<EmbeddedMediaPlayer>>(10);
		new Thread(this).start();
	}
	public void adicionar(Consumer<EmbeddedMediaPlayer> con) {
		arr.add(con);
	}
	@Override
	public void run() {
		while (true) {
			try {
				Consumer<EmbeddedMediaPlayer> con = arr.take();
				con.accept(player);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
