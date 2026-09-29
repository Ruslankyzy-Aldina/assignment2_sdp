package org.example.model;
public record Plan<F extends Family>(Shipment shipment, int cost, int days, String handling) {}
