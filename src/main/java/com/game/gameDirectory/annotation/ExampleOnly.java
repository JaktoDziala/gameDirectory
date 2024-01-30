package com.game.gameDirectory.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation introduced to mark methods kept in project as only for educational purposes.
 * It indicates that method serves as reference for learning and reviewing specific topics, that occurred during project's creation.
 * <p>Methods marked with this annotation are not intended for use in production code and should not be referenced in such contexts.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface ExampleOnly {
}
