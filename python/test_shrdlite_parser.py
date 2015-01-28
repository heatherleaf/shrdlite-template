
from __future__ import print_function

import nltk


def parse_to_tree(parser, sentence):
    for result in parser.parse(sentence):
        yield result.label()['sem']


def main():
    grammar = nltk.data.load("file:shrdlite_grammar.fcfg", cache=False)
    parser = nltk.FeatureChartParser(grammar)
    print("Write a sentence to parse; empty line or EOF to quit.")
    print()
    while True:
        try:
            sentence = raw_input("> ").split()
        except EOFError:
            break
        if not sentence:
            break
        print("Parsing:", sentence)
        try:
            trees = list(parse_to_tree(parser, sentence))
        except ValueError:
            trees = []
        if not trees:
            print("No solutions")
        else:
            print("%d solutions:" % (len(trees),))
            for t in trees:
                print("-->", t)
        print()
    print("Goodbye!")


if __name__ == '__main__':
    main()
