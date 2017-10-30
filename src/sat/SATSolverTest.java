package sat;

/*import static org.junit.Assert.*;

import org.junit.Test;
*/

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import sat.env.Bool;
import sat.env.Environment;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.NegLiteral;
import sat.formula.PosLiteral;


public class SATSolverTest {
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();



	
	// TODO: add the main method that reads the .cnf file and calls SATSolver.solve to determine the satisfiability
    
	
    public void testSATSolver1(){
    	// (a v b)
    	Environment e = SATSolver.solve(makeFm(makeCl(a,b))	);
    	System.out.println(e);
    	/*
    	assertTrue( "one of the literals should be set to true",
    			Bool.TRUE == e.get(a.getVariable())  
    			|| Bool.TRUE == e.get(b.getVariable())	);
    	 */	
    }
    
    
    public void testSATSolver2(){
    	// (~a)
    	Environment e = SATSolver.solve(makeFm(makeCl(na)));
    	System.out.println(e);
    	/*
    	assertEquals( Bool.FALSE, e.get(na.getVariable()));
    	*/
    }
    
    private static Formula makeFm(Clause... e) {
        Formula f = new Formula();
        for (Clause c : e) {
            f = f.addClause(c);
        }
        return f;
    }
    
    private static Clause makeCl(Literal... e) {
        Clause c = new Clause();
        for (Literal l : e) {
            c = c.add(l);
        }
        return c;
    }
    
    private static Formula parse(String filename) {
    	try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line;
			Clause clause = new Clause();
			Formula formula = new Formula();
			while ((line = br.readLine()) != null) {
				if (line.equals("") || line.charAt(0) == 'c' || line.charAt(0) == 'p') {
					continue;
				}
				String[] tokens = line.split("\\s+");
				for (String token : tokens) {
					if (token.equals("0")) {
						formula = formula.addClause(clause);
						clause = new Clause();
					} else {
						if (token.charAt(0) == '-') {
							clause = clause.add(NegLiteral.make(token.substring(1)));
						} else {
							clause = clause.add(PosLiteral.make(token));
						}
					}
				}
			}
			return formula;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    	
    }
    
    public static void main(String[] args) {
    	Formula fom = parse("./src/Project-2D/project-2d-starting/sampleCNF/largeSat.cnf");
    	System.out.println("started");
    	long started = System.nanoTime();
    	Environment solution = SATSolver.solve(fom);
    	long end = System.nanoTime();
    	System.out.println("ended");
    	System.out.println((end - started)/1000000 + "ms");
    	System.out.println(solution);
    	// new SATSolverTest().testSATSolver2();
    }
    
    
    
}