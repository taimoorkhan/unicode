package validation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;

@Target({ElementType.METHOD,ElementType.FIELD,ElementType.CONSTRUCTOR,ElementType.PARAMETER,
				ElementType.ANNOTATION_TYPE})  
@Retention(RUNTIME)
@Constraint (validatedBy=CodepointLengthValidator.class)
public @interface CodePointLength {

	String message() default "unicode.codepoint";  
	  
    Class[] groups() default {};  
      
    Class[] payload() default {};  
	int max() default Integer.MAX_VALUE;
	int min() default 0;
}
