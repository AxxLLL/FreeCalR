package model.export;

import model.calendar.CalendarManager;
import model.calendar.freedays.FreeDaysManager;
import model.community.groups.GroupManager;
import model.community.users.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ExportToWordTest {
    private static List<User> usersList;
    private static CalendarManager calendarManager;
    private ExportToWord exportToWord;

    @BeforeAll
    static void beforeAll() {
        usersList = new ArrayList<>();
        calendarManager = CalendarManager.of(new FreeDaysManager());

        usersList.add(new User("TestA", "TestA", GroupManager.DEFAULT_GROUP));
        usersList.add(new User("TestB", "TestB", GroupManager.DEFAULT_GROUP));
    }

    @BeforeEach
    void beforeEach() {
        exportToWord = ExportToWord.of(usersList, calendarManager);
    }

    @DisplayName("of(List, CalendarManager): Should return null pointer exception if one of param is null")
    @Test
    void test_0() {
        assertThatNullPointerException().isThrownBy(() -> ExportToWord.of(null, calendarManager));
        assertThatNullPointerException().isThrownBy(() -> ExportToWord.of(usersList, null));
    }

    @DisplayName("exportDataToFile(Path): Should return null pointer exception if path is null")
    @Test
    void test_1() {
        assertThatNullPointerException().isThrownBy(() -> exportToWord.exportDataToFile(null));
    }

    @DisplayName("exportDataToFile(Path): Should return IllegalArgumentException if path to file doesn't exists or file is not in word format (.doc or .docx)")
    @ParameterizedTest
    @CsvSource({"fail, file.doc", "non, file.doc", ", file.txt"})
    void test_2(String dir, String file) {
        Path path = Paths.get(dir, file);
        assertThatIllegalArgumentException().isThrownBy(() -> exportToWord.exportDataToFile(path));
    }

    @DisplayName("exportDataToFile(Path): Should return IllegalArgumentException if path to file doesn't exists or file is not in word format (.doc or .docx)")
    @ParameterizedTest
    @CsvSource({"zFileTest, file.doc", "zFileTest, file.docx", ", file.doc", ", file.docx"})
    void test_3(String dir, String file) {
        Path path = Paths.get(dir == null ? "" : dir, file);
        assertThat(exportToWord.checkIfPathIsValid(path)).isTrue();
    }

    @DisplayName("exportDataToFile(Path): Should create regular .docx file with size is greater than zero")
    @Test
    void test_4() throws IOException {
        Path temp = Files.createTempFile(Paths.get("zFileTest"), "test_", ".docx");
        exportToWord.exportDataToFile(temp);
        assertThat(Files.isRegularFile(temp)).isTrue();
        assertThat(Files.size(temp)).isPositive();
        Files.delete(temp);
    }
}