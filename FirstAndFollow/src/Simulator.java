public class Simulator 
{
	public static void main(String[] args) 
	{
		CFG cfg1 = new CFG();
		
		cfg1.addNonTerminal("S");
		cfg1.addNonTerminal("T");
		
		cfg1.addTerminal("a");
		cfg1.addTerminal("b");
		
		cfg1.addRule("S", "a S b");
		cfg1.addRule("S", "T");
		cfg1.addRule("T", "a T");
		cfg1.addRule("T", "epsilon");
		
		cfg1.printGrammar();
		System.out.println("---------------------");
		cfg1.computeFirst();
		System.out.println();
		cfg1.computeFollow();
		
		System.out.println("------------------------------------------------------------------------------------------");
		
		CFG cfg2 = new CFG();
		
		cfg2.addNonTerminal("E");
		cfg2.addNonTerminal("E'");
		cfg2.addNonTerminal("T");
		cfg2.addNonTerminal("T'");
		cfg2.addNonTerminal("F");
		
		cfg2.addTerminal("+");
		cfg2.addTerminal("*");
		cfg2.addTerminal("(");
		cfg2.addTerminal(")");
		cfg2.addTerminal("id");
		
		cfg2.addRule("E", "T E'");
		cfg2.addRule("E'", "+ T E'");
		cfg2.addRule("E'", "epsilon");
		cfg2.addRule("T", "F T'");
		cfg2.addRule("T'", "* F T'");
		cfg2.addRule("T'", "epsilon");
		cfg2.addRule("F", "( E )");
		cfg2.addRule("F", "id");
		
		cfg2.printGrammar();
		System.out.println("---------------------");
		cfg2.computeFirst();
		System.out.println();
		cfg2.computeFollow();
		
		System.out.println("------------------------------------------------------------------------------------------");
		
		CFG cfg3 = new CFG();
		
		cfg3.addNonTerminal("S");
		cfg3.addNonTerminal("T");
		
		cfg3.addTerminal("0");
		cfg3.addTerminal("1");
		
		cfg3.addRule("S", "0 T 1 S");
		cfg3.addRule("S", "epsilon");
		cfg3.addRule("T", "0 T 1");
		cfg3.addRule("T", "epsilon");
		
		cfg3.printGrammar();
		System.out.println("---------------------");
		cfg3.computeFirst();
		System.out.println();
		cfg3.computeFollow();
		
		System.out.println("------------------------------------------------------------------------------------------");

		CFG cfg4 = new CFG();
		
		cfg4.addNonTerminal("S");
		cfg4.addNonTerminal("A");
		cfg4.addNonTerminal("B");
		cfg4.addNonTerminal("C");
		
		cfg4.addTerminal("a");
		cfg4.addTerminal("b");
		cfg4.addTerminal("c");
		
		cfg4.addRule("S", "S A B");
		cfg4.addRule("S", "S B C");
		cfg4.addRule("S", "epsilon");
		cfg4.addRule("A", "a A a");
		cfg4.addRule("A", "epsilon");
		cfg4.addRule("B", "b B");
		cfg4.addRule("B", "epsilon");
		cfg4.addRule("C", "c C");
		cfg4.addRule("C", "epsilon");
		
		cfg4.printGrammar();
		System.out.println("---------------------");
		cfg4.computeFirst();
		System.out.println();
		cfg4.computeFollow();
		
		System.out.println("------------------------------------------------------------------------------------------");
		
		CFG cfg5 = new CFG();
		
		cfg5.addNonTerminal("S");
		cfg5.addNonTerminal("L");
		cfg5.addNonTerminal("LPrime");
		
		cfg5.addTerminal("(");
		cfg5.addTerminal(")");
		cfg5.addTerminal("a");
		cfg5.addTerminal(";");
		
		cfg5.addRule("S", "( L )");
		cfg5.addRule("S", "a");
		cfg5.addRule("L", "( L ) LPrime");
		cfg5.addRule("L", "a LPrime");
		cfg5.addRule("LPrime", "; S LPrime");
		cfg5.addRule("LPrime", "epsilon");
		
		cfg5.printGrammar();
		System.out.println("---------------------");
		cfg5.computeFirst();
		System.out.println();
		cfg5.computeFollow();
		
		System.out.println("------------------------------------------------------------------------------------------");

		CFG cfg6 = new CFG();
		
		cfg6.addNonTerminal("S");
		cfg6.addNonTerminal("SPrime");
		cfg6.addNonTerminal("X");
		
		cfg6.addTerminal("a");
		cfg6.addTerminal("+");
		cfg6.addTerminal("*");
		
		cfg6.addRule("S", "a SPrime");
		cfg6.addRule("SPrime", "S X SPrime");
		cfg6.addRule("SPrime", "epsilon");
		cfg6.addRule("X", "+");
		cfg6.addRule("X", "*");
		
		cfg6.printGrammar();
		System.out.println("---------------------");
		cfg6.computeFirst();
		System.out.println();
		cfg6.computeFollow();
		
		System.out.println("------------------------------------------------------------------------------------------");	
	}
}