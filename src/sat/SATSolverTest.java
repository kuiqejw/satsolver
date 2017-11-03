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
                  if (literalString.equals("")) {
                    continue;
                  }
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

    public static void solveCNF(String file) {
        System.out.println("File: " + file);
        Formula f= parse(file);
        String[] path = file.split("/");
        String filename = path[path.length -1].split("\\.")[0];

        System.out.println("SAT solve starts!!!");
        long started = System.nanoTime();
        Environment e = SATSolver.solve(f);
        long time = System.nanoTime();
        long timeTaken = time - started;
        System.out.println("Time: " + timeTaken/1000000.0 + "ms");
        if (e != null) {
            System.out.println("satisfiable");
            File txtFile = new File("BoolAssignment_"  + filename + ".txt");

            if (!txtFile.exists()){
                try (Writer writeFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(txtFile), "utf-8"))){

                    String s = e.toString().replace("Environment:[", "");
                    s = s.replace("]", "");
                    String[] lines = s.split(", ");
                    for (String line: lines) {
                        writeFile.write(line + "\r\n");
                    }
                    writeFile.close();
                    System.out.println("Wrote solution to " + "BoolAssignment_"  + filename + ".txt");
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
    	if (args.length < 1) {
    		System.out.println("File not provided");
    		System.exit(1);
    	}

        String filename = args[0];
        solveCNF(filename);

    }
}
