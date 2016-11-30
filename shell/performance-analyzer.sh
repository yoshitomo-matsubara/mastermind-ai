#!/bin/sh

file=c${1}-p${2}.txt
round=1000

echo "# of colors = ${1}"
echo "# of positions = ${2}"
echo "# of rounds = ${round}"

if [ -f $file ]; then
	rm $file
fi

if [ $# -eq 2 ]; then
	for i in `seq ${round}`
	do
		java -classpath mastermind-ai.jar mastermind.game.GameMaster -c $1 -p $2 -m cfp | tail -1 | cut -f2,4 >>$file
	done
fi

echo 'Average # of guesses'
cat $file | cut -f1 | awk '{sum+=$1}END{print sum/NR}'
echo 'Average execution time [ns]'
cat $file | cut -f2 | awk '{sum+=$1}END{print sum/NR}'
