package com.wldk.tick.controller;

import org.apache.poi.hwpf.extractor.WordExtractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class WordTest {

    public static void main(String[] args) {
        String filePath="/Users/nx-1/Downloads/丰台区/20200617100059DOC.doc";
//        String filePath="E:\\java导入word表格.docx";
//        test.testWord(filePath);
        getTextFromWord(filePath);
    }
    /**
     *
     * @Title: getTextFromWord
     * @Description: 读取word
     * @param filePath
     *            文件路径
     * @return: String 读出的Word的内容
     */
    public static String getTextFromWord(String filePath) {
        String result = null;
//        filePath="E:\\java导入word表格.docx";
        File file = new File(filePath);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            @SuppressWarnings("resource")
            WordExtractor wordExtractor = new WordExtractor(fis);
            result = wordExtractor.getText();
            System.err.println("result"+ "========================= "+result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}