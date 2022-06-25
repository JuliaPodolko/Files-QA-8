package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import static com.codeborne.pdftest.assertj.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HomeWorkParsingTest {
    ClassLoader classLoader = getClass().getClassLoader();

    @Test
    void parseZipTest() throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/files/file_example.zip"));
        ZipInputStream is = new ZipInputStream(classLoader.getResourceAsStream("files/file_example.zip"));
        ZipEntry entry;
        while ((entry = is.getNextEntry()) != null) {
            switch (entry.getName()) {
                case ("file_example_XLSX_10.xlsx"):
                    assertEquals(entry.getName(), "file_example_XLSX_10.xlsx");
                    try (InputStream inputStream = zf.getInputStream(entry)) {
                        assert inputStream != null;
                        XLS xls = new XLS(inputStream);
                        String value = xls.excel.
                                getSheetAt(0).
                                getRow(7).
                                getCell(1).
                                getStringCellValue();
                        assertThat(value).isEqualTo("Etta");
                    }
                    break;
                case ("file_example_CSV_5000.csv"):
                    assertEquals(entry.getName(), "file_example_CSV_5000.csv");
                    try (InputStream inputStream = zf.getInputStream(entry)) {
                        assert inputStream != null;
                        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                            List<String[]> content = reader.readAll();
                            assertThat(content.get(1)).contains(
                                    "1",
                                    "Dulce",
                                    "Abril",
                                    "Female",
                                    "United States",
                                    "32",
                                    "15/10/2017",
                                    "1562"
                            );
                        }
                    }
                    break;
                case ("file-sample_150kB.pdf"):
                    assertEquals(entry.getName(), "file-sample_150kB.pdf");
                    try (InputStream inputStream = zf.getInputStream(entry)) {
                        assert inputStream != null;
                        PDF pdf = new PDF(inputStream);
                        assertThat(pdf.text).contains("Vestibulum neque massa");
                    }
                    break;
            }

        }
    }
}