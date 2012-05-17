/**
 * Test_Lisp.java
 * Test of Scom Lisp based Interpreter
 * http://lib.store.yahoo.net/lib/paulgraham/jmc.lisp
 * ...................................................................................
 * SCOM: Single Class Object Model (http://code.google.com/p/scom/)
 * Licence: MIT (http://en.wikipedia.org/wiki/MIT_License)
 * Michel Kern - 17 may 2012 - 23:46
 * Copyright (C) <2012> www.terimakasi.com
 * ...................................................................................
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to deal 
 * in the Software without restriction, including without limitation the rights to use, 
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of 
 * the Software, and to permit persons to whom the Software is furnished to do so, 
 * subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all 
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS 
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * ...................................................................................
 */
package scom.test;

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
    System.out.println("**** Test_Lisp ****");
    
    _repl = It.New(It.K_VALUE, "repl", ItLisp.CLASS_NAME);
    
    System.out.println("   'TRUE' atom:       " + It.TRUE);
    
    It cons1 = It.New(It.K_VALUE, "1", It.NIL, ItCons.CLASS_NAME);
    System.out.println("   cons1:             " + cons1.evaluate());
   
    It cons2 = It.New(It.K_VALUE, "2", It.NIL, ItCons.CLASS_NAME);
    System.out.println("   cons2:             " + cons2.evaluate());
    
    It cons3 = It.New(It.K_VALUE, "3", It.NIL, ItCons.CLASS_NAME);
    System.out.println("   cons3:             " + cons3.evaluate());
    
    It list1 = It.New(It.K_VALUE, cons1, cons2, ItCons.CLASS_NAME);
    System.out.println("   list1:             " + list1.evaluate());
    
    ArrayList<It> params = It.ToArgList(list1);
    System.out.println("   atom(TRUE):        " + It.ENVIRONMENT.getIt("atom").evaluate(It.ToArgList(It.TRUE)));
    System.out.println("   atom(NIL):         " + It.ENVIRONMENT.getIt("atom").evaluate(It.ToArgList(It.NIL)));
    System.out.println("   atom(cons1):       " + It.ENVIRONMENT.getIt("atom").evaluate(It.ToArgList(cons1)));
    System.out.println("   atom(cons2):       " + It.ENVIRONMENT.getIt("atom").evaluate(It.ToArgList(cons2)));
    System.out.println("   atom(car):         " + It.ENVIRONMENT.getIt("atom").evaluate(It.ToArgList(It.ENVIRONMENT.getIt("car"))));
    System.out.println("   atom(list1):       " + It.ENVIRONMENT.getIt("atom").evaluate(params));
        
    System.out.println("   car(list1):        " + It.ENVIRONMENT.getIt("car").evaluate(params));
    System.out.println("   cdr(list1):        " + It.ENVIRONMENT.getIt("cdr").evaluate(params));
    
    params = It.ToArgList(new Object[] {list1, cons3});
    System.out.println("   cons(list1,cons3): " + It.ENVIRONMENT.getIt("cons").evaluate(params));
    
    It list2 = It.New(It.K_VALUE, cons1, cons2, ItCons.CLASS_NAME);
    System.out.println("   atom(list2):       " + It.ENVIRONMENT.getIt("atom").evaluate(It.ToArgList(list2)));
    
    params = It.ToArgList(new Object[] {cons1, cons1});
    System.out.println("   eq(cons1,cons1):   " + It.ENVIRONMENT.getIt("eq").evaluate(params)); 
    
    params = It.ToArgList(new Object[] {cons1, cons2});
    System.out.println("   eq(cons1,cons2):   " + It.ENVIRONMENT.getIt("eq").evaluate(params));   
    
    params = It.ToArgList(new Object[] {"1 2 3"});
    System.out.println("   quote(1,2,3):      " + It.ENVIRONMENT.getIt("quote").evaluate(params));
   
    _repl.evaluate(It.ToArgList("1"));
    _repl.evaluate(It.ToArgList("1.0"));
    _repl.evaluate(It.ToArgList("\"Hello\""));
    _repl.evaluate(It.ToArgList("car"));
    
    _repl.evaluate(It.ToArgList("(quote (2 3 5 7 11 13 17 19))"));
    _repl.evaluate(It.ToArgList("('(2 3 5 7 11 13 17 19))"));
    
    _repl.evaluate();
  } //---- main()
} //---------- Test_Lisp