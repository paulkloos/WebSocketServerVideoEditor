package video;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import server.*;

import video.OutputLogs.ACTION;

public class Interface extends JFrame implements SourceFiles
{
	JPanel main;
	JButton setprocessButton,addinputButton,addoutputButton,runprocessButton,saveButton,probeButton,rootButton,convertedButton;
	JList<String> inList,outList;
	JTextField processText,probeText,rootText,convertedText;
	DefaultListModel<String> inModel,outModel;
	private WindowEventHandler listener;
	private InputFiles ifiles;
	private OutputFiles ofiles;
	private Properties serversettings;
	private ArrayList<SourceFile> files;
	private Timer check,sourcechange;
	private ExecutorService thread;
	private Server contest;
	public Interface()
	{
		super("Video Processor");
		
		files = new ArrayList<SourceFile>();
		listener = new WindowEventHandler();
		check = new Timer();
		check.scheduleAtFixedRate(new TimerTask(){
			public void run()
			{
				checkChanges();
			}
		}, 60000, 60000);
		sourcechange = new Timer();
		sourcechange.scheduleAtFixedRate(new TimerTask(){
			public void run()
			{
				saveSourceList();
			}
		}, 300000,300000);
		Dimension size = new Dimension(400,20);
		Container pane = getContentPane();
		main = new JPanel(new GridBagLayout());
		processText = new JTextField();
		processText.setPreferredSize(size);
		processText.setMaximumSize(size);
		processText.setSize(size);
		
		setprocessButton = new JButton("Set FFMPEG");
		setprocessButton.addActionListener(listener);
		
		probeText = new JTextField();
		probeText.setPreferredSize(new Dimension(400,20));
		probeText.setSize(size);
		probeText.setMaximumSize(size);
		
		probeButton = new JButton("Set FFPROBE");
		probeButton.addActionListener(listener);
		
		rootText = new JTextField();
		rootText.setPreferredSize(new Dimension(400,20));
		rootText.setSize(size);
		rootText.setMaximumSize(size);
		
		rootButton = new JButton("Select Root");
		rootButton.addActionListener(listener);
		
		convertedText = new JTextField();
		convertedText.setPreferredSize(new Dimension(400,20));
		convertedText.setSize(size);
		convertedText.setMaximumSize(size);
		
		convertedButton = new JButton("Select Converted File Storage");
		convertedButton.addActionListener(listener);
		
		saveButton = new JButton("Save Settings");
		saveButton.addActionListener(listener);
		
		inList = new JList<String>();
		inList.setLayoutOrientation(JList.VERTICAL);
		inList.setPreferredSize(new Dimension(400,20));
		inList.setSize(size);
		inList.setMaximumSize(size);
		inModel = new DefaultListModel<String>();
		
		addinputButton = new JButton("Add Input");
		addinputButton.addActionListener(listener);
		
		outList = new JList<String>();
		outList.setLayoutOrientation(JList.VERTICAL);
		outList.setPreferredSize(new Dimension(400,20));
		outList.setSize(size);
		outList.setMaximumSize(size);
		outModel = new DefaultListModel<String>();
		
		addoutputButton = new JButton("Add Output");
		addoutputButton.addActionListener(listener);
		
		runprocessButton = new JButton("Run");
		runprocessButton.addActionListener(listener);
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 1;
		c.gridwidth = 3;
		main.add(new JLabel("FFMPEG path"),c);
		
		c.gridy = 1;
		main.add(processText,c);
		
		c.gridy = 2;
		c.gridwidth = 1;
		main.add(setprocessButton,c);
		
		c.gridy = 3;
		c.gridwidth = 3;
		main.add(probeText,c);
		
		c.gridy = 4;
		c.gridwidth = 1;
		main.add(probeButton,c);
		
		c.gridy = 5;
		c.gridwidth = 3;
		main.add(rootText,c);
		
		c.gridy = 6;
		c.gridwidth = 1;
		main.add(rootButton,c);
		
		c.gridy = 7;
		c.gridwidth = 3;
		main.add(convertedText,c);
		
		c.gridy = 8;
		c.gridwidth = 3;
		main.add(convertedButton,c);
		
		c.gridy = 9;
		c.gridx = 2;
		main.add(saveButton,c);
		
		c.gridy = 10;
		main.add(new JScrollPane(inList,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),c);
		
		c.gridy = 11;
		c.gridwidth = 1;
		main.add(addinputButton,c);
		
		c.gridy = 12;
		main.add(runprocessButton,c);
		
		pane.add(main);
		loadSettings();
		contest = new Server();
		contest.runServer(this);
		loadSourceList();
	}
	private void checkChanges()
	{
		if(rootText.getText().length() > 0 && processText.getText().length() > 0 && probeText.getText().length() > 0)
		{
			File folder = new File(rootText.getText());
			checkDirectory(folder);
			saveSourceList();
		}
	}
	private void checkDirectory(File value)
	{
		File[] filelist = value.listFiles();
		String[] baseparts = rootText.getText().split("\\\\");
		String[] parts = value.getAbsolutePath().split("\\\\");
		String path;
		if(parts.length > baseparts.length)
		{
			path = "";
			for(int x = baseparts.length; x < parts.length; x++)
				path.concat("\\"+parts[x]);
		}
		else
			 path = "\\";
		
		for(int x = 0; x < filelist.length; x++)
		{
			if(filelist[x].isFile() && approvedExtension(filelist[x].getAbsoluteFile().toString()))
			{
				if(getFile(path,filelist[x].getAbsoluteFile().toString()) == null)
				{
					SourceFile source = new SourceFile(filelist[x].getAbsolutePath(),serversettings);
					source.setFolderPath(path);
					files.add(source);
					updateInputList();
				}
			}
			else if(filelist[x].isDirectory())
				checkDirectory(filelist[x]);
		}
	}
	private void updateInputList()
	{
		inModel.clear();
		for(int i = 0; i < files.size(); i++)
		{
			inModel.addElement((String)files.get(i).getRelativeFile());
		}
		inList.setModel(inModel);
	}
	private void updateOutputList()
	{
		ArrayList<String> list = ofiles.getFileList();
		outModel.clear();
		for(int i = 0; i < list.size(); i++)
		{
			outModel.addElement((String)list.get(i));
		}
		outList.setModel(outModel);
	}
	private void openInput()
	{
		JFileChooser file = new JFileChooser();
		int value = file.showOpenDialog(this);
		File path;
		
		if(value == JFileChooser.APPROVE_OPTION)
		{
			path = file.getSelectedFile();
			String name = path.getPath();
			SourceFile source = new SourceFile(name,serversettings);
			files.add(source);
			updateInputList();
		}
	}
	private void openOutput()
	{
		JFileChooser file = new JFileChooser();
		int value = file.showSaveDialog(this);
		File path;
		if(value == JFileChooser.APPROVE_OPTION)
		{
			path = file.getSelectedFile();
			String name = path.getPath();
			ofiles.addFile(name);
			updateOutputList();
		}
	}
	private void openProcess()
	{
		JFileChooser file = new JFileChooser();
		int value = file.showOpenDialog(this);
		File path;
		if(value == JFileChooser.APPROVE_OPTION)
		{
			path = file.getSelectedFile();
			String name = path.getPath();
			processText.setText(name);
		}
	}
	private void openProbe()
	{
		JFileChooser file = new JFileChooser();
		int value = file.showOpenDialog(this);
		File path;
		if(value == JFileChooser.APPROVE_OPTION)
		{
			path = file.getSelectedFile();
			String name = path.getPath();
			probeText.setText(name);
		}
	}
	private void selectRoot()
	{
		JFileChooser file = new JFileChooser();
		file.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int value = file.showDialog(this, "Select");
		
		if(value== JFileChooser.APPROVE_OPTION)
		{
			rootText.setText(file.getSelectedFile().getAbsolutePath());
		}
	}
	private void selectConverted()
	{
		JFileChooser file = new JFileChooser();
		file.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int value = file.showDialog(this, "Select");
		
		if(value== JFileChooser.APPROVE_OPTION)
		{
			convertedText.setText(file.getSelectedFile().getAbsolutePath());
		}
	}
	private void runCommands()
	{
		Process p;
		OutputLogs stdout = null, errors = null;
		List<String> commands = new ArrayList<String>();
		ProcessBuilder builder = new ProcessBuilder();
		//build command list
		commands.add(processText.getText());
		commands.addAll(ifiles.getCommand());
		commands.add("-f");
		commands.add("image2");
		commands.add("-");

		//commands.addAll(ofiles.getCommand());
			
		builder.command(commands);
		builder.redirectErrorStream(true);
		try {
			p = builder.start();
			stdout = new OutputLogs(p.getInputStream(),ACTION.PRINT);
			errors = new OutputLogs(p.getErrorStream(),ACTION.PRINT);
	
	//OutputStream stdin = p.getOutputStream();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			stdout.Done();
			errors.Done();
		}
	}
	private void saveSettings()
	{
		try {
			String path = Interface.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			String decoded = URLDecoder.decode(path,"UTF-8");
			String file = decoded+"//settings.properties";
			FileWriter fstream = new FileWriter(file);
			Properties settings = new Properties();
			settings.setProperty("FFMPEG", processText.getText());
			settings.setProperty("FFPROBE",probeText.getText());
			settings.setProperty("ROOT",rootText.getText());
			settings.setProperty("CONVERTED", convertedText.getText());
			settings.store(fstream, "SERVER SETTINGS");
			fstream.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void loadSettings()
	{
		try {
			String path = Interface.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			String decoded = URLDecoder.decode(path,"UTF-8");
			String file = decoded+"//settings.properties";
			FileReader fstream = new FileReader(file);
			Properties settings = new Properties();
			settings.load(fstream);
			fstream.close();
			processText.setText(settings.getProperty("FFMPEG"));
			probeText.setText(settings.getProperty("FFPROBE"));
			rootText.setText(settings.getProperty("ROOT"));
			convertedText.setText(settings.getProperty("CONVERTED"));
			serversettings = settings;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void saveSourceList()
	{
		if(filesChanged() == true)
		{
			try {
				String path = Interface.class.getProtectionDomain().getCodeSource().getLocation().getPath();
				String decoded = URLDecoder.decode(path,"UTF-8");
				String file = decoded+"//source.dat";
				FileOutputStream out = new FileOutputStream(file);
				ObjectOutputStream output = new ObjectOutputStream(out);
				output.writeObject(files);
				output.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	@SuppressWarnings("unchecked")
	private void loadSourceList()
	{
		try
		{
			String path = Interface.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			String decoded = URLDecoder.decode(path,"UTF-8");
			String file = decoded+"//source.dat";
			FileInputStream in = new FileInputStream(file);
			ObjectInputStream input = new ObjectInputStream(in);
			files = (ArrayList<SourceFile>) input.readObject();
			input.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateInputList();
	}
	private boolean filesChanged()
	{
		boolean change = false;
		int size = files.size();
		for(int x = 0; x < size && change == false; x++)
		{
			if(files.get(x).getChanged())
				change = true;
			
			int depth = files.get(x).getChildren().size();
			for(int y = 0; y < depth && change == false; y++)
			{
				if(files.get(x).getChildren().get(y).getChanged())
					change = true;
			}
		}
		return change;
	}
	public SourceFile getFile(int index)
	{
		if(index >= 0 && index < files.size())
			return files.get(index);
		else
			return null;
	}
	public SourceFile getFile(String path,String name)
	{
		int file = -1;
		String[] parts = name.split("\\\\");
		for(int index = 0; index < files.size() && file == -1; index++)
		{
			if(files.get(index).getFolderPath().equals(path) && files.get(index).getFileName().equals(parts[parts.length-1]))
				file = index;
		}
		if(file == -1)
			return null;
		else
			return files.get(file);
	}
	public int countAll()
	{
		return files.size();
	}
	public int count(String path)
	{
		int count = 0;
		for(int index = 0; index < files.size(); index++)
		{
			if(files.get(index).getFolderPath().equals(path))
				count++;
		}
		return count;
	}
	public String[] getFolders(String path)
	{
		ArrayList<String> temp = new ArrayList<String>();
		String[] baseparts = path.split("\\\\");
		for(int index = 0; index < files.size(); index++)
		{
			String[] parts = files.get(index).getFolderPath().split("\\\\");
			if(parts.length == baseparts.length +1)
			{
				boolean alltrue = true;
				for(int x = 0; x < parts.length && alltrue; x++)
				{
					if(parts[x].equals(baseparts[x]) == false)
						alltrue = false;
				}
				temp.add(parts[parts.length-1]);
			}
		}
		return temp.toArray(new String[temp.size()]);
	}
	public String[] getAllFiles(String path)
	{
		ArrayList<String> temp = new ArrayList<String>();
		for(int index = 0; index < files.size(); index++)
		{
			if(files.get(index).getFolderPath().equals(path))
				temp.add(files.get(index).getFileName());
		}
		return temp.toArray(new String[temp.size()]);
	}
	public boolean isDirectory(String path)
	{
		boolean found = false;
		for(int index = 0; index < files.size() && !found; index++)
		{
			if(files.get(index).getFolderPath().equals(path))
				found = true;
		}
		return found;
	}
	public Properties getSettings()
	{
		return serversettings;
	}
	private class WindowEventHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == setprocessButton)
			{
				openProcess();
			}
			else if(e.getSource() == addinputButton)
			{
				openInput();
			}
			else if(e.getSource() == addoutputButton)
			{
				openOutput();
			}
			else if(e.getSource() == runprocessButton)
			{
				runCommands();
			}
			else if(e.getSource() == saveButton)
			{
				saveSettings();
			}
			else if(e.getSource() == probeButton)
			{
				openProbe();
			}
			else if(e.getSource() == rootButton)
			{
				selectRoot();
			}
			else if(e.getSource() == convertedButton)
			{
				selectConverted();
			}
		}
	}
	private boolean approvedExtension(String name)
	{
		String[] parts = name.split("\\.");
		boolean result;
		switch(parts[parts.length-1].toLowerCase())
		{
			case "avi":
			case "webm":
			case "flv":
			case "mp4":
			case "ogg":
			case "ogv":
				result = true;
				break;
			default:
				result = false;
		}
		return result;
	}
}
