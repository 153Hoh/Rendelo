import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class ExcelInterface {

    static List<Data> getDatafromXls(String file_path) throws IOException, InvalidFormatException {
        InputStream inp = new FileInputStream(file_path);
        Workbook wb = WorkbookFactory.create(inp);
        Sheet sheet = wb.getSheetAt(0);
        int num = sheet.getPhysicalNumberOfRows();
        //System.out.println(num);
        Row row;
        List<Data> data = new ArrayList<>();
        int ind = 0;
        for(int i = 1; i < num; i++){
            row = sheet.getRow(i);
            double mink = 0;
            double afak = 0;
            double masark = 0;
            //System.out.println(row.getCell(3).toString() + ";" + row.getCell(5).toString() + ";" + row.getCell(6).toString());
            if(!row.getCell(3).toString().equals("") && row.getCell(3).toString().matches("^(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?$")){
                mink = Double.parseDouble(row.getCell(3).toString());
            }
            if(!row.getCell(5).toString().equals("") && row.getCell(5).toString().matches("^(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?$")){
                afak = Double.parseDouble(row.getCell(5).toString());
            }
            if(!row.getCell(6).toString().equals("") && row.getCell(6).toString().matches("^(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?$")){
                masark = Double.parseDouble(row.getCell(6).toString());
            }
            data.add(new Data(row.getCell(0).toString(), row.getCell(1).toString(),row.getCell(2).toString(),mink,row.getCell(4).toString(),afak,masark,ind));
            ind++;
            //System.out.println(row.getCell(0) + ":" + row.getCell(1) + ":" + row.getCell(2) + ":" + mink + ":" + row.getCell(4) + ":" + afak + ":" + masark);
            //m.add(new Massage(row.getCell(0).toString(),row.getCell(1).toString(),row.getCell(2).toString(),row.getCell(3).toString()));
        }

        /*for(Data d : data){
            System.out.println(d.getCC() + ":" + d.getCS() + ":" + d.getCN() + ":" + d.getMin() + ":" + d.getME() + ":" + d.getAfa() + ":" + d.getMA());
        }*/
        return data;
    }

    static void SaveAsXls(String name, List<Data> data) throws IOException {
        Workbook wb = new XSSFWorkbook();
        String safeName = WorkbookUtil.createSafeSheetName("[Adatok*?]"); // returns " O'Brien's sales   "
        Sheet sheet = wb.createSheet(safeName);
        Font font = wb.createFont();
        font.setFontHeightInPoints((short)8);
        font.setFontName("Arial");
        CellStyle bal = wb.createCellStyle();
        bal.setFont(font);
        bal.setAlignment(HorizontalAlignment.LEFT);
        CellStyle kozep = wb.createCellStyle();
        kozep.setFont(font);
        kozep.setAlignment(HorizontalAlignment.CENTER);
        CellStyle jobb = wb.createCellStyle();
        jobb.setFont(font);
        jobb.setAlignment(HorizontalAlignment.RIGHT);
        Row fej = sheet.createRow((short)0);
        fej.createCell(0).setCellStyle(bal);
        fej.getCell(0).setCellValue("Cikkszám");
        fej.createCell(1).setCellStyle(bal);
        fej.getCell(1).setCellValue("Cikk név");
        fej.createCell(2).setCellStyle(kozep);
        fej.getCell(2).setCellValue("Min. rendelhető");

        for(int i = 0; i < data.size(); i++){
            Row row = sheet.createRow((short)i+1);
            row.createCell(0).setCellStyle(bal);
            row.getCell(0).setCellValue(data.get(i).getCS());
            row.createCell(1).setCellStyle(bal);
            row.getCell(1).setCellValue(data.get(i).getCN());
            row.createCell(2).setCellStyle(kozep);
            row.getCell(2).setCellValue(data.get(i).getMin());
        }
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        FileSystemView filesys = FileSystemView.getFileSystemView();
        String asztal = filesys.getHomeDirectory().toString();
        FileOutputStream fileOut = new FileOutputStream(asztal + "\\" + name + "i rendelés.xlsx");
        wb.write(fileOut);
        fileOut.close();
    }

    static List<Data> getDataFromOds(String file_name) throws IOException {
        File file = new File(file_name);
        final org.jopendocument.dom.spreadsheet.Sheet osheet = SpreadSheet.createFromFile(file).getSheet(0);
        List<Data> data = new ArrayList<>();
        int y = 0;
        String tmp = "a";
        while(!tmp.equals("")){
            tmp = osheet.getValueAt(0,y).toString();
            y++;
        }
        int ind = 0;
        for(int i = 1; i < y - 1 ; i++){
            double mink = 0;
            double afak = 0;
            double masark = 0;
            //System.out.println(row.getCell(3).toString() + ";" + row.getCell(5).toString() + ";" + row.getCell(6).toString());
            if(!osheet.getValueAt(3,i).toString().equals("") && osheet.getValueAt(3,i).toString().matches("^(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?$")){
                mink = Double.parseDouble(osheet.getValueAt(3,i).toString());
            }
            if(!osheet.getValueAt(5,i).toString().equals("") && osheet.getValueAt(5,i).toString().matches("^(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?$")){
                afak = Double.parseDouble(osheet.getValueAt(5,i).toString());
            }
            if(!osheet.getValueAt(6,i).toString().equals("") && osheet.getValueAt(6,i).toString().matches("^(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?$")){
                masark = Double.parseDouble(osheet.getValueAt(6,i).toString());
            }
            data.add(new Data(osheet.getValueAt(0,i).toString(),osheet.getValueAt(1,i).toString(),osheet.getValueAt(2,i).toString(),mink,osheet.getValueAt(4,i).toString(),afak,masark,ind));
            ind++;
                //System.out.println(row.getCell(0) + ":" + row.getCell(1) + ":" + row.getCell(2) + ":" + mink + ":" + row.getCell(4) + ":" + afak + ":" + masark);
                //m.add(new Massage(row.getCell(0).toString(),row.getCell(1).toString(),row.getCell(2).toString(),row.getCell(3).toString()));
        }
        return data;
    }

    static void saveAsOds(String name, List<Data> data) throws IOException {
        String[]columns = new String[]{"Cikkszám", "Cikk név", "Min. rendelhető"};
        final Object[][] tableData = new Object[data.size()][7];
       // System.out.println(data.size());
        for(int i = 0; i < data.size(); i++){
            tableData[i] = new Object[]{data.get(i).getCS(), data.get(i).getCN(), data.get(i).getMin()};
        }
        TableModel model = new DefaultTableModel(tableData,columns);
        FileSystemView filesys = FileSystemView.getFileSystemView();
        String asztal = filesys.getHomeDirectory().toString();
        final File file = new File(asztal + "\\" + name + "i rendelés.ods");
        SpreadSheet.createEmpty(model).saveAs(file);
    }
}
