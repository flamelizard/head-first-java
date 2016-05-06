package hamcrest.example;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


/**
 * Created by Tom on 3/29/2016.
 */
/*
Hamcrest notes
JUnit ships with only core hamcrest matchers. It does not include some array,
 comparison and other matchers. Get hamcrest-all from hamcrest web site.

!! Primitive types (int, byte etc.) have zero or limited support. Always use
wrapper classes (Integer etc.) alone, in standard array or in collections
(generics)

!! Arrays (not collection) do work fine ONLY with matchers having in their
name word "array". Other matchers like "contains, everyitem" show strange
compiler errors about type. So, these are probably meant for Collections only.

is() is wrapper to equalTo()

Side note - primitive types do not work here because there is no automatic
boxing/unboxing.

Hamcrest quick reference pdf in books_it
https://www.javacodegeeks.com/2015/11/hamcrest-matchers-tutorial.html#comment-82453
 */
public class HamCrestHandsOn {
    public static void main(String[] args) {

//        int[] numPrim = {10, 20, -1, 0};  WRONG, no support !!
        Integer[] numWrap = {10, 20, -1, 0};
        String[] strings = {"rain", "ball", "shell"};
        List<Integer> numGen = new ArrayList<>(asList(10, 20, -1, 0));
        List<String> strGen = new ArrayList<>(asList("rain", "ball, " +
                "shell"));

        assertThat("string has to pass all tests", strings[0],
                allOf(startsWith("ra"), containsString("in"), instanceOf
                        (String.class)));

        assertThat("lists have the same content", numGen,
                contains(10, 20, -1, 0));

        assertThat("list items are instance of ", strGen,
                everyItem(instanceOf(String.class)));

        assertThat("numbers are small", numGen,
                everyItem(lessThan(50)));

//        Use ..array.. matchers for arrays, otherwise strange compiler errors
        assertThat("check for specific *array* content", numWrap,
                is(arrayContaining(10, 20, -1, 0)));

//        equalTo() can compare either single value or array content
//        Using here anonymous array, it does not accept list var
        assertThat("alternative array content check", numWrap,
                equalTo(new Integer[]{10, 20, -1, 0}));

        Integer[] oddNum = {1, 3};
        assertThat("array items can check different criteria", oddNum,
                arrayContaining(greaterThan(0), is(3)));

        assertThat("array has specific item", numGen,
                hasItem(is(lessThanOrEqualTo(-1))));

//        .hasSize() counts from 0 !!
        assertThat("collection size check", strGen,
                hasSize(greaterThanOrEqualTo(2)));
        assertThat("array size check", strings,
                arrayWithSize(is(3)));

    }
}
