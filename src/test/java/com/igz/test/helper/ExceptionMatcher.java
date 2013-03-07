package com.igz.test.helper;


import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.igz.exception.IgzException;

/**
 * Custom class to test IgzException codes in jUnit tests
 *
 */
public class ExceptionMatcher extends TypeSafeMatcher<IgzException> {  
  
    public static ExceptionMatcher hasCode(int p_code) {  
        return new ExceptionMatcher(p_code);  
    }  
  
    private int foundErrorCode;  
    private final int expectedErrorCode;  
  
    private ExceptionMatcher(int p_expectedErrorCode) {  
        this.expectedErrorCode = p_expectedErrorCode;  
    }  
  
    @Override  
    protected boolean matchesSafely(final IgzException p_exception) {  
        foundErrorCode = p_exception.getErrorCode();
        return foundErrorCode == expectedErrorCode;  
    }  
  
    @Override  
    public void describeTo(Description description) {  
        description.appendValue(foundErrorCode ).appendText(" was found instead of ").appendValue(expectedErrorCode);  
    }  
}
