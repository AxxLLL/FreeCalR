package model.community;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class CommunityListAbsTest {
    TestIntegerClass tClass;

    @BeforeEach
    void beforeEach() {
        tClass = new TestIntegerClass();
    }

    @DisplayName("add(T): Should return NullPointerException when param to add is null.")
    @Test
    void test_1(){
        assertThatNullPointerException().isThrownBy(() -> tClass.add(null));
    }

    @DisplayName("add(T): Should return true when T object is valid.")
    @Test
    void test_2() {
        assertThat(tClass.add(new Integer(5))).isTrue(); //manually packed to Integer due to better visible for me in test...
    }

    @DisplayName("add(T): Should return false when T object is already exists in list.")
    @Test
    void test_3() {
        assertThat(tClass.add(new Integer(5))).isTrue();
        assertThat(tClass.add(new Integer(5))).isFalse();
    }

    @DisplayName("remove(T): Should return NullPointerException when param is null.")
    @Test
    void test_4() {
        assertThatNullPointerException().isThrownBy(() ->tClass.remove(null));
    }

    @DisplayName("remove(T): Should return false when object isn't exist at list.")
    @Test
    void test_5() {
        assertThat(tClass.remove(new Integer(5))).isFalse();
    }

    @DisplayName("remove(T): Should return true and delete index from list, when param is valid.")
    @Test
    void test_6() {
        addDefaultValidListOfObjects();
        Integer remove_1 = tClass.getByIndex(0);
        Integer remove_2 = tClass.getByIndex(tClass.getSize() - 1);
        int beforeSize = tClass.getSize();
        assertThat(tClass.remove(remove_1)).isTrue();
        assertThat(tClass.remove(remove_2)).isTrue();
        assertThat(tClass.getSize()).isEqualTo(beforeSize - 2);
    }

    @DisplayName("remove(T): Should return null when index is invalid.")
    @ParameterizedTest
    @ValueSource(ints = {-1, 99})
    void test_4(int value) {
        assertThat(tClass.getByIndex(value)).isNull();
    }

    @DisplayName("remove(T): Should return object when index is valid.")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4})
    void test_5(int value) {
        addDefaultValidListOfObjects();
        assertThat(tClass.getByIndex(value)).isExactlyInstanceOf(Integer.class);
    }

    //------------------------------------------------------------

    private void addDefaultValidListOfObjects() {
        tClass.add(new Integer(1));
        tClass.add(new Integer(2));
        tClass.add(new Integer(3));
        tClass.add(new Integer(4));
        tClass.add(new Integer(5));
    }

    private class TestIntegerClass extends CommunityListAbs<Integer> {
        private TestIntegerClass() {
            super();
        }
    }
}