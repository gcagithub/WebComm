package com.webComm.utils.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The Class Localizable. Workaround for l13n
 */
public class Localizable {

	/** The deflang. */
	private static String deflang;

	/** The defcountry. */
	private static String defcountry;

	static {
		setDeflang("en");
		setDefcountry("US");
	}

	/** The language. */
	private String language;

	/** The country. */
	private String country;

	/** The bundlename. */
	private String bundlename;

	/** The messages. */
	private ResourceBundle messages;

	/**
	 * Instantiates a new localizable.
	 * 
	 * @param bundlename
	 *            the bundlename
	 */
	public Localizable(String bundlename) {
		this(getDeflang(), getDefcountry(), bundlename);
	}

	/**
	 * Instantiates a new localizable.
	 * 
	 * @param language
	 *            the language
	 * @param country
	 *            the country
	 * @param bundlename
	 *            the bundlename
	 */
	public Localizable(String language, String country, String bundlename) {
		this.language = language;
		this.country = country;
		this.bundlename = bundlename;

		messages = Utf8ResourceBundle.getBundle(this.bundlename, new Locale(this.language, this.country));
	}

	public static Locale getLocale() {
		Locale currentLocale;

		currentLocale = new Locale(getDeflang(), getDefcountry());
		return currentLocale;
	}

	/**
	 * Localize message
	 * 
	 * @param key
	 *            the key
	 * @return the string
	 */
	public String m(String key) {
		String s = key;
		try {
			s = messages.getString(key);
		} catch (Exception e) {
			s = key;
		}
		return s;
	}

	/**
	 * Localize message with params
	 * 
	 * @param key
	 *            the key
	 * @param par
	 *            the par
	 * @return the string
	 */
	public String m(String key, Object... par) {
		String msg = m(key);
		return MessageFormat.format(msg, par);
	}

	/**
	 * Sets the deflang.
	 * 
	 * @param deflang
	 *            the deflang to set
	 */
	public static void setDeflang(String deflang) {
		Localizable.deflang = deflang;
	}

	/**
	 * Gets the deflang.
	 * 
	 * @return the deflang
	 */
	public static String getDeflang() {
		return deflang;
	}

	/**
	 * Sets the defcountry.
	 * 
	 * @param defcountry
	 *            the defcountry to set
	 */
	public static void setDefcountry(String defcountry) {
		Localizable.defcountry = defcountry;
	}

	/**
	 * Gets the defcountry.
	 * 
	 * @return the defcountry
	 */
	public static String getDefcountry() {
		return defcountry;
	}

}
