/**
 * ItCons.java
 * The Lisp 'cons' data structure:
 * a cons is a pair:
 *   - 1st element is returned by car() function
 *   - 2nd element is returned by cdr() function
 * http://en.wikipedia.org/wiki/Conshttp://en.wikipedia.org/wiki/Cons
 * ...................................................................................
 * SCOM: Single Class Object Model (http://code.google.com/p/scom/)
 * Licence: MIT (http://en.wikipedia.org/wiki/MIT_License)
 * Michel Kern - 27 may 2012 - 16:54
 * Copyright (C) <2012> www.terimakasi.com
 */
package scom.samples.lang.lisp;

import scom.It;

public class ItCons extends It
{
  public static final String BASENAME   = ItCons.class.getSimpleName();
  public static final String CLASS_NAME = ItCons.class.getCanonicalName();
  
  protected ItCons(Object key, Object value, Object next) 
  {
    super(key, value, next);
    //System.out.println("  > ScomCons key:" + key + " value:" + value + " option:" + option);
    
    putFacet(ItLisp.CAR, New(It.K_VALUE, value));
    putFacet(ItLisp.CDR, New(It.K_VALUE, next));
  } // Private Constructor
  
  @Override
  public String toString()
  {      
    String to_string = "";
    It car_it = getFacet(ItLisp.CAR);
    String car_it_str = car_it.toString();
    
    It cdr_it = getFacet(ItLisp.CDR);
    String cdr_it_str = cdr_it.toString();
    if (cdr_it_str.equals(It.K_NIL))
      to_string = car_it.getValue().toString();
    else
      to_string = "(" + car_it_str + "," + cdr_it_str + ")";   
    return to_string;
  } //---- toString() 
} //---------- ItCons