package io.github.theangrydev.yatspecfluent;

/**
 * When the {@link #verify(TestResult)} method is invoked, a verification should be made about the {@link TestResult}.
 *
 * For example, this could mean verifying a HTTP interaction took place.
 *
 * This class should act as a builder for use in {@link FluentTest}.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Builder_pattern#Java_example">The Builder Pattern</a>
 * @param <TestResult> The test result that the {@link #verify(TestResult)} operates on
 */
@FunctionalInterface
public interface ThenVerification<TestResult> {

    /**
     * Verify that the {@link TestResult} matches the criteria that were built up to make this {@link ThenVerification}.
     *
     * @param testResult The result from the system under test
     */
    void verify(TestResult testResult);
}
