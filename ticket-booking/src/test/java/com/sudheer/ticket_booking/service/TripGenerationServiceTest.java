package com.sudheer.ticket_booking.service;

import org.junit.jupiter.api.Test;

import com.sudheer.ticket_booking.entity.TripSchedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TripGenerationServiceTest {

    private final TripGenerationService service =
        new TripGenerationService(null, null);

    // @Test
    // void shouldReturnTrueWhenScheduleIsEligible() {

    //     TripSchedule schedule = new TripSchedule();

    //     schedule.setActive(true);
    //     schedule.setStartDate(LocalDate.of(2026, 11, 18));
    //     schedule.setEndDate(LocalDate.of(2026, 11, 30));
    //     schedule.setDepartureTime(LocalTime.of(22, 0));

    //     schedule.setOperatingDays(
    //         Set.of(DayOfWeek.MONDAY,
    //                DayOfWeek.WEDNESDAY,
    //                DayOfWeek.FRIDAY)
    //     );

    //     boolean result = service.isScheduleEligible(
    //         schedule,
    //         LocalDate.of(2026, 11, 20)
    //     );

    //     assertTrue(result);
    // }

    // @Test
    // void shouldReturnFalseWhenOperatingDayDoesNotMatch() {

    //     TripSchedule schedule = new TripSchedule();

    //     schedule.setActive(true);
    //     schedule.setStartDate(LocalDate.of(2026, 11, 18));
    //     schedule.setEndDate(LocalDate.of(2026, 11, 30));

    //     schedule.setOperatingDays(
    //         Set.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY)
    //     );

    //     boolean result = service.isScheduleEligible(
    //         schedule,
    //         LocalDate.of(2026, 11, 21)
    //     );

    //     assertFalse(result);
    // }

    // @Test 
    // void shouldReturnFalseWhenScheduleIsInActice() {
    //     TripSchedule schedule = new TripSchedule();

    //     schedule.setActive(true);
    //     schedule.setStartDate(LocalDate.of(2026, 11, 18));
    //     schedule.setEndDate(LocalDate.of(2026, 11, 30));

    //     schedule.setActive(false);

    //     boolean result = service.isScheduleEligible(schedule, LocalDate.of(2026,11,20));

    //     assertFalse(result);
    // }

    // @Test
    // void shouldReturnFalseBeforeStartDate() {
    //     TripSchedule schedule = new TripSchedule();

    //     schedule.setActive(true);
    //     schedule.setStartDate(LocalDate.of(2026, 11, 18));
    //     schedule.setEndDate(LocalDate.of(2026, 11, 30));

    //     boolean result = service.isScheduleEligible(schedule, LocalDate.of(2026,11,15));

    //     assertFalse(result);
    // }

    // @Test 
    // void shouldReturnFalseAfterEndDate() {
    //     TripSchedule schedule = new TripSchedule();

    //     schedule.setActive(true);
    //     schedule.setStartDate(LocalDate.of(2026, 11, 18));
    //     schedule.setEndDate(LocalDate.of(2026, 11, 30));

    //     boolean result = service.isScheduleEligible(schedule, LocalDate.of(2026,12,1));

    //     assertFalse(result);
    // }
}
