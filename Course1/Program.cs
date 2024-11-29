namespace ConsoleApp1
{
    /// <summary>
    /// This is also a comment
    /// </summary>
    internal class Program
    {
        /// <summary>
        /// This is how we comment in this code.
        /// </summary>
        /// <param name="args"></param>
        static void Main(string[] args)
        {
            Console.WriteLine("Hello, World!");
            Console.WriteLine("");
            var time = 60;
            const int sel = 40; // From this we can conclude that we cannot use const var as we need to explicitly declare a datatype.
            Console.WriteLine(time + sel);
            int ar = int.Parse(Console.ReadLine());
            Console.WriteLine(ar + sel);
            float val = (float)ar / time;
            var v = val;


            Console.WriteLine(v + val); //prints 0.06666667 when ar is 2
            Console.WriteLine(MathF.Cos(v));            


            var cosineOf45 = MathF.Cos(45 * MathF.PI / 180);
            Console.WriteLine(cosineOf45);


            //Temperature Conversions
            Console.Write("Enter in a celcius temperature");
            int C = int.Parse(Console.ReadLine());
            float F = (float)C * 9 / 5 + 32;
            Console.WriteLine(F);
            return;
        }
    }
}
