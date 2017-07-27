import datetime
import re
import string
import sys
import os


def dequote(s):
	if s[0] == "(":
		return s[1:-1]
	return s

def requote(s):                                         #to remove quotes from string
    if (s[0] == s[-1]) and s.startswith("'"):
        s=s.replace("'","!!!")        #single has to be made double
        s=s.replace('"',"'")
        s=s.replace("!!!",'"')

        return s

    return s

def space_remove(data,temp):
    str_ind=data.find(temp)   #index of empty space in quoted part
    temp=temp.replace(' ',"&&&")
    data=data.replace(x[z],temp)
    return data

def dot_remove(data):

    data=data.replace(".",'')
    return data



#################################################################
print "RUN START: {:%Y-%m-%d %H:%M:%S}".format(datetime.datetime.now())
run_start=datetime.datetime.now()

usr_input=open(sys.argv[1], 'r')
usr_input_lines=usr_input.readlines()

for ind in range(0,len(usr_input_lines),1):
    line_syntax=usr_input_lines[ind].split('->')
    if line_syntax[0].strip()== "LP_out_files_path":

        lp_out_path=line_syntax[1].strip()

    elif line_syntax[0].strip()=="Output_csv_path":
        csv_path=line_syntax[1].strip()


print "                             USER INPUT"
print "LP output files path-->" + lp_out_path
print "Path where the CSV's will be created-->" + csv_path

if not os.path.exists(csv_path):        #to create or replace output csv folder
    os.makedirs(csv_path)

lp_out_folder=lp_out_path + '/'
csv_folder=csv_path + '/'

print "                             OUTPUT"
fl=os.listdir(lp_out_folder)

for fl_ind in range(0,len(fl),1):
    name=lp_out_folder + fl[fl_ind]
    out_name=csv_folder + fl[fl_ind] + ".csv"

    fl_name=open(name,'r')
    csv_name=open(out_name,'w')

    lines=fl_name.readlines()         #readlines in file


    line_num=len(lines)            #number of lines
    print str(line_num) + " lines in " + fl[fl_ind]

################################################    for NOH ##############
    if (fl[fl_ind].strip() == "NOH.log") or (fl[fl_ind].strip() == "noh.log"):
        x=[]
        print "--------NOH is here -----------------"
        for a in range(0,len(lines),1):
        	if "Buffer" in lines[a]:
        		x.append(a)

        print x[0]

        #filter each section
        csv_name.write("Buffer_Name,Type,Date,Bucket,BOH,EOH,Operation_ID,Operation_Name,")
        csv_name.write('\n')


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
        		csv_name.write(y + ',')

        		for i in range(0,len(z),1):
        			r=dequote(z[i])
        			csv_name.write(r +',')
        		csv_name.write('\n')

                print "------------NOH DONE------------"
#################################################################
    else:
        headers_com=lines[0]    #commented header
        headers_all=headers_com[1:]
        header_split=headers_all.split()

        for h in range(0,len(header_split),1):
            if header_split[h]== "date":
                print "date found"
                header_split[h]=header_split[h].replace('date','date,time')
            csv_name.write(header_split[h]+ ',')

        csv_name.write('\n')

        for j in range(1,line_num,1):
            data=lines[j]
            if data[0] != "#":
                ln_split_check= data.split()
        ##########  including check for space within quotes######
        #print len(ln_split)
                count=0
                for k in range(0,len(ln_split_check),1):

                    if (ln_split_check[k].startswith("'") and not ln_split_check[k].endswith("'") ) :
                        count+=1
                        x=re.findall(r"'([^']*)'", data)  #find all quoted entries
                        if x is not None:
                            for z in range(0,len(x),1):
                                if ' ' in x[z]:                 #to find empty space
                                    data=space_remove(data,x[z])

                                ln_split=data.split()
                            #print ln_split

                    else:
                        continue

                if count==0:
                    ln_split=ln_split_check
                else:
                    for yo in range(0,len(ln_split),1):
                        if "&&&" in ln_split[yo]:
                            ln_split[yo]=ln_split[yo].replace("&&&",' ')  #replace &&& to space after parsing
                        #break
                ##########################################################
                ################    take equal to into consideration    ########


                for prs in ln_split:


                    prs_final=requote(prs)
                    if prs_final=='.':
                        prs_final=dot_remove(prs_final)
                    csv_name.write(prs_final + ',')
                #name.write(prs_final + ',')
                csv_name.write('\n')

                #if ';' in line:
    csv_name.close()
    fl_name.close()


print "RUN END: {:%Y-%m-%d %H:%M:%S}".format(datetime.datetime.now())
run_end=datetime.datetime.now()
print run_end-run_start
