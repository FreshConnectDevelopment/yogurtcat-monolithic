package org.yogurtcat.server.common.utils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.yogurtcat.server.common.utils.lang.ExcelData;

import com.google.common.io.Files;

import lombok.extern.slf4j.Slf4j;

/**
 * excel工具类
 * @author s
 *
 */
@Slf4j
public class ExcelUtil {
	
	private static final String XLS = "xls";

	/**
	 * 导出Excel
	 * @param response
	 * @param data
	 * @throws Exception 
	 */
    public static void exportExcel(HttpServletResponse response, ExcelData data) throws Exception {
        try {
            //实例化HSSFWorkbook
            HSSFWorkbook workbook = new HSSFWorkbook();
            //创建一个Excel表单，参数为sheet的名字
            HSSFSheet sheet = workbook.createSheet("sheet");
            //设置表头
            setTitle(workbook, sheet, data.getHead());
            //设置单元格并赋值
            setData(sheet, data.getData());
            //设置浏览器下载
            setBrowser(response, workbook, data.getFileName());
        } catch (Exception e) {
            log.error("导出解析失败!", e);
            throw e;
        }
    }
 
    /**
     * 设置表头
     * @param workbook
     * @param sheet
     * @param str
     */
    private static void setTitle(HSSFWorkbook workbook, HSSFSheet sheet, String[] str) {
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        for (int i = 0; i <= str.length; i++) {
            sheet.setColumnWidth(i, 15 * 256);
        }
        //设置为居中加粗,格式化时间格式
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        //创建表头名称
        HSSFCell cell;
        for (int j = 0; j < str.length; j++) {
            cell = row.createCell(j);
            cell.setCellValue(str[j]);
            cell.setCellStyle(style);
        }
    }
 
    /**
     * 表格赋值
     * @param sheet
     * @param data
     */
    private static void setData(HSSFSheet sheet, List<String[]> data) {
        int rowNum = 1;
        for (int i = 0; i < data.size(); i++) {
            HSSFRow row = sheet.createRow(rowNum);
            for (int j = 0; j < data.get(i).length; j++) {
                row.createCell(j).setCellValue(data.get(i)[j]);
            }
            rowNum++;
        }
    }
 
    /**
     * 使用浏览器下载
     * @param response
     * @param workbook
     * @param fileName
     * @throws IOException 
     */
    private static void setBrowser(HttpServletResponse response, HSSFWorkbook workbook, String fileName) throws IOException {
        //清空response
        response.reset();
        // 允许的跨域
        response.setHeader("Access-Control-Allow-Origin", "*");
        //设置response的Header
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        OutputStream os = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream;charset=utf-8");
        //将excel写入到输出流中
        workbook.write(os);
        os.flush();
        os.close();
    }
 
 
    
    
    /**
	 * excel加载并解析，仅支持xls和xlxs格式
	 * 
	 * @param inputStream 文件流
	 * @param excelName   文件名
	 * @param sheetAt     需要解析的sheet表，座标1开始
	 * @param startRow    需要解析的row，座标1开始
	 * @return List<String[]>
	 */
	public static List<String[]> load(InputStream inputStream, String excelName, int sheetAt, int startRow) {
		// 文件扩展名
		String ext = Files.getFileExtension(excelName);
		sheetAt = sheetAt - 1;
		startRow = startRow - 1;
		try {
			if (XLS.equals(ext)) {
				POIFSFileSystem poi = new POIFSFileSystem(inputStream);
				HSSFWorkbook work = new HSSFWorkbook(poi);
				return readXls(work, sheetAt, startRow);
			} else {
				XSSFWorkbook work = new XSSFWorkbook(OPCPackage.open(inputStream));
				return readXlsx(work, sheetAt, startRow);
			}
		} catch (Exception e) {
			log.error("load excel file is error.", e);
		}
		return new ArrayList<String[]>();
	}

	/**
	 * 解析07+版xls
	 * @param work
	 * @param sheetAt
	 * @param startRow
	 * @return
	 */
	private static List<String[]> readXlsx(XSSFWorkbook work, int sheetAt, int startRow) {
		int allsheet = work.getNumberOfSheets();
		if (sheetAt >= allsheet) {
			return new ArrayList<String[]>(0);
		}

		XSSFSheet sheet = work.getSheetAt(sheetAt);
		if (sheet == null) {
			return new ArrayList<String[]>(0);
		}
		// 这里比较特殊，cell个数计算正确，row个数需要加1才对
		int rowNum = sheet.getLastRowNum() + 1;

		List<String[]> resultList = new ArrayList<String[]>(rowNum);
		for (int i = startRow; i < rowNum; i++) {

			XSSFRow row = sheet.getRow(i);
			if (row == null) {
				break;
			}

			short cellsnum = row.getLastCellNum();
			if (-1 == cellsnum) {
				continue;
			}
			String[] cells = new String[cellsnum];

			for (int k = 0; k < cellsnum; k++) {
				XSSFCell cell = row.getCell((short) k);
				Object val = getCellValue(cell);
				cells[k] = val == null || "".equals(val.toString()) ? "" : val.toString();
			}
			resultList.add(cells);
		}
		return resultList;
	}

	/**
	 * <解析03版xls>
	 * @param work
	 * @param sheetAt
	 * @param startRow
	 * @param endRow
	 * @return
	 */
	private static List<String[]> readXls(HSSFWorkbook work, int sheetAt, int startRow) {
		int allsheet = work.getNumberOfSheets();
		if (sheetAt >= allsheet) {
			return new ArrayList<String[]>(0);
		}
		HSSFSheet sheet = work.getSheetAt(sheetAt);
		if (sheet == null) {
			return new ArrayList<String[]>(0);
		}

		// 这里比较特殊，cell个数计算正确，row个数需要加1才对
		int rowNum = sheet.getLastRowNum() + 1;

		List<String[]> resultList = new ArrayList<String[]>(rowNum);
		for (int i = startRow; i < rowNum; i++) {

			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				break;
			}

			short cellsnum = row.getLastCellNum();
			if (-1 == cellsnum) {
				continue;
			}
			String[] cells = new String[cellsnum];
			for (int k = 0; k < cellsnum; k++) {
				HSSFCell cell = row.getCell((short) k);
				Object val = getCellValue(cell);
				cells[k] = val == null || "".equals(val.toString()) ? "" : val.toString();
			}
			resultList.add(cells);
		}
		return resultList;
	}

	/**
	 * <根据Cell类型返回正确的值>
	 * @param cell
	 * @return
	 */
	private static Object getCellValue(Cell cell) {

		if ((cell == null) || (CellType.BLANK == cell.getCellType())) {
			return "";
		} else if (CellType.BOOLEAN == cell.getCellType()) {
			return cell.getBooleanCellValue();
		} else if (CellType.FORMULA == cell.getCellType()) {
			return cell.getNumericCellValue();
		} else if (CellType.NUMERIC == cell.getCellType()) {
			// 判断是否为日期
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        return sdf.format(cell.getDateCellValue());
			} else {
				return NumberToTextConverter.toText(cell.getNumericCellValue());
			}
		} else if (CellType.STRING == cell.getCellType()) {
			return cell.getRichStringCellValue().getString();
		} else {
			return cell.getRichStringCellValue().getString();
		}
	}
    
}