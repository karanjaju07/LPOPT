import datetime
import re
import string
import sys
import os

def requote(s):                                         #to remove quotes from string
    if (s[0] == s[-1]) and s.startswith("'"):
        s=s.replace("'","!!!")        #single has to be made double
        s=s.replace('"',"'")
        s=s.replace("!!!",'"')

        return s

    return s

def search_string(name,line):                            #to search for set and header
    if name== "sec":
        if (line[0:len("set")]=="set") and ("HOLIDAY" not in line):                           # find set(to getcsv name)
            x= line[len("set")+1:line.rfind(":")]
            return x

        elif line[0:len("param:")]=="param:" and ("HOLIDAY" not in line):
            y= line[len("param:")+1:line.find(":",7)]
            return y



        elif line[0:len("param ")]=="param " and ("HOLIDAY" not in line):
            z= line[len("param "):line.find(":",7)]
            return z

        elif "HOLIDAY" in line and  ((line[0:len("set")]=="set") or (line[0:len("param ")]=="param ")) :
            holi=line.split()
            #print holi
            return holi[1]

        else:
            return "0"

    elif line[1:len(name)+1]==name:                       #find HEADER(to find column names)
        return line[len(name)+2:]

    else:
        return "0"

def space_remove(data,temp):
    str_ind=data.find(temp)   #index of empty space in quoted part
    temp=temp.replace(' ',"&&&")
    data=data.replace(x[z],temp)
    return data

def dot_remove(data):

    data=data.replace(".",'')
    return data

############################################

print "RUN START: {:%Y-%m-%d %H:%M:%S}".format(datetime.datetime.now())
run_start=datetime.datetime.now()

usr_input=open(sys.argv[1], 'r')
usr_input_lines=usr_input.readlines()

for ind in range(0,len(usr_input_lines),1):
    line_syntax=usr_input_lines[ind].split('->')
    if line_syntax[0].strip()== "lp_input":

        lp_path=line_syntax[1].strip()

    elif line_syntax[0].strip()=="csv_out":
        csv_path=line_syntax[1].strip()

    elif line_syntax[0].strip()=="tables_req":
        tables_input=line_syntax[1].strip()

print "                             USER INPUT"
print "LP_input.dat path-->" + lp_path
print "Path where the CSV's will be created-->" + csv_path
print "Tables required to be update-->" +tables_input

if not os.path.exists(csv_path):
    os.makedirs(csv_path)
######### MAIN CODE##################
lp=open(lp_path,'r')
lines=lp.readlines()         #readlines in file


tables_list=tables_input.split(",")
print tables_list

line_num=len(lines)            #number of lines
print "Number of lines in LP_input-->" + str(line_num)
for line in lines:
    section_find=search_string("sec",line)    #find section

    if section_find!="0":
        section_start=lines.index(line)      #get starting index of section
        section_name=section_find.strip()                      #get name of each section
        if (section_name.lower() in tables_list) or (section_name.upper() in tables_list) or (tables_list[0]== "ALL"):
            print section_name

            for x in range(section_start,line_num,1):
                if ";" in lines[x]:                 #to find end of section
                    section_end=x                   #section end index
                    break

            for i in range(section_start + 1,section_end,1):        #line after set till ;
                set_name=csv_path + '/' + section_name + ".csv"

                name= open(set_name,'a')                   #create and open csv file

                headers=search_string("HEADER",lines[i])
                if headers!="0":
                    if "(" in headers:
                        h1=headers.index("(")
                        h2=headers.index(")")
                        h_rem=headers[h1:h2+1]
                        headers=headers.replace(h_rem,'')
                    header_list=headers.split()
                    for h in range(0,len(header_list),1):
                        if header_list[h] != ",":
                            name.write(header_list[h]+ ',')

                    name.write('\n')

                    for j in range(section_start+1,section_end,1):
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
                            if ln_split[0]!="'='":
                                prs_old=ln_split[0]
                            elif ln_split[0]=="'='":
                                ln_split[0]=prs_old

                            for prs in ln_split:


                                prs_final=requote(prs)
                                if prs_final=='.':
                                    prs_final=dot_remove(prs_final)
                                name.write(prs_final + ',')
                            #name.write(prs_final + ',')
                            name.write('\n')

    #if ';' in line:
                    name.close()

lp.close()


print "RUN END: {:%Y-%m-%d %H:%M:%S}".format(datetime.datetime.now())
run_end=datetime.datetime.now()
print run_end-run_start




#########
