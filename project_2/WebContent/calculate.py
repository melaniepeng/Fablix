Querytime = 0.0
Querylen = 0
JDBCtime = 0.0
JDBC = 0
searchtime=0.0
search = 0
with open("/Users/melaniepeng/Desktop/text.txt") as file:
     openfile = file.read().replace("few","0").split()
     for i in range(len(openfile)-1):
         if (openfile[i] == "SearchServlet"):
             Querytime+=float(openfile[i+1])
             Querylen+=1
         elif (openfile[i] == "totalJDBCtime"):
             JDBCtime+=float(openfile[i+1])
             JDBC+=1
print("Average Search Servlet Time(ms)"+str(Querytime/1000000/Querylen))
print("Average JDBC Time(ms)"+str(JDBCtime/1000000/JDBC))
             
