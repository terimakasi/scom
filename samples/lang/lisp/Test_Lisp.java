/**
 * Test_Lisp.java
 * Test of It Lisp based Interpreter
 * http://lib.store.yahoo.net/lib/paulgraham/jmc.lisp
 * ...................................................................................
 * SCOM: Single Class Object Model (http://code.google.com/p/scom/)
 * Licence: MIT (http://en.wikipedia.org/wiki/MIT_License)
 * Michel Kern - 27 may 2012 - 16:54
 * Copyright (C) <2012> www.terimakasi.com
 */
package scom.samples.lang.lisp;

import scom.samples.lang.lisp.ItLisp;
import java.util.ArrayList;
import scom.*;
import scom.samples.lang.lisp.*;
import scom.samples.lang.lisp.functions.*;

public class Test_Lisp 
{
  private static It _repl;
    
  public static void main(String[] args) 
  {
    It.Print("**** Test_Lisp ****");
    
    _repl = It.New(It.K_VALUE, "repl", ItLisp.CLASS_NAME);
    
    It.Print("   'TRUE' atom:       " + It.TRUE);
    
    It cons1 = It.New(It.K_VALUE, "1", It.NIL, ItCons.CLASS_NAME);
    It.Print("   cons1:             " + cons1.evaluate());
   
    It cons2 = It.New(It.K_VALUE, "2", It.NIL, ItCons.CLASS_NAME);
    It.Print("   cons2:             " + cons2.evaluate());
    
    It cons3 = It.New(It.K_VALUE, "3", It.NIL, ItCons.CLASS_NAME);
    It.Print("   cons3:             " + cons3.evaluate());
    
    It list1 = It.New(It.K_VALUE, cons1, cons2, ItCons.CLASS_NAME);
    It.Print("   list1:             " + list1.evaluate());
    
    ArrayList<It> params = It.ToList(list1);
    It.Print("   atom(TRUE):        " + It.ENVIRONMENT.getFacet("atom").evaluate(It.ToList(It.TRUE)));
    It.Print("   atom(NIL):         " + It.ENVIRONMENT.getFacet("atom").evaluate(It.ToList(It.NIL)));
    It.Print("   atom(cons1):       " + It.ENVIRONMENT.getFacet("atom").evaluate(It.ToList(cons1)));
    It.Print("   atom(cons2):       " + It.ENVIRONMENT.getFacet("atom").evaluate(It.ToList(cons2)));
    It.Print("   atom(car):         " + It.ENVIRONMENT.getFacet("atom").evaluate(It.ToList(It.ENVIRONMENT.getFacet("car"))));
    It.Print("   atom(list1):       " + It.ENVIRONMENT.getFacet("atom").evaluate(params));
        
    System.out.println("   car(list1):        " + It.ENVIRONMENT.getFacet("car").evaluate(params));
    It.Print("   cdr(list1):        " + It.ENVIRONMENT.getFacet("cdr").evaluate(params));
    
    params = It.ToList(new Object[] {list1, cons3});
    It.Print("   cons(list1,cons3): " + It.ENVIRONMENT.getFacet("cons").evaluate(params));
    
    It list2 = It.New(It.K_VALUE, cons1, cons2, ItCons.CLASS_NAME);
    It.Print("   atom(list2):       " + It.ENVIRONMENT.getFacet("atom").evaluate(It.ToList(list2)));
    
    params = It.ToList(new Object[] {cons1, cons1});
    It.Print("   eq(cons1,cons1):   " + It.ENVIRONMENT.getFacet("eq").evaluate(params)); 
    
    params = It.ToList(new Object[] {cons1, cons2});
    It.Print("   eq(cons1,cons2):   " + It.ENVIRONMENT.getFacet("eq").evaluate(params));   
    
    params = It.ToList(new Object[] {"1 2 3"});
    It.Print("   quote(1,2,3):      " + It.ENVIRONMENT.getFacet("quote").evaluate(params));
   
    _repl.evaluate(It.ToList("1"));
    _repl.evaluate(It.ToList("1.0"));
    _repl.evaluate(It.ToList("\"Hello\""));
    _repl.evaluate(It.ToList("car"));
    
    _repl.evaluate(It.ToList("(quote (2 3 5 7 11 13 17 19))"));
    _repl.evaluate(It.ToList("('(2 3 5 7 11 13 17 19))")).toString();
    
    //_repl.evaluate();
  } //---- main()
} //---------- Test_Lisp