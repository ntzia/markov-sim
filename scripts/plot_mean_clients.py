#!/usr/bin/env python

import sys, os
import itertools, operator
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
import numpy as np

ks = [1,2,3,4,5,6,7,8,9]
ls = [1,2,3]


for l in ls:
	x = []
	y = []

	plt.clf()

	for k in ks:
		fp = open("../outputs/Qsim-"+str(l)+"-"+str(k)+".out")
	
		line = fp.readline()
		while line:
			tokens = line.split()
			if line.startswith("Final"):
				mean = tokens[5]
			line = fp.readline()


		x.append(k)
		y.append(mean)


	fig = plt.figure()
	ax = fig.add_axes([0,0,1,1])

	plt.plot(x,y,color="darkblue",marker='*', markersize=5)
	
	plt.ylabel('$E[N]$', fontsize=15)
	plt.xlabel('$Threshold(k)$', fontsize=15)

	textstr = '$lambda = %d  clients/sec$'%(l)
	props = dict(boxstyle='square', facecolor='black', alpha=0.5)
	plt.text(0.05, 0.95, textstr, transform=ax.transAxes, fontsize=17,
        verticalalignment='top', bbox=props)

	plt.title(" Final Mean Number Of Clients for Different k ", fontsize=20)


	plt.savefig("../plots/Clients-l=" + str(l) + ".pdf", format="pdf", bbox_inches="tight")

