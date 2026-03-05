package loadBalancer;

import org.example.loadBalancer.LoadBalancer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for org.example.loadBalancer.LoadBalancer.
 *
 * These tests verify:
 * 1. Basic instance registration
 * 2. Maximum instances limit
 * 3. Duplicate instance protection
 * 4. Round Robin load distribution
 * 5. Correct behavior when no instances exist
 */
class LoadBalancerTest {

    /**
     * Verifies that a registered instance can be returned by the load balancer.
     *
     * Scenario:
     * - register a single instance
     * - call get()
     *
     * Expected result:
     * - the same instance is returned
     *
     * What this test checks:
     * - basic functionality
     * - happy path behavior
     */
    @Test
    void shouldRegisterInstanceAndReturnIt() {
        LoadBalancer lb = new LoadBalancer();

        lb.register("server1");

        String result = lb.get();

        assertEquals("server1", result);
    }


    /**
     * Verifies that the load balancer enforces the maximum instance limit.
     *
     * Scenario:
     * - register 10 instances (allowed)
     * - try to register the 11th instance
     *
     * Expected result:
     * - IllegalStateException is thrown
     *
     * What this test checks:
     * - business rule: maximum number of instances is 10
     * - correct error handling
     */
    @Test
    void shouldThrowExceptionWhenMaxInstancesExceeded() {
        LoadBalancer lb = new LoadBalancer();

        for (int i = 0; i < 10; i++) {
            lb.register("server" + i);
        }

        assertThrows(IllegalStateException.class,
                () -> lb.register("server10"));
    }


    /**
     * Verifies that duplicate instance registration is not allowed.
     *
     * Scenario:
     * - register an instance
     * - attempt to register the same instance again
     *
     * Expected result:
     * - IllegalArgumentException is thrown
     *
     * What this test checks:
     * - uniqueness constraint for instances
     * - protection from duplicate registrations
     */
    @Test
    void shouldNotAllowDuplicateInstances() {
        LoadBalancer lb = new LoadBalancer();

        lb.register("server1");

        assertThrows(IllegalArgumentException.class,
                () -> lb.register("server1"));
    }


    /**
     * Verifies that instances are returned using Round Robin strategy.
     *
     * Scenario:
     * - register three instances: A, B, C
     * - call get() multiple times
     *
     * Expected order:
     * A → B → C → A
     *
     * What this test checks:
     * - correct round-robin load distribution
     * - index cycling behavior
     */
    @Test
    void shouldReturnInstancesInRoundRobinOrder() {
        LoadBalancer lb = new LoadBalancer();

        lb.register("A");
        lb.register("B");
        lb.register("C");

        assertEquals("A", lb.get());
        assertEquals("B", lb.get());
        assertEquals("C", lb.get());
        assertEquals("A", lb.get());
    }


    /**
     * Verifies behavior when no instances are registered.
     *
     * Scenario:
     * - create load balancer
     * - call get() without registering instances
     *
     * Expected result:
     * - IllegalStateException is thrown
     *
     * What this test checks:
     * - protection against invalid state
     * - correct error signaling
     */
    @Test
    void shouldThrowExceptionWhenNoInstancesRegistered() {
        LoadBalancer lb = new LoadBalancer();

        assertThrows(IllegalStateException.class, lb::get);
    }
}