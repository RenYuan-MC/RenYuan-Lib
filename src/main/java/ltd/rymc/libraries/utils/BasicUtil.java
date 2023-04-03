package ltd.rymc.libraries.utils;

import java.util.Arrays;

public class BasicUtil {


    public static String[] arraysFilter(String[] arr,String fil) {
        return Arrays.stream(arr).filter(s -> !fil.equals(s)).toArray(String[]::new);
    }
}
