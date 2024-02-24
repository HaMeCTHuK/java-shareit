package ru.practicum.shareit.booking;

public enum BookingStatus {
    WAITING,  // новое бронирование, ожидает одобрения
    APPROVED,  //бронирование подтверждено владельцем
    REJECTED,  //бронирование отклонено владельцем
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
            default:
                return null;
        }
    }
}
