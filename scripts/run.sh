#!/bin/bash

cd ../classes

for lambda in 1 2 3; do
	for threshold in 1 2 3 4 5 6 7 8 9; do
		java Simulation $lambda 4 1 $threshold > ../outputs/Qsim-${lambda}-${threshold}.out
	done
done