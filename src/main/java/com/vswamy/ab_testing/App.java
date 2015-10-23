package com.vswamy.ab_testing;

import redis.clients.jedis.Jedis;

/**
 * Hello world!
 *
 */

public class App
{
    public static void main(String[] args)
    {
        Jedis jedis = new Jedis("localhost");
        jedis.set("foo", "bar");
        String value = jedis.get("foo");

        jedis.close();
        System.out.println(value);
        System.out.println("Done");
    }
}
