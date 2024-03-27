package ru.practicum.shareit.booking;

public enum BookingStatus {
    WAITING,  // новое бронирование, ожидает одобрения
    APPROVED,  //бронирование подтверждено владельцем
    REJECTED,  //бронирование отклонено владельцем
    ALL,
    FUTURE,
    CANCELED;  //бронирование отменено создателем.

    public static BookingStatus fromString(String str) {
        switch (str) {
            case "WAITING":
                return BookingStatus.WAITING;
            case "APPROVED":
                return BookingStatus.APPROVED;
            case "REJECTED":
                return BookingStatus.REJECTED;
            case "CANCELED":
                return BookingStatus.CANCELED;
            case "ALL":
                return BookingStatus.ALL;
            case "FUTURE":
                return BookingStatus.FUTURE;
            default:
                return null;
        }
    }
}
