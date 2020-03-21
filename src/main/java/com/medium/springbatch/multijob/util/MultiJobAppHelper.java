package com.medium.springbatch.multijob.util;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by fepelichero on 17/02/2020.
 */
public class MultiJobAppHelper {

    public static Stream<Field> allFieldsFor(Class<?> c) {
	return walkInheritanceTreeFor(c).flatMap(k -> Arrays.stream(k.getDeclaredFields()));
    }

    private static Stream<Class<?>> walkInheritanceTreeFor(Class<?> c) {
	return iterate(c, k -> Optional.ofNullable(k.getSuperclass()));
    }

    private static <T> Stream<T> iterate(T seed, Function<T, Optional<T>> fetchNextFunction) {
	Objects.requireNonNull(fetchNextFunction);

	Iterator<T> iterator = new Iterator<T>() {

	    private Optional<T> t = Optional.ofNullable(seed);

	    public boolean hasNext() {
		return t.isPresent();
	    }

	    public T next() {
		T v = t.get();

		t = fetchNextFunction.apply(v);

		return v;
	    }
	};

	return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED | Spliterator.IMMUTABLE), false);
    }

}
