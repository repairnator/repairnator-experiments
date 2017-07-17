package org.grobid.core.utilities;

import org.grobid.core.exceptions.GrobidException;
import org.grobid.core.lang.Language;
import org.grobid.core.lang.LanguageDetectorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for using language guessers (singleton).
 * 
 * @author Patrice Lopez
 * @author Vyacheslav Zholudev
 */
public class LanguageUtilities {
	public static final Logger LOGGER = LoggerFactory
			.getLogger(LanguageUtilities.class);

	private static LanguageUtilities instance = null;

	private boolean useLanguageId = false;
	private LanguageDetectorFactory ldf = null;

	public static/* synchronized */LanguageUtilities getInstance() {
		if (instance == null) {
			getNewInstance();
		}
		return instance;
	}

	/**
	 * Return a new instance.
	 */
	protected static synchronized void getNewInstance() {
		// GrobidProperties.getInstance();
		LOGGER.debug("synchronized getNewInstance");
		instance = new LanguageUtilities();
	}

	/**
	 * Hidden constructor
	 */
	private LanguageUtilities() {
		useLanguageId = GrobidProperties.isUseLanguageId();
		if (useLanguageId) {
			String className = GrobidProperties.getLanguageDetectorFactory();
			try {
				ldf = (LanguageDetectorFactory) Class.forName(className)
						.newInstance();
			} catch (ClassCastException e) {
				throw new GrobidException("Class " + className
						+ " must implement "
						+ LanguageDetectorFactory.class.getName(), e);
			} catch (ClassNotFoundException e) {
				throw new GrobidException(
						"Class "
								+ className
								+ " were not found in the classpath. "
								+ "Make sure that it is provided correctly is in the classpath", e);
			} catch (InstantiationException e) {
				throw new GrobidException("Class " + className
						+ " should have a default constructor", e);
			} catch (IllegalAccessException e) {
				throw new GrobidException(e);
			}
		}
	}

	/**
	 * Basic run for language identification, return the language code and
	 * confidence score separated by a semicolon
	 * 
	 * @param text
	 *            text to classify
	 * @return language ids concatenated with ;
	 */
	public Language runLanguageId(String text) {
		if (!useLanguageId) {
			return null;
		}
        try {
            return ldf.getInstance().detect(text);
        } catch (Exception e) {
            LOGGER.warn("Cannot detect language because of: " + e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
    }

	/**
	 * Less basic run for language identification, where a maxumum length of text is used to 
	 * identify the language. The goal is to avoid wasting resources using a too long piece of 
	 * text, when normally only a small chunk is enough for a safe language prediction.  
	 * Return a Language object consisting of the language code and a confidence score.
	 * 
	 * @param text
	 *            text to classify
	 * @param maxLength 
	 *   		  maximum length of text to be used to identify the language, expressed in characters 	
	 * @return language Language object consisting of the language code and a confidence score
	 */
	public Language runLanguageId(String text, int maxLength) {
		if (!useLanguageId) {
			return null;
		}
        try {
			int max = text.length();
			if (maxLength < max)
				max = maxLength; 
            return ldf.getInstance().detect(text.substring(0, max));
        } catch (Exception e) {
            LOGGER.warn("Cannot detect language because of: " + e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
    }

}