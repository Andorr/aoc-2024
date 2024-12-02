using utils;

var input = parse(args.FirstOrDefault() ?? "input.txt");

Console.WriteLine($"Part01: {Part01(input)}");
Console.WriteLine($"Part02: {Part02(input)}");

int Part01(List<Report> input) => input.Count(r => r.IsSafe());

int Part02(List<Report> input) => input.Count(r => r.IsSafe(true));

List<Report> parse(string fileName)
{
    return File.ReadAllLines(fileName)
        .Select(s => s.Split(" ").Select(long.Parse).ToList())
        .Select(s => new Report(s))
        .ToList();
}

record Report(List<long> levels)
{
    public bool IsSafe(bool withRemoval = false) =>
        levels.Zip(levels.Skip(1))
            .Select(x => x.First - x.Second)
            .Where(x => Math.Abs(x) >= 1 && Math.Abs(x) <= 3)
            .GroupBy(x => x > 0)
            .Let(nums =>
            {
                if(nums.Sum(g => g.Count()) != levels.Count - 1 || nums.Count() > 1)
                {
                    return !withRemoval ? false : Enumerable.Range(0, levels.Count)
                        .Any(i => new Report(levels.ToList().Also(l => l.RemoveAt(i)).ToList()).IsSafe(false));
                }
                return true;
            });
       
}