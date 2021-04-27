# Peter Norvig's implementation of Lutz Prechelt's phone number exercise 


See [Lisp as an Alternative to Java](https://norvig.com/java-lisp.html) for more information.

The implementation is in Common Lisp and on a Mac it will run in Steel Bank Common Lisp (SBCL). This can be installed with homebrew:


    brew install sbcl


To see it working on the provided test input, switch directory to _norvig_ and run the following:


	sbcl | tee output.txt
	(load "norvig-phone-numbers.cl")
	(main "dictionary.txt" "input.txt")
	(quit)


On the otherhand to see it "hang" on pathological input do


	sbcl
	(load "norvig-phone-numbers.cl")
	(main "dict-hard.txt" "input-hard.txt")
	