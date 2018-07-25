package model.export;

import com.google.common.base.Preconditions;
import init.StartFX;
import model.calendar.CalendarManager;
import model.community.users.User;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ExportToWord implements Export {

    private CalendarManager calendarManager;
    private List<User> usersList;

    public static ExportToWord of(List<User> usersList, CalendarManager calendarManager) {
        Preconditions.checkNotNull(usersList, "Lista użytkowników jest pusta.");
        Preconditions.checkNotNull(calendarManager, "Obiekt kalendarza musi zostać określony.");
        return new ExportToWord(usersList, calendarManager);
    }

    @Override
    public void exportDataToFile(Path path) throws IOException {
        Preconditions.checkArgument(checkIfPathIsValid(path), "Niepoprawna ścieżka lub format pliku.");

        Files.deleteIfExists(path);
        WordElements word = new WordElements();

        usersList.forEach(u -> {
            word.createTopTable(u);
            word.createEmptyLine();
            word.createCalendarTable();
            word.createEmptyLine();
            word.createBottomTable();
        });

        try (FileOutputStream outFile = new FileOutputStream(path.toFile())) {
            word.getXWPFDocument().write(outFile);
        }
    }

    boolean checkIfPathIsValid(Path path)  {
        Preconditions.checkNotNull(path, "Scieżka zapisu musi zostać określona.");
        Path parentDir = path.getParent();
        String fileName = path.getFileName().toString();
        return  (parentDir == null || Files.exists(parentDir)) &&
                (fileName.endsWith(".doc") || fileName.endsWith(".docx"));
    }

    private ExportToWord(List<User> usersList, CalendarManager calendarManager) {
        this.usersList = usersList;
        this.calendarManager = calendarManager;
    }

    private class WordElements {

        private final XWPFDocument document;

        private WordElements() {
            this.document = new XWPFDocument();
            setMarginsProperties();
        }

        private XWPFDocument getXWPFDocument() {
            return this.document;
        }

        private void setMarginsProperties() {
            CTPageMar pageMar = document.getDocument().getBody().addNewSectPr().addNewPgMar();
            pageMar.setTop(BigInteger.valueOf(500));
            pageMar.setBottom(BigInteger.valueOf(200));
        }

        private void createTopTable(User user) {

            String groupName = user.getGroupName();

            XWPFTable topNameTable = document.createTable(1, 2);
            XWPFTableRow mainRow = topNameTable.getRow(0);
            XWPFTableCell mainCell = mainRow.getCell(1);

            mainRow.getCell(0).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(3000));
            mainRow.getCell(1).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(7000));
            mainCell.getCTTc().addNewTcPr().addNewTcBorders().addNewRight().setColor("FFFFFF");
            mainCell.getCTTc().addNewTcPr().addNewTcBorders().addNewBottom().setColor("FFFFFF");
            mainCell.getCTTc().addNewTcPr().addNewTcBorders().addNewTop().setColor("FFFFFF");
            mainCell.removeParagraph(0);

            XWPFParagraph cardName = setFastCellPararagraph(mainCell, ParagraphAlignment.CENTER, 1.5, 0);
            XWPFParagraph cardUserName = setFastCellPararagraph(mainCell, ParagraphAlignment.CENTER, 1.5, 0);
            XWPFParagraph cardUserSection = setFastCellPararagraph(mainCell, ParagraphAlignment.CENTER, 1.5, 0);
            XWPFParagraph cardMonthName = setFastCellPararagraph(mainCell, ParagraphAlignment.CENTER, 1, 0);

            setFastRun(cardName, "Arial", 14, true, "000000", "KARTA EWIDENCJI CZASU PRACY");
            setFastRun(cardUserName, "Times New Roman", 12, true, "000000", String.format("%s %s", user.getFirstName(), user.getLastName()));
            setFastRun(cardUserSection, "Times New Roman", 10, false, "000000", String.format("Dział: %s", groupName));
            setFastRun(cardMonthName, "Times New Roman", 12, true, "000000", String.format("%s %d", StartFX.monthName.getName(calendarManager.getMonth()), calendarManager.getYear())).setCapitalized(true);
        }

        private void createCalendarTable() {
            headerOfCalendarTable();
            contentOfCalendarTable();
        }

        private void createBottomTable() {
            XWPFParagraph workHoursPar = setFastPararagraph(ParagraphAlignment.CENTER, 1);
            setFastRun(workHoursPar, "Times New Roman", 12, true, "000000", String.format("RAZEM GODZIN: %d/ ..........", calendarManager.countWorkDaysInMonth() * 8));

            XWPFParagraph tableLegend = setFastPararagraph(ParagraphAlignment.CENTER, 1);
            setFastRun(tableLegend, "Times New Roman", 8, false, "000000", "Należy zaznaczyć urlopy, zwolnienia od pracy oraz inne usprawiedliwione i nieusprawiedliwione nieobecności w pracy Objaśnienia symboli wyjść: O – zwolnienia w sprawach osobistych	Sł – wyjście w celach służbowych	P – zwolnienia w celach społecznych 	U – pozostałe nieobecn. usprawiedliwione – zwolnienia dla matek karmiących 	S – zwolnienia w celach szkoleniowych.	N -  nieobecności nieusprawiedliwione");
        }

        private void headerOfCalendarTable() {
            XWPFTable contentTablesHeaders = document.createTable(1, 6);

            int[] initials = {1000, 2000, 2000, 2000, 1000, 2000};
            for (int i = 0; i < initials.length; i++) {
                contentTablesHeaders.getRow(0).getCell(i).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(initials[i]));
            }

            contentTablesHeaders.getRow(0).getCell(1).getCTTc().getTcPr().addNewGridSpan();
            contentTablesHeaders.getRow(0).getCell(1).getCTTc().getTcPr().getGridSpan().setVal(BigInteger.valueOf(2));
            contentTablesHeaders.getRow(0).getCell(5).getCTTc().getTcPr().addNewGridSpan();
            contentTablesHeaders.getRow(0).getCell(5).getCTTc().getTcPr().getGridSpan().setVal(BigInteger.valueOf(2));

            for (int index = 0; index < 6; index++) contentTablesHeaders.getRow(0).getCell(index).removeParagraph(0);

            XWPFParagraph tableHeader_Date = setFastCellPararagraph(contentTablesHeaders.getRow(0).getCell(0), ParagraphAlignment.CENTER, 1.15, 0);
            XWPFParagraph tableHeader_WorkHours = setFastCellPararagraph(contentTablesHeaders.getRow(0).getCell(1), ParagraphAlignment.CENTER, 1.15, 0);
            XWPFParagraph tableHeader_WorkerSign = setFastCellPararagraph(contentTablesHeaders.getRow(0).getCell(2), ParagraphAlignment.CENTER, 1.15, 0);
            XWPFParagraph tableHeader_Informations = setFastCellPararagraph(contentTablesHeaders.getRow(0).getCell(3), ParagraphAlignment.CENTER, 1.15, 0);
            XWPFParagraph tableHeader_ExitSymbol = setFastCellPararagraph(contentTablesHeaders.getRow(0).getCell(4), ParagraphAlignment.CENTER, 1.15, 0);
            XWPFParagraph tableHeader_ExitHours = setFastCellPararagraph(contentTablesHeaders.getRow(0).getCell(5), ParagraphAlignment.CENTER, 1.15, 0);

            setFastRun(tableHeader_Date, "Times New Roman", 12, false, "000000", "Data");
            setFastRun(tableHeader_WorkHours, "Times New Roman", 12, false, "000000", "Godziny pracy");
            setFastRun(tableHeader_WorkerSign, "Times New Roman", 12, false, "000000", "Podpis pracownika");
            setFastRun(tableHeader_Informations, "Times New Roman", 9, false, "000000", "Praca w godz.nadliczb w niedz. i święta w dodatk. dni  wolne od pracy dyżury");
            setFastRun(tableHeader_ExitSymbol, "Times New Roman", 9, false, "000000", "Symbol wyjścia");
            setFastRun(tableHeader_ExitHours, "Times New Roman", 12, false, "000000", "Wyjścia w godz. pracy");

            XWPFTable contentTablesMinorHeaders = document.createTable(1, 8);

            initials = new int[]{1000, 1000, 1000, 2000, 2000, 1000, 1000, 1000};
            for (int i = 0; i < initials.length; i++) {
                contentTablesMinorHeaders.getRow(0).getCell(i).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(initials[i]));
            }

            contentTablesMinorHeaders.getRow(0).getCell(1).removeParagraph(0);
            contentTablesMinorHeaders.getRow(0).getCell(2).removeParagraph(0);
            contentTablesMinorHeaders.getRow(0).getCell(6).removeParagraph(0);
            contentTablesMinorHeaders.getRow(0).getCell(7).removeParagraph(0);

            XWPFParagraph tableHeader_StartWork = setFastCellPararagraph(contentTablesMinorHeaders.getRow(0).getCell(1), ParagraphAlignment.CENTER, 1.15, 0);
            XWPFParagraph tableHeader_EndWork = setFastCellPararagraph(contentTablesMinorHeaders.getRow(0).getCell(2), ParagraphAlignment.CENTER, 1.15, 0);
            XWPFParagraph tableHeader_ExitHour = setFastCellPararagraph(contentTablesMinorHeaders.getRow(0).getCell(6), ParagraphAlignment.CENTER, 1.15, 0);
            XWPFParagraph tableHeader_BackHour = setFastCellPararagraph(contentTablesMinorHeaders.getRow(0).getCell(7), ParagraphAlignment.CENTER, 1.15, 0);

            setFastRun(tableHeader_StartWork, "Times New Roman", 9, false, "000000", "Rozpoczęcie");
            setFastRun(tableHeader_EndWork, "Times New Roman", 9, false, "000000", "Zakończenie");
            setFastRun(tableHeader_ExitHour, "Times New Roman", 9, false, "000000", "Godz. wyjścia");
            setFastRun(tableHeader_BackHour, "Times New Roman", 9, false, "000000", "Godz. przyjścia");
        }

        private void contentOfCalendarTable() {
            XWPFTable contentTables = document.createTable(31, 8);
            XWPFTableCell tableCell;
            XWPFParagraph parTableDayIndex;
            XWPFParagraph parTableDayData;

            for (int dayRow = 1, index; dayRow <= 31; dayRow++) {
                for (index = 0; index < 8; index++) contentTables.getRow(dayRow - 1).getCell(index).removeParagraph(0);
                contentTables.getRow(dayRow - 1).setHeight(50);

                if (dayRow > calendarManager.countDaysInMonth()) {
                    for (index = 0; index < 8; index++) {
                        tableCell = contentTables.getRow(dayRow - 1).getCell(index);
                        tableCell.addParagraph().setSpacingAfter(0);
                        tableCell.getCTTc().addNewTcPr().addNewTcBorders().addNewRight().setColor("FFFFFF");
                        tableCell.getCTTc().addNewTcPr().addNewTcBorders().addNewLeft().setColor("FFFFFF");
                        tableCell.getCTTc().addNewTcPr().addNewTcBorders().addNewBottom().setColor("FFFFFF");
                        if (dayRow + 1 != calendarManager.countDaysInMonth()) {
                            tableCell.getCTTc().addNewTcPr().addNewTcBorders().addNewTop().setColor("FFFFFF");
                        }
                    }
                } else {
                    parTableDayIndex = setFastCellPararagraph(contentTables.getRow(dayRow - 1).getCell(0), ParagraphAlignment.CENTER, 1.15, 0);
                    setFastRun(parTableDayIndex, "Times New Roman", 12, false, "000000", String.format("%d", dayRow));

                    switch (calendarManager.getDay(dayRow).getDayType()) {
                        case HOLIDAY:
                            for (index = 1; index < 8; index++) {
                                parTableDayData = setFastCellPararagraph(contentTables.getRow(dayRow - 1).getCell(index), ParagraphAlignment.CENTER, 1.15, 0);
                                setFastRun(parTableDayData, "Times New Roman", 10, false, "FF0000", "XXX");
                            }
                            break;
                        case SATURDAY:
                            for (index = 1; index < 8; index++) {
                                parTableDayData = setFastCellPararagraph(contentTables.getRow(dayRow - 1).getCell(index), ParagraphAlignment.CENTER, 1.15, 0);
                                setFastRun(parTableDayData, "Times New Roman", 10, false, "0000FF", "XXX");
                            }
                            break;
                        default:
                            for (index = 1; index < 8; index++)
                                contentTables.getRow(dayRow - 1).getCell(index).addParagraph().setSpacingAfter(0);
                            break;
                    }
                }
            }
        }

        private void createEmptyLine() {
            document.createParagraph();
        }

        private XWPFParagraph setFastPararagraph(ParagraphAlignment alignment, double spacingBetween) {
            XWPFParagraph paragraph = document.createParagraph();
            paragraph.setAlignment(alignment);
            paragraph.setSpacingBetween(spacingBetween);
            return paragraph;
        }

        private XWPFParagraph setFastCellPararagraph(XWPFTableCell cell, ParagraphAlignment alignment, double spacingBetween, int spacingAfter) {
            XWPFParagraph paragraph = cell.addParagraph();
            paragraph.setAlignment(alignment);
            paragraph.setSpacingBetween(spacingBetween);
            paragraph.setSpacingAfter(spacingAfter);
            return paragraph;
        }

        private XWPFRun setFastRun(XWPFParagraph paragraph, String fontName, int fontSize, boolean bold, String color, String text) {
            XWPFRun run = paragraph.createRun();
            run.setText(text);
            run.setFontFamily(fontName);
            run.setFontSize(fontSize);
            run.setBold(bold);
            run.setColor(color);
            return run;
        }
    }
}
