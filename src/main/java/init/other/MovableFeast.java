package init.other;

import java.time.LocalDate;

public class MovableFeast {
	//See: https://pl.wikipedia.org/wiki/Wielkanoc
	private static final int MAX_YEAR = 2099;
	private static final int MIN_YEAR = 1900;
	/*
	 * Values A and B are only for years 1900 - 2099
	 */
	private static final int A_VALUE = 24;
	private static final int B_VALE = 5;
	
	public static LocalDate getEasterDate(int year) {
		if(year < MIN_YEAR || year > MAX_YEAR) return null;
		
		LocalDate date;
		
		int a = (year % 19);
		int b = (year % 4);
		int c = (year % 7);
		int d = (((a * 19) + A_VALUE) % 30);
		int e = ((2 * b + 4 * c + 6 * d) + B_VALE) % 7;
		
		if(d == 29 && e == 6) {
			date = LocalDate.of(year,  4, 19);
		} else if(d == 28 && e == 6 && a > 10) {
			date = LocalDate.of(year,  3,  29);
		} else {
			int result = d + e;
			date = LocalDate.of(year, 3, 22);
			date = date.plusDays(result);
		}
		
		return date;
	}
	
	public static LocalDate getCorpusChristiDate(int year) {
		if(year < MIN_YEAR || year > MAX_YEAR) return null;
		return getEasterDate(year).plusDays(60);
	}
}
