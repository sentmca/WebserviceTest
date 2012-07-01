import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Sharif Ul Islam
 *
 */
public class ConfigBundle extends ResourceBundle {

	private static String BUNDLE_NAME = "config";

	private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
  
  
	private static String locale = "";
  
	public ConfigBundle(){
		Locale l = Locale.getDefault();
		locale = l.getLanguage(); 
		//init(locale);
	}
  
	private void init(String l){
		if (!locale.equals(l)){
			if (l == null){
				locale = "en";
			}else{
				locale = l;
			}
			BUNDLE_NAME = BUNDLE_NAME+"_"+locale;
			RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
		}
	}
  
	@Override
	public Enumeration<String> getKeys() {
		// TODO Auto-generated method stub
		return RESOURCE_BUNDLE.getKeys();
	}

	@Override
	protected Object handleGetObject(String key) {
		try {
			String label = new String(RESOURCE_BUNDLE.getString(key).getBytes("ISO-8859-1"),"UTF8");
			return label;
		} catch (Exception e) {
			return '!' + key + '!';
		}
	}

}