package apijson;

public @interface JSONField {
    boolean serialize() default true;
}
