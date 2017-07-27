#section divided by the next buffer to get section start and end index
#debracket

import datetime
import re
import string
import sys
import os

def dequote(s):
	if s[0] == "(":
		return s[1:-1]
	return s

noh=open("C:/Users/1021376/Documents/Kaam/Scripts/LpOpt_Debug/FINAL_USER/NOH/NOH_1.txt",'r')
x=[]
noh_csv=open("C:/Users/1021376/Documents/Kaam/Scripts/LpOpt_Debug/FINAL_USER/NOH/NOH_CSV.csv",'a')
lines=noh.readlines()

print len(lines)

for a in range(0,len(lines),1):
	if "Buffer" in lines[a]:
		x.append(a)

print x[0]

#filter each section
noh_csv.write("Buffer_Name,Type,Date,Bucket,BOH,EOH,Operation_ID,Operation_Name,")
noh_csv.write('\n')


for l in range(0,len(x),1):
	start=x[l]
	print l,len(x)
	if (l+1)!=len(x):
		end=x[l+1]-1
		print start,end
	else:
		end=len(lines)
#start=0
#end=5

#if "Buffer" in lines[b]:
	y=lines[start].split("'")[1]
#noh_csv.write(y + ',')

	for b in range(start+1,end-1,1):
		z=lines[b].split()
		noh_csv.write(y + ',')

		for i in range(0,len(z),1):
			r=dequote(z[i])
			noh_csv.write(r +',')
		noh_csv.write('\n')
