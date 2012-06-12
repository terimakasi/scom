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
 * Michel Kern - 10 june 2012 - 22:46
 * Copyright (C) <2012> www.terimakasi.com
 * ...................................................................................
 * ToDo:
 * - Readme.md or Readme.html ?
 * - Automatic tests for regression check
 * - Check if Windows/Unix to adapt (e.g: GetDesktop(),...):
 *   - Text editor (Notepad/vi)
 * - Events / Hooks / Callbacks ?
 *   - Simulation of digital electronic components (e.g: logic gates)
 * - Environment
 *   - Implement 'Plugins' for self-extensibility of 'environment'
 *     - SHELL          could be a Plugin !
 *     - TextFileWriter could be a Plugin !
 *   - 'ERROR' variable which stores a message about last error
 * - Shell: 
 *   - if input ends with ';' then interpret it as Javascript
 *   - 'info' command in Shell: show version, instance count, plugins
 *   - 'exec' command
 *   - 'run' command (runs commands from a script file)
 *   - 'history' command
 *   - implement 'history' with up/down arrow keys
 * - StringWriter and TextFileWriter as instances of 'Writer' (check in SHELL with 'list Writer')
 * - Writer:
 *   - RDF / Owl / JSon
 *   - Database !
 *   - D3/JS Graph drawing
 *   - Html (1 file per object/facet and links)
 *   - GraphViz format
 * - Reader !
 *   - e.g: read graph from Git or from url
 *   - Database !
 * - API consistency: 
 *   - get rid of 'NewValue()'
 *   - getValue() and 'VALUE' facet should be only 2 different means to access value 
 * - AOM:
 *   - New Integer,...: .next should not be NIL
 *   - Allow to define name when instanciating a class (e.g: New(name, class_it))
 *   - Chain Inheritance (attributes of superclasses recursively)
 *   - 'implement IInterface' cf. Java and allow implementations of multiple 'Interface' classes
 *   - Multiple values for _value of an It instance (getValue() = get(0) and add getValue(index) and valueCount) 
 *   - Scope/Context for a facet (ex: localization of 'NAME')
 * - Port to JVM !
 * ...................................................................................
 */
