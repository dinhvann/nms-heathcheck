package vms.nms.scanner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.filefilter.RegexFileFilter;

public class LogReader {

	public LogReader() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void readAllFiles(Config config, String fileType) {
		//prepare to open mdd log
		String dirPath = config.getLogPath();
		if (dirPath == null || dirPath.length() == 0) {
			dirPath = "/app/clarity/home/clarity1/var/log";
		}
		String fileNamePattern = config.getFileNamePattern();
		File [] files;
		File dir = new File(dirPath);
		// open file to log if get errors ( filenam = fileType+datetime.log)
		 DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
		 Date date = new Date();
		 File healthCheckLog = new File ("/app/HealthCheck/" + fileType + "-" + dateFormat.format(date) + ".log");
		 String newLine = System.getProperty("line.separator");
		 try {
			 if (!healthCheckLog.exists()) {
				 healthCheckLog.createNewFile();
			 }
			 //ready to write
			 FileWriter fileWriter = new FileWriter(healthCheckLog, true);
			 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
	        if (fileNamePattern == null || fileNamePattern.length() ==0) {
				files = dir.listFiles();
			} else {
				FileFilter fileFilter = new RegexFileFilter (fileNamePattern);
			    files = dir.listFiles(fileFilter);
			}
	        List<File> todayLogs = new ArrayList<File>();
	        if (files != null) todayLogs = getTodayLog(files); 
	        if (todayLogs != null && todayLogs.size() > 0) {
	        	List<String> logList = new ArrayList<String>();
	        	for (File logFile: todayLogs) {
	    			//read and process file here
	    			System.out.println("reading file:" + logFile.getName());
	    			if (logFile.isFile()) {
	    				logList = readFile( logFile, config);
	    				System.out.println(logList.size());
	    				if (logList != null && logList.size()> 0) {
	    					for (String line : logList) {
	    						bufferedWriter.write(line+newLine);
	    						System.out.println(line + "write to file: " + healthCheckLog.getName());
	    					}
	    				}
	    			}

	    		}
	        }
	        bufferedWriter.close();
		 } catch (IOException e) {
			 System.out.println("problem when open healt check log file" + e.toString());
			 
		 } 

    }
	public void readPerfRaw(Config config) {
		String dirPath = config.getLogPath();
		String fileNamePattern = config.getFileNamePattern();
		FileFilter fileFilter = new RegexFileFilter (fileNamePattern);
		File [] files;
		File [] logfiles = null;
		File dir = new File (dirPath);
		// open file to log if get errors ( filenam = fileType+datetime.log)
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
		Date date = new Date();
		File healthCheckLog = new File ("/app/HealthCheck/PerfRaw-" + dateFormat.format(date) + ".log");
		String newLine = System.getProperty("line.separator");
		System.out.println("PerfRaw dir: "+ dir.getAbsolutePath() + dir.getName());
		files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				config.setLogPath(dirPath + "/" + files[i].getName());
				System.out.println("came here...");
				System.out.println("log path "+ config.getLogPath());
				readPerfRaw(config);
			} else {
				logfiles = dir.listFiles(fileFilter);
				break;
			}
		}

		if (logfiles != null && logfiles.length > 0) {
			try {
				 if (!healthCheckLog.exists()) {
					 healthCheckLog.createNewFile();
				 }
				 //ready to write
				 FileWriter fileWriter = new FileWriter(healthCheckLog, true);
				 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				 List<String> logList = new ArrayList<String>();
				 for (int i =0 ; i < logfiles.length; i ++ ){
						System.out.println("reading file: " + logfiles[i].getName());
						logList = readFile(logfiles[i], config);
						if (logList != null && logList.size() > 0) {
							for (String line : logList) {
								bufferedWriter.write(line+newLine);
							}
						}			
				}
				 bufferedWriter.close();
			} catch (IOException e) {
				System.out.println("problem when open healt check log file" + e.toString());
			}

		}
	}
	public List<String> readFile(File file, Config config) {
		List<String> result = new ArrayList<String>();
		try {
			BufferedReader buf = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = buf.readLine()) != null) {
				String errorLine = processLine(line, config);
				if (errorLine != null) {
					result.add(file.getName() + ":" + errorLine);
				}
			}
			buf.close();
		} catch (IOException e) {
			System.out.println("Error when reading file "+ file.getName());
		} 
		return result;
	}
	private String processLine(String line, Config config) {
		String result = null;
		List<String> errorKewords = config.getErrorKeyword();
		if (errorKewords != null && errorKewords.size()>0) {
			  for (String errorKeyword : config.getErrorKeyword()) {
				  if (line.contains(errorKeyword)) {
					  // process and write error to storage
					 // System.out.println("File "+ fileName + ":" +line);
					  result = line;
					  break;
				  }
			  }
		}
		return result;
	}
	private List<File> getTodayLog(File [] files) {
		List<File>  result = new ArrayList<File>();
		Long yesterday = System.currentTimeMillis() - 24*60*60*1000;
		for (int i =0; i< files.length; i++) {
			if (files[i].lastModified()> yesterday) {
				result.add(files[i]);
			}
		}
		return result;
	}
}
