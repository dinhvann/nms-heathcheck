package vms.nms.scanner;

import java.util.TimerTask;

public class ThreadReader extends Thread {
	
	private Config config;
	private String fileType;

//	@Override
	public void run() {
		// call to read the log files
		LogReader logReader = new LogReader();
		System.out.println("Thread: "+ fileType);
		if (fileType.equals("PerfRaw")) {
			logReader.readPerfRaw(config);
		} else {
			logReader.readAllFiles(config, fileType);	
		}

	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	

	
	

}
