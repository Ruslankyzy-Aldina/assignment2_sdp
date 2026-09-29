package org.example.model;
public record Shipment(String artifactId, int kilograms, String destination) {
    public Shipment {
        if (artifactId == null || !artifactId.matches("[A-Z0-9-]+"))
            throw new IllegalArgumentException("Artifact ID must contain uppercase letters, digits or hyphens");
        if (kilograms <= 0) throw new IllegalArgumentException("Weight must be positive");
        if (destination == null || !destination.matches("[A-Z0-9-]+"))
            throw new IllegalArgumentException("Destination must be an uppercase site code");
    }
    public Shipment redirectedTo(String destination) { return new Shipment(artifactId, kilograms, destination); }
}
