package fi.nls.oskari.util;

/**
 * Conversion helper methods
 */
public class ConversionHelper {

    /**
     * Count the number of instances of substring within a string.
     *
     * @param string     String to look for substring in.
     * @param substring  Sub-string to look for.
     * @return           Count of substrings in string.
     */
    public static int count(final String string, final String substring)
    {
        int count = 0;
        int idx = 0;

        while ((idx = string.indexOf(substring, idx)) != -1)
        {
            idx++;
            count++;
        }

        return count;
    }
    /**
     * Returns a string that if its not null and default value if it is
     *
     * @param str
     * @param defaultValue
     * @return string
     */
    public static final String getString(final String str, final String defaultValue) {
        final String value = str;
        if (value != null) {
            return value;
        }
        return defaultValue;
    }

    /**
     * Parses long from String
     *
     * @param strToParse
     * @param defaultValue
     * @return long
     */
    public static final long getLong(final String strToParse, final long defaultValue) {
        try {
            return Long.parseLong(strToParse);
        } catch (Exception e) {
            return defaultValue;
        }   
    }

    /**
     * Parses int from String
     *
     * @param strToParse
     * @param defaultValue
     * @return
     */
    public static final int getInt(final String strToParse, final int defaultValue) {
        try {
            return Integer.parseInt(strToParse);
        } catch (Exception e) {
            return defaultValue;
        }   
    }

    /**
     * Parses double from String
     *
     * @param strToParse
     * @param defaultValue
     * @return
     */
    public static final double getDouble(final String strToParse, final double defaultValue) {
        try {
            return Double.parseDouble(strToParse);
        } catch (Exception e) {
            return defaultValue;
        }   
    }

    /**
     * Parses double from String
     *
     * @param strToParse
     * @param defaultValue
     * @return
     */
    public static final boolean getBoolean(final String strToParse, final boolean defaultValue) {
        try {
            return Boolean.parseBoolean(strToParse);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

}
