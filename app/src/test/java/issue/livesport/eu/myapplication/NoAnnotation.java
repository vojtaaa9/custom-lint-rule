package issue.livesport.eu.myapplication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class NoAnnotation {
    @Test
    public void bla() {
        assertEquals(4, 2 + 2);
    }
}
