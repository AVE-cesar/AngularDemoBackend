package com.jaxio.demo.validation;


/**
 *  juste pour éviter des erreurs de compilation sur:  @FixedLength(length = 5)
 *     
 * @author avebertrand
 *
 */
public @interface FixedLength {

	int length();

}
