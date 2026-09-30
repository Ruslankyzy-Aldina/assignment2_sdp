package org.example.model;
public record Label<F extends Family>(Plan<F> plan, String payload) {}
