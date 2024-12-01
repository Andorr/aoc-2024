using utils;

var input = parse("input.txt");

Console.WriteLine($"Part01: {part01(input)}");
Console.WriteLine($"Part02: {part02(input)}");

long part01(Input input) 
{
    input.A.Sort();
    input.B.Sort();
    return input.A.Zip(input.B).Sum(x => Math.Abs(x.First - x.Second));
}


long part02(Input input)
{
    var dict = input.B.GroupBy(x => x).ToDictionary(k => k.Key, k => k.Count());
    return input.A.Select(x => x*dict.GetValueOrDefault(x)).Sum();
}

Input parse(string fileName) => 
    File.ReadAllLines(fileName)
        .Select(x => 
            x.Split("   ").Select(long.Parse).ToArray()
        )
        .Aggregate(
            new { A = new List<long>(), B = new List<long>() },
            (acc, cur) => 
            {
                acc.A.Add(cur[0]);
                acc.B.Add(cur[1]);
                return acc;
            })
        .Let(lsts => new Input(lsts.A, lsts.B));

record Input(List<long> A, List<long> B);