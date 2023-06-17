package org.black_ixx.bossshop.managers.misc;

import java.util.List;

public class StringManipulationLib {
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
}
