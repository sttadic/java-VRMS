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
// EXTRA (Java 25) - Scoped Values (JEP 487)
public class LocaleManager {
	// EXTRA ScopedValue binds the locale per-call-scope rather than globally
	public static final ScopedValue<Locale> CURRENT_LOCALE = ScopedValue.newInstance();

	// Tracks the user's chosen locale between menu iterations
	private static Locale activeLocale = Locale.of("en", "IE");

	public static Locale getActiveLocale() {
		return activeLocale;
	}

	public static void setLocale(Locale newLocale) {
		activeLocale = newLocale;
	}

	public static String getString(String key) {
		Locale locale = CURRENT_LOCALE.get();
		return ResourceBundle.getBundle("messages", locale).getString(key);
	}
}
