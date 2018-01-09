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
	ya = []
	yb = []

	plt.clf()

	for k in ks:
		fp = open("../outputs/Qsim-"+str(l)+"-"+str(k)+".out")
	
		line = fp.readline()
		while line:
			tokens = line.split()
			if line.startswith("Throughput of server a"):
				ga = tokens[4]
			elif line.startswith("Throughput of server b"):
				gb = tokens[4]
			line = fp.readline()


		x.append(k)
		ya.append(ga)
		yb.append(gb)


	fig = plt.figure()
	ax = fig.add_axes([0,0,1,1])

	plt.plot(x,ya,label="Server a", color="darkred",marker='*', markersize=5)
	plt.plot(x,yb,label="Sever b", color="yellowgreen",marker='*', markersize=5)

	textstr = '$lambda = %d  clients/sec$'%(l)
	props = dict(boxstyle='square', facecolor='black', alpha=0.5)
	plt.text(0.00, 1.15, textstr, transform=ax.transAxes, fontsize=17,
        verticalalignment='top', bbox=props)

	
	plt.ylabel('$Throughput (clients/sec)$', fontsize=15)
	plt.xlabel('$Threshold(k)$', fontsize=15)

	plt.legend(bbox_to_anchor=(1, 1.10), loc=4, ncol=1, fancybox=True, shadow=True)
	plt.title("Throughput for Different k", fontsize=20)


	plt.savefig("../plots/Throughput-l=" + str(l) + ".pdf", format="pdf", bbox_inches="tight")