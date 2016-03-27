package ru.javawebinar.topjava;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;

/**
 * Created by Таалайбек on 28.03.2016.
 */
public class HasGlobalTimeout
{
    public static String log;

    @Rule
    public TestRule globalTimeout = new Timeout(1);

    @Test
    public void testInfiniteLoop1() {
        log+= "ran1";
        for(int i = 0; i < 500; i ++) {}
    }

    @Test
    public void testInfiniteLoop2() {
        log+= "ran2";
        for(int i = 0; i < 1000; i ++) {}
    }
}
