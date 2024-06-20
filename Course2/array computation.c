/*You plan to make a delicious meal and want to take the money you need to buy the ingredients. Fortunately you know in advance the price per pound of each ingredient as well as the exact amount you need. The program should read in the number of ingredients (up to a maximum of 10 ingredients), then for each ingredient the price per pound. Finally your program should read the weight necessary for the recipe (for each ingredient in the same order). Your program should calculate the total cost of these purchases, then display it with 6 decimal places.*/

//Solution:
#include <stdio.h>
int main()
{
    double pr[10],po[10];
    double total = 0;
    int i,n;
    scanf("%d",&n);
    for(i=0;i<n;i++)
    {
        scanf("%lf",&pr[i]);
    }
    for (i=0;i<n;i++)
    {
        scanf("%lf",&po[i]);
        total += po[i]*pr[i];
    }
    printf("%lf",total);
}