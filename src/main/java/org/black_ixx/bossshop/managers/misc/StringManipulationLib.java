package org.black_ixx.bossshop.managers.misc;

import java.util.List;

public class StringManipulationLib {


    /**
     * Replace placeholders in a string
     * @param s input string
     * @param name name of placeholder
     * @param replacement  replacement string
     * @param fromIndex index
     * @return replaced string
     */
    public static String replacePlaceholder(String s, String name, String replacement, int fromIndex) {
        String complete = getCompleteVariable(s, name, fromIndex);
        if (complete != null) {
            return s.replace(complete, replacement);
        }
        return s;
    }

    public static String figureOutVariable(String s, int fromIndex, String... names) {
        for (String name : names) {
            String variable = figureOutVariable(s, name, fromIndex);
            if (variable != null) {
                return variable;
            }
        }
        return null;
    }

    public static String figureOutVariable(String s, String name, int fromIndex) {
        String symbol = "%";
        String start = symbol + name + "_";
        String complete = getCompleteVariable(s, name, fromIndex);
        if (complete != null) {
            String variable = complete.substring(start.length(), complete.length() - symbol.length());
            return variable;
        }
        return null;
    }

    public static String getCompleteVariable(String s, String name, int fromIndex) {
        String symbol = "%";
        String start = symbol + name + "_";
        if (s.contains(start)) {
            int firstStart = s.indexOf(start, fromIndex);
            if (firstStart != -1) {
                int firstEnd = s.indexOf(symbol, firstStart + 1);
                if (firstEnd != -1) {
                    String complete = s.substring(firstStart, firstEnd + 1);
                    return complete;
                }
            }
        }
        return null;
    }

    public static int getIndexOfVariableEnd(String s, String name, int fromIndex) {
        String symbol = "%";
        String start = symbol + name + "_";
        if (s.contains(start)) {
            int firstStart = s.indexOf(start, fromIndex);
            int firstEnd = s.indexOf(symbol, firstStart + 1);
            return firstEnd;
        }
        return -1;
    }

    public static String formatList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        String output = null;
        for (String s : list) {
            if (output == null) {
                output = s;
            } else {
                output += "\n" + s;
            }
        }
        return output;
    }


    public static String getBlock(String s, String beginning, String end, int fromIndex) {
        if (s.contains(beginning) && s.contains(end)) {
            int firstStart = s.indexOf(beginning, fromIndex);
            if (firstStart != -1) {
                int firstEnd = s.indexOf(end, firstStart + 1);
                if (firstEnd != -1) {
                    String complete = s.substring(firstStart, firstEnd + 1);
                    return complete;
                }
            }
        }
        return null;
    }

    public static int getIndexOfBlockEnd(String s, String beginning, String end, int fromIndex) {
        if (s.contains(beginning) && s.contains(end)) {
            int firstStart = s.indexOf(beginning, fromIndex);
            if (firstStart != -1) {
                int firstEnd = s.indexOf(end, firstStart + 1);
                return firstEnd;
            }
        }
        return -1;
    }


}
