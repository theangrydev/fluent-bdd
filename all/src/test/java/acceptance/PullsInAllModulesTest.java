package acceptance;

import io.github.theangrydev.fluentbdd.core.FluentBdd;
import io.github.theangrydev.fluentbdd.mockito.FluentMockito;
import io.github.theangrydev.fluentbdd.yatspec.FluentYatspec;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class PullsInAllModulesTest {

    @Test
    public void pullsInAllModules() {
        assertNotNull(FluentBdd.class);
        assertNotNull(FluentMockito.class);
        assertNotNull(FluentYatspec.class);
    }
}
