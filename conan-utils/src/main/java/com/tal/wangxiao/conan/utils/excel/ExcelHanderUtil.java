package com.tal.wangxiao.conan.utils.excel;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: dengkunnan
 * @date: 2020-07-12 00:45
 * @description: Excel 读取和生成
 **/
public class ExcelHanderUtil {

    /**
     * 从输入流中读取excel文件，并转换成List<T></>对象
     *
     * @param inputStream
     * @param className
     * @param <T>
     * @return
     */
    public static <T> List<T> readToList(InputStream inputStream, Class<T> className) {
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<T> list = reader.readAll(className);
        return list;
    }


    /**
     * 从输入流中读取excel文件，并转换成List<T></>对象
     *
     * @param excelFile
     * @param className
     * @param <T>
     * @return
     */
    public static <T> List<T> readToList(File excelFile, Class<T> className) {
        ExcelReader reader = ExcelUtil.getReader(excelFile);
        List<T> list = reader.readAll(className);
        return list;
    }


    /**
     * 从输入流中读取excel文件，并转换成List<T></>对象
     *
     * @param excelFile
     * @param className
     * @param headerRowIndex 标题所在行，如果标题行在读取的内容行中间，这行做为数据将忽略，，从0开始计数
     * @param startRowIndex  起始行（包含，从0开始计数）
     * @param <T>
     * @return
     */
    public static <T> List<T> readToList(int headerRowIndex, int startRowIndex, File excelFile, Class<T> className) {
        ExcelReader reader = ExcelUtil.getReader(excelFile);
        List<T> list = reader.read(headerRowIndex, startRowIndex, Integer.MAX_VALUE, className);
        return list;
    }


    /**
     * 从输入流中读取excel文件，并转换成List<T></>对象
     *
     * @param excelFile
     * @param className
     * @param sheetNumber sheet 编号
     * @return
     */
    public static <T> List<T> readToList(File excelFile, Class<T> className, Integer sheetNumber) {
        ExcelReader reader = ExcelUtil.getReader(excelFile, sheetNumber);
        List<T> list = reader.readAll(className);
        return list;
    }


    /**
     * 从输入流中读取excel文件，并转换成List<T></>对象
     *
     * @param excelFile
     * @param className
     * @param sheetName sheet 名称
     * @return
     */
    public static <T> List<T> readToList(File excelFile, Class<T> className, String sheetName) {
        ExcelReader reader = ExcelUtil.getReader(excelFile, sheetName);
        List<T> list = reader.readAll(className);
        return list;
    }


    /**
     * 生成Excel文件
     *
     * @param pathName eg:d:/writeTest.xlsx
     * @param rows     行集合
     * @param title    标题
     * @return
     */
    public static void excelWriter(String pathName, List<List<String>> rows, String title) {
        //通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter(pathName);
        //通过构造方法创建writer
        if (!StringUtils.isEmpty(title)) {
            //跳过当前行，既第一行，非必须，在此演示用
            //writer.passCurrentRow();

            //合并单元格后的标题行，使用默认标题样式
            writer.merge(rows.get(0).size() - 1, title);
        }
        writer.write(rows, true);
        writer.close();

    }

    /**
     * 生成Excel文件
     *
     * @param pathName eg:d:/writeTest.xlsx
     * @param rows     行集合
     * @param title    标题
     *                 *
     *                 * @param firstRow 起始行，0开始
     *                 * @param lastRow 结束行，0开始
     *                 * @param firstColumn 起始列，0开始
     *                 * @param lastColumn 结束列，0开始
     *                 * @param content 合并单元格后的内容
     *                 * @param isSetHeaderStyle 是否为合并后的单元格设置默认标题样式
     *                 * @return this
     *                 * @since 4.0.10
     * @return
     */
    public static void excelWriter(int firstRow, int lastRow, String pathName, List<List<String>> rows, String title) {
        //通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter(pathName);
        //通过构造方法创建writer
        if (!StringUtils.isEmpty(title)) {
            //合并单元格后的标题行，使用默认标题样式
            writer.merge(firstRow, lastRow, 0, rows.get(0).size() - 1, title, true);

        }
        for (int i = 0; i <= lastRow; i++) {
            writer.passCurrentRow();
        }

        writer.write(rows, true);
        writer.close();

    }

    /**
     * 生成Excel文件
     *
     * @param fileName eg:d:/writeTest.xlsx
     * @param rows     行集合
     * @param title    标题
     *                 *
     *                 * @param firstRow 起始行，0开始
     *                 * @param lastRow 结束行，0开始
     *                 * @param firstColumn 起始列，0开始
     *                 * @param lastColumn 结束列，0开始
     *                 * @param content 合并单元格后的内容
     *                 * @param isSetHeaderStyle 是否为合并后的单元格设置默认标题样式
     *                 * @return this
     *                 * @since 4.0.10
     * @return
     */
    public static File excelWriterReturnFile(int firstRow, int lastRow, String fileName, List<List<String>> rows, String title) {
        File file = new File(fileName);
        //通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter(file);
        //通过构造方法创建writer
        if (!StringUtils.isEmpty(title)) {
            //合并单元格后的标题行，使用默认标题样式
            writer.merge(firstRow, lastRow, 0, rows.get(0).size() - 1, title, true);

        }
        for (int i = 0; i <= lastRow; i++) {
            writer.passCurrentRow();
        }

        writer.write(rows, true);
        writer.close();
        return file;
    }


}
