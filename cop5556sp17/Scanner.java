package cop5556sp17;

import java.util.ArrayList;
import java.util.Arrays;

public class Scanner {
	/**
	 * Kind enum
	 */
	
	public static enum Kind {
		IDENT(""), INT_LIT(""), KW_INTEGER("integer"), KW_BOOLEAN("boolean"), 
		KW_IMAGE("image"), KW_URL("url"), KW_FILE("file"), KW_FRAME("frame"), 
		KW_WHILE("while"), KW_IF("if"), KW_TRUE("true"), KW_FALSE("false"), 
		SEMI(";"), COMMA(","), LPAREN("("), RPAREN(")"), LBRACE("{"), 
		RBRACE("}"), ARROW("->"), BARARROW("|->"), OR("|"), AND("&"), 
		EQUAL("=="), NOTEQUAL("!="), LT("<"), GT(">"), LE("<="), GE(">="), 
		PLUS("+"), MINUS("-"), TIMES("*"), DIV("/"), MOD("%"), NOT("!"), 
		ASSIGN("<-"), OP_BLUR("blur"), OP_GRAY("gray"), OP_CONVOLVE("convolve"), 
		KW_SCREENHEIGHT("screenheight"), KW_SCREENWIDTH("screenwidth"), 
		OP_WIDTH("width"), OP_HEIGHT("height"), KW_XLOC("xloc"), KW_YLOC("yloc"), 
		KW_HIDE("hide"), KW_SHOW("show"), KW_MOVE("move"), OP_SLEEP("sleep"), 
		KW_SCALE("scale"), EOF("eof");

		Kind(String text) {
			this.text = text;
		}

		final String text;

		String getText() {
			return text;
		}
	}
/**
 * Thrown by Scanner when an illegal character is encountered
 */
	@SuppressWarnings("serial")
	public static class IllegalCharException extends Exception {
		public IllegalCharException(String message) {
			super(message);
		}
	}
	
	/**
	 * Thrown by Scanner when an int literal is not a value that can be represented by an int.
	 */
	@SuppressWarnings("serial")
	public static class IllegalNumberException extends Exception {
	public IllegalNumberException(String message){
		super(message);
		}
	}
	

	/**
	 * Holds the line and position in the line of a token.
	 */
	static class LinePos {
		public final int line;
		public final int posInLine;
		
		public LinePos(int line, int posInLine) {
			super();
			this.line = line;
			this.posInLine = posInLine;
		}

		@Override
		public String toString() {
			return "LinePos [line=" + line + ", posInLine=" + posInLine + "]";
		}
	}
		

	public class Token {
		public final Kind kind;
		public final int pos;  //position in input array
		public final int length;  

		//returns the text of this Token
		public String getText() {
			//TODO IMPLEMENT THIS
			return chars.substring(this.pos,this.pos+this.length).toString();
		}
		
		//returns a LinePos object representing the line and column of this Token
		LinePos getLinePos(){
			//TODO IMPLEMENT THIS
			int[] lArray=new int[eol.size()];
			for(int i = 0; i < eol.size() ; i++){
				lArray[i]=eol.get(i);
			}
			Arrays.sort(lArray);
			int line=Arrays.binarySearch(lArray,this.pos);
			int linePos;
			if(line<0){
				line=(line*-1)-2;
			}
			linePos=lArray[line];
			return new LinePos(line,this.pos-linePos);
		}

		Token(Kind kind, int pos, int length) {
			this.kind = kind;
			this.pos = pos;
			this.length = length;
		}

		/** 
		 * Precondition:  kind = Kind.INT_LIT,  the text can be represented with a Java int.
		 * Note that the validity of the input should have been checked when the Token was created.
		 * So the exception should never be thrown.
		 * 
		 * @return  int value of this token, which should represent an INT_LIT
		 * @throws NumberFormatException
		 */
		public int intVal() throws NumberFormatException{
			//TODO IMPLEMENT THIS
			return Integer.parseInt(chars.substring(this.pos,this.pos+this.length));
		}
		
	}


