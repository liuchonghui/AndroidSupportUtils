package android.compact.utils;

public class StringCompactUtil {

    public static boolean hasEmptyString(String... args) {
        if (args == null) {
            return false;
        }
        boolean hasEmpty = false;
        if (args.length > 0) {
            for (String item : args) {
                if (item == null || item.length() == 0) {
                    hasEmpty = true;
                    break;
                }
            }
        }
        return hasEmpty;
    }

    public static boolean hasLegalString(String... args) {
        if (args == null) {
            return false;
        }
        boolean hasLegal = false;
        if (args.length > 0) {
            for (String item : args) {
                if (item != null && item.length() > 0) {
                    hasLegal = true;
                    break;
                }
            }
        }
        return hasLegal;
    }

    public static String getSingleStringOrNot(String... args) {
        if (args == null) {
            return null;
        }
        int i = 0;
        String result = null;
        if (args.length > 0) {
            for (String item : args) {
                if (item != null && item.length() > 0) {
                    result = item;
                    i = i + 1;
                }
            }
        }
        if (i == 1) {
            return result;
        }
        return null;
    }
}
