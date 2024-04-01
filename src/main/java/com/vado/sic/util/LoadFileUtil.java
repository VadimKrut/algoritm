package com.vado.sic.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class LoadFileUtil {

    public static byte[] getBytes(int i) throws IOException {
        byte[] array = Files.readAllBytes(Paths.get("src\\main\\resources\\file\\164MB.txt"));
        array = Arrays.copyOf(array, array.length * i);
        return array;
    }
}