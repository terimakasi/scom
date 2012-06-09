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
 * Michel Kern - 28 may 2012 - 22:52
 * Copyright (C) <2012> www.terimakasi.com
 */
package scom;

import java.io.*; 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.UUID;
import java.lang.Class;
import java.lang.Integer;
import java.lang.reflect.Constructor;
import java.util.*;
import javax.script.*;

public class It implements java.util.Map.Entry<Object, Object> 
                
{
  public static final String BASENAME   = It.class.getSimpleName();
  public static final String CLASS_NAME = It.class.getCanonicalName();
  
  private static      HashMap<String, It>  _Instance_map            = new HashMap<String, It>();
    
  public static final String               K_NIL                    = "NIL";
  public static       It                   NIL                      = new It(K_NIL, K_NIL, K_NIL);

  public static       It                   ID                       = new It("id", "id", K_NIL);  
  public static       It                   NAME;
  public static       It                   COUNT;
  
  public static       It                   STRING_WRITER;
  public static       It                   TEXT_FILE_WRITER;
  
  public static final String               K_FUNCTION               = "function";
  
  public static final String               K_VALUE                  = "value";
  public static       It                   VALUE;
  
  public static final String               K_SCRIPT_F               = "script()";
  public static       It                   SCRIPT_F;
  
  public static final String               K_CLASS                  = "class";
  
  public static final String               K_SUPERCLASS             = "superclass";

  public static final String               K_MODEL_LAYER            = "model_layer";
  
  public static final String               V_CLASS_MODEL            = "class_model";
  public static final It                   CLASS_MODEL              = new It(V_CLASS_MODEL, V_CLASS_MODEL, K_NIL);
  
  public static final String               V_USER_MODEL             = "user_model";
  public static final It                   USER_MODEL               = new It(V_USER_MODEL, V_USER_MODEL, K_NIL);
  
  public static final String               K_NEW_F                  = "new()";
  public static       It                   NEW_F;
  
  public static final String               K_METACLASS              = "Class";
  public static       It                   METACLASS                = null;
  public static       int                  _init_count              = 0;
  
  public static final String               K_OBJECT                 = "Object";
  public static       It                   OBJECT;                  // Caution: don't initialize with null
  
  public static final String               K_DOUBLE                 = "Double";
  public static       It                   DOUBLE;                  // Caution: don't initialize with null
  
  public static final String               K_INTEGER                = "Integer";
  public static       It                   INTEGER;                 // Caution: don't initialize with null
  
  public static final String               K_STRING                 = "String";
  public static       It                   STRING;                  // Caution: don't initialize with null
  
  public static final String               K_COMMAND                = "Command";
  public static       It                   COMMAND;                 // Caution: don't initialize with null
  
  public static final String               K_TRUE                   = "true";
  public static       It                   TRUE                     = new It(K_TRUE, K_TRUE, K_TRUE);
  
  public static final String               K_ENVIRONMENT            = "Environment";
  public static       It                   ENVIRONMENT              = GetEnvironment();
  
  public static       It                   SHELL;
  
  public static final int                  WITHOUT_DEPTH            = -1;
  public static final int                  WITH_UNLIMITED_DEPTH     = 0;
  
  protected           HashMap<It, It>      _facet_map               = new HashMap<It, It>();
    
  protected           HashMap<String, It>  _kv2it                   = new HashMap<String, It>();
  protected final     Object               _key;
  protected           Object               _value                   = NIL;
  protected           Object               _next                    = NIL;
  protected final     UUID                 _uuid                    = UUID.randomUUID();
  
  public static       String               _string_writer_str;
  
  //*** Private Constructor: API clients must use factory method 'New' instead
  // Usage: if provided key is empty string ("") then it will be replaced by instance's UUID
  protected It(Object key, Object value, Object next) 
  {
    if (key.toString().equals(""))
      _key = _uuid.toString();
    else
      _key  = key;
    
    _value  = value;
    _next   = next;
   
    initEnvironment();
    
    if (IsString(key))
    {
      String key_str   = CastToString(key);
      _Instance_map.put(key_str, this);
    }
  } // Private Constructor
  
