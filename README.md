# My Script - Calculate.py
In our calculate.py, we use the python to calculate the average time of the search servlet and the average time of the JDBC. To calculate these, We first use Jmeter and get the result text file. Then, in our python file, we open the text file and read it as a string. After that, we use split to remove spaces and "\n," and make the file string as the list to make a list looks like "[place of browsing, Time of search servlet, 3103021, Time for JDBC, 20831,....]. Due to this list, we write a for loop to get the result. In our loop, we run for the index of the length of the list as i. If we find the string like " Time of search servlet," we add list[i] to the time of search servlet. After we add all the numbers, we use the total time of search servlet to divide by the number of this term occurs to get the average time and turn it into ms. Same to the Time for JDBC. 