using System.Text.RegularExpressions;
using Microsoft.Z3;
using utils;

var input = parse("input.txt");

Console.WriteLine($"Part01: {part01(input)}");
Console.WriteLine($"Part02: {part02(input)}");

long part01(List<Equation> input) => solve(input);
long part02(List<Equation> input) => solve(input, 10000000000000);

long solve(List<Equation> input, long sum = 0) 
{
    // a1x + b1y = c1
    // a2x + b2y = c2

    var tokens = 0L;
    using(var context = new Context())
    {
        foreach(var eq in input)
        {
            var x = context.MkIntConst("x");
            var y = context.MkIntConst("y");
            var a1 = context.MkReal(eq.A.X);
            var b1 = context.MkReal(eq.B.X);
            var c1 = context.MkInt(sum + eq.Sum.X);
            var a2 = context.MkReal(eq.A.Y);
            var b2 = context.MkReal(eq.B.Y);
            var c2 = context.MkInt(sum + eq.Sum.Y);

            var eq1 = context.MkEq(c1, context.MkAdd(context.MkMul(a1, x), context.MkMul(b1, y)));
            var eq2 = context.MkEq(c2, context.MkAdd(context.MkMul(a2, x), context.MkMul(b2, y)));

            var solver = context.MkSolver();
            solver.Assert(eq1);
            solver.Assert(eq2);
            if (solver.Check() != Status.SATISFIABLE)
            {
                continue;
            }

            var model = solver.Model;
            var xVal = long.Parse(model.Evaluate(x).ToString());
            var yVal = long.Parse(model.Evaluate(y).ToString());
            tokens += xVal * 3;
            tokens += yVal * 1;
        }
    }

    return tokens;    
}

List<Equation> parse(string fileName) => 
    File.ReadAllLines(fileName)
        .Chunk(4)
        .Select(it => it.SkipWhile(x => string.IsNullOrWhiteSpace(x)).ToArray())
        .Select(lines =>
        {
            var a = Regex.Match(lines[0], @"X\+(-?\d+), Y\+(-?\d+)").Let(x => new Coord(long.Parse(x.Groups[1].Value), long.Parse(x.Groups[2].Value)));
            var b = Regex.Match(lines[1], @"X\+(-?\d+), Y\+(-?\d+)").Let(x => new Coord(long.Parse(x.Groups[1].Value), long.Parse(x.Groups[2].Value)));
            var sum = Regex.Match(lines[2], @"X=(-?\d+), Y=(-?\d+)").Let(x => new Coord(long.Parse(x.Groups[1].Value), long.Parse(x.Groups[2].Value)));
            return new Equation(a, b, sum);
        })
        .ToList();


record Coord(long X, long Y);
record Equation(Coord A, Coord B, Coord Sum);