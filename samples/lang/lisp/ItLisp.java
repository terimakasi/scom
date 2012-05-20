/**
 * ItLisp.java
 * Lisp Interpreter: REPL (Read-Eval-Print-Loop)
 * http://en.wikipedia.org/wiki/Read%E2%80%93eval%E2%80%93print_loop
 * http://lib.store.yahoo.net/lib/paulgraham/acl2.txt
 * http://www.cs.sfu.ca/CourseCentral/310/pwfong/Lisp/1/tutorial1.html
 * 
 * Lisp in Lisp
 * http://lib.store.yahoo.net/lib/paulgraham/jmc.lisp
 * 
 * Lisp in awk:
 * http://www.cs.cmu.edu/Groups/AI/html/faqs/lang/lisp/part1/faq-doc-6.html
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
package scom.samples.lang.lisp;

import java.io.*;
import java.util.ArrayList;
import scom.It;
import scom.samples.lang.lisp.functions.*;

public class ItLisp extends It
{
  public static final String CLASS_NAME         = "scom.samples.lang.lisp.ItLisp";
  public static final String K_PARSE            = "parse";
  
  public static final String CAR                = "car";
  public static final String CDR                = "cdr";
  public static final String K_UNKNOWN_TYPE     = "Unknown";
  public static final String K_INTEGER_TYPE     = "Integer";
  public static final String K_DOUBLE_TYPE      = "Double";
  public static final String K_STRING_TYPE      = "String";
  public static final String K_SYMBOL_TYPE      = "Symbol";
  public static final String K_WHITESPACE       = " ";
  public static final String K_OPEN_LIST        = "(";
  public static final String K_CLOSE_LIST       = ")";
  
  public static       int    _init_count        = 0;
  
  private BufferedReader     _reader            = new BufferedReader(new InputStreamReader(System.in));
  private It                 _parser;
          
  protected ItLisp(Object key, Object value, Object next) 
  {
    super(key, value, next);
  } // Private Constructor
  
  //---- Initialize Lisp Environment
  @Override
  protected void init_environment()
  {
    super.init_environment();

    _parser = ENVIRONMENT.getFunction("parser", ItLispParser.CLASS_NAME);
    ENVIRONMENT.addFacet("lexer", ENVIRONMENT.getFunction("lexer",  ItLispLexer.CLASS_NAME));
    
    TRUE.setValue("T"); // standard value of 'TRUE' in Lisp
      
    ENVIRONMENT.addFacet("car",   ENVIRONMENT.getFunction("car",  ItLispCarF.CLASS_NAME));  
    ENVIRONMENT.addFacet("cdr",   ENVIRONMENT.getFunction("cdr",  ItLispCdrF.CLASS_NAME));
    ENVIRONMENT.addFacet("cons",  ENVIRONMENT.getFunction("cons",  ItLispConsF.CLASS_NAME));
    ENVIRONMENT.addFacet("atom",  ENVIRONMENT.getFunction("atom",  ItLispAtomF.CLASS_NAME));
    ENVIRONMENT.addFacet("eq",    ENVIRONMENT.getFunction("eq",    ItLispEqF.CLASS_NAME));
    ENVIRONMENT.addFacet("quote", ENVIRONMENT.getFunction("quote", ItLispQuoteF.CLASS_NAME));
  } //---- init_environment
  
  @Override
  public It evaluate(ArrayList<It> input)
  { 
    boolean loop_enabled = true;
              
    while (loop_enabled)
    {
      System.out.print("> ");
      String input_str = "";
      
      if (input!= null && input.size()>0)
      { 
        loop_enabled = false;
        input_str = input.get(0).toString();
        System.out.println(input_str);
      }
      else
      {
        try
        {
          input_str = _reader.readLine();
        }
        catch (Exception e) {}
      }
      
      It output = _parser.evaluate(It.ToList(input_str));
      System.out.println("  " + output.getValue());
    }
    
    return It.NIL;
  } //---- evaluate() 
} //---------- ItLisp

/* Lisp in Lisp
 * http://lib.store.yahoo.net/lib/paulgraham/jmc.lisp
; The Lisp defined in McCarthy's 1960 paper, translated into CL.
; Assumes only quote, atom, eq, cons, car, cdr, cond
;                     ****  **  ****  ***  ***
; Bug reports to lispcode@paulgraham.com.

; caar:    (defun caar (list) (car (car list)))
; cadar:
; caddar:
; cadr:
; caddr:

(defun null. (x)
  (eq x '()))

(defun and. (x y)
  (cond (x (cond (y 't) ('t '())))
        ('t '())))

(defun not. (x)
  (cond (x '())
        ('t 't)))

(defun append. (x y)
  (cond ((null. x) y)
        ('t (cons (car x) (append. (cdr x) y)))))

(defun list. (x y)
  (cons x (cons y '())))

(defun pair. (x y)
  (cond ((and. (null. x) (null. y)) '())
        ((and. (not. (atom x)) (not. (atom y)))
         (cons (list. (car x) (car y))
               (pair. (cdr x) (cdr y))))))

(defun assoc. (x y)
  (cond ((eq (caar y) x) (cadar y))
        ('t (assoc. x (cdr y)))))

(defun eval. (e a)
  (cond
    ((atom e) (assoc. e a))
    ((atom (car e))
     (cond
       ((eq (car e) 'quote) (cadr e))
       ((eq (car e) 'atom)  (atom   (eval. (cadr e) a)))
       ((eq (car e) 'eq)    (eq     (eval. (cadr e) a)
                                    (eval. (caddr e) a)))
       ((eq (car e) 'car)   (car    (eval. (cadr e) a)))
       ((eq (car e) 'cdr)   (cdr    (eval. (cadr e) a)))
       ((eq (car e) 'cons)  (cons   (eval. (cadr e) a)
                                    (eval. (caddr e) a)))
       ((eq (car e) 'cond)  (evcon. (cdr e) a))
       ('t (eval. (cons (assoc. (car e) a)
                        (cdr e))
                  a))))
    ((eq (caar e) 'label)
     (eval. (cons (caddar e) (cdr e))
            (cons (list. (cadar e) (car e)) a)))
    ((eq (caar e) 'lambda)
     (eval. (caddar e)
            (append. (pair. (cadar e) (evlis. (cdr e) a))
                     a)))))

(defun evcon. (c a)
  (cond ((eval. (caar c) a)
         (eval. (cadar c) a))
        ('t (evcon. (cdr c) a))))

(defun evlis. (m a)
  (cond ((null. m) '())
        ('t (cons (eval.  (car m) a)
                  (evlis. (cdr m) a)))))
*/