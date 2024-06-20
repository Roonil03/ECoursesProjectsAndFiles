/*You are helping a teacher average grades. You get bored computing averages by hand, so you decide to write a computer program to do the work for you.

Your program must first read an integer indicating the number of grades to be averaged. Next, your program will read the grades one by one, all of which are integers as well. Finally, your program will calculate and print the average of the grades to two decimal places.*/
#include <stdio.h>
int main()
{
    int n,mrks,total=0;
    int i;
    double avg;
    scanf("%d",&n);
    for(i=0;i<n;i++)
    {
        scanf("%d",&mrks);
        total += mrks;
    }
    avg = (double)total/n;
    printf("%.2lf",avg);
    return 0;
}