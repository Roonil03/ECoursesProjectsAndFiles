using System;
using ConsoleCards;

namespace ProgrammingAssignment3
{
    // IMPORTANT: Only add code in the section
    // indicated below. The code I've provided
    // makes your solution work with the 
    // automated grader on Coursera

    /// <summary>
    /// Programming Assignment 3
    /// </summary>
    class Program
    {
        /// <summary>
        /// Programming Assignment 3
        /// </summary>
        /// <param name="args">command-line args</param>
        static void Main(string[] args)
        {
            // loop while there's more input
            string input = Console.ReadLine();
            while (input[0] != 'q')
            {
                // Add your code between this comment
                // and the comment below. You can of
                // course add more space between the
                // comments as needed

                // declare a deck variables and create a deck object
                // DON'T SHUFFLE THE DECK
                Deck deckOfCards = new Deck();

                // deal 2 cards each to 4 players (deal properly, dealing
                // the first card to each player before dealing the
                // second card to each player)
                Card p1 = deckOfCards.TakeTopCard();
                Card p2 = deckOfCards.TakeTopCard();
                Card p3 = deckOfCards.TakeTopCard();
                Card p4 = deckOfCards.TakeTopCard();
                Card p5 = deckOfCards.TakeTopCard();
                Card p6 = deckOfCards.TakeTopCard();
                Card p7 = deckOfCards.TakeTopCard();
                Card p8 = deckOfCards.TakeTopCard();
                // deal 1 more card to players 2 and 3
                Card p9 = deckOfCards.TakeTopCard();
                Card p10 = deckOfCards.TakeTopCard();

                // flip all the cards over
                p1.FlipOver();
                p2.FlipOver();
                p3.FlipOver();
                p4.FlipOver();
                p5.FlipOver();
                p6.FlipOver();
                p7.FlipOver();
                p8.FlipOver();
                p9.FlipOver();
                p10.FlipOver();
                // print the cards for player 1
                Console.WriteLine(p1.Rank +","+p1.Suit);
                Console.WriteLine(p5.Rank + "," + p5.Suit);

                // print the cards for player 2
                Console.WriteLine(p2.Rank + "," + p2.Suit);
                Console.WriteLine(p6.Rank + "," + p6.Suit);
                Console.WriteLine(p9.Rank + "," + p9.Suit);

                // print the cards for player 3
                Console.WriteLine(p3.Rank + "," + p3.Suit);
                Console.WriteLine(p7.Rank + "," + p7.Suit);
                Console.WriteLine(p10.Rank + "," + p10.Suit);

                // print the cards for player 4
                Console.WriteLine(p4.Rank + "," + p4.Suit);
                Console.WriteLine(p8.Rank + "," + p8.Suit);

                // Don't add or modify any code below
                // this comment
                input = Console.ReadLine();
            }
        }
    }
}
