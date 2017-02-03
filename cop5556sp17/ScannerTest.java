package cop5556sp17;

import static cop5556sp17.Scanner.Kind.AND;
import static cop5556sp17.Scanner.Kind.ARROW;
import static cop5556sp17.Scanner.Kind.BARARROW;
import static cop5556sp17.Scanner.Kind.COMMA;
import static cop5556sp17.Scanner.Kind.DIV;
import static cop5556sp17.Scanner.Kind.EQUAL;
import static cop5556sp17.Scanner.Kind.GE;
import static cop5556sp17.Scanner.Kind.GT;
import static cop5556sp17.Scanner.Kind.IDENT;
import static cop5556sp17.Scanner.Kind.INT_LIT;
import static cop5556sp17.Scanner.Kind.KW_BOOLEAN;
import static cop5556sp17.Scanner.Kind.KW_FRAME;
import static cop5556sp17.Scanner.Kind.KW_HIDE;
import static cop5556sp17.Scanner.Kind.KW_IF;
import static cop5556sp17.Scanner.Kind.KW_IMAGE;
import static cop5556sp17.Scanner.Kind.KW_INTEGER;
import static cop5556sp17.Scanner.Kind.KW_MOVE;
import static cop5556sp17.Scanner.Kind.KW_SHOW;
import static cop5556sp17.Scanner.Kind.KW_WHILE;
import static cop5556sp17.Scanner.Kind.LBRACE;
import static cop5556sp17.Scanner.Kind.LE;
import static cop5556sp17.Scanner.Kind.LPAREN;
import static cop5556sp17.Scanner.Kind.LT;
import static cop5556sp17.Scanner.Kind.MINUS;
import static cop5556sp17.Scanner.Kind.MOD;
import static cop5556sp17.Scanner.Kind.NOT;
import static cop5556sp17.Scanner.Kind.NOTEQUAL;
import static cop5556sp17.Scanner.Kind.OR;
import static cop5556sp17.Scanner.Kind.PLUS;
import static cop5556sp17.Scanner.Kind.RBRACE;
import static cop5556sp17.Scanner.Kind.RPAREN;
import static cop5556sp17.Scanner.Kind.SEMI;
import static cop5556sp17.Scanner.Kind.TIMES;
import static cop5556sp17.Scanner.Kind.ASSIGN;
import static cop5556sp17.Scanner.Kind.KW_FILE;
import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


import cop5556sp17.Scanner.IllegalCharException;
import cop5556sp17.Scanner.IllegalNumberException;
import cop5556sp17.Scanner.LinePos;
import cop5556sp17.Scanner.Token;

public class ScannerTest {

	@Rule
    public ExpectedException thrown = ExpectedException.none();

	Token checkNext(Scanner scanner, Scanner.Kind kind, int pos, String text) {
		Scanner.Token token = scanner.nextToken();
		assertEquals(kind, token.kind);
		assertEquals(pos, token.pos);
		assertEquals(text.length(), token.length);
		assertEquals(text, token.getText());
		return token;
	}

	Token checkNext(Scanner scanner, Scanner.Kind kind, int pos) {
		Scanner.Token token = scanner.nextToken();
		assertEquals(kind, token.kind);
		assertEquals(pos, token.pos);
		String text = kind.getText();
		assertEquals(text.length(), token.length);
		assertEquals(text, token.getText());
		return token;
	}
	
	Token checkNext(Scanner scanner, Scanner.Kind kind) {
		Scanner.Token token = scanner.nextToken();
		assertEquals(kind, token.kind);
		return token;
	}

	Token getAndCheckEnd(Scanner scanner) {
		Scanner.Token token = scanner.nextToken();
		assertEquals(Scanner.Kind.EOF,token.kind);
		return token;
	}
	
	void checkPos(Scanner scanner, Token t, int line, int posInLine){
		LinePos p = scanner.getLinePos(t);
		assertEquals(line,p.line);
		assertEquals(posInLine, p.posInLine);
	}
	
	@Test
	public void testEmpty() throws IllegalCharException, IllegalNumberException {
		String input = "";
		Scanner scanner = new Scanner(input);
		scanner.scan();
	}

	@Test
	public void test1() throws IllegalCharException, IllegalNumberException {
		String input = ">=}!={+);(!<,";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		checkNext(scanner, GE, 0);
		checkNext(scanner, RBRACE, 2);
		checkNext(scanner, NOTEQUAL, 3);
		checkNext(scanner, LBRACE, 5);
		checkNext(scanner, PLUS, 6);
		checkNext(scanner, RPAREN, 7);
		checkNext(scanner, SEMI, 8);
		checkNext(scanner, LPAREN, 9);
		checkNext(scanner, NOT, 10);
		checkNext(scanner, LT, 11);
		checkNext(scanner, COMMA, 12);
		getAndCheckEnd(scanner);
	}

