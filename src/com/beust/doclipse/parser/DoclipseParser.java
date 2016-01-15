// $ANTLR 2.7.3: "src/doclipse.g" -> "DoclipseParser.java"$

package com.beust.doclipse.parser;
import java.io.StringReader;

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;

public class DoclipseParser extends antlr.LLkParser       implements DoclipseParserTokenTypes
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
    
protected DoclipseParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public DoclipseParser(TokenBuffer tokenBuf) {
  this(tokenBuf,1);
}

protected DoclipseParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public DoclipseParser(TokenStream lexer) {
  this(lexer,1);
}

public DoclipseParser(ParserSharedInputState state) {
  super(state,1);
  tokenNames = _tokenNames;
}

	public final void start() throws RecognitionException, TokenStreamException {
		
		Token  tag = null;
		
		try {      // for error handling
			if ((LA(1)==TAG)) {
				match(TAG);
				{
				int _cnt3=0;
				_loop3:
				do {
					if ((LA(1)==IDENTIFIER)) {
						completeAssignment();
					}
					else {
						if ( _cnt3>=1 ) { break _loop3; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt3++;
				} while (true);
				}
				{
				switch ( LA(1)) {
				case IDENTIFIER:
				{
					partialAssignment();
					break;
				}
				case EOF:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
			}
			else if ((LA(1)==TAG)) {
				tag = LT(1);
				match(TAG);
				{
				switch ( LA(1)) {
				case IDENTIFIER:
				{
					partialAssignment();
					break;
				}
				case EOF:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				System.out.println("TAG :" + tag);
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
	}
	
	public final void completeAssignment() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			match(IDENTIFIER);
			match(EQ);
			value();
			System.out.println("COMPLETE");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_1);
		}
	}
	
	public final void partialAssignment() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			match(IDENTIFIER);
			{
			switch ( LA(1)) {
			case EQ:
			{
				match(EQ);
				break;
			}
			case EOF:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			System.out.println("PARTIAL 1");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
	}
	
	public final void value() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case IDENTIFIER:
			{
				match(IDENTIFIER);
				break;
			}
			case 7:
			{
				match(7);
				match(IDENTIFIER);
				match(7);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_1);
		}
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"TAG",
		"IDENTIFIER",
		"EQ",
		"\"\\\"\"",
		"DQ",
		"WS"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 2L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 34L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	
	}
