#!/usr/local/bin/swipl -g mainloop,halt -t 'halt(1)' -s

:- [dcg_parser].
:- [shrdlite_grammar].


mainloop :-
    nl, write('Write a sentence to parse; empty line or EOF to quit.'), nl, nl,
    repeat,
    read_line_to_codes(user_input, Codes),
    ( (Codes == end_of_file ; Codes == [])
    -> !,
       write('Goodbye!'), nl
    ;
       split_line(Codes, Words),
       write('Parsing: '), write(Words), nl,
       parse_all(command, Words, Trees),
       length(Trees, N),
       write(N), write(' solution(s)'), nl,
       forall(member(T, Trees),
              (write('--> '), write(T), nl)),
       nl,
       fail
    ).


%% split_line(+Codes : list(int), -Words : list(atom))
split_line(Codes, Words) :-
    atom_codes(Line, Codes),
    atomic_list_concat(Words0, ' ', Line),
    findall(W, (member(W, Words0), W \== ''), Words).

