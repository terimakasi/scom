/**
 * ItLispConsF.java
 * cons() Lisp function
 * http://en.wikipedia.org/wiki/Conshttp://en.wikipedia.org/wiki/Cons
 * ...................................................................................
 * SCOM: Single Class Object Model (http://code.google.com/p/scom/)
 * Licence: MIT (http://en.wikipedia.org/wiki/MIT_License)
 * Michel Kern - 27 may 2012 - 16:54
 * Copyright (C) <2012> www.terimakasi.com
 */
package scom.samples.lang.lisp.functions;

import java.util.ArrayList;
import scom.*;
import scom.samples.lang.lisp.ItCons;

public class ItLispConsF extends It
{
  public static final String BASENAME   = "cons";
  public static final String CLASS_NAME = ItLispConsF.class.getCanonicalName();
  
  protected ItLispConsF(Object key, Object value, Object next) 
  {
    super(key, value, next);
  } // Private Constructor
  
  @Override
  public It evaluate(ArrayList<It> input)
  { 
    if (input.size() != 2) return It.NIL;
    return New(It.K_VALUE, input.get(0), input.get(1), ItCons.CLASS_NAME);
  } //---- evaluate() 
  
  @Override
  public String toString()
  {  
    return BASENAME;
  } //---- toString() 
} //---------- ItLispConsF