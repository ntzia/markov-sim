#!/usr/bin/env python

import sys, os
import itertools, operator
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
import numpy as np

ks = [1,2,3,4,5,6,7,8,9]
ls = [1,2,3]

x = []
l1 = []
l2 = []
l3 = []

for k in ks:
	x.append(k)

	plt.clf()

	for l in ls:
		fp = open("../outputs/Qsim-"+str(l)+"-"+str(k)+".out")
	
		line = fp.readline()
		while line:
			tokens = line.split()
			if line.startswith("Throughput of server a"):
				ga = tokens[4]
			elif line.startswith("Throughput of server b"):
				gb = tokens[4]
			line = fp.readline()
		
		if gb=="0.000":
			ratio = np.nan
		else:
			ratio = float(ga)/float(gb)
		

		if l==1:
			l1.append(ratio)
		elif l==2:
			l2.append(ratio)
		else:
			l3.append(ratio)


plt.plot(x,l1,label="lambda = 1 clients/sec", color="blue",marker='*', markersize=5)
plt.plot(x,l2,label="lambda = 2 clients/sec", color="green",marker='*', markersize=5)
plt.plot(x,l3,label="lambda = 3 clients/sec", color="red",marker='*', markersize=5)

	
plt.ylabel('$Throughput(a) / Throughput(b)$', fontsize=15)
plt.xlabel('$Threshold(k)$', fontsize=15)

plt.legend(bbox_to_anchor=(1, 1.10), loc=4, ncol=1, fancybox=True, shadow=True)
plt.title("Throughput Ratio for Different k", fontsize=20)

plt.savefig("../plots/ThroughputRatio.pdf", format="pdf", bbox_inches="tight")
