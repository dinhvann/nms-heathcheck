package vms.nms.scanner;

import java.util.Map;


public class Test {

	public static void main(String[] args) {
		
		
		if (args.length> 0) {
			Map<String, Config> configs = Config.loadConfig(args[0]);
			if (configs != null ) {
				for (Map.Entry<String, Config> config:configs.entrySet()) {
					ThreadReader threadReader = new ThreadReader();
					System.out.println ("Key: " + config.getKey() + "path: " + config.getValue().getLogPath());
					threadReader.setConfig(config.getValue());
					threadReader.setFileType(config.getKey());
					threadReader.start();
				}
			}

		} else {
			System.out.print("you are missing configuration file");
		}

	}

}
