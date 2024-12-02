
using System.Formats.Asn1;

var input = parse("input.txt");

Console.WriteLine($"Part01: {Part01(input)}");
Console.WriteLine($"Part02: {Part02(input)}");

int Part01(List<Report> input)
{
    return input.Count(r => r.IsSafe());
}

int Part02(List<Report> input)
{
    return input.Count(r => r.IsSafe(true));
}

List<Report> parse(string fileName)
{
    return File.ReadAllLines(fileName)
        .Select(s => s.Split(" ").Select(long.Parse).ToList())
        .Select(s => new Report(s))
        .ToList();
}

enum Direction { Inc, Equal, Dec }

record Report(List<long> levels)
{
    
    public bool IsSafe(bool withRemoval = false)
    {
        List<int> numInc = [];
        List<int> numEqual = [];
        List<int> numDec = [];
        for(int i = 0; i < levels.Count - 1; i++)
        {
            var diff = levels[i+1] - levels[i];
            if(diff > 0)
            {
                numInc.Add(i);
            }
            else if(diff == 0)
            {
                numEqual.Add(i);
            }
            else if(diff < 0)
            {
                numDec.Add(i);
            }
        }

        var nums = new List<int>[] { numInc, numEqual, numDec }.Where(l => l.Count >= 1).ToList();
        if(nums.Count >= 2)
        {
            if(withRemoval)
            {
                for(int i = 0; i < levels.Count; i++)
                {
                    var newLevels = new List<long>(levels);
                    newLevels.RemoveAt(i);
                    if(new Report(newLevels).IsSafe(false   ))
                    {
                        return true;
                    }
                }
            }
            return false;
        }

        for(int i = 0; i < levels.Count - 1; i++)
        {
            var diff = Math.Abs(levels[i+1] - levels[i]);
            if(!(diff >= 1 && diff <= 3))
            {   
                if(withRemoval)
                {
                    for(int j = 0; j < levels.Count; j++)
                    {
                        var newLevels = new List<long>(levels);
                        newLevels.RemoveAt(j);
                        if(new Report(newLevels).IsSafe(false   ))
                        {
                            return true;
                        }
                    }
                }
                return false;
            }
        }

        return true;
    }
}