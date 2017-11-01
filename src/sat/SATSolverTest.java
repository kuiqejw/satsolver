package sat;

/*
 import static org.junit.Assert.*;
 
 import org.junit.Test;
 */

import sat.env.*;
import sat.formula.*;

import java.io.*;


public class SATSolverTest {
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();

    // TODO: add the main method that reads the .cnf file and calls SATSolver.solve to determine the satisfiability

    public void testSATSolver1() {
        // (a v b)
        Environment e = SATSolver.solve(makeFm(makeCl(a, b)));
        /*
         assertTrue( "one of the literals should be set to true",
         Bool.TRUE == e.get(a.getVariable())
         || Bool.TRUE == e.get(b.getVariable())    );
         
         */
    }


    public void testSATSolver2() {
        // (~a)
        Environment e = SATSolver.solve(makeFm(makeCl(na)));
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

    public static Formula parse(String filename) {
        try {
            BufferedReader br1 = new BufferedReader(new FileReader(filename));
            String line1;
            Clause clause = new Clause();
            Formula formula = new Formula();

            while ((line1 = br1.readLine()) != null) {
                if (line1.equals("") || line1.charAt(0) == 'c' || line1.charAt(0) == 'p') {
                    continue;
                }
                String[] literalStrings = line1.split("\\s+");
                for (String literalString : literalStrings) {
                    if (!literalString.equals("0")) {
                        Literal literal;
                        if (literalString.charAt(0) == '-') {
                            literal = NegLiteral.make(literalString.substring(1));
                        } else {
                            literal = PosLiteral.make(literalString);
                        }
                        clause = clause.add(literal);
                    } else {
                        formula = formula.addClause(clause);
                        clause = new Clause();
                    }
                }
            }
            return formula;
        } catch (FileNotFoundException ex) {
            System.out.println("File not found.");
            return null;
        } catch (java.io.IOException ex) {
            System.out.println("Error reading file.");
            return null;
        }
    }

    public static void printOutput(String file) {
        System.out.println("File: " + file);
        Formula f= parse(file);

        System.out.println("SAT solve starts!!!");
        long started = System.nanoTime();
        Environment e = SATSolver.solve(f);
        long time = System.nanoTime();
        long timeTaken = time - started;
        System.out.println("Time: " + timeTaken/1000000.0 + "ms");
        if (e != null) {
            System.out.println("satisfiable");
            File txtFile = new File("BoolAssignment" + ".txt");
            int counter = 1;
            while (txtFile.exists()){
                counter++;
                txtFile = new File("BoolAssignment" + (counter - 1) + ".txt");
            }
            if (!txtFile.exists()){
                try (Writer writeFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(txtFile), "utf-8"))){

                    String s = e.toString().replace("Environment:[", "");
                    s = s.replace("]", "");
                    String[] lines = s.split(", ");
                    for (String line: lines) {
                        writeFile.write(line + "\r\n");
                    }
                    writeFile.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        }
        else {
            System.out.println("unsatisfiable");
        }
    }

    public static void main(String[] args) {

        String largeSat = "./src/Project-2D/project-2d-starting/sampleCNF/largeSat.cnf";
        String largeUnsat = "./src/Project-2D/project-2d-starting/sampleCNF/largeUnsat.cnf";
        String s8Sat = "./src/Project-2D/project-2d-starting/sampleCNF/s8Sat.cnf";

        printOutput(largeSat);
        printOutput(largeUnsat);
        printOutput(s8Sat);
    }
}