	Scanner(String chars) {
		this.chars = chars;
		tokens = new ArrayList<Token>();
		eol=new ArrayList<Integer>();
		eol.add(0);
	}


	
	/**
	 * Initializes Scanner object by traversing chars and adding tokens to tokens list.
	 * 
	 * @return this scanner
	 * @throws IllegalCharException
	 * @throws IllegalNumberException
	 */
	public Scanner scan() throws IllegalCharException, IllegalNumberException {
		int pos = 0; 
		//TODO IMPLEMENT THIS!!!!
		int length = chars.length();
	    String state = "START";
	    int startPos = 0;
	    int ch;
	    while(pos <= length){
	        ch = pos < length ? chars.charAt(pos) : -1;
	        switch(state){
	        case "START":{
	        	pos = skipWhiteSpace(pos);
	        	ch = pos < length ? chars.charAt(pos) : -1;
	            startPos = pos;
	            
	            switch (ch) {
	            case -1: {
	            	tokens.add(new Token(Kind.EOF, pos, 0)); 
	            	pos++;
	            	} break; 
	            	
	            case '+': {
	            	tokens.add(new Token(Kind.PLUS, startPos, 1));
	            	pos++;
	            	} break;
	            	
	            case '0': {
	            	tokens.add(new Token(Kind.INT_LIT,startPos, 1));
	            	pos++;
	            	} break;
	            	
	            case '%': {
	            	tokens.add(new Token(Kind.MOD,startPos, 1));
	            	pos++;
	            } break;
	            
	            case '&': {
	            	tokens.add(new Token(Kind.AND,startPos, 1));
	            	pos++;
	            } break;
	
	            case ';': {
	            	tokens.add(new Token(Kind.SEMI,startPos, 1));
	            	pos++;
	            } break;
	            
	            case ',': {
	            	tokens.add(new Token(Kind.COMMA,startPos, 1));
	            	pos++;
	            } break;
	            
	            case '(': {
	            	tokens.add(new Token(Kind.LPAREN,startPos, 1));
	            	pos++;
	            } break;
	            
	            case ')': {
	            	tokens.add(new Token(Kind.RPAREN,startPos, 1));
	            	pos++;
	            } break;
	            
	            case '{': {
	            	tokens.add(new Token(Kind.LBRACE,startPos, 1));
	            	pos++;
	            } break;
	            
	            case '}': {
	            	tokens.add(new Token(Kind.RBRACE,startPos, 1));
	            	pos++;
	            } break;
	           
	            case '|': {
	            	state = "AFTER_OR";
	            	pos++;
	            } break;
	            
	            case '-': {
	            	state = "AFTER_MINUS";
	            	pos++;
	            } break;            
	            
	            case '/': {
	            	state = "AFTER_DIV";
	            	pos++;
	            } break;
	            
	            case '=': {
	            	state = "AFTER_EQ";
	            	pos++;
	            	} break;
	            
	            case '!': {
	            	state = "AFTER_NT";
	            	pos++;
	            	} break;
	            	
	            case '<': {
	            	state = "AFTER_LT";
	            	pos++;
	            	} break;
	            	
	            case '>': {
	            	state = "AFTER_GT";
	            	pos++;
	            	} break;
	            	
	            case '*': {
	            	tokens.add(new Token(Kind.TIMES,startPos, 1));
	            	pos++;
	            	} break;
	            
	            default: {
	                if (Character.isDigit(ch)) {
	                	state = "IN_DIGIT";
	                	pos++;
	                	} 
	                else if (Character.isJavaIdentifierStart(ch)) {
	                     state = "IN_IDENT";
	                     pos++;
	                 } 
	                 else {
	                	 throw new IllegalCharException("illegal char " +(char)ch+" at pos "+pos);
	                 }
	              }

	            }
	        } break;
	        
	        case "IN_DIGIT": {
	        	if(Character.isDigit(ch)){
	        		pos++;
	        	}
	        	else {
	        		  isValidInteger(startPos, pos);
	        		  tokens.add(new Token(Kind.INT_LIT, startPos, pos - startPos));
	                  state = "START";
	        	}
	        } break;
	        
	        case "IN_IDENT": {
	        	if (Character.isJavaIdentifierPart(ch)) {
	                pos++;
	          } else {
	        	  	  Kind enumVal= getEnum(this.chars.substring(startPos, pos));
	        	  	  if(enumVal != null) 
	        	  		  tokens.add(new Token(enumVal, startPos, pos - startPos));
	        	  	  else 
	        	  		  tokens.add(new Token(Kind.IDENT, startPos, pos - startPos));
	                  state = "START";
	          }
	        } break;
	        
	        case "AFTER_OR": {
	        	if(ch == '-' && (pos+1) < this.chars.length() && this.chars.charAt(pos+1) == '>'){
	        		tokens.add(new Token(Kind.BARARROW, startPos, 3));
	        		pos = pos+2;
	        		state = "START";
	        	}
	        	else{
	        		tokens.add(new Token(Kind.OR, startPos, 1));
	                  state = "START";
	        	}
	        } break;
	        
	        case "AFTER_MINUS": {
	        	if(ch == '>'){
	        		tokens.add(new Token(Kind.ARROW, startPos, 2));
	        		pos++;
	        		state = "START";
	        	}
	        	else{
	        		tokens.add(new Token(Kind.MINUS, startPos, 1));
	                  state = "START";
	        	}
	        } break;
	        
	        case "AFTER_DIV": {
	        	if(ch == '*'){
	        		state= "COMMENT";
            		pos++;
	        	}
	        	else{
	        		tokens.add(new Token(Kind.DIV, startPos, 1));
	                  state = "START";
	        	}
	        } break;
	        
	        case "AFTER_EQ": {
            	if(ch == '='){
            		tokens.add(new Token(Kind.EQUAL, startPos, 2));
            		pos++;
            		state = "START";
            	}
            	else{
            		throw new IllegalCharException("illegal char " +ch+" at pos "+pos);
            	}
	        } break;
	        
	        case "AFTER_NT": {
	        	if(ch == '='){
	        		tokens.add(new Token(Kind.NOTEQUAL, startPos, 2));
	        		pos++;
	        		state = "START";
	        	}
	        	else{
	        		tokens.add(new Token(Kind.NOT, startPos, 1));
	                  state = "START";
	        	}
	        } break;
	        
	        case "AFTER_LT": {
	        	if(ch == '='){
	        		tokens.add(new Token(Kind.LE, startPos, 2));
	        		pos++;
	        		state = "START";
	        	}
	        	else if(ch == '-'){
	        		tokens.add(new Token(Kind.ASSIGN, startPos, 2));
	        		pos++;
	        		state = "START";
	        	}
	        	else{
	        		tokens.add(new Token(Kind.LT, startPos, 1));
	                  state = "START";
	        	}
	        } break;
	        
	        case "AFTER_GT": {
	        	if(ch == '='){
	        		tokens.add(new Token(Kind.GE, startPos, 2));
	        		pos++;
	        		state = "START";
	        	}
	        	else{
	        		tokens.add(new Token(Kind.GT, startPos, 1));
	                  state = "START";
	        	}
	        } break; 
	        
	        case "COMMENT": {
	        if(ch == '*' && (pos+1) < this.chars.length() && this.chars.charAt(pos+1) == '/'){
            		pos += 2;
            		state="START";
            	}
	        	else{
		        	if(ch == '\n')
		        	{
		        		eol.add(pos+1);
		        	}
		        	pos++;	
	        	}

	        } break;
	        default:  assert false;
	        }
	    }
		tokens.add(new Token(Kind.EOF,pos,0));
		return this;  
	}



