package io.github.jsonSnapshot;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.jsonSnapshot.SnapshotMatcher.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class SnapshotIntegrationTest {

    @BeforeAll
    public static void beforeAll() {
        start();
    }

    @AfterAll
    public static void afterAll() {
        validateSnapshots();
    }

    @Test
    public void shouldMatchSnapshotOne() {
        expect(FakeObject.builder().id("anyId1").value(1).name("anyName1").build()).toMatchSnapshot();
    }

    @Test
    public void shouldMatchSnapshotTwo() {
        expect(FakeObject.builder().id("anyId2").value(2).name("anyName2").build()).toMatchSnapshot();
    }

    @Test
    public void shouldMatchSnapshotThree() {
        expect(FakeObject.builder().id("anyId3").value(3).name("anyName3").build()).toMatchSnapshot();
    }

    @Test
    public void shouldMatchSnapshotFour() {
        expect(FakeObject.builder().id("anyId4").value(4).name("any\n\n\nName4").build()).toMatchSnapshot();
    }

    @Test
    public void shouldMatchSnapshotInsidePrivateMethod() {
        matchInsidePrivate();
    }

    private void matchInsidePrivate() {
        expect(FakeObject.builder().id("anyPrivate").value(5).name("anyPrivate").build()).toMatchSnapshot();
    }

    @Test
    public void shouldThrowSnapshotMatchException() {
        assertThrows(SnapshotMatchException.class, expect(FakeObject.builder().id("anyId5").value(6).name("anyName5").build())::toMatchSnapshot, "Error on: \n" +
                "io.github.jsonSnapshot.SnapshotIntegrationTest.shouldThrowSnapshotMatchException=[");
    }

    @Test
    public void shouldThrowStackOverflowError() {
        // Create cycle JSON
        FakeObject fakeObject1 = FakeObject.builder().id("anyId1").value(1).name("anyName1").build();
        FakeObject fakeObject2 = FakeObject.builder().id("anyId2").value(2).name("anyName2").build();
        fakeObject1.setFakeObject(fakeObject2);
        fakeObject2.setFakeObject(fakeObject1);

        assertThrows(SnapshotMatchException.class, () -> expect(fakeObject1).toMatchSnapshot());
    }
}
