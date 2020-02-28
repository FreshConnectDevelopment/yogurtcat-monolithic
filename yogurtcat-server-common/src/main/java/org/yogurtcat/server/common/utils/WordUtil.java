package org.yogurtcat.server.common.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import lombok.extern.slf4j.Slf4j;

/**
 * word文件工具类
 * @author s
 *
 */
@Slf4j
public class WordUtil {
	
	private static final String DOCX = ".docx";
	private static final String DOC = ".doc";


	/**
	 * 读取Word文件
	 * @param doc
	 * @return
	 */
	public static String readDoc(String doc) {
	    String text = null;
	    try {
	        File file = new File(doc);
	        String fileName = file.getName();
	        int a  = fileName.lastIndexOf(".");
	        String name = fileName.substring(a,fileName.length());

	        if(DOC.equals(name)){
	            // 创建输入流读取DOC文件
	        	try (FileInputStream in = new FileInputStream(file)) {
		            // 创建WordExtractor
		            WordExtractor extractor = new WordExtractor(in);
		            // 对DOC文件进行提取
		            text = extractor.getTextFromPieces();
		            extractor.close();
	        	}
	        }else if(DOCX.equals(name)){
	        	try (FileInputStream fis = new FileInputStream(file)) {
	        		
		            XWPFDocument xdoc = new XWPFDocument(fis);
		            XWPFWordExtractor wordExtractor = new XWPFWordExtractor(xdoc);
		            text = wordExtractor.getText();
		            wordExtractor.close();
	        	}
	            
	        }else{
	            text = "文件格式不符";
	        }
	    } catch (Exception e) {
	        log.error("Read word file fails.", e);
	    }
	    return text;
	}

	 /**
	  * 将内容写成一个doc文件导出
	  * @param path
	  * @param content
	  * @throws FileNotFoundException
	  * @throws IOException
	  */
	public static void writeDoc(HttpServletResponse response, String content, String fileName) {
	    content+="\r\n";
        byte b[] = content.getBytes();
        String path = IdUtil.simpleUuid() + ".pdf";
        File file = new File(path);
	    try (ByteArrayInputStream bais = new ByteArrayInputStream(b);
	    		FileOutputStream ostream = new FileOutputStream(file)) {
	        
	        POIFSFileSystem fs = new POIFSFileSystem(); 
	        fs.getRoot().createDocument("WordDocument", bais);
	        fs.writeFilesystem(ostream);
	        fs.close();
	        
	        setBrowser(response, FileUtils.readFileToByteArray(file), fileName);
	    } catch (Exception e) {
	    	log.error(e.getMessage(), e);
		} finally {
			FileUtils.deleteQuietly(file);
		}
	}
	
	
	/**
	 * 使用浏览器下载
	 * @param response
	 * @param fileBytes
	 * @param fileName
	 * @throws IOException
	 */
    public static void setBrowser(HttpServletResponse response, byte[] fileBytes, String fileName) throws IOException {
        //清空response
        response.reset();
        // 允许的跨域
        response.setHeader("Access-Control-Allow-Origin", "*");
        //设置response的Header
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        OutputStream os = response.getOutputStream();
        response.setContentType("application/octet-stream;charset=utf-8");
        os.write(fileBytes);
        os.flush();
        os.close();
    }
}