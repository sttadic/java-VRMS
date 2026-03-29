package ie.tus.oop;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Manages the active locale and resource bundle for localisation support.
 * Provides access to localised strings throughout the application.
 *
 * @author Stjepan Tadic
 */
// ADVANCED Localisation - ResourceBundle and Locale management
public class LocaleManager {
	private static Locale locale = Locale.of("en", "IE");
	private static ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);

	public static Locale getLocale() {
		return locale;
	}

	public static void setLocale(Locale newLocale) {
		locale = newLocale;
		bundle = ResourceBundle.getBundle("messages", newLocale);
	}

	public static String getString(String key) {
		return bundle.getString(key);
	}
}
