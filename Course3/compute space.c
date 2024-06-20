/*A delivery company has hired you to manage their tracking services division. It is your job to store all of the currently used tracking codes in the company's database. These codes consist of either all integers, all decimal numbers, or all characters. The chief technology officer has warned you that the database is old and has limited space, so you want to determine how much memory the tracking codes will occupy before storing them. You decide to write a program to assist you in this process. 

Your program should first read an integer number indicating how many tracking codes you plan on entering. Next, for each successive tracking code your program should read in the integer length of code followed by a space and then the type of code ('i' for integer, 'd' for decimal, or 'c' for character). Finally your program should print the total amount of space required to store all of the tracking codes (in bytes). If the user enters an incorrect type for any tracking number, the program should print 'Invalid tracking code type' and exit.*/


//Solution:
#include <stdio.h>

int main()
{
    int n,i,temp,sum=0;char c;
    scanf("%d",&n);
    for(i=0;i<n;i++)
    {
        scanf("%d %c",&temp, &c);
        switch(c)
        {
            case 'i':
            sum += temp*sizeof(int);
            break;
            case 'd':
            sum += temp*sizeof(double);
            break;
            case 'c':
            sum += temp*sizeof(char);
            break;
            default:
            printf("Invalid tracking code type");
            return 0;
            break;
        }
    }
    printf("%d bytes",sum);
    return 0;
}