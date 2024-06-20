/*
Write a C-program that reads an input character (using scanf) and displays the following pyramid pattern using the character read: 

Examples
Input
#
Output
++++#++++
+++###+++
++#####++
+#######+
#########
*/

//Solution:
#include <stdio.h>
int main()
{
    /*++++#++++
+++###+++
++#####++
+#######+
#########
*/
char c;
scanf("%c",&c);
printf("++++%c++++\n+++%c%c%c+++\n++%c%c%c%c%c++\n+%c%c%c%c%c%c%c+\n%c%c%c%c%c%c%c%c%c",c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c,c);
    return 0;
}
