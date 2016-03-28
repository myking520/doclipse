package com.beust.doclipse.doclet;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thoughtworks.qdox.model.AbstractBaseJavaEntity;
import com.thoughtworks.qdox.model.DocletTag;

public class DefaultXDocletTag implements DocletTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<NamedParam> namedParams = null;
	private Map<String, List<NamedParam>> namedParamMap = new HashMap<String, List<NamedParam>>();

	public List<NamedParam> getNamedParams() {
		if (namedParams == null) {
			namedParams = parseNamedParameters(this.getValue());
		}
		return namedParams;
	}

	public List<NamedParam> getNamedParams(String name) {
		List<NamedParam> namedParamsrt = this.namedParamMap.get(name);
		if (namedParamsrt != null) {
			return namedParamsrt;
		}
		namedParamsrt = new ArrayList<NamedParam>();

		List<NamedParam> namedParams = this.getNamedParams();
		for (int i = 0; i < namedParams.size(); i++) {
			NamedParam namedParam = namedParams.get(i);
			if (namedParam.getName().equals(name)) {
				namedParamsrt.add(namedParam);
			}
		}
		namedParamMap.put(name, namedParamsrt);
		return namedParamsrt;
	}

	private final String name;
	private final String value;
	private final int lineNumber;

	private String[] parameters;
	private Map namedParameters;
	private AbstractBaseJavaEntity context;

	public DefaultXDocletTag(String name, String value, AbstractBaseJavaEntity context, int lineNumber) {
		this.name = name;
		this.value = value;
		this.context = context;
		this.lineNumber = lineNumber;
	}

	public DefaultXDocletTag(String name, String value) {
		this(name, value, null, 0);
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public String[] getParameters() {
		if (parameters == null) {
			parameters = TagParser.parseWords(value);
		}
		return parameters;
	}

	public Map getNamedParameterMap() {
		if (namedParameters == null) {
			namedParameters = TagParser.parseNamedParameters(value);
		}
		return namedParameters;
	}

	public String getNamedParameter(String key) {
		return (String) getNamedParameterMap().get(key);
	}

	public final AbstractBaseJavaEntity getContext() {
		return context;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	static StreamTokenizer makeTokenizer(String tagValue) {
		StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(tagValue));
		tokenizer.resetSyntax();
		tokenizer.wordChars('A', 'Z');
		tokenizer.wordChars('a', 'z');
		tokenizer.wordChars('0', '9');
		tokenizer.wordChars('-', '-');
		tokenizer.wordChars('_', '_');
		tokenizer.wordChars('.', '.');
		tokenizer.quoteChar('\'');
		tokenizer.quoteChar('"');
		tokenizer.whitespaceChars(' ', ' ');
		tokenizer.whitespaceChars('\t', '\t');
		tokenizer.whitespaceChars('\n', '\n');
		tokenizer.whitespaceChars('\r', '\r');
		tokenizer.eolIsSignificant(false);
		return tokenizer;
	}

	public static List<NamedParam> parseNamedParameters(String tagValue) {
		StreamTokenizer tokenizer = makeTokenizer(tagValue);
		List<NamedParam> namedParams = new ArrayList<NamedParam>();
		try {
			while (tokenizer.nextToken() == StreamTokenizer.TT_WORD) {
				String key = tokenizer.sval;
				if (tokenizer.nextToken() != '=') {
					break;
				}
				switch (tokenizer.nextToken()) {
				case StreamTokenizer.TT_WORD:
				case '"':
				case '\'':
					namedParams.add(new NamedParam(key, tokenizer.sval));
				default:
					break;
				}
			}
		} catch (IOException e) {
			// ignore
		}
		return namedParams;
	}

}
