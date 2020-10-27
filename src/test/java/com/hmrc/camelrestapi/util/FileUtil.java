package com.hmrc.camelrestapi.util;

import org.apache.commons.io.FileUtils;
import org.springframework.util.ResourceUtils;

import java.io.IOException;

public class FileUtil {
    public static String readFileToString(String filePath) {

        try {
            return FileUtils.readFileToString(ResourceUtils.getFile(filePath), "UTF-8");

        } catch (IOException e) {
            return "";
        }
    }
}
