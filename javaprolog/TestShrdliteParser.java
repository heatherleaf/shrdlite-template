
import java.util.Arrays;
import java.util.List;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

import gnu.prolog.term.Term;
import gnu.prolog.term.CompoundTerm;
import gnu.prolog.vm.PrologException;

public class TestShrdliteParser {

	public static void main(String[] args) throws PrologException, IOException {
        System.out.println("Write a sentence to parse; empty line or EOF to quit.");

        DCGParser parser = new DCGParser("shrdlite_grammar.pl");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line;
        System.out.print("\n> ");
        while ((line = in.readLine()) != null && line.length() != 0) {
            String[] words = line.trim().split("\\s+");
            System.out.println("Parsing: " + Arrays.toString(words));
            List<ParseTree> results = parser.parseSentence("command", words);
            if (results.isEmpty()) {
                System.out.println("No solutions");
            } else {
                System.out.println(results.size() + " solution(s):");
                for (ParseTree t : results) {
                    System.out.println("NODE: " + t.getNode());
                    System.out.println("--> " + t);
                }
            }
            System.out.print("\n> ");
        }
        System.out.println("Goodbye!");
    }

}

