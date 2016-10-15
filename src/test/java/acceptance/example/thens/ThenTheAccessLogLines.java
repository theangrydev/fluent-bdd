package acceptance.example.thens;

import org.assertj.core.api.WithAssertions;

import java.util.List;

public class ThenTheAccessLogLines implements WithAssertions {

    private final List<String> lines;

    public ThenTheAccessLogLines(List<String> lines) {
        this.lines = lines;
    }

    public ThenTheAccessLogLines hasSize(int size) {
        assertThat(lines).hasSize(size);
        return this;
    }
}
