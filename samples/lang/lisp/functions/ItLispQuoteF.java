/**
 * ItLispQuoteF.java
 * quote() Lisp function
 * http://www.math.utah.edu/docs/info/emacs-lisp-intro_8.html
 * ...................................................................................
 * SCOM: Single Class Object Model (http://code.google.com/p/scom/)
 * Licence: MIT (http://en.wikipedia.org/wiki/MIT_License)
 * Michel Kern - 27 may 2012 - 16:54
 * Copyright (C) <2012> www.terimakasi.com
 */
package scom.samples.lang.lisp.functions;

import java.util.ArrayList;
import scom.It;

public class ItLispQuoteF extends It
{
  public static final String BASENAME   = "quote";
  public static final String CLASS_NAME = ItLispQuoteF.class.getCanonicalName();
          
  protected ItLispQuoteF(Object key, Object value, Object next) 
  {
    super(key, value, next);
  } // Private Constructor
  
  @Override
  public It evaluate(ArrayList<It> input)
  {  
    if (input.size() != 1) return It.NIL;
    return input.get(0);
  } //---- evaluate() 
  
  @Override
  public String toString()
  {  
    return BASENAME;
  } //---- toString() 
} //---------- ItLispQuoteF