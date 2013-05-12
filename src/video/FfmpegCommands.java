package video;

import javax.swing.JFrame;

public class FfmpegCommands {

		public static void main(String [] args)
		{
			Interface window = new Interface();
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.setSize(600, 600);
			window.setVisible(true);
		}
}
