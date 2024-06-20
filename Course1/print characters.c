/*Write a C-program that displays the following:

Programming in C
using this printf statement:

printf ("Programming %c%c %c\n", letter1, letter2, letter3);
Warning: do not use a scanf statement in this exercise!*/

//Solution:
#include <stdio.h>
int main()
{
    char letter1='i',letter2='n',letter3='C';
    printf ("Programming %c%c %c\n", letter1, letter2, letter3);
    return 0;
}