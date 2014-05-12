package validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CodepointLengthValidator implements ConstraintValidator<CodePointLength, String>{

	private int max;
	private int min;
	
	@Override
	public void initialize(CodePointLength codePoint) {
		if(codePoint.min() < 0 ) throw new IllegalArgumentException("Minimum value can not be negative");
		max = codePoint.max();
		min = codePoint.min();
	}

	@Override
	public boolean isValid(String s, ConstraintValidatorContext c) {
		if(s == null){ //only validate the string if a non-null string is passed.
					   //this mimics the behavior of @Size of jsr303.
			return true;
		}
		
		if(s.length() < max && min == 0){ //don't need to get a codepoint count if 
										  //length is already less than char count.
			return true;
		}
		
		int length = s.codePointCount(0, s.length());
		
		return length >= min && length <= max;
	}

}
