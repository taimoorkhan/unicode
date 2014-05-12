package validation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Unit test for CodePointLengthValidator.
 */

public class CodePointLengthValidatorTests 
{
	private static Validator validator; 
    public CodePointLengthValidatorTests( )
    {
    }

    @BeforeClass  
    public static void setUp() throws Exception {  
      ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();  
      validator = validatorFactory.getValidator();  
    } 

    @Test
    public void shouldValidateBMPValues(){
    	String name="Alexi";
    	SampleBean sb = new SampleBean(name);
    	
    	Set<ConstraintViolation<SampleBean>> constraintViolations = validator.validate(sb);  
    	for(ConstraintViolation<SampleBean> constraintViolation : constraintViolations){  
    		String message = constraintViolation.getMessage();  
    		System.out.println(message);  
    	}  
    
    	org.junit.Assert.assertEquals(0,constraintViolations.size()); 
    }
    
    
   
}

class SampleBean{
	
	@CodePointLength(min=5 , max=20)
	String name;
	
	public SampleBean(String name) {
		this.name = name;
	}
	
}
