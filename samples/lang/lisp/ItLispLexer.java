/**
 * ItLispLexer.java
 * REPL: Lexer / Tokenizer
 * http://liquicode.com/wordpress/dev/259/tokenizer-a-class-for-the-lexical-analysis-of-text
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

import scom.samples.lang.lisp.ItLisp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import scom.It;
import scom.samples.lang.lisp.*;

public class ItLispLexer extends It
{
  public static final String CLASS_NAME           = "scom.samples.lang.lisp.ItLispLexer";
  
  public static final String SEPARATOR_OPEN_LIST  = "(";
  public static final String SEPARATOR_CLOSE_LIST = ")";
  public static final String SEPARATOR_STRING     = "\"";
  public static final String SEPARATOR_QUOTE      = "'";
  
  // NB: don't add "0x0b" in WhiteSpace separators as it breaks Integer parsing
  public static final String SEPARATOR_WHITESPACE = " \t\n\f\r"; 
  
  public static final String SEPARATORS = 
         SEPARATOR_WHITESPACE + SEPARATOR_OPEN_LIST + SEPARATOR_CLOSE_LIST + SEPARATOR_STRING + SEPARATOR_QUOTE;
          
  protected ItLispLexer(Object key, Object value, Object next) 
  {
    super(key, value, next);
  } // Private Constructor
   
  @Override
  //--------------- Lexer ---------------
  public It evaluate(ArrayList<It> input)
  {         
    if (input.size()==0) return It.NIL;
    
    ArrayList<It> tokens_1 = tokenize_phase1(input.get(0).toString());
    ArrayList<It> tokens_2 = tokenize_phase2(tokens_1);
    ArrayList<It> tokens_3 = tokenize_phase2(tokens_2);
    
    //setValue(output_str);
    setValue(tokens_3);
    return this;
  } //---- evaluate() 
  
  ArrayList<It> tokenize_phase1(String arg)
  {
    StringTokenizer st     = new StringTokenizer(arg, SEPARATORS, true);
    ArrayList<It>   tokens = new ArrayList<It>();
    while (st.hasMoreTokens()) 
    {
      String token_str  = st.nextToken();
      String token_type = getTokenType(token_str);
      It token_it = It.NewValue(token_str, token_type);
      tokens.add(token_it);
    }
    return tokens;
  } //---- tokenize_phase1
  
  ArrayList<It> tokenize_phase2(ArrayList<It> tokens_in)
  {
    ArrayList<It>  tokens_out = new ArrayList<It>();
    It token_current;
    It token_next;
    It token_next_next;
    for (int i=0; i < tokens_in.size(); i++) 
    {
      token_current   = tokens_in.get(i);
      token_next      = It.NIL;
      token_next_next = It.NIL;
      
      if (i+1 < tokens_in.size())
        token_next = tokens_in.get(i+1);     
      
      if (i+2 < tokens_in.size())
        token_next_next = tokens_in.get(i+2);
                
      if (   token_current.getNext()  ==SEPARATOR_STRING
          && token_next!=It.NIL && token_next_next!=It.NIL
          && token_next_next.getNext()==SEPARATOR_STRING)
      {
        It new_token = It.NewValue(token_next.getValue().toString(), ItLisp.K_STRING_TYPE);
        tokens_out.add(new_token);
        i+=2;
      }
      else if (token_current.getNext()==SEPARATOR_QUOTE)
      {
        It new_token = It.NewValue("quote", ItLisp.K_SYMBOL_TYPE);
        tokens_out.add(new_token);
      }
      else
        tokens_out.add(token_current);
    }
    return tokens_out;
  } //---- tokenize_phase2
  
  ArrayList<It> tokenize_phase3(ArrayList<It> tokens)
  {
    //---- surround input with parenthesis if not the case
    if (tokens.size()>1 && tokens.get(0).getNext()!=SEPARATOR_STRING)
      if (! tokens.get(0).getValue().equals(SEPARATOR_OPEN_LIST))
        tokens.add(0, It.NewValue(SEPARATOR_OPEN_LIST, SEPARATOR_OPEN_LIST));
     
    if (tokens.size()>2 && tokens.get(tokens.size()-1).getNext()!=SEPARATOR_STRING)
      if (! tokens.get(tokens.size()-1).getValue().equals(SEPARATOR_CLOSE_LIST))
        tokens.add(It.NewValue(SEPARATOR_CLOSE_LIST, SEPARATOR_CLOSE_LIST));
    return tokens;
  } //---- tokenize_phase3
  
  // http://www.java2s.com/Tutorial/SCJP/0160__Utility-Classes/EachJavaprimitivedatatypehasacorrespondingwrapperclass.htm
  private String getTokenType(String token_str)
  {
    String token_type = ItLisp.K_UNKNOWN_TYPE;
    
    //---- 1. Check first if token value is NIL
    if (   token_str.equals(It.K_NIL)
        || token_str.toUpperCase().equals("F"))
      token_type = It.K_NIL;
    //----
    
    //---- 2. Check if token is WhiteSpace:
    //        - ' ', \n, \r, \r, 0x0b
    if (SEPARATOR_WHITESPACE.indexOf(token_str)!= -1)
      token_type = ItLisp.K_WHITESPACE;
    //----
    
    //---- 3. Check if token value is " : string delimiter   
    if (token_str.equals(SEPARATOR_STRING))
      token_type = SEPARATOR_STRING;
    //----
    
    //---- 4. Check  if token value is either:
    //        - ( : open list
    //        - ) : close list
    if (token_str.equals(SEPARATOR_OPEN_LIST))
      token_type = SEPARATOR_OPEN_LIST;
    
    if (token_str.equals(SEPARATOR_CLOSE_LIST))
      token_type = SEPARATOR_CLOSE_LIST;
    //----
    
    //---- 5. Check if token value is an Integer
    try
    {
      Integer value = Integer.valueOf(token_str);
      token_type = ItLisp.K_INTEGER_TYPE;
    }
    catch (Exception e) {}
    //----
    
    //---- 6. Check if token value is a Double/Float/Real
    if (token_type.equals(ItLisp.K_UNKNOWN_TYPE))
    try
    {
      Double value = Double.valueOf(token_str);
      token_type   = ItLisp.K_DOUBLE_TYPE;
    }
    catch (Exception e) {}
    //----
    
    //---- 7. Check if token value is a known Symbol in ENVIRONMENT
    if (ENVIRONMENT.getFacet(token_str) != It.NIL)
      token_type = ItLisp.K_SYMBOL_TYPE;
    //----
    
    //---- 8. Check if token is ':
    if (token_str.equals(SEPARATOR_QUOTE))
      token_type = SEPARATOR_QUOTE;
    //----
    
    return token_type;
  } //---- getTokenType
} //---------- ItLispLexer