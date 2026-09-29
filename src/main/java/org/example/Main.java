package org.example;
import org.example.legacy.DirectDispatch;
public class Main {
    public static void main(String[] args) {
        System.out.println(DirectDispatch.dispatch(args.length == 0 ? "road" : args[0], 20));
    }
}

