package me.googas.io;

import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import lombok.NonNull;
import me.googas.io.mocks.Person;
import me.googas.net.api.exception.MessengerListenFailException;
import me.googas.net.api.messages.Message;
import me.googas.net.api.messages.Request;
import me.googas.net.api.messages.StarboxRequest;
import me.googas.net.api.messages.RequestBuilder;
import me.googas.net.cache.MemoryCache;
import me.googas.net.sockets.json.ParamName;
import me.googas.net.sockets.json.Receptor;
import me.googas.net.sockets.json.adapters.MessageDeserializer;
import me.googas.net.sockets.json.client.JsonClient;
import me.googas.net.sockets.json.server.JsonClientThread;
import me.googas.net.sockets.json.server.JsonSocketServer;
import me.googas.starbox.scheduler.TimerScheduler;
import me.googas.starbox.time.Time;
import me.googas.starbox.time.unit.Unit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

/** Tests for the package 'me.googas.net' */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NetTest {

  @NonNull private static final Logger logger = LoggerFactory.getLogger(IOTest.class);
  @NonNull private static final TimerScheduler scheduler = new TimerScheduler();

  @NonNull private static final MemoryCache cache = new MemoryCache().register(NetTest.scheduler);
  // The id which will be used for testing using the Person mock
  private static final int id = 0;
  @NonNull private static TestingMocks mocks = new TestingMocks();
  private static JsonSocketServer server;
  private static JsonClient client;

  @BeforeAll
  static void prepareMocks()
      throws IOException, InterruptedException, MessengerListenFailException {
    int port = 3000;
    GsonBuilder gson =
        new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Message.class, new MessageDeserializer());
    long timeout = 5000;
    NetTest.mocks =
        TestingFiles.Contexts.JSON
            .read(TestingFiles.Resources.MOCKS, TestingMocks.class)
            .handle((e) -> NetTest.logger.error(e, () -> "Could not load mocking resources"))
            .provide()
            .orElseThrow(() -> new NullPointerException("Mocks could not be loaded"));
    NetTest.server =
        JsonSocketServer.listen(port)
            .addReceptors(new TestingReceptors())
            .handle(e -> NetTest.logger.warn(e, () -> "Caught error in server"))
            .start();
    NetTest.client =
        JsonClient.join("localhost", port)
            .addReceptors(new TestingReceptors())
            .handle(e -> NetTest.logger.warn(e, () -> "Caught error in client"))
            .start();
    // We should wait a little until the client has been fully connected
    long wait = 0;
    while (NetTest.server.getClients().isEmpty()) {
      Thread.sleep(1);
      wait++;
      if (wait > timeout) throw new MessengerListenFailException("Messenger failed to connect");
    }
  }

  @AfterAll
  static void close() throws IOException {
    NetTest.client.close();
    NetTest.server.close();
    Assertions.assertTrue(NetTest.client.isClosed());
  }

  @Test
  @Order(0)
  void clientRequests() throws MessengerListenFailException {
    NullPointerException exception = new NullPointerException("Did not return the existing person");
    RequestBuilder<Person> builder = Request.builder(Person.class, "person").put("id", NetTest.id);
    // Sync request
    Person person = builder.send(NetTest.client).orElseThrow(() -> exception);
    Assertions.assertEquals(NetTest.id, person.getId());
    NetTest.cache.add(person);
    // Async Request
    NetTest.client.sendRequest(
        builder.build(),
        optional -> {
          Person asyncPerson = optional.orElseThrow(() -> exception);
          Assertions.assertEquals(NetTest.id, person.getId());
        });
    // Async Request with a failed result: incorrect type of parameter
    NetTest.client.sendRequest(
        builder.put("id", "foo").build(),
        optional -> {
          Person asyncPerson = optional.orElseThrow(() -> exception);
          Assertions.assertEquals(NetTest.id, person.getId());
        },
        expected -> NetTest.logger.info(expected, () -> "Expected exception"));
  }

  @Test
  @Order(1)
  void cacheTests() {
    Person person =
        NetTest.cache
            .get(Person.class, cachePerson -> cachePerson.getId() == NetTest.id)
            .orElseThrow(() -> new NullPointerException("Person was not found in cache"));
    NetTest.logger.info(
        () -> person.getUsername() + " has " + NetTest.cache.getTimeLeft(person) + " time left");
    // Person gets removed in 5 seconds so lets wait 6
    NetTest.scheduler.later(
        Time.of(6, Unit.SECONDS), () -> Assertions.assertFalse(NetTest.cache.contains(person)));
  }

  @Test
  @Order(2)
  void serverRequests() {
    // Sync request
    Map<JsonClientThread, Optional<Integer>> pings =
        NetTest.server.sendRequest(
                Request.builder(int.class, "ping").put("init", System.currentTimeMillis()).build());
    Assertions.assertEquals(1, pings.size());
    pings.forEach(
        (client, optionalPing) ->
            optionalPing.ifPresent(
                ping ->
                    NetTest.logger.info(
                        () ->
                            String.format("Sync: Ping from server to %s is: %dms", client, ping))));
    // Async request
    NetTest.server.sendRequest(
        Request.builder(int.class, "ping").put("init", System.currentTimeMillis()).build(),
        (client, optionalPing) ->
            optionalPing.ifPresent(
                ping ->
                    NetTest.logger.info(
                        () ->
                            String.format(
                                "Async: Ping from server to %s is: %dms", client, ping))));
  }

  /** Testing mock receptors. */
  public static class TestingReceptors {

    /**
     * Get a person.
     *
     * @param id the id of the person
     * @return the person matching the id
     */
    @Receptor("person")
    public Person person(@ParamName("id") int id) {
      return NetTest.mocks.getPerson(id).orElse(null);
    }

    /**
     * Check the ping between the two messengers.
     *
     * @param init the initial {@link System#currentTimeMillis()}
     * @return the {@link System#currentTimeMillis()} - initial
     */
    @Receptor("ping")
    public int ping(@ParamName("init") long init) {
      return (int) (System.currentTimeMillis() - init);
    }
  }
}
