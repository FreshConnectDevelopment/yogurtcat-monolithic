package org.yogurtcat.server.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.pdf.BaseFont;

import lombok.extern.slf4j.Slf4j;

/**
 * PDF工具类
 * @author s
 *
 */
@Slf4j
public class PdfUtil {

	/**
	 * 导出pdf
	 * @param content 必须是标准的XHTML页面
	 * @param response
	 * @param fileName
	 */
	public static void exportPdf(String content, HttpServletResponse response, String fileName) {
		
		content = toXhtml(content);
		
		String path = IdUtil.simpleUuid() + ".pdf";
		File file = new File(path);
		byte[] fileBytes = null;
		try (OutputStream outputStream = new FileOutputStream(file)) {
			
			ITextRenderer renderer = new ITextRenderer();
			
			renderer.setDocumentFromString(content);
			// 解决中文问题
			ClassPathResource simsun = new ClassPathResource("font/simsun.ttc");
			renderer.getFontResolver().addFont(simsun.getFile().getPath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			renderer.layout();
			renderer.createPDF(outputStream);
			renderer.finishPDF();
			
			fileBytes = FileUtils.readFileToByteArray(file);

			WordUtil.setBrowser(response, fileBytes, fileName);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			FileUtils.deleteQuietly(file);
		}
		
		//return fileBytes;
	}
	
	/**
	 * 拼接XHTML
	 * @param content
	 * @return
	 */
	private static String toXhtml(String content) {
		String h1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		String h2 = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
		String h3 = "<html xmlns=\"http://www.w3.org/1999/xhtml\">";
		String h4 = "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><title>title</title><style>body{FONT-FAMILY: SimSun}</style></head><body>";
		String l1 = "</body></html>";
		
		return h1 + h2 + h3 + h4 + content + l1;
	}
}