  // special use: store instance's 'ID' facet (uuid)
  protected It(Object key, Object value) 
  {
    _key   = key;    
    _value = value;
    _next  = NIL;
  } // Private Constructor
  
  static protected It GetEnvironment()
  {
    if (ENVIRONMENT == null)
      ENVIRONMENT = new It(K_ENVIRONMENT, K_ENVIRONMENT, K_NIL);
    
    return ENVIRONMENT;
  } //---- GetEnvironment
  
  protected void initEnvironment()
  {
    if (_init_count > 1)
      return;
    
    _init_count++;
    GetEnvironment();
    
    SHELL            = new It("shell",      "shell",       K_NIL); 
    NAME             = new It("name",       "name",        K_NIL);  
    NEW_F            = new It(K_NEW_F,      K_NEW_F,       K_NIL);
    VALUE            = new It(K_VALUE,      K_VALUE,       K_NIL);
    SCRIPT_F         = new It(K_SCRIPT_F,   K_SCRIPT_F,    K_NIL);
    
    STRING_WRITER    = new It("StringWriter", "StringWriter", K_NIL);
    TEXT_FILE_WRITER = new It("FileWriter",   "FileWriter",   K_NIL);
    
    COUNT  = new It("count",  "count",   K_NIL)
      .putFacet(K_MODEL_LAYER, CLASS_MODEL);
    
    // http://en.wikipedia.org/wiki/Metaclass  
    METACLASS = NewNativeClass_(K_METACLASS, NIL, NIL);       // NB: don't add 'COUNT' value yet, INTEGER is required  
    
    OBJECT = NewNativeClass_(K_OBJECT, METACLASS, NIL);       // NB: don't add 'COUNT' value yet, INTEGER is required 
      OBJECT.putFacet(VALUE,            NIL);
    
    INTEGER = NewNativeClass_(K_INTEGER, METACLASS, OBJECT);  // NB: don't add 'COUNT' value yet, INTEGER is required 
      INTEGER.putFacet(VALUE,           New(0)); 
      
    STRING = NewNativeClass_(K_STRING,  METACLASS, OBJECT);    // NB: don't add 'COUNT' value yet, INTEGER is required 
      STRING.putFacet(VALUE,            New("")); 
      
    COMMAND = NewNativeClass_(K_COMMAND,METACLASS, OBJECT);    // NB: don't add 'COUNT' value yet, INTEGER is required 
      COMMAND.putFacet(SCRIPT_F,        New("")); 
    
    METACLASS.putFacet(COUNT,           New(4)); // NB: requires INTEGER so don't move this code up (OBJECT creation)
    OBJECT.putFacet(COUNT,              New(0));
    INTEGER.putFacet(COUNT,             New(0)); 
    STRING.putFacet(COUNT,              New(0));
    COMMAND.putFacet(COUNT,             New(0));
    
    ENVIRONMENT.putFacet(K_METACLASS,   METACLASS)
               .putFacet(K_OBJECT,      OBJECT) 
               .putFacet(K_INTEGER,     INTEGER)
               .putFacet(K_STRING,      STRING) 
               .putFacet(K_COMMAND,     COMMAND); 
  } //---- initEnvironment
  
  
  //---------- New ----------
  // 'pseudo constructor' required to handle 'NIL' instance of It (inspired from LISP) **** 
  // this one instanciates an 'It class' (an It instance which has the required 'It class' facets)
  static public It New(It class_it) 
  {
    //System.out.println("New class(" + class_it + ")");
    if (! IsClass(class_it))  return NIL;
      
    //---- Instanciate class ----
    String instance_base_name = GetInstanceBaseName_(class_it);
      //---- increment count of class intances ----
      It      count_it       = class_it.getFacet(COUNT);
      Integer instance_count = CastToInteger_(count_it.getValue()) + 1;
      count_it.setValue(instance_count.toString());
      //---- increment count of class intances
    
    String instance_name = instance_base_name + instance_count;
    It instance_it = new It(instance_name, instance_name, NIL);
      instance_it.putFacet(K_CLASS, class_it);
    //---- Instanciate class
    
      
    //----- Add facets to instance from class template -----
    HashMap<It, It> relations   = class_it.getFacets();
    Set<It>         facets      = relations.keySet();
    Iterator<It>    iterator    = facets.iterator();
    while (iterator.hasNext())
    {
      It facet = iterator.next();
      
      //System.out.print("  facet(" + class_it + "): '" + facet + "'");
      if (facet.getFacet(K_MODEL_LAYER) != CLASS_MODEL)
      {
         //System.out.println("  facet(" + class_it + "): '" + facet + "' is user model");
         instance_it.putFacet(facet, relations.get(facet));
      }
    }
    //----- Add facets from class
    
    // Caution: this code should be AFTER 'Add facets from class'
    instance_it.putFacet(NAME, New(instance_name));
      
    return instance_it;
  } //---- New (instanciate 'It class')
  
