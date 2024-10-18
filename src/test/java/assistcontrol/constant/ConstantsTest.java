package assistcontrol.constant;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class ConstantsTest {

    public static final String TOKEN = "token";
    @Test
    void tokenConstantIsNotNull() {
        assertNotNull(ConstantsTest.TOKEN);
    }

    @Test
    void tokenConstantHasExpectedValue() {
        assertEquals("token", ConstantsTest.TOKEN);
    }

}
