package efficient_chatbot_use.utils;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DotEnv {
	
	static Properties envProps = new Properties();
	static {
		try {
			envProps.load(new FileInputStream(".env"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    public static String getEnv(String envName) {
    	return envProps.getProperty(envName);
    }
}