  static public It New() 
  {
    It item = New(OBJECT);
    return item;
  }
  static public It New(Object key, Object value) 
  {
    It item = New(key, value, NIL);
    return item;
  }
  static public It New(Object key, Object value, Object option) 
  {
    It item = IsNIL_(key,value);
    if (item == null)
      item = new It(key, value, option);
    return item;
  }
  static public It New(Object key, Object value, String class_name) 
  {
    It item = New(key, value, NIL, class_name);
    return item;
  }
  static public It New(Object key, Object value, Object next, String class_name) 
  {
    It is_nil = IsNIL_(key,value);

    if (is_nil == null)
    {
      try
      {
        Class       cls         = Class.forName(class_name);
        Constructor constructor = cls.getDeclaredConstructor(new Class[]{Object.class, Object.class, Object.class});
          constructor.setAccessible(true);
        It item = (It) constructor.newInstance(new Object[]{key, value, next});
        return item;
      }
      catch(Exception e)
      {  
        System.out.println("*** Exception in It.New *** " + e.getMessage());
        return NIL; 
      }
    }
    return NIL;
  }
  //---------- New
  
  
  //---------- NewClass ----------
  static public It NewClass(String class_name)
  {  return NewClass(class_name, METACLASS);  }
  static public It NewClass(String class_name, It metaclass_it)
  {  return NewClass(class_name, metaclass_it, OBJECT);  }
  static public It NewClass(String class_name, It metaclass_it, It super_it)
  {  
    //System.out.println("It.NewClass class_name:" + class_name + "  class:" + metaclass_it + "  super:" + super_it);
    if (! IsClass(metaclass_it))  return It.NIL;
    
    It new_class = new It(class_name, class_name, K_NIL);
      new_class.putFacet      (NAME,         new_class);
      new_class.putFacet      (COUNT,        New(0));
      new_class.putClassFacet_(K_CLASS,      metaclass_it);
      new_class.putClassFacet_(K_NEW_F,      new_class);
      new_class.putClassFacet_(K_SUPERCLASS, super_it);
      
      ENVIRONMENT.putFacet(class_name, new_class);
          
      //---- increment count of class intances ----
      It      count_it       = metaclass_it.getFacet(COUNT);
      Integer instance_count = CastToInteger_(count_it) + 1;
      count_it.setValue(instance_count.toString());
      //---- increment count of class intances
    return new_class;
  } //---- NewClass
  
  
  static public boolean IsClass(It o_it)
  {
    if (o_it == null)             return false;
    if (o_it == NIL)              return false;
    
    It  metaclass_it = o_it.getFacet(K_CLASS);
    if (metaclass_it == NIL)      return false;
    
    It  count_it = o_it.getFacet(COUNT);
    if (count_it == NIL)          return false;
    
    It  new_f_it = o_it.getFacet(K_NEW_F);
    if (new_f_it == NIL)          return false;
    
    It  superclass_it = o_it.getFacet(K_SUPERCLASS);
    if (superclass_it == NIL)     return false;
    
    return true;
  } //---- IsClass 
  
