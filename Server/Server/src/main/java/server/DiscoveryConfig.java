//https://github.com/jbrucker/server-discovery
package server;

/**
 * Some constants needed by client and server.
 * Define separately to avoid duplication.
 */
public class DiscoveryConfig {
	/** Port used for broadcast and listening. */
	public static final int DISCOVERY_PORT = 9999;
	/** String the client sends, to disambiguate packets on this port. */
	public static final String DISCOVERY_REQUEST = "Where-are-you?";
	/** Prefix string that server sends along with his IP address. */
	public static final String DISCOVERY_REPLY = "Here-I-am! ";
}
