package net.earthcomputer.enchcrack;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class Util {

	private Util() {
	}

	private static final Field RAND_SEED;

	static {
		try {
			RAND_SEED = Random.class.getDeclaredField("seed");
		} catch (Exception e) {
			throw new AssertionError(e);
		}
		RAND_SEED.setAccessible(true);
	}

	public static long getRandomSeed(Random rand) {
		try {
			return ((AtomicLong) RAND_SEED.get(rand)).get();
		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}

}