	@Test
	public void test2() throws IllegalCharException, IllegalNumberException {
		String input = "|->-->->-";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		checkNext(scanner, BARARROW, 0);
		checkNext(scanner, MINUS, 3);
		checkNext(scanner, ARROW, 4);
		checkNext(scanner, ARROW, 6);
		checkNext(scanner, MINUS, 8);
		getAndCheckEnd(scanner);
	}

	@Test
	public void test3() throws IllegalCharException, IllegalNumberException {
		String input = "$_c-d,india";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		checkNext(scanner, IDENT, 0, "$_c");
		checkNext(scanner, MINUS, 3);
		checkNext(scanner, IDENT, 4, "d");
		checkNext(scanner, COMMA, 5);
		checkNext(scanner, IDENT, 6, "india");
		getAndCheckEnd(scanner);
	}

	@Test
	public void test4() throws IllegalCharException, IllegalNumberException {
		String input = "   !";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		checkNext(scanner, NOT, 3);
		getAndCheckEnd(scanner);
	}
		
	@Test
	public void test5() throws IllegalCharException, IllegalNumberException{
		String input = "/* * ** */\npqstu&%";
		Scanner scanner = new Scanner(input);
		scanner.scan();		
		checkNext(scanner, IDENT,11,"pqstu");
		checkNext(scanner, AND, 16);
		checkNext(scanner, MOD, 17);
		getAndCheckEnd(scanner);			
	}
	
	@Test
	public void test6() throws IllegalCharException, IllegalNumberException {
		String input = "\n\n\n ,\n;\n\n *";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Token t0 = checkNext(scanner,COMMA,4);
		checkPos(scanner,t0,3,1);
		Token t1 = checkNext(scanner,SEMI,6);
		checkPos(scanner,t1,4,0);
		Token t2 = checkNext(scanner,TIMES,10);
		checkPos(scanner,t2,6,1);
	}
	
	@Test
	public void test7() throws IllegalCharException, IllegalNumberException{
		String input = "hide\r\n show \n move ";
		Scanner scanner = new Scanner(input);
		scanner.scan();		
		Token t0 = checkNext(scanner,KW_HIDE,0);
		checkPos(scanner,t0,0,0);
		Token t1 = checkNext(scanner,KW_SHOW,7);
		checkPos(scanner,t1,1,1);
		Token t2 = checkNext(scanner,KW_MOVE,14);
		checkPos(scanner,t2,2,1);		
	}
		
	@Test
	public void test8() throws IllegalCharException, IllegalNumberException{
		thrown.expect(IllegalCharException.class);	
		String input="{/===120a|/<=>01b9999901\nframe\nhide\nr|okay=|while|eating0\n*/";
		Scanner scanner = new Scanner(input);
		scanner.scan();		
	}
	
	@Test
	public void test9() throws IllegalCharException, IllegalNumberException {
	String input = "{a++b++c++++!-|";
	Scanner scanner = new Scanner(input);
	scanner.scan();
	checkNext(scanner, LBRACE, 0);
	checkNext(scanner, IDENT, 1 , "a");
	checkNext(scanner, PLUS, 2);
	checkNext(scanner, PLUS, 3);
	checkNext(scanner, IDENT, 4 , "b");
	checkNext(scanner, PLUS, 5);
	checkNext(scanner, PLUS, 6);
	checkNext(scanner, IDENT, 7 , "c");
	checkNext(scanner, PLUS, 8);
	checkNext(scanner, PLUS, 9);
	checkNext(scanner, PLUS, 10);
	checkNext(scanner, PLUS, 11);
	checkNext(scanner, NOT, 12);
	checkNext(scanner, MINUS, 13);
	checkNext(scanner, OR, 14);
	getAndCheckEnd(scanner);
	}
	
	@Test
	public void test10() throws IllegalCharException, IllegalNumberException {
		String input = "{/*\n\n\n\n*/}";
		Scanner scanner = new Scanner(input);
		scanner.scan();	
		Token t0 = checkNext(scanner,LBRACE,0);
		checkPos(scanner,t0,0,0);
		Token t1 = checkNext(scanner,RBRACE,9);
		checkPos(scanner,t1,4,2);
	}
	
	@Test
	public void test11() throws IllegalCharException, IllegalNumberException {
		String input = "{\n-|=->\n\n\nA$&#\n";
		Scanner scanner = new Scanner(input);
		thrown.expect(IllegalCharException.class);
		scanner.scan();
	}
	
	@Test
	public void testIntOverflowError() throws IllegalCharException, IllegalNumberException{
		String input = "7777997799988895559966";
		Scanner scanner = new Scanner(input);
		thrown.expect(IllegalNumberException.class);
		scanner.scan();		
	}
	
	@Test
	public void test12() throws IllegalCharException, IllegalNumberException{
		String input="/*\n";
		Scanner scanner = new Scanner(input);
		scanner.scan();		
		getAndCheckEnd(scanner);	
	}
	
}
