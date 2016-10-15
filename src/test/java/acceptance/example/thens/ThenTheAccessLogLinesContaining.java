package acceptance.example.thens;

import acceptance.example.test.TestResult;
import io.github.theangrydev.yatspecfluent.ThenAssertion;

import java.util.List;

public class ThenTheAccessLogLinesContaining implements ThenAssertion<ThenTheAccessLogLines, TestResult> {

    private String term;

    @Override
    public ThenTheAccessLogLines then(TestResult testResult) {
        List<String> lines = testResult.accessLogLinesContaining(term);
        return new ThenTheAccessLogLines(lines);
    }

    public ThenTheAccessLogLinesContaining containing(String term) {
        this.term = term;
        return this;
    }
}
