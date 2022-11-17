package modele;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class TrajetTest {
    Intersection intersection1 = new Intersection(1L, 10, 20);
    Intersection intersection2 = new Intersection(2L, 20, 10);
    Intersection intersection3 = new Intersection(3L, 30, 10);

    Segment segment1 = new Segment(intersection1, intersection2, 10L, "rue Street");
    Segment segment2 = new Segment(intersection2, intersection3, 10L, "avenue Avenue");

    ArrayList<Segment> listeSegments = new ArrayList<Segment>();

    @Test
    @DisplayName("Test des setters")
    void settersTest() {

    }

}
