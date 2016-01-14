header {
package com.beust.doclipse.parser;
import java.io.StringReader;
}

class DoclipseParser extends Parser;

options {
//  analyzerDebug = true;
}


{

    static public boolean completeOnTagName = true;
    static public boolean completeOnAttributeName = false;
    static public boolean completeOnAttributeValue = false;
    static public Token fragment;
    private String identifier = null;

    static public void main(String[] argv)  
      throws antlr.RecognitionException, antlr.TokenStreamException
    {
      String s = "@ej a = b";
      if (argv.length > 0) {
        s = argv[0];
      }
      System.out.println("Parsing " + s);
      {
	      StringReader sr = new StringReader(s);
	      DoclipseLexer dl = new DoclipseLexer(sr);
	      Token t = dl.nextToken();
	      while (t.getType() != Token.EOF_TYPE) {
	        System.out.println("Token: " + t.getText());
	        t = dl.nextToken();
	      }
	    }
	      /*
	      DoclipseParser parser = new DoclipseParser(dl);
	      parser.start();
	      System.out.println("Parsing successful @" + fragment.toString() + "@"
	       + " " + completeOnTagName
	       + " " + completeOnAttributeName
	       + " " + completeOnAttributeValue);
	     }
	     
	     {
	      StringReader sr = new StringReader(s);
	      DoclipseLexer dl = new DoclipseLexer(sr);
	      DoclipseParser parser = new DoclipseParser(dl);
	      parser.start();
	      System.out.println("Parsing successful @" + fragment.toString() + "@"
	         + " " + completeOnTagName
	         + " " + completeOnAttributeName
	         + " " + completeOnAttributeValue);
	      }
	      */
	    }
    }

start :
  TAG (completeAssignment)+ ( options {greedy=true;} :  partialAssignment)?
  | 
  tag: TAG ( options {greedy=true;} :  partialAssignment)?
  { System.out.println("TAG :" + tag); }  
  ;
   
partialAssignment :
  IDENTIFIER ( options {greedy=true;} : EQ)?
  {  System.out.println("PARTIAL 1"); }
  ;
  
completeAssignment:
  IDENTIFIER EQ value
  { System.out.println("COMPLETE"); }
  ;
  
value :
  IDENTIFIER |
  "\"" IDENTIFIER "\""
   ;

class DoclipseLexer extends Lexer;

TAG:
  '@' IDENTIFIER
  ;
    
EQ :
  '='
  ;
  
DQ :
  '"'
  ;
  
IDENTIFIER :
   ( 'a'..'z'|'A'..'Z'|'_'  | '-' | ':' | '.') +
    ;
    
WS  :   (   ' ' | '*'
        |   '\t'
        |   '\r' '\n' { newline(); }
        |   '\n'      { newline(); }
        )
        {$setType(Token.SKIP);} //ignore this token
    ;