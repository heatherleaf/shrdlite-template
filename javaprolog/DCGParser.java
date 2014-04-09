
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

import gnu.prolog.database.PrologTextLoaderError;
import gnu.prolog.io.TermWriter;
import gnu.prolog.term.AtomTerm;
import gnu.prolog.term.CompoundTerm;
import gnu.prolog.term.IntegerTerm;
import gnu.prolog.term.Term;
import gnu.prolog.term.VariableTerm;
import gnu.prolog.vm.Environment;
import gnu.prolog.vm.Interpreter;
import gnu.prolog.vm.Interpreter.Goal;
import gnu.prolog.vm.PrologCode;
import gnu.prolog.vm.PrologException;
import gnu.prolog.vm.TermConstants;

public class DCGParser {
    private static final String prologParserFile = "dcg_parser.pl";

	private static Environment env;
	private static Interpreter interpreter;

	public DCGParser(String prologGrammarFile) throws PrologException {
		env = new Environment();
		env.ensureLoaded(AtomTerm.get(prologParserFile));
		env.ensureLoaded(AtomTerm.get(prologGrammarFile));
		interpreter = env.createInterpreter();
		env.runInitialization(interpreter);
    }

    public List<ParseTree> parseSentence(String startCat, List sentence) throws PrologException {
        return parseSentence(AtomTerm.get(startCat), atomListTerm(sentence));
    }

    public List<ParseTree> parseSentence(String startCat, String[] sentence) throws PrologException {
        return parseSentence(AtomTerm.get(startCat), atomListTerm(sentence));
    }

    public List<ParseTree> parseSentence(Term startCat, Term sentence) throws PrologException {
        Term resultsTerm = new VariableTerm("Results");
		Term goalTerm = new CompoundTerm("parse_all", new Term[]{startCat, sentence, resultsTerm});
        Goal goal = interpreter.prepareGoal(goalTerm);
        int rc = interpreter.execute(goal);
        assert (rc == PrologCode.SUCCESS || rc == PrologCode.SUCCESS_LAST);
        Term results = resultsTerm.dereference();
        return collectResults(results);
    }

    // // Note to self: use the following skeleton to run a goal several times:
    // Goal goal = interpreter.prepareGoal(goalTerm);
    // int rc = PrologCode.SUCCESS;
    // while (rc == PrologCode.SUCCESS) {
    //     rc = interpreter.execute(goal);
    //     if (rc == PrologCode.SUCCESS || rc == PrologCode.SUCCESS_LAST) {
    //         Term result = resultTerm.dereference();
    //         DO_SOMETHING_WITH_RESULT(result);
    //     }
    // }

    public Term atomListTerm(List atoms) {
        Term listTerm = TermConstants.emptyListAtom;
        ListIterator li = atoms.listIterator(atoms.size());
        while(li.hasPrevious()) {
            listTerm = CompoundTerm.getList(AtomTerm.get((String) li.previous()), listTerm);
        }
        return listTerm;
    }

    public Term atomListTerm(String[] atoms) {
        Term listTerm = TermConstants.emptyListAtom;
        for (int i = atoms.length-1; i >= 0; i--) {
            listTerm = CompoundTerm.getList(AtomTerm.get(atoms[i]), listTerm);
        }
        return listTerm;
    }

    public List<ParseTree> collectResults(Term results) {
        List<ParseTree> resultList = new ArrayList<ParseTree>();
        while (CompoundTerm.isListPair(results)) {
            resultList.add(toParseTree(((CompoundTerm)results).args[0]));
            results = ((CompoundTerm)results).args[1];
        }
        return resultList;
    }

    public ParseTree toParseTree(Term term) {
        if (term.getTermType() == Term.COMPOUND) {
            ParseTree tree = new ParseTree(((CompoundTerm)term).tag.functor.value);
            for (Term child : ((CompoundTerm)term).args) {
                tree.addChild(toParseTree(child));
            }
            return tree;
        } else if (term.getTermType() == Term.ATOM) {
            return new ParseTree(((AtomTerm)term).value);
        } else {
            throw new IllegalArgumentException("Unexpected type of Prolog term: " + 
                                               term.getClass().toString() + 
                                               " [" + term.toString() + "]");
        }
    }

}

