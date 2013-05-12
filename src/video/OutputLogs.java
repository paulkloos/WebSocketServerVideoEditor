package video;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OutputLogs
{
	public enum ACTION{NONE,PRINT}
	private Consume thread;
	private ExecutorService runner;
	private ACTION mode;
	private boolean done;
	public OutputLogs(InputStream stream, ACTION action)
	{
		mode = action;
		done = false;
		thread = new Consume(stream);
		runner = Executors.newFixedThreadPool(1);
		runner.execute(thread);
	}
	public void Done()
	{
		done = true;
		runner.shutdown();
	}

	private class Consume implements Runnable
	{
		private BufferedReader br;
		public Consume(InputStream stdout)
		{
			br = new BufferedReader(new InputStreamReader(stdout));
		}
		public void run()
		{
			String line = null;
			try {
				while(!done)
				{
					line = br.readLine();
					if(line != null)
					{
						switch(mode)
						{
						case PRINT:
								System.out.println(line);
							break;
						case NONE:
						}
					}
				}
					
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
	}
}
