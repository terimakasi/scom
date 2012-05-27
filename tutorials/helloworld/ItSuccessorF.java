// ItSuccessorF.java
// Michel Kern - 27 may 2012 - 16:54
package scom.tutorials.helloworld;

import scom.It;

import java.util.ArrayList;

public class ItSuccessorF extends It
{
  public static final String NAME       = ItSuccessorF.class.getSimpleName();
  public static final String CLASS_NAME = ItSuccessorF.class.getCanonicalName();
          
  protected ItSuccessorF(Object key, Object value, Object next) 
  {
    super(key, value, next);
  } // Private Constructor
  
  @Override
  public It evaluate(ArrayList<It> input)
  { 
    if (input.size() < 1) return It.NIL;
    
    It      arg1   = input.get(0);
    Integer value  = Integer.parseInt(arg1.getValue().toString());
    //System.out.println("ItSuccessorF.evaluate arg1: " + arg1);
    Integer output = value + 1;
    return New(output);
  } //---- evaluate() 
} //---------- ItSuccessorF