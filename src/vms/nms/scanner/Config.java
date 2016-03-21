package vms.nms.scanner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Config {
	private String logPath;
	private String fileNamePattern;
	private List<String> errorKeyword;
	
	
	public Config() {
		super();
		logPath = System.getProperty("user.dir");
		fileNamePattern = "";
		errorKeyword = new ArrayList<String>();
	}
	
	public Config(String logPath, String fileNamePattern,
			List<String> errorKeyword) {
		super();
		this.logPath = logPath;
		this.fileNamePattern = fileNamePattern;
		this.errorKeyword = errorKeyword;
	}

	public static Map<String, Config> loadConfig (String configFilePath) {
		Map <String, Config> result = new HashMap<String, Config>();
		Properties pro = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(configFilePath);
			pro.load(input);
			String disLogPath = pro.getProperty("DiscLogDir", "/app/clarity/home/clarity1/var/log");
			String disLogFileNamePattern = pro.getProperty("DiscLogFileNamePattern", ".*DISC.*");
			String [] discLogErrorDescription = pro.getProperty("DiscLogErrorKeywords","Exception,exception").split(",");
			List <String> discLogError = new ArrayList<String>();
			discLogError.addAll(Arrays.asList(discLogErrorDescription));
			result.put("DiscLog", new Config(disLogPath, disLogFileNamePattern, discLogError));
			
			String perfLogPath = pro.getProperty("PerfLogDir", "/app/clarity/home/clarity1/var/log");
			String perfLogFileNamePattern = pro.getProperty("PerfLogFileNamePattern", ".*PERF.*");
			String [] perfLogErrorDescription = pro.getProperty("PerfLogErrorKeywords","Exception,exception").split(",");
			List <String> perfLogError = new ArrayList<String>();
			perfLogError.addAll(Arrays.asList(perfLogErrorDescription));
			result.put("PerfLog", new Config(perfLogPath, perfLogFileNamePattern, perfLogError));
						
			
			String alarmLogPath = pro.getProperty("AlarmLogDir", "/app/clarity/home/clarity1/var/log");
			String alarmLogFileNamePattern = pro.getProperty("AlarmLogFileNamePattern", ".*ALARM.*");
			String [] alarmLogErrorDescription = pro.getProperty("AlarmLogErrorKeywords","Exception,exception").split(",");
			List <String> alarmLogError = new ArrayList<String>();
			alarmLogError.addAll(Arrays.asList(alarmLogErrorDescription));
			result.put("AlarmLog", new Config(alarmLogPath, alarmLogFileNamePattern, alarmLogError));
			
			String perfRawPath = pro.getProperty("PerfRawDir", "/app/PerfDumps/PM_DUMP_FILE");
			String perfRawFileNamePattern = pro.getProperty("PerfRawFileNamePattern", ".*log");
			String [] perfRawErrorDescription = pro.getProperty("PerfRawErrorKeywords","Exception,exception").split(",");
			List <String> perfRawError = new ArrayList<String>();
			perfRawError.addAll(Arrays.asList(perfRawErrorDescription));
			result.put("PerfRaw", new Config(perfRawPath, perfRawFileNamePattern, perfRawError));
			
			String discRawPath = pro.getProperty("DiscRawDir", "/app/clarity/home/clarity1/etc/DiscDownloadScript");
			String discRawFileNamePattern = pro.getProperty("DiscRawFileNamePattern", "");
			String [] discRawErrorDescription = pro.getProperty("DiscRawErrorKeywords","Exception,exception").split(",");
			List <String> discRawError = new ArrayList<String>();
			discRawError.addAll(Arrays.asList(discRawErrorDescription));
			result.put("DiscRaw", new Config(discRawPath, discRawFileNamePattern, discRawError));
			
	/*		String alarmRawPath = pro.getProperty("AlarmRawDir", "");
			String alarmRawFileNamePattern = pro.getProperty("AlarmRawFileNamePattern", "");
			String [] alarmRawErrorDescription = pro.getProperty("AlarmRawErrorKeywords","Exception,exception").split(",");
			List <String> alarmRawError = new ArrayList<String>();
			alarmRawError.addAll(Arrays.asList(alarmRawErrorDescription));
			result.put("AlarmRaw", new Config(alarmRawPath, alarmRawFileNamePattern, alarmRawError));*/
			
		} catch (IOException e) {
			System.out.println("can not read properties file" + e.toString());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	public String getLogPath() {
		return logPath;
	}
	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}
	public String getFileNamePattern() {
		return fileNamePattern;
	}
	public void setFileNamePattern(String fileNamePattern) {
		this.fileNamePattern = fileNamePattern;
	}
	public List<String> getErrorKeyword() {
		return errorKeyword;
	}
	public void setErrorKeyword(List<String> errorKeyword) {
		this.errorKeyword = errorKeyword;
	}
}
