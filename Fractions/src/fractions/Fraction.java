package fractions;

import java.util.ArrayList;
import java.util.List;

public class Fraction {

	//If only there was operator overloading

	private long numerator;
	private long denominator;

	public Fraction(long num, long denom)
	{
		this.numerator = num;
		this.denominator = denom;
	}

	public Fraction()
	{
		this.numerator = 1;
		this.denominator = 1;
	}

	private static long LCM(long a, long b)
	{
		List<Long> arr = new ArrayList<Long>();
		boolean broke = false;
		while (true)
		{
			long least = 0;
			if (a > b) least = b;
			else if (a < b) least = a;
			else least = a;
			for (long i = 0; i < least; i++)
			{
				if (((a % i) == 0) & ((b % i) == 0))
				{
					a = a / i;
					b = b / i;
					arr.add(i);
					broke = true;
					break;
				}
			}
			if (!broke) break;
			broke = false;
		}
		long total = a * b;
		for (long i : arr)
		{
			total = total * i;
		}
		return total;
	}

	public static Fraction add(Fraction a, Fraction b)
	{
		long lcm = Fraction.LCM(a.denominator, b.denominator);
		return new Fraction((a.numerator * (lcm / a.denominator)) + (b.numerator * (lcm / b.denominator)), lcm);
	}

	public static Fraction sub(Fraction a, Fraction b)
	{
		long lcm = Fraction.LCM(a.denominator, b.denominator);
		return new Fraction((a.numerator * (lcm / a.denominator)) - (b.numerator * (lcm / b.denominator)), lcm);
	}

	public static Fraction mul(Fraction a, Fraction b)
	{
		return new Fraction(a.numerator * b.numerator, a.denominator * b.denominator);
	}

	public static Fraction div(Fraction a, Fraction b)
	{
		return new Fraction(a.numerator * b.denominator, a.denominator * b.numerator);
	}

	public long getNumerator()
	{
		return this.numerator;
	}

	public long getDenominator()
	{
		return this.denominator;
	}

	@Override
	public String toString()
	{
		return this.numerator + "/" + this.denominator;
	}

	public double toDouble()
	{
		return  (double) this.numerator / (double) this.denominator;
	}

	public float toFloat()
	{
		return  (float) this.numerator / (float) this.denominator;
	}
}
