package test.fr.heyjojo.game;

import java.util.Date;
import java.util.Random;

import org.junit.Test;

public class TestJojo {

	@Test
	public void test() {

		Random r = new Random();

		Date t0 = new Date();
		long l0 = t0.getTime();

		int num1 = 1458796312;
		int num2 = 1984562547;

		int num3;

		for (long i = 0; i < 10000000000l; i++) {

			num1 = r.nextInt();
			num2 = r.nextInt();
			num3 = num1 / num2;
		}

		Date t1 = new Date();
		long l1 = t1.getTime();
		System.out.println("int =" + (l1 - l0));

	}

	@Test
	public void test2() {

		Random r = new Random();

		Date t0 = new Date();
		long l0 = t0.getTime();

		float num1 = 3588798.7868f;
		float num2 = 51683135854.684684f;

		float num3;

		for (long i = 0; i < 10000000000l; i++) {
			num1 = r.nextFloat();
			num2 = r.nextFloat();
			num3 = num1 / num2;
		}

		Date t1 = new Date();
		long l1 = t1.getTime();
		System.out.println("float=" + (l1 - l0));

	}

	@Test
	public void test3() {

		Random r = new Random();

		Date t0 = new Date();
		long l0 = t0.getTime();

		for (long i = 0; i < 10000000000l; i++) {
			r.nextInt();
			r.nextInt();
		}

		Date t1 = new Date();
		long l1 = t1.getTime();
		System.out.println("void int=" + (l1 - l0));

	}

	@Test
	public void test4() {

		Random r = new Random();

		Date t0 = new Date();
		long l0 = t0.getTime();

		for (long i = 0; i < 10000000000l; i++) {
			r.nextFloat();
			r.nextFloat();
		}

		Date t1 = new Date();
		long l1 = t1.getTime();
		System.out.println("void float=" + (l1 - l0));

	}
	
	@Test
	public void test5() {

		long a = 314000000l;
		long b = 581l;
		System.out.println("a/b=" + (a/b));
	}
	
	@Test
	public void test6() {

		double total = 0;
        total += 5.6;
        total += 5.8;
        System.out.println(total);
        double total2= 11.4d;
        total2 -= 5.8;
        System.out.println(total2);
	}

}
