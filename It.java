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
package scom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.lang.Class;
import java.lang.reflect.Constructor;
import scom.samples.lang.lisp.functions.ItLispCdrF;

public class It implements java.util.Map.Entry<Object, Object> 
{
  public final String CLASS_NAME = "scom.It";  
  
  public static final String          K_NAME             = "name";
  public static final String          K_FUNCTION         = "function";
  public static final String          K_VALUE            = "value";
  public static final String          K_CLASS            = "class";
  
  public static final String          K_NIL              = "NIL";
  public static It                    NIL                = new It(K_NIL, K_NIL, K_NIL);
  
  public static final String          K_TRUE             = "true";
  public static It                    TRUE               = new It(K_TRUE, K_TRUE, K_TRUE);
  
  public static final String          K_ENVIRONMENT      = "Environment";
  public static It                    ENVIRONMENT        = new It(K_ENVIRONMENT, K_ENVIRONMENT, K_NIL);
    
  protected HashMap<It, It>           _connections       = new HashMap<It, It>();
  protected HashMap<String, It>       _kv2it             = new HashMap<String, It>();
  protected final           Object    _key;
  protected                 Object    _value             = It.NIL;
  protected                 Object    _next              = It.NIL;
  protected final           UUID      _uuid              = UUID.randomUUID();
  
  //*** Private Constructor: API clients must use factory method 'New' instead
  // Usage: if provided key is "" it's value will be instance's UUID
  protected It(Object key, Object value, Object next) 
  {
    if (key.toString().equals(""))
      _key = _uuid.toString();
    else
      _key  = key;
    
    _value  = value;
    _next   = next;
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
  
  static public It New(Object key, Object value, Object next, String class_name) 
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
        return (It) constructor.newInstance(new Object[]{key, value, next});
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
    
    String key_str   = CastToString(key);
    String value_str = CastToString(value);
    
    if (key_str == It.K_NIL && value_str == It.K_NIL)
      return It.NIL;
      
    return null;
  } //---- isNIL()
  
  static private String CastToString(Object o) 
  {
    String o_str = "";
    try
    {
      It o_it = (It) o;
      if (o_it != null)
        o_str = o_it.getKey().toString();
    }
    catch (Exception e) 
    {
      o_str = o.toString().toUpperCase();
    }
    return o_str;
  } // CastToString
  
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
    return evaluate(It.NIL);
  } //---- evaluate() 
  
  public It evaluate(It arg)
  {   
    return this;
  } //---- evaluate() 
    
  public It evaluate(ArrayList<It> arg_list)
  {   
    return this;
  } //---- evaluate() 
  
  static public It buildValue(String value)
  {
    return buildValue(value, It.NIL);
  } //---- buildValue()
  
  static public It buildValue(String value, Object next)
  {
    return new It(It.K_VALUE, value, next);
  } //---- buildValue()
  
  static public ArrayList<It> ToArgList(Object arg)
  {
    return ToArgList(new Object[]{arg});
  } //---- ToArgList()
  
  static public ArrayList<It> ToArgList(Object[] args)
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
  } //---- ToArgList()
  
  protected void init()
  {
  } //---- init
  
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
  
  public Object getOption() { return _next; }
  
  public HashMap<It, It> getConnections()
  {   
    return _connections;
  } //---- getConnections()
  
  @Override
  public String toString() 
  { return getValue().toString(); }

  @Override
  public Object getKey()   { return _key; }
  
  @Override
  public Object getValue()              
  { return _value; }

  @Override
  public Object setValue(Object new_value) 
  {
    Object previous_value = _value;
    _value = new_value;
    return previous_value;
  } //---- setValue()
  
  public Object getNext()              
  { return _next; }
  
  public String getUUID()              
  { return _uuid.toString(); }
} //---------- It
