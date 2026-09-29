package org.example.model;
/** F ties the manifest to the planner and intake protocol at compile time. */
public record Label<F extends Family>(Plan<F> plan, String payload) {}
