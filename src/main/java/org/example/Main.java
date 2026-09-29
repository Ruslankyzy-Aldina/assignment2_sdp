package org.example;
import org.example.client.MuseumDispatch;
import org.example.config.FactoryRegistry;
import org.example.factories.TransportFactory;
import org.example.model.*;
public class Main {
    public static void main(String[] args) {
        try {
            demonstrate(FactoryRegistry.select(args.length == 0 ? "road" : args[0]));
        } catch (IllegalArgumentException | IllegalStateException error) {
            System.err.println("Dispatch failed: " + error.getMessage());
            System.exit(1);
        }
    }

    private static <F extends Family> void demonstrate(TransportFactory<F> factory) {
        var client = new MuseumDispatch<>(factory);
        var reading = new Reading(1, 101, 40);
        var booked = client.book(new Shipment("ART-101", 20, "SITE-A"), 1000, 20);
        System.out.println("BOOKED " + booked.payload());
        System.out.println("Delivery days: " + booked.plan().days());
        var redirected = client.reroute(booked, reading, "SITE-B", 1000, 20);
        System.out.println("REROUTED " + redirected.payload());
        System.out.println(client.receive(redirected, reading));
        System.out.println(client.receive(redirected, new Reading(8, 101, 80)));
    }
}