	private Kind getEnum(String text) {
		// TODO Auto-generated method stub
		for(Kind p: Kind.values()) {
		    if(p.getText().equals(text)) {
		      return p;
		    }
		}
		return null;
	}



	private void isValidInteger(int startPos, int pos) throws IllegalNumberException{
		// TODO Auto-generated method stub
		try{
			Integer.parseInt(this.chars.substring(startPos, pos));
		}
		catch(NumberFormatException e){
			throw new IllegalNumberException("Not a valid Integer at"+ pos);
		}
		
	}



	private int skipWhiteSpace(int pos) {
		// TODO Auto-generated method stub
		int size = this.chars.length();
		int i;
		for(i = pos; i < size ; i++){
			if(this.chars.charAt(i) =='\n') 
				eol.add(i+1);
			if(Character.isWhitespace(this.chars.charAt(i))) 
				continue;
			else 
				return i;
		}
		return i;
	}

	final ArrayList<Token> tokens;
	final ArrayList<Integer> eol;
	final String chars;
	int tokenNum;
	//boolean checkValid = false;

	/*
	 * Return the next token in the token list and update the state so that
	 * the next call will return the Token..  
	 */
	public Token nextToken() {
		if (tokenNum >= tokens.size())
			return null;
		return tokens.get(tokenNum++);
	}
	
	/*
	 * Return the next token in the token list without updating the state.
	 * (So the following call to next will return the same token.)
	 */
	public Token peek(){
		if (tokenNum >= tokens.size())
			return null;
		return tokens.get(tokenNum);		
	}

	

	/**
	 * Returns a LinePos object containing the line and position in line of the 
	 * given token.  
	 * 
	 * Line numbers start counting at 0
	 * 
	 * @param t
	 * @return
	 */
	public LinePos getLinePos(Token t) {
		//TODO IMPLEMENT THIS
		return t.getLinePos();
	}


}
