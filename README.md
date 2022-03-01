# meow
Meow is a simple JSON parsing library.
It implement a simplified version of [Parsec](https://github.com/haskell/parsec).
This parser has no error hinting, all parsing results are wrapped by Maybe.
It returns the `Just result` if the parsing succeeds and `Nothing` if it fails.
