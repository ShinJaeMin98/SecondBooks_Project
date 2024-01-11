package org.choongang.commons;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ExcelUtils {

    private List<String> sqlData; // 생성된 SQL 데이터 담을 공간

    /**
     * 엑셀 데이터 추출
     *
     * @param filePath : 엑셀 파일 경로
     * @param cellNums : 데이터로 추출할 셀번호, 0번 부터 시작
     * @param sheetNo : 엑셀 시트 번호, 0번 부터 시작
     * @return
     */
    public List<String[]> getData(String filePath, int[] cellNums, int sheetNo) {
        Path path = Path.of(filePath);
        sheetNo = sheetNo < 0 ? 0 : sheetNo; // sheetNo가 0미만 인 경우 첫번째 시트인 0으로 변경

        if (cellNums == null || cellNums.length == 0) {
            return null;
        }

        List<String[]> data = new ArrayList<>();
        try (InputStream in = Files.newInputStream(path, StandardOpenOption.READ);
             OPCPackage opcPackage = OPCPackage.open(in)) {

            XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
            XSSFSheet sheet = workbook.getSheetAt(sheetNo);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // 행별 데이터 추출
                XSSFRow row = sheet.getRow(i);

                String[] items = new String[cellNums.length];
                for (int j = 0; j < cellNums.length; j++) {
                    items[j] = getCellData(row.getCell(cellNums[j]));
                }

                data.add(items);
            }

        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }

        return data;
    }

    /**
     * 엑셀 데이터를 문자열로 결합
     *
     * @param delimiter : 구분 문자
     * @return
     */
    public List<String> getData(String filePath, int[] cellNums, int sheetNo, String delimiter) {

        return getData(filePath, cellNums, sheetNo).stream().map(s -> Arrays.stream(s).collect(Collectors.joining(delimiter))).toList();
    }

    public String getCellData(XSSFCell cell) {
        if (cell == null) return "";

        // 데이터 형식 관련 오류 방지를 위해 문자가 아닌 자료형인 경우 문자형으로 변경
        if (cell.getCellType() != CellType.STRING) {
            cell.setCellType(CellType.STRING);
        }

        return Objects.requireNonNullElse(cell.getStringCellValue(), "");
    }

    /**
     * SQL로 생성
     *
     * @param filePath : 파일 경로
     * @param cellNums : 데이터로 추출할 셀번호, 0번 부터 시작
     * @param sheetNo : 엑셀 시트 번호, 0번 부터 시작
     * @param tableNm : 테이블명  예) CENTER_INFO
     * @param fields : SQL 생성 필드 예) new String[] { "location", "centerNm", "centerType", "address", "tel"};
     * @return
     */
    public ExcelUtils makeSql(String filePath, int[] cellNums, int sheetNo, String tableNm, String[] fields) {
        sqlData = new ArrayList<>();

        List<String[]> lines = getData(filePath, cellNums,sheetNo);
        if (lines == null || lines.isEmpty() || fields == null || fields.length == 0) {
            return this;
        }

        lines.forEach(line -> {
            StringBuffer sb = new StringBuffer(3000);
            sb.append("INSERT INTO ");
            sb.append(tableNm);
            sb.append(" (");
            sb.append(Arrays.stream(fields).collect(Collectors.joining(",")));
            sb.append(" ) VALUES (");
            sb.append(Arrays.stream(line).map(s -> "\"" + s + "\"").collect(Collectors.joining(",")));
            sb.append(");\n");
            sqlData.add(sb.toString());
        });

        return this;
    }

    /**
     * 가공된 SQL 데이터 반환
     *
     * @return
     */
    public List<String> toList() {
        return sqlData;
    }

    /**
     * 가공된 SQL 데이터 SQL로 파일 작성
     *
     * @param destination : 생성될 파일 경로
     */
    public void toFile(String destination) {
        if (sqlData == null || sqlData.isEmpty()) {
            return;
        }

        Path path = Path.of(destination);

        try {
            Files.write(path, sqlData, Charset.forName("UTF-8"), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}