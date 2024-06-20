/*Create a program that displays on the screen a square of n x n stars, with the integer n given as an input.*/

//Solution:
#include <stdio.h>
int main()
{
    int i,n,j;
    scanf("%d",&n);
    for(i=0;i<n;i++)
    {
        for(j=0;j<n;j++)
        {
            printf("*");
        }
        printf("\n");
    }
    return 0;
}