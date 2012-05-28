/**
 * ItLispCdrF.java
 * cdr() Lisp function
 * http://www.math.utah.edu/docs/info/emacs-lisp-intro_8.html
 * ...................................................................................
 * SCOM: Single Class Object Model (http://code.google.com/p/scom/)
 * Licence: MIT (http://en.wikipedia.org/wiki/MIT_License)
 * Michel Kern - 27 may 2012 - 16:54
 * Copyright (C) <2012> www.terimakasi.com
 */
package scom.samples.lang.lisp.functions;

import java.util.ArrayList;
import scom.*;
import static scom.It.*;
import scom.samples.lang.lisp.*;

public class ItLispCdrF extends It
{
  public static final String BASENAME   = "cdr";
  public static final String CLASS_NAME = ItLispCdrF.class.getCanonicalName();
          
  protected ItLispCdrF(Object key, Object value, Object next) 
  {
    super(key, value, next);
  } // Private Constructor
  
  @Override
  public It evaluate(ArrayList<It> input)
  {  
    if (input.size() != 1) return NIL;
    
    It cons_it = input.get(0);
    if (cons_it.getNext() == NIL)
      return NIL;
    
    It    next_it = NIL;
    try { next_it = (It) cons_it.getNext(); }
    catch (Exception e) {}
    
    return next_it;
  } //---- evaluate() 
  
  @Override
  public String toString()
  {  
    return BASENAME;
  } //---- toString() 
} //---------- ItLispCdrF