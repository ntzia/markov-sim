#!/usr/bin/env python

import sys, os
import itertools, operator
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
import numpy as np

ks = [1,2,3,4,5,6,7,8,9]
ls = [1,2,3]


for k in ks:
	x = []
	y1 = []
	y2 = []
	y3 = []

	plt.clf()

	for l in ls:
		fileno = 3*(k-1) + l

		fp = open("../outputs/Qsim-"+str(l)+"-"+str(k)+".out")
	
		line = fp.readline()
		while line:
			tokens = line.split()
			if line.startswith("Groups"):
				groups = tokens[4]
			line = fp.readline()

		fp.seek(0,0)
		line = fp.readline()
		line = fp.readline()
		line = fp.readline()
		line = fp.readline()
		line = fp.readline()
		line = fp.readline()
		line = fp.readline()
		line = fp.readline()
		line = fp.readline()
		
		for i in range(1,int(groups)):
			line = fp.readline()
			tokens = line.split()
			if l==1:
				y1.append(tokens[1])
			elif l==2:
				y2.append(tokens[1])
			else:
				y3.append(tokens[1])
				
	len1 = len(y1)
	len2 = len(y2)
	len3 = len(y3)
	maxlen = max(len1,len2,len3)

	for i in range(1,maxlen+1):
		x.append(i*1000)

	for i in range(len1+1,maxlen+1):
		y1.append(np.nan)
	for i in range(len2+1,maxlen+1):
		y2.append(np.nan)
	for i in range(len3+1,maxlen+1):
		y3.append(np.nan)

	plt.plot(x,y1,label="lambda = 1 clients/sec", color="blue",marker='*', markersize=3)
	plt.plot(x,y2,label="lambda = 2 clients/sec", color="green",marker='*', markersize=3)
	plt.plot(x,y3,label="lambda = 3 clients/sec", color="red",marker='*', markersize=3)
	
	plt.ylabel('$Mean Number Of Clients$', fontsize=15)
	plt.xlabel('$Number Of Events$', fontsize=15)

	plt.legend(bbox_to_anchor=(1, 1.10), loc=4, ncol=1, fancybox=True, shadow=True)
	plt.title(" System Convergence for k = "+ str(k), fontsize=25)


	plt.tight_layout()
	plt.savefig("../plots/Conv-K=" + str(k) + ".pdf", format="pdf", bbox_inches="tight")

