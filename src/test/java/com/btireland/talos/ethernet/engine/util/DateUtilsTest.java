package com.btireland.talos.ethernet.engine.util;

import com.btireland.talos.core.common.test.tag.UnitTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

@UnitTest
class DateUtilsTest {

    @Test
    @DisplayName("When input date is LocalDateTime is given, string is returned in the format 'dd/MM/yyyy HH:mm:ss' ")
    void btStringToDateTime() {
        String expected = "01/01/2021 01:12:13";
        String actual = DateUtils.btDateTimeToString(LocalDateTime.of(2021, Month.JANUARY, 1, 1, 12, 13));
        Assertions.assertThat(actual).isEqualTo(expected);

    }

    @Test
    @DisplayName("When input date is LocalDate is given, string is returned in the format 'yyyy-MM-dd HH:mm:ss'")
    void isoDateTimeToString() {
        String expected = "2021-01-01T01:12:13";
        String actual = DateUtils.isoDateTimeToString(LocalDateTime.of(2021, Month.JANUARY, 1, 1, 12, 13));
        Assertions.assertThat(actual).isEqualTo(expected);

    }

    @Test
    @DisplayName("Input date String in ISO format is parsed as LocalDateTime")
    void isoStringToDateTime() {
        LocalDateTime expected = LocalDateTime.of(2021, Month.JANUARY, 1, 1, 12, 13);
        LocalDateTime actual = DateUtils.isoStringToDateTime("2021-01-01T01:12:13");
        Assertions.assertThat(actual).isEqualTo(expected);

    }

}
