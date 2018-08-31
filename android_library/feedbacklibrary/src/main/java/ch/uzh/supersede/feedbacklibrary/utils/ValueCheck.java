package ch.uzh.supersede.feedbacklibrary.utils;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE})
public @interface ValueCheck {
    String[] value();
}
