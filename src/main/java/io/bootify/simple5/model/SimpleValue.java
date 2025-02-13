package io.bootify.simple5.model;

import org.springframework.hateoas.EntityModel;


public class SimpleValue<T> {

    private final T value;

    private SimpleValue(final T value) {
        this.value = value;
    }

    public static <T> EntityModel<SimpleValue<T>> entityModelOf(final T value) {
        final SimpleValue<T> simpleValue = new SimpleValue<>(value);
        return EntityModel.of(simpleValue);
    }

    public T getValue() {
        return value;
    }

}