  public boolean isInstanceOf(It o_it)
  {
    if (o_it == null)             return false;
    if (o_it == NIL)              return false;
    
    if (! IsClass(o_it))          return false;
    
    //System.out.println("isInstanceOf item: " + this.toString());
    if (getFacet(K_CLASS)==NIL)   return false;
    
    It class_it = this.getFacet(K_CLASS);
    if (class_it == o_it)
      return true;
    return false;
  } //---- isInstanceOf 
  
  static protected It IsNIL_(Object key, Object value)
  {
    if (key == null || value == null)     
      return NIL;
    
    String key_str   = CastToString(key);
    String value_str = CastToString(value);
    
    if (key_str == K_NIL && value_str == K_NIL)
      return NIL;
      
    return null;
  } //---- IsNIL_()
  
  
  private static String KeyValue_(Object key, Object value)
  { 
    //System.out.println("It.KeyValue key:" + key + " value:" + value);
    String kv = "[" + K_NIL + "," + K_NIL + "]";
    try
    {
      kv = "[" + key.toString() + "," + value.toString() + "]";
    }
    catch (Exception e)
    {}
    
    return kv;
  } //---- KeyValue_
  
  
  //---------- evaluate ----------
  public It evaluate()
  {  return evaluate(NIL);  }
  
  public It evaluate(It arg)
  {   
    ArrayList<It> it_list = new ArrayList<It>();
    it_list.add(arg);
    return evaluate(it_list);
  }
  public It evaluate(ArrayList<It> arg_list)
  { 
    if (IsClass(this))
      return New(this);
    
    if (this == SHELL)
      RunShell_();
    else if (this.isInstanceOf(COMMAND))
      return New(runJavascript_());
    
    return this;
  }
  //---------- evaluate
  
  
  //---------- NewValue ----------
  static public It New(Integer value)
  { 
     It new_integer = New(INTEGER, NIL);
     new_integer.setValue(value);
     return new_integer;
  } // New (NewValue Integer)
  
  static public It New(Float value)
  {  
    //return New(value.toString(), It.NIL);  
    return New(value, value);  // ToDo: .next is not NIL
  } // New (NewValue Float)
  
  static public It New(Double value)
  {  
    //return New(value.toString(), It.NIL);
    return New(value, value);  // ToDo: .next is not NIL
  } // New (NewValue Double)
  
  static public It New(String value)
  {  
    //return NewValue(value, NIL);
    It new_string = New(STRING, NIL);
    new_string.setValue(value);
    return new_string;
  } // New (NewValue String)
  
  // Caution !! Regression if replaced by New ( String)
  static private It NewValue(String value)
  {  return NewValue(value, NIL);  }
  
