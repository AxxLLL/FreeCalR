package controllers.fx;
import controllers.ControllerManager;
import init.StartFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import model.calendar.CalendarManager;

import java.time.LocalDate;

public class CalendarController {
    private final Initializer calendarInitializer = new Initializer();

    @FXML private Spinner <String> monthSpinner;
    @FXML private Spinner <Integer> yearSpinner;
    @FXML private Label workHoursLabel;

    public CalendarController() {
        ControllerManager.add(this);
    }

    /*
    * Dont remove this, due to null pointer exception when it is initialized before JavaFX fully load components.
    * It cant be in JavaFX 'initialize' method!
    * */
    public void initializeCalendar() {
        calendarInitializer.initializeCalendar(StartFX.getCalendarManager());
    }

    @FXML
    private void initialize() {
        calendarInitializer.initializeMonthSpinner();
        calendarInitializer.initializeYearSpinner();
        updateWorkHoursLabel(StartFX.getCalendarManager());
    }

    @FXML
    private void currentDayButtonPressed() {
        LocalDate date = LocalDate.now();
        monthSpinner.getValueFactory().valueProperty().setValue(StartFX.monthName.getName(date.getMonth().getValue()));
        yearSpinner.getValueFactory().valueProperty().setValue(date.getYear());
    }

    private void dateChanged() {
        int monthValue = StartFX.monthName.getMonthIndex(monthSpinner.getValue());
        int yearValue = yearSpinner.getValue();
        CalendarManager calendarManager = StartFX.getCalendarManager();

        if(yearValue != calendarManager.getYear() || monthValue != calendarManager.getMonth().getValue()) {
            CalendarManager newManager = CalendarManager.of(StartFX.getFreeDaysManager(), LocalDate.of(yearValue, monthValue, 1).getMonth(), yearValue);
            calendarManager.copy(newManager);
            updateWorkHoursLabel(calendarManager);
            calendarInitializer.initializeCalendar(StartFX.getCalendarManager());
        }
    }

    private void updateWorkHoursLabel(CalendarManager calendar) {
        workHoursLabel.setText(String.format("Liczba godzin pracy w miesiącu: %d (%d dni)", calendar.countWorkDaysInMonth() * 8, calendar.countWorkDaysInMonth()));
    }

    private void setDayColorByType(Button dayBtn, CalendarManager calendar, int dayIndex) {
        switch (calendar.getDay(dayIndex).getDayType()) {
            case HOLIDAY:
                dayBtn.setStyle("-fx-text-fill: RED; -fx-background-color: null;");
                break;
            case SATURDAY:
                dayBtn.setStyle("-fx-text-fill: BLUE; -fx-background-color: null;");
                break;
            default:
                dayBtn.setStyle("-fx-text-fill: BLACK; -fx-background-color: null;");
                break;
        }
    }

    private void hideCalendarDayLabel(Button dayBtn, Scene scene) {
        dayBtn.setText(null);
        dayBtn.setOnMouseEntered((EventHandler<Event>) me -> scene.setCursor(Cursor.DEFAULT));
        dayBtn.setOnAction(Event::consume);
        dayBtn.setStyle("-fx-text-fill: null; -fx-background-color: null");
    }

    private void showCalendarDayButton(Button dayBtn, Scene scene, CalendarManager calendar, int dayIndex) {
        setDayColorByType(dayBtn, calendar, dayIndex);
        dayBtn.setText(String.valueOf(dayIndex));
        dayBtn.setOnMouseEntered((EventHandler<Event>) me -> scene.setCursor(Cursor.HAND));

        dayBtn.setOnAction((event) -> {
            calendar.getDay(dayIndex).switchDayType();
            setDayColorByType(dayBtn, calendar, dayIndex);
            updateWorkHoursLabel(calendar);
        });
    }

    private class Initializer {
        private void initializeMonthSpinner() {
            ObservableList<String> obListMonthNames = FXCollections.observableArrayList(StartFX.monthName.getList());
            SpinnerValueFactory<String> monthFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(obListMonthNames);
            monthSpinner.setValueFactory(monthFactory);
            monthSpinner.getValueFactory().valueProperty().setValue(StartFX.monthName.getName(StartFX.getCalendarManager().getMonth()));
            monthSpinner.valueProperty().addListener((a) -> dateChanged());
        }

        private void initializeYearSpinner() {
            SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2018, 2099, StartFX.getCalendarManager().getYear());
            yearSpinner.setValueFactory(valueFactory);
            yearSpinner.valueProperty().addListener((a) -> dateChanged());
        }

        void initializeCalendar(CalendarManager calendar) {
            Scene scene = workHoursLabel.getScene();
            Button dayBtn;
            for (int row = 0, col, dayIndex = 1; row < 6; row++) {
                for (col = 0; col < 7; col++) {
                    dayBtn = (Button) scene.lookup(String.format("#D_BTN_%d_%d", row, col));
                    if ((row == 0 && col < calendar.getStartingDayOfWeek().getValue() - 1) || dayIndex > calendar.countDaysInMonth()) {
                        hideCalendarDayLabel(dayBtn, scene);
                    } else {
                        showCalendarDayButton(dayBtn, scene, calendar, dayIndex++);
                    }
                }
            }
            updateWorkHoursLabel(calendar);
        }
    }
}
