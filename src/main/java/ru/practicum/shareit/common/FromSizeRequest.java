package ru.practicum.shareit.common;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.exception.ValidationException;

@ToString
@EqualsAndHashCode
public class FromSizeRequest extends PageRequest {

    @Getter
    private final int from;

    protected FromSizeRequest(int from, int size, Sort sort) {
        super(from / size, size, sort);
        this.from = from;
    }

    public static FromSizeRequest of(int from, int size, Sort sort) {
        if (from < 0) {
            throw new ValidationException("Данные from должны быть >= 0");
        }
        if (size <= 0) {
            throw new ValidationException("Размер страницы должен быть больше 0");
        }
        return new FromSizeRequest(from, size, sort);
    }
}