  static public It NewValue(String value, Object next)
  {  return new It(K_VALUE, value, next);  }
  //---------- NewValue
  
  
  //---------- ToList ----------
  static public ArrayList<It> ToList(Object arg)
  {
    return ToList(new Object[]{arg});
  }
  static public ArrayList<It> ToList(Object[] args)
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
         arg_list.add(It.New(K_VALUE, args[i]));
       else
         arg_list.add(item);
     }
     return arg_list;
  }
  //---------- ToList

  
  //---------- putFacet ----------
  private It putClassFacet_(String facet_name, It facet_value)
  {
    It facet = NewValue(facet_name)
               .putFacet(K_MODEL_LAYER, CLASS_MODEL);
    return putFacet(facet, facet_value);
  }
  public It putFacet(String facet_name, It facet_value)
  {
    It facet = NewValue(facet_name);
    return putFacet(facet, facet_value);
  }
  public It putFacet(It facet, It facet_value)
  {  
    if (_facet_map.containsKey(facet))
    {
      It previous_facet_value = _facet_map.get(facet);
      _facet_map.remove(facet);
      _kv2it.remove(KeyValue_(facet._key, facet.getValue()));
      _kv2it.remove(KeyValue_(previous_facet_value._key, previous_facet_value.getValue()));
    }
    _facet_map.put(facet, facet_value);
    _kv2it.put(KeyValue_(facet.getValue(), this.getValue()), facet);
    
    Object o_facet_value = facet_value;
    try
    {
      It facet_value_it = (It)facet_value;
      o_facet_value = facet_value_it.getValue();
    }
    catch (Exception e) 
    {
      o_facet_value = facet_value;
    }
    
    _kv2it.put(KeyValue_(o_facet_value, this.getValue()), facet_value);
    
    return this;
  } 
  //---------- putFacet
  
  
  //---------- getFacet ----------
  public It getFacet(Object facet)
  { 
    String key_value = KeyValue_(facet, this.getValue());
    It     it        = _kv2it.get(key_value);
    return getFacet(it);
  }
  public It getFacet(Object facet, Object facet_value)
  { 
    String key_value = KeyValue_(facet, facet_value);
    It     it        = _kv2it.get(key_value);
    return getFacet(it);
  }
  public It getFacet(It it)
  {   
    if (_facet_map.containsKey(it))
    {
      It facet = _facet_map.get(it);
      return facet;
    }
    return NIL;
  }
  //---------- getFacet
  
  
  public It getFunction(String function_name, String class_name)
  {
    It  function_it = ENVIRONMENT.getFacet(function_name);
    if (function_it == NIL)
      function_it = New(K_FUNCTION, class_name, class_name);

    return function_it;
  } //---- getFunction
  
  
  public HashMap<It, It> getFacets()
  {   
    return _facet_map;
  } //---- getFacets()
 
  
  @Override
  public String toString() { return getValue().toString(); }

  @Override
  public Object getKey()   { return _key; }
  
  
  //---------- 'Value' accessors ----------
  @Override
  public Object getValue() { return _value; }

  @Override
  public Object setValue(Object value) 
  {
    Object previous_value = _value;
    _value = value;
    return previous_value;
  }
  //---------- 'Value' accessors
  
  
  //---------- 'Next' accessors ----------
  public Object getNext()              
  { return _next; }
  
  public Object setNext(Object new_next)              
  {
    Object previous_next = _next;
    _next = new_next;
    return previous_next;
  }
  //---------- 'Next' accessors
  
  
  public String getUUID()              
  { return _uuid.toString(); }
  //---- getUUID
  
  
  static public boolean IsIt(Object o) 
  {
    try
    {
      It o_it = (It) o;
      return true;
    }
    catch (Exception e) 
    {}
    return false;
  } //---- IsIt
  
  static public boolean IsString(Object o) 
  {
    try
    {
      String o_str = (String) o;
      return true;
    }
    catch (Exception e) 
    {}
    return false;
  } //---- IsString
    
    
  static private Integer CastToInteger_(Object o) 
  {
    Integer o_int = 0;
    try
    {
      String o_str = (String) o;
      o_int = Integer.parseInt(o_str);
    }
    catch (Exception e) 
    {}
    return o_int;
  } //---- CastToInteger_
  
  static public String CastToString(Object o) 
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
      o_str = o.toString();
    }
    return o_str;
  } //--- CastToString
  
  
  static private String CamelCaseToUnderscore_(String input_str)
  {
    String[] words    = input_str.split("(?<=[a-z])(?=[A-Z])");
    String output_str = "";
    for (int i=0; i < words.length; i++)
    {
      output_str += words[i];
      if (i+1 < words.length)
        output_str += "_";
    }
    return output_str.toLowerCase();
  } //---- CamelCaseToUnderscore_
  
  static private String GetInstanceBaseName_(It class_it)
  {
    String   instance_base_name = class_it.getValue().toString();
    String[] words              = instance_base_name.split("(?<=[a-z])(?=[A-Z])");
    instance_base_name          = CamelCaseToUnderscore_(instance_base_name); 
    return instance_base_name;
  } //---- GetInstanceBaseName_
    
  static private String NewInstanceName_(It class_it)
  {
    String   instance_base_name = GetInstanceBaseName_(class_it);
    
    It      count_it       = class_it.getFacet(COUNT);
    if (count_it != NIL)
    {
      Integer instance_count = CastToInteger_(count_it.getValue()) + 1;
      return instance_base_name + instance_count;
    }
   else
      return instance_base_name + "_" + UUID.randomUUID().toString();
  } //---- NewInstanceName_
  
  static private It NewNativeClass_(String class_name, It class_it, It superclass_it)
  {
    It native_class = NIL;
    native_class = new It(class_name, class_name, K_NIL);
    if (class_name.equals(K_METACLASS))
      native_class.putClassFacet_(K_CLASS,      native_class);
    else
      native_class.putClassFacet_(K_CLASS,      class_it); 
    
    native_class.putFacet(NAME,                 native_class);
    native_class.putClassFacet_(K_NEW_F,        native_class); 
    
    if (superclass_it == NIL)
      native_class.putClassFacet_(K_SUPERCLASS, native_class);
    else
      native_class.putClassFacet_(K_SUPERCLASS, superclass_it);
    
    return native_class;
  } //---- NewNativeClass_
  
  static public String Repeat(String str, int times)
  {
    StringBuilder ret = new StringBuilder();
    for(int i = 0;i < times;i++) ret.append(str);
    return ret.toString();
  } //---- Repeat
  
  //---------- Print ----------
  static public String Print(String message)
  {  
    return Print(message, NIL);
  }
  static public String Print(String message, It writer_it)
  {  
    It message_it = New(message);
    return Print(message_it, writer_it);
  }
  static public String Print(It item)
  {
    return Print(item, NIL);
  }
  static public String Print(It item, It writer_it)
  {
    return Print(item, writer_it, WITHOUT_DEPTH);
  }
  static public String Print(It item, int depth)
  { 
    return Print(item, NIL, depth); 
  }
  static public String Print(It item, It writer_it, int depth)
  { 
    return Print(item, writer_it, depth, "", "", true); 
  }
  static public String Print(It item, It writer_it, int depth, String indent_title, String option, boolean first_call)
  {  
    if (first_call)
      _string_writer_str = "";
    
    String suffix = "";
    if (depth != WITHOUT_DEPTH)
      suffix = ":";
    
    String output_str = indent_title + item + suffix;
    String filename = Print_(output_str, option, indent_title, item, writer_it, true);
      
    HashMap<It, It> facet_map  = item.getFacets();
    Set<It>         facets     = facet_map.keySet();
    Iterator<It>    iterator   = facets.iterator();
    while (iterator.hasNext())
    {
      It     facet       = iterator.next();
      It     facet_value = facet_map.get(facet);
      
      if (facet_value != null)
      {
        output_str = indent_title + "  " + facet.getValue() + ": " + facet_value.getValue();
        Print_(output_str, filename, indent_title, item, writer_it, false);
      
        if (depth>=0 && IsIt(facet_value) && facet_value.getFacet(K_CLASS) != NIL)
          if (facet_value != item) // to avoid infinite metaclass recursion: class(Class) = Class
            Print((It)facet_value, writer_it, depth + 1, indent_title + "    ", filename, false);
      }
    }
    return _string_writer_str;
  } //---- Print (It)
  
  static private String Print_(String message, String filename, String indent_title, It item, It writer_it, boolean first_call)
  {
    if (filename == "")
    {
      filename = item.getFacet(NAME).getValue().toString();
      filename = filename.replace(" ", "_");
      filename = filename.replace("/", "_");
      filename = filename.replace("*", "_");
      filename = filename.replace("?", "_");
      filename = filename.replace(".", "_");
      filename += ".txt";
    }
              
    if (writer_it == TEXT_FILE_WRITER)
    {
      try
      {  
        if (first_call)
        {
          boolean success = (new File(filename)).delete();
        }
        FileWriter     fstream = new FileWriter(filename, true); // append
        BufferedWriter out     = new BufferedWriter(fstream);
        if (first_call)
          out.write(message);
        else
          out.write("\r\n" + message); // CrLf (Windows end of line)
        out.close();
      }
      catch (Exception e) {}
    }
    else if (writer_it == STRING_WRITER)
    {
      if (first_call)
      {
        _string_writer_str = "";
        _string_writer_str += message;
      }
      else
        _string_writer_str += "\n" + indent_title + message;
    }
    else
      System.out.println(message);
    
    return filename;
  } //---------- Print_
  
  static private void RunShell_()
  {
    boolean         loop_enabled = true;
    BufferedReader  reader       = new BufferedReader(new InputStreamReader(System.in));   
    while (loop_enabled)
    {
      System.out.print("> ");
      String input_str  = "";
      String output_str = "";
      try
      {
        input_str                = reader.readLine();
        StringTokenizer   st     = new StringTokenizer(input_str, " ");
        ArrayList<String> tokens = new ArrayList<String>();
        while (st.hasMoreTokens()) 
        {
          String token_str  = st.nextToken();
          tokens.add(token_str);
        }
        //System.out.println("tokens_size " + tokens.size());
        //for (int i=0; i<tokens.size(); i++)
        //  System.out.println("token(" + i + ") = '"+ tokens.get(i) + "'");
            
        //---------- help ----------
        if (   input_str.toLowerCase().equals("?") 
            || input_str.toLowerCase().equals("h")
            || input_str.toLowerCase().equals("help"))
        {
          output_str =     "print 'variable':        prints value of variable (e.g: print Object)\n"
                       + "  eval 'variable':         evaluates 'variable'\n"
                       + "  set 'variable' 'value':  sets value of 'variable'\n" 
                       + "  edit 'variable':         edit value of 'variable' in default editor\n" 
                       + "  new:                     creates an 'Object' instance\n" 
                       + "  new 'class':             creates an instance of 'class' (eg: new Integer)\n" 
                       + "  list 'class':            lists all instances of 'class' (eg: list Command)\n"
                       + "  ?, h, help:              prints this text\n"
                       + "  q, quit or exit:         exits this Shell";
          System.out.println("  " + output_str);
        }
        //---------- print ----------
        else if (  tokens.size()==2 
                && (tokens.get(0).equals("print")))
        {
          It item = _Instance_map.get(tokens.get(1));
          if (item == null)
            item = NIL;
          output_str = Print(item, STRING_WRITER);
          System.out.println(IndentLines_(output_str));
        }
        //---------- list ----------
        else if (  tokens.size()==2 
                && (tokens.get(0).equals("list")))
        {
          It class_it = _Instance_map.get(tokens.get(1));
          if (class_it == null)
            class_it = NIL;
          output_str = "";
          if (IsClass(class_it))
          {
            Set<String>  instance_names = _Instance_map.keySet();
            Iterator<String> iterator   = instance_names.iterator();
            while (iterator.hasNext())
            {
              String instance_name = iterator.next();
              It     instance      = _Instance_map.get(instance_name);
              if (instance.isInstanceOf(class_it))
                output_str += Print(instance_name + "\n", STRING_WRITER);  
            }
            if (output_str != "")
              System.out.println(IndentLines_(output_str));
          }
        }
        //---------- edit ----------
        else if (  tokens.size()==2 
                && (tokens.get(0).equals("edit")))
        {
           It item = _Instance_map.get(tokens.get(1));
           if (item == null)
             item = NIL;
          
           if (item != NIL)
           {
             String         filename = "facet_value.txt";
             
             boolean success = (new File(filename)).delete();
             
             FileWriter     fstream  = new FileWriter(filename);
             BufferedWriter out      = new BufferedWriter(fstream);
         
             It value_it = NIL;
             if (item.isInstanceOf(COMMAND))
               value_it = item.getFacet(SCRIPT_F); 
             else
               value_it = item.getFacet(VALUE);            
             out.write(value_it.toString());
             out.close();
           
             Runtime runtime = Runtime.getRuntime();
             Process process = runtime.exec("notepad.exe " + filename);
             process.waitFor();
             String new_value_str = ReadFileAsString_(filename);
             if (item.isInstanceOf(COMMAND))
               item.putFacet(SCRIPT_F, New(new_value_str)); 
             else
               item.putFacet(VALUE, New(new_value_str)); 
           }
           output_str = Print(item, STRING_WRITER);
           System.out.println(IndentLines_(output_str));
        }
        //---------- set ----------
        else if (  tokens.size()>=3 
                && (tokens.get(0).equals("set")))
        {
          It item = _Instance_map.get(tokens.get(1));
          if (item == null)
            item = NIL;
          String value_str = tokens.get(2);
          if (item.isInstanceOf(COMMAND))
            item.putFacet(SCRIPT_F, New(value_str));
          else
            item.putFacet(VALUE, New(value_str));
          output_str = Print(item, STRING_WRITER);
          System.out.println(IndentLines_(output_str));
        }
        //---------- eval ----------
        else if (  tokens.size()==2 
                && (tokens.get(0).equals("eval")))
        {
          It item = _Instance_map.get(tokens.get(1));
          if (item == null)
            item = NIL;
          output_str = item.evaluate().toString();
          System.out.println(IndentLines_(output_str));
        }
        //---------- new ----------
        else if (  (tokens.size()==1 || tokens.size()==2)
                && (tokens.get(0).equals("new")))
        {
          It new_item = NIL;
          if (tokens.size()==2)
          {
            String class_name = tokens.get(1);
            It class_item = _Instance_map.get(tokens.get(1));
            if (class_item!=null && IsClass(class_item))
              new_item = New(class_item);
          }
          else
            new_item = New();
          output_str = Print(new_item, STRING_WRITER);
          System.out.println(IndentLines_(output_str));
        }
        //---------- quit ----------
        else if (  input_str.toLowerCase().equals("q") 
                || input_str.toLowerCase().equals("quit") 
                || input_str.toLowerCase().equals("exit"))
          loop_enabled = false;
        else
        {
          // check if first token is a Command instance
          if (tokens.size()==1) 
          {
            It command_it = _Instance_map.get(tokens.get(0));
            if (command_it != null && command_it.isInstanceOf(COMMAND))
            {
              output_str = command_it.evaluate().toString();
              System.out.println(IndentLines_(output_str));
            }
            else
            {
              // Unknown command
              output_str = "Unknown command: " + tokens.get(0) + "\n  use 'help' for a list of available commands";
              System.out.println("  " + output_str);
            }
          }
        }
      }
      catch (Exception e) {}
    }
  } //---- RunShell_
 
  // http://docs.oracle.com/javase/6/docs/technotes/guides/scripting/programmer_guide/index.html#helloworld
  private String runJavascript_()
  {
    String output_str = "";
    try
    {
      // create a script engine manager
      ScriptEngineManager factory = new ScriptEngineManager();
      
      // create a JavaScript engine
      ScriptEngine        engine  = factory.getEngineByName("JavaScript");
      
      // Redirect the engine's standard output to a StringWriter instance.
      StringWriter writer = new StringWriter ();
      PrintWriter  pw     = new PrintWriter(writer, true);
      engine.getContext().setWriter(pw);

      String script = this.getFacet(SCRIPT_F).toString();
      engine.eval(script);

      // Obtain the string buffer's contents and output these contents.
      StringBuffer sb = writer.getBuffer();
      output_str      = sb.toString();
    }
    catch (Exception e) 
    {
      // Syntax error in Javascript
      output_str = e.getMessage();
    }
    return output_str;
  } //---- runJavascript_

  static private String IndentLines_(String message)
  {
    String[] lines      = message.split("\n");
    String   output_str = "";
    for (int i=0; i<lines.length; i++)
    {
      output_str += "  " + lines[i];
      if (i<lines.length-1)
        output_str += "\n";
    }
    return output_str;
  } //---- IndentLines_
  
  private static String ReadFileAsString_(String filename)
  {
    try
    {
      StringBuffer   fileData = new StringBuffer(1000);
      BufferedReader reader   = new BufferedReader(new FileReader(filename));
      char[]         buf      = new char[1024];
      int            numRead  = 0;
      while((numRead = reader.read(buf)) != -1)
      {
        String readData = String.valueOf(buf, 0, numRead);
        fileData.append(readData);
        buf = new char[1024];
      }
      reader.close();
      return fileData.toString();
    }
    catch (Exception e) 
    {
      System.out.println(e.getMessage());
    }
    return "";
  } //---- ReadFileAsString_
} //---------- It
