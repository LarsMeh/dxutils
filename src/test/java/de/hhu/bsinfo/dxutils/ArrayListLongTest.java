package de.hhu.bsinfo.dxutils;

import org.junit.Assert;
import org.junit.Test;

public class ArrayListLongTest {
    @Test
    public void create() {
        ArrayListLong list = new ArrayListLong();

        Assert.assertEquals(0, list.getSize());
        Assert.assertTrue(list.isEmpty());
    }

    @Test
    public void create2() {
        ArrayListLong list = new ArrayListLong(5);

        Assert.assertEquals(0, list.getSize());
        Assert.assertTrue(list.isEmpty());
    }

    @Test
    public void create3() {
        ArrayListLong list = new ArrayListLong(100L);

        Assert.assertEquals(1, list.getSize());
        Assert.assertFalse(list.isEmpty());
    }

    @Test
    public void create4() {
        ArrayListLong list = ArrayListLong.wrap(new long[] {1, 2, 3, 4});

        Assert.assertEquals(4, list.getSize());
        Assert.assertFalse(list.isEmpty());
    }

    @Test
    public void create5() {
        ArrayListLong list = ArrayListLong.copy(new long[] {1, 2, 3, 4});

        Assert.assertEquals(4, list.getSize());
        Assert.assertFalse(list.isEmpty());
    }

    @Test
    public void add() {
        ArrayListLong list = new ArrayListLong();

        list.add(5);
        list.add(10);
        list.add(13);

        Assert.assertEquals(3, list.getSize());
        Assert.assertFalse(list.isEmpty());

        Assert.assertEquals(5, list.get(0));
        Assert.assertEquals(10, list.get(1));
        Assert.assertEquals(13, list.get(2));
    }

    @Test
    public void add2() {
        ArrayListLong list = new ArrayListLong();

        list.add(5);
        list.add(2, 10);
        list.add(5, 13);

        Assert.assertEquals(6, list.getSize());
        Assert.assertFalse(list.isEmpty());

        Assert.assertEquals(5, list.get(0));
        Assert.assertEquals(0, list.get(1));
        Assert.assertEquals(10, list.get(2));
        Assert.assertEquals(0, list.get(3));
        Assert.assertEquals(0, list.get(4));
        Assert.assertEquals(13, list.get(5));
    }

    @Test
    public void set() {
        ArrayListLong list = new ArrayListLong();

        list.add(5);
        list.add(2, 10);
        list.add(5, 13);

        list.set(1, 15);
        list.set(3, 20);

        Assert.assertEquals(6, list.getSize());
        Assert.assertFalse(list.isEmpty());

        Assert.assertEquals(5, list.get(0));
        Assert.assertEquals(15, list.get(1));
        Assert.assertEquals(10, list.get(2));
        Assert.assertEquals(20, list.get(3));
        Assert.assertEquals(0, list.get(4));
        Assert.assertEquals(13, list.get(5));
    }

    @Test
    public void remove() {
        ArrayListLong list = new ArrayListLong();

        list.add(5);
        list.add(2, 10);
        list.add(5, 13);

        Assert.assertEquals(6, list.getSize());
        Assert.assertFalse(list.isEmpty());

        Assert.assertEquals(10, list.remove(2));

        Assert.assertEquals(5, list.getSize());
        Assert.assertFalse(list.isEmpty());

        Assert.assertEquals(5, list.get(0));
        Assert.assertEquals(0, list.get(2));
        Assert.assertEquals(13, list.get(4));
    }

    @Test
    public void addFront() {
        ArrayListLong list = new ArrayListLong();

        list.add(5);
        list.add(2, 10);
        list.add(5, 13);
        list.addFront(9);

        Assert.assertEquals(7, list.getSize());
        Assert.assertFalse(list.isEmpty());

        Assert.assertEquals(9, list.get(0));
        Assert.assertEquals(5, list.get(1));
        Assert.assertEquals(0, list.get(2));
        Assert.assertEquals(10, list.get(3));
        Assert.assertEquals(0, list.get(4));
        Assert.assertEquals(0, list.get(5));
        Assert.assertEquals(13, list.get(6));
    }
}
