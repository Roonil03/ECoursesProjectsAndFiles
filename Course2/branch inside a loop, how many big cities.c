/*You want to determine the number of cities in a given region that have a population strictly greater than 10,000. To do this, you write a program that first reads the number of cities in a region as an integer, and then the populations for each city one by one (also integers).*/

//Solution:
#include <stdio.h>
int main()
{
    int n,i,pop, count =0;
    scanf("%d",&n);
    for(i=0;i<n;i++)
    {
        scanf("%d",&pop);
        if(pop>10000)
        {
            count++;
        }
    }
    printf("%d",count);
    return 0;
}