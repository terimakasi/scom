/**
 * It.java
 * Core and Single class of SCOM project
 * NB: Shortest name because 
 *     1. used widely by client code for it's constants (e.g: It.NIL)
 *     2. used as a prefix for class names (like in 'hungarian notation')
 *        e.g: ItCons class in List interpreter sample
 * NB: originally 'ScomIt', a mix between 'Scom' and 'comit' (The first string-handling and pattern-matching language)
 *     see: http://en.wikipedia.org/wiki/COMIT
 * ...................................................................................
 * SCOM: Single Class Object Model (http://code.google.com/p/scom/)
 * Licence: MIT (http://en.wikipedia.org/wiki/MIT_License)
 * Michel Kern - 11 may 2012 - 02:09
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
package scom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.lang.Class;
import java.lang.reflect.Constructor;
import scom.samples.lang.lisp.functions.ItLispCdrF;

public class It implements java.util.Map.Entry<Object, Object> 
{
  public final String CLASS_NAME = "scom.ScomIt";  
  
  public static final String          K_NAME             = "name";
  public static final String          K_FUNCTION         = "function";
  public static final String          K_VALUE            = "value";
  public static final String          K_CLASS            = "class";
  
  public static final String          NIL_KV             = "NIL";
  public static It                    NIL                = new It(NIL_KV, NIL_KV, NIL_KV);
  
  public static final String          ENVIRONMENT_KV     = "Environment";
  public static It                    ENVIRONMENT        = new It(ENVIRONMENT_KV, ENVIRONMENT_KV, NIL_KV);
  
  public static final String          K_CONTEXT          = "context";
  public static final String          K_DEFAULT_CONTEXT  = "Default Context";
    
  protected HashMap<It, It>           _connections       = new HashMap<It, It>();
  protected HashMap<String, It>       _kv2it             = new HashMap<String, It>();
  protected final           Object    _key;
  protected HashMap<String, Object>   _contextual_values = new HashMap<String, Object>();
  protected                 Object    _option;
  protected final           UUID      _uuid              = UUID.randomUUID();
  
  //*** Private Constructor: API clients must use factory method 'New' instead
  protected It(Object key, Object value, Object option) 
  {
    _key    = key;
    _contextual_values.put(K_DEFAULT_CONTEXT, value);
    _option = option;
    init();
  } // Private Constructor
  
  //**** static 'pseudo constructor' allows to handle 'NIL' ScomIt (inspired from LISP) **** 
  static public It New(Object key, Object value) 
  {
    return New(key, value, It.NIL);
  } //---- New()
  
  //**** static 'pseudo constructor' allows to handle 'NIL' ScomIt (inspired from LISP) **** 
  static public It New(Object key, Object value, Object option) 
  {
    It item = isNIL(key,value);
    if (item == null)
      item = new It(key, value, option);
    return item;
  } //---- New()
  
  //**** static 'pseudo constructor' allows to handle 'NIL' ScomIt (inspired from LISP) **** 
  static public It New(Object key, Object value, String class_name) 
  {
    return New(key, value, It.NIL, class_name);
  } //---- New()
  
  static public It New(Object key, Object value, Object option, String class_name) 
  {
    It is_nil = isNIL(key,value);
    //System.out.println("> ScomIt.New class_name = " + class_name);
    //System.out.println("  key:" + key + " value:" + value + " option:" + option);
    
    if (is_nil == null)
    {
      try
      {
        Class       cls         = Class.forName(class_name);
        //System.out.println("   cls = " + cls.getName());
        
        Constructor constructor = cls.getDeclaredConstructor(new Class[]{Object.class, Object.class, Object.class});
          constructor.setAccessible(true);
        return (It) constructor.newInstance(new Object[]{key, value, option});
      }
      catch(Exception e)
      {  
        System.out.println("*** Exception in It.New *** " + e.getMessage());
        return It.NIL; 
      }
    }
    return It.NIL;
  } //---- New()
  
  static protected It isNIL(Object key, Object value)
  {
    if (key == null || value == null)     
      return It.NIL;
    
    String key_str   = key.toString().toUpperCase();
    String value_str = value.toString().toUpperCase();
    
    //---- special test when key/value is ScomIt NIL ----
    try
    {
      It key_it = (It) key;
      if (key_it != null)
        key_str = key_it.getKey().toString();
    }
    catch (Exception e) {}
    
    try
    {
      It value_it = (It) value;
      if (value_it != null)
        value_str = value_it.getKey().toString();
    }
    catch (Exception e) {}
    //----
    
    if (key_str == It.NIL_KV && value_str == It.NIL_KV)
      return It.NIL;
      
    return null;
  } //---- isNIL()
  
  private static String keyValue(Object key, Object value)
  { 
    return "[" + key.toString() + "," + value.toString() + "]";
  } //---- KeyValuePair
  
  public void connect(String relation_name, It target)
  {
    It relation = New(It.K_NAME, relation_name);
    connect(relation, target);
  } //---- connect
  
  public void connect(It relation, It target)
  {  
    //System.out.println("> connect relation=" + relation.getValue() + " target=" + target.getValue());
    if (_connections.containsKey(relation))
    {
      It previous_target = _connections.get(relation);
      _connections.remove(relation);
      _kv2it.remove(keyValue(relation._key, relation.getValue()));
      _kv2it.remove(keyValue(previous_target._key, previous_target.getValue()));
    }
    _connections.put(relation, target);
    _kv2it.put(keyValue(relation.getValue(), this.getValue()), relation);
    _kv2it.put(keyValue(target.getValue(), this.getValue()), target);
  } //---- connectWith()  
  
  public It evaluate()
  {   
    return evaluate(null);
  } //---- evaluate() 
    
  public It evaluate(ArrayList<It> input)
  {   
    return this;
  } //---- evaluate() 
  
  static public It buildValue(String value)
  {
    return It.New(It.K_VALUE, value);
  } //---- buildValue()
  
  static public ArrayList<It> asList(Object arg)
  {
    return asList(new Object[]{arg});
  } //---- asList()
  
  static public ArrayList<It> asList(Object[] args)
  {
     ArrayList<It> arg_list= new ArrayList<It>();
     for (int i=0; i<args.length; i++)
     {
       It item = null;
       try
       {
         item = (It) args[i];
       }
       catch (Exception e) {}
       
       if (item == null)
         arg_list.add(It.New(It.K_VALUE, args[i]));
       else
         arg_list.add(item);
     }
     return arg_list;
  } //---- asList()
  
  protected void init()
  {
  } //---- init
  
  /*
  public It getIt(String relation_name)
  { 
    It it = _kv2it.get(relation_name);
    return getIt(it);
  } //---- getIt
  */
  
  public It getIt(Object relation)
  { 
    String key_value = keyValue(relation, this.getValue());
    It it = _kv2it.get(key_value);
    return getIt(it);
  } //---- getIt
  
  public It getIt(It it)
  {   
    if (_connections.containsKey(it))
    {
      It relation = _connections.get(it);
      return relation;
    }
    return It.NIL;
  } //---- getIt
  
  public It getFunction(String class_name)
  {
     return It.New(It.K_FUNCTION, class_name, class_name);
  } //---- getFunction
  
  public Object getOption() { return _option; }
  
  public HashMap<It, It> getConnections()
  {   
    return _connections;
  } //---- getConnections()
  
  @Override
  public String toString() { return getValue().toString(); }

  @Override
  public Object getKey()   { return _key; }


  public Object getValue(String context) { return _contextual_values.get(context); }
  @Override
  public Object getValue()               { return getValue(K_DEFAULT_CONTEXT); }

  
  public Object setValue(Object new_value, String context) 
  {
    Object previous_value = getValue(context);
    _contextual_values.put(context, new_value);
    return previous_value;
  } //---- setValue()
  
  @Override
  public Object setValue(Object new_value) 
  {
    return setValue(new_value, K_DEFAULT_CONTEXT);
  } //---- setValue()
} //---------- It