package scom;

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.*;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class It implements java.util.Map.Entry<Object, Object>               
{
  public static final String               VERSION                  = "0.1.9";
  public static final String               BASENAME                 = It.class.getSimpleName();
  public static final String               CLASS_NAME               = It.class.getCanonicalName();
  
  private static      HashMap<String, It>  _Instance_map            = new HashMap<String, It>();
    
  public static final String               K_NIL                    = "NIL";
  public static final It                   NIL                      = new It(K_NIL, K_NIL, K_NIL);
  
  private static final String              _K_ID                    = "id";
  public static final It                   ID                       = new It(_K_ID, _K_ID, K_NIL); 
  
  public static final String               K_VALUE                  = "value";
    
  public static       It                   NAME;
  public static       It                   INTERFACE;
  public static       It                   COUNT;
  
  public static       It                   STRING_WRITER;
  public static       It                   TEXT_FILE_WRITER;
  
  public static       It                   VALUE;
  public static       It                   SCRIPT_F;
  
  public static final String               K_MODEL_LAYER            = "model_layer";
  
  private static final String              _K_CLASS_MODEL           = "class_model";
  public static final It                   CLASS_MODEL              = new It(_K_CLASS_MODEL, _K_CLASS_MODEL, K_NIL);
  
  public static final String               V_USER_MODEL             = "user_model";
  public static final It                   USER_MODEL               = new It(V_USER_MODEL, V_USER_MODEL, K_NIL);
  public static       It                   NEW_F;
  
  public static       It                   META_METACLASS           = null;
  public static       It                   METACLASS                = null;
  public static       It                   INTERFACE_METACLASS      = null;
  public static       int                  _init_count              = 0;
  
  public static       It                   OBJECT;                  // Caution: don't initialize with null
  public static       It                   DOUBLE;                  // Caution: don't initialize with null
  public static       It                   INTEGER;                 // Caution: don't initialize with null
  public static       It                   STRING;                  // Caution: don't initialize with null
  public static       It                   WRITER;                  // Caution: don't initialize with null
  public static       It                   COMMAND;                 // Caution: don't initialize with null
  
  private static final String              _K_TRUE                  = "true";
  public static       It                   TRUE                     = new It(_K_TRUE, _K_TRUE, _K_TRUE);
  
  public static       It                   ENVIRONMENT              = GetEnvironment_();
  public static       It                   SHELL;
  
  public static final int                  WITHOUT_DEPTH            = -1;
  public static final int                  WITH_UNLIMITED_DEPTH     = 0;
  protected static    String               _String_writer_str;
  
  protected           HashMap<It, It>      _facet_map               = new HashMap<It, It>();
  protected           HashMap<String, It>  _kv2it                   = new HashMap<String, It>();
  protected final     Object               _key;
  protected           Object               _value                   = NIL;
  protected           Object               _next                    = NIL;
  protected final     UUID                 _uuid                    = UUID.randomUUID();
  
  //............. internal purpose .............
  private static final String              _K_EMPTY_STRINg          = "";
  //
  private static final String              _K_NAME                  = "name";
  private static final String              _K_COUNT                 = "count";
  private static final String              _K_CLASS                 = "class";
  private static final String              _K_SUPERCLASS            = "superclass";
  private static final String              _K_NEW_F                 = "new()";
  private static final String              _K_SCRIPT_F              = "script()";
  private static final String              _K_FUNCTION              = "function";
  private static final String              _K_INTERFACE             = "interface";
  private static final String              _K_ENVIRONMENT           = "environment";
  private static final String              _K_SHELL                 = "shell";
  //
  private static final String              _K_META_METACLASS        = "MetaClass";
  private static final String              _K_METACLASS             = "Class";
  private static final String              _K_INTERFACE_METACLASS   = "Interface";
  private static final String              _K_OBJECT                = "Object";
  private static final String              _K_INTEGER               = "Integer";
  //public static final String               _K_FLOAT                 = "Float";
  private static final String              _K_STRING                = "String";
  private static final String              _K_COMMAND               = "Command";
  private static final String              _K_WRITER                = "Writer";
  private static final String              _K_STRING_WRITER         = "StringWriter";
  private static final String              _K_TEXT_FILE_WRITER      = "TextFileWriter";
  //............. internal purpose .............
  
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
      String key_str = CastToString(key);
      _Instance_map.put(key_str, this);
    }
  } // Private Constructor (use 'New' helper method to instanciate It)
  
  // special use: store instance's 'ID' facet (uuid)
  protected It(Object key, Object value) 
  {
    _key   = key;    
    _value = value;
    _next  = NIL;
  } // Private Constructor
  
  static protected It GetEnvironment_()
  {
    if (ENVIRONMENT == null)
      ENVIRONMENT = new It(_K_ENVIRONMENT, _K_ENVIRONMENT, K_NIL);
    
    return ENVIRONMENT;
  } //---- GetEnvironment_
  
  protected void initEnvironment()
  {
    if (_init_count > 1)
      return;
    
    _init_count++;
    GetEnvironment_();
    
    VALUE            = new It(K_VALUE,             K_VALUE,             K_NIL);
    SHELL            = new It(_K_SHELL,            _K_SHELL,            K_NIL); 
    NAME             = new It(_K_NAME,             _K_NAME,             K_NIL);  
    NEW_F            = new It(_K_NEW_F,            _K_NEW_F,            K_NIL);
    SCRIPT_F         = new It(_K_SCRIPT_F,         _K_SCRIPT_F,         K_NIL);
    INTERFACE        = new It(_K_INTERFACE,        _K_INTERFACE,        K_NIL);
    
    STRING_WRITER    = new It(_K_STRING_WRITER,    _K_STRING_WRITER,    K_NIL);
    TEXT_FILE_WRITER = new It(_K_TEXT_FILE_WRITER, _K_TEXT_FILE_WRITER, K_NIL);
    
    COUNT  = new It(_K_COUNT,  _K_COUNT,   K_NIL)
      .putFacet(K_MODEL_LAYER, CLASS_MODEL);
    
    // http://en.wikipedia.org/wiki/Metaclass  
    META_METACLASS = NewNativeClass_(_K_META_METACLASS, NIL, NIL);   // NB: don't add 'COUNT' value yet, INTEGER is required  
    
    METACLASS = NewNativeClass_(_K_METACLASS, META_METACLASS, NIL); // NB: don't add 'COUNT' value yet, INTEGER is required  
    INTERFACE_METACLASS = NewNativeClass_(_K_INTERFACE_METACLASS, META_METACLASS, NIL); // NB: don't add 'COUNT' value yet, INTEGER is required  
    
    OBJECT = NewNativeClass_(_K_OBJECT, METACLASS, NIL);        // NB: don't add 'COUNT' value yet, INTEGER is required 
      OBJECT.putFacet(VALUE,            NIL);
    
    INTEGER = NewNativeClass_(_K_INTEGER, METACLASS, OBJECT);   // NB: don't add 'COUNT' value yet, INTEGER is required 
      INTEGER.putFacet(VALUE,           New(0)); 
      
    STRING = NewNativeClass_(_K_STRING,  METACLASS, OBJECT);   // NB: don't add 'COUNT' value yet, INTEGER is required 
      STRING.putFacet(VALUE,            New(_K_EMPTY_STRINg)); 
      
    WRITER = NewNativeClass_(_K_WRITER, METACLASS, OBJECT);    // NB: don't add 'COUNT' value yet, INTEGER is required 
      WRITER.putFacet(VALUE,            New(_K_EMPTY_STRINg));
      
    COMMAND = NewNativeClass_(_K_COMMAND, METACLASS, OBJECT);  // NB: don't add 'COUNT' value yet, INTEGER is required 
      COMMAND.putFacet(SCRIPT_F,        New(_K_EMPTY_STRINg)); 
    
    META_METACLASS.putFacet(COUNT,      New(1)); // NB: requires INTEGER so don't move this code up (OBJECT creation)
    METACLASS.putFacet(COUNT,           New(4)); // NB: requires INTEGER so don't move this code up (OBJECT creation)
    OBJECT.putFacet(COUNT,              New(0));
    INTEGER.putFacet(COUNT,             New(0)); 
    STRING.putFacet(COUNT,              New(0));
    COMMAND.putFacet(COUNT,             New(0));
    
    ENVIRONMENT.putFacet(_K_METACLASS,  METACLASS)
               .putFacet(_K_OBJECT,     OBJECT) 
               .putFacet(_K_INTEGER,    INTEGER)
               .putFacet(_K_STRING,     STRING) 
               .putFacet(_K_COMMAND,    COMMAND); 
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
      instance_it.putFacet(_K_CLASS, class_it);
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
  
  
  //---------- New Class ----------
  static public It New(String class_name, It metaclass_it)
  {   
    return New(class_name, METACLASS, OBJECT);  
  }
  static public It New(String class_name, It metaclass_it, It super_it)
  {  
    //System.out.println("It.NewClass class_name:" + class_name + "  class:" + metaclass_it + "  super:" + super_it);
    if (! IsClass(metaclass_it))  return NIL;
    
    It new_class = new It(class_name, class_name, K_NIL);
      new_class.putFacet      (NAME,         new_class);
      new_class.putFacet      (COUNT,        New(0));
      new_class.putClassFacet_(_K_CLASS,     metaclass_it);
      new_class.putClassFacet_(_K_NEW_F,     new_class);
      new_class.putClassFacet_(_K_SUPERCLASS, super_it);
      
      ENVIRONMENT.putFacet(class_name, new_class);
          
      //---- increment count of class intances ----
      It      count_it       = metaclass_it.getFacet(COUNT);
      Integer instance_count = CastToInteger_(count_it) + 1;
      count_it.setValue(instance_count.toString());
      //---- increment count of class intances
    return new_class;
  }
  //---------- New Class ----------
  
  
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
  
  
  //---------- New Value (e.g: Integer, String, ...) ----------
  static public It New(Integer value)
  { 
     It new_integer = New(INTEGER, NIL);
     new_integer.setValue(value);
     return new_integer;
  } // New Integer
  static public It New(Float value)
  {   
    return New(value, value);  // ToDo: .next is not NIL
  } // New Float
  static public It New(Double value)
  {  
    return New(value, value);  // ToDo: .next is not NIL
  } // New Double
  static public It New(String value)
  {  
    It new_string = New(STRING, NIL);
    new_string.setValue(value);
    return new_string;
  } // New String
  //---------- New Value (e.g: Integer, String, ...) ----------
  
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
      function_it = New(_K_FUNCTION, class_name, class_name);

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
  
  static public boolean IsClass(It o_it)
  {
    if (o_it == null)             return false;
    if (o_it == NIL)              return false;
    
    It  metaclass_it = o_it.getFacet(_K_CLASS);
    if (metaclass_it == NIL)      return false;
    
    It  count_it = o_it.getFacet(COUNT);
    if (count_it == NIL)          return false;
    
    It  new_f_it = o_it.getFacet(_K_NEW_F);
    if (new_f_it == NIL)          return false;
    
    It  superclass_it = o_it.getFacet(_K_SUPERCLASS);
    if (superclass_it == NIL)     return false;
    
    return true;
  } //---- IsClass 
  
  public boolean isInstanceOf(It o_it)
  {
    if (o_it == null)             return false;
    if (o_it == NIL)              return false;
    
    if (! IsClass(o_it))          return false;
    
    //System.out.println("isInstanceOf item: " + this.toString());
    if (getFacet(_K_CLASS)==NIL)   return false;
    
    It class_it = this.getFacet(_K_CLASS);
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
    if (class_name.equals(_K_META_METACLASS))
      native_class.putClassFacet_(_K_CLASS,      native_class);
    else
      native_class.putClassFacet_(_K_CLASS,      class_it); 
    
    native_class.putFacet(NAME,                 native_class);
    native_class.putClassFacet_(_K_NEW_F,        native_class); 
    
    if (superclass_it == NIL)
      native_class.putClassFacet_(_K_SUPERCLASS, native_class);
    else
      native_class.putClassFacet_(_K_SUPERCLASS, superclass_it);
    
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
      _String_writer_str = "";
    
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
      
        if (depth>=0 && IsIt(facet_value) && facet_value.getFacet(_K_CLASS) != NIL)
          if (facet_value != item) // to avoid infinite metaclass recursion: class(Class) = Class
            Print((It)facet_value, writer_it, depth + 1, indent_title + "    ", filename, false);
      }
    }
    return _String_writer_str;
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
        _String_writer_str = "";
        _String_writer_str += message;
      }
      else
        _String_writer_str += "\n" + indent_title + message;
    }
    else
      System.out.println(message);
    
    return filename;
  } //---------- Print_
  
  static private void RunShell_()
  {
    boolean         loop_enabled = true;
    BufferedReader  reader       = new BufferedReader(new InputStreamReader(System.in)); 
    boolean         first_time   = true;
    while (loop_enabled)
    {
      if (first_time)
      {
        System.out.print("SCOM Shell " + VERSION + " is ready.\n");
        System.out.print("Type 'help' to get help.\n");
        first_time = false;
      }
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
          output_str =     "print VARIABLE           prints value of VARIABLE (e.g: print Object)\n"
                       + "  eval VARIABLE            evaluates VARIABLE (e.g: eval command1\n"
                       + "  set VARIABLE VALUE       sets VALUE of VARIALE (e.g: set command1 print('Hello');\n" 
                       + "  edit VARIABLE            edit value of VARIABLE in default editor\n" 
                       + "  new [CLASS]              creates an CLASS instance (CLASS is 'Object' if not\n" 
                       + "                           provided. eg: new Integer)\n" 
                       + "  list CLASS               lists all instances of CLASS (eg: list Command)\n"
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
  
  public static void main(String[] args) 
  {
    // Calls interactive command shell
    SHELL.evaluate();
  } //---- main()
} //---------- It
