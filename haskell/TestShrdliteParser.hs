
module Main where 

import ShrdliteGrammar
import CombinatorParser
import System.IO

main :: IO ()
main = do putStrLn "Write a sentence to parse; empty line or EOF to quit."
          mainloop
          putStrLn "Goodbye!"

mainloop :: IO ()
mainloop = do putStr "\n> "
              do sent <- fmap words getLine
                 if sent == [] then 
                     return ()
                 else
                     do putStrLn $ "Parsing: " ++ show sent
                        let trees = parse command sent
                        if trees == [] then
                            putStrLn $ "No solutions"
                        else
                            do putStrLn $ show (length trees) ++ " solutions:"
                               mapM_ putStrLn ["--> " ++ show t | t <- trees]
                        mainloop

