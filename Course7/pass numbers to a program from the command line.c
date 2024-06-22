/*
You are selling plants and are working on creating invoices for customer orders. You are testing a program that should receive, as arguments passed in from the command line, a number of plants to be purchased (an integer) and the price per plant (a decimal number). Your job is to create an invoice for the customer (see examples) that prints out the total price of the order (which can be found by multiplying the price per plant by the number of plants purchased). 

If your program is called with an incorrect number of arguments the user should receive the message "Invalid input" (see Example 3).

Be sure to print prices with two decimal places, and to print the invoice exactly as presented in the examples below.

When testing your program in Weblinux, you need to provide the command line arguments in the same way you just learned in the video. When you submit your program to taskgrader however, we will be supplying those arguments in order to test your program. In your submission you thus do not have to worry about providing command line arguments to your program.
*/

//Solution:
#include <stdio.h>
#include <stdlib.h>

double total(int n, double pr)
{
    return n*pr;
}
int main(int argc, char *arg[])
{
    int numPlants;
    double price;
    if(argc != 3)
    {
        printf("Invalid input\n");
    }
    else{
        numPlants = atoi(arg[1]);
        price = atof(arg[2]);
        printf("%d plants for %.2lf dollars each cost %.2lf dollars.\n",numPlants, price, total(numPlants,price));
    }
}
