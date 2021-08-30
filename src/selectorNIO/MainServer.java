package selectorNIO;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainServer {

    ServerSocketChannel serverChannel; // for stream-oriented listening sockets
    Selector selector; // the multiplexor of SelectChannel object
    SelectionKey serverKey; // for representing the registration of a SelectableChannel with a Selector.

    MainServer(InetSocketAddress listenAddress) throws Throwable {
        serverChannel = ServerSocketChannel.open(); // to open new ServerSocketChannel
        serverChannel.configureBlocking(false); // to configure the ServerSocketChannel as non-blocking

        /*Selector.open() is to open the selector .
         *  SelectionKey.OP_ACCEPT is for if the selector detects that the corresponding
         *      server-socket channel is ready to accept another connection,
         *      or has an error pending, then it will add OP_ACCEPT to the key's ready set.
         */

        serverKey = serverChannel.register(selector = Selector.open(), SelectionKey.OP_ACCEPT); // Registers this channel with the given selector, returning a selection key.
        serverChannel.bind(listenAddress);// binds the ServerSocketChannel to the passed InetSocketAddress.

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            /* Executor is the interface which allows us to execute tasks on threads asynchronously
             *  but here I make a single thread for the sample that we need to
             */
            try {
                loop();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }, 0/* for the first execution is ran */, 500, TimeUnit.MILLISECONDS);
    }

    static HashMap<SelectionKey, ClientSession> clientMap = new HashMap<SelectionKey, ClientSession>(); // the hashMap is to store all clients currently connected

    void loop() throws Throwable {

        selector.selectNow();

        /*
         * DOSE NOT BLOCK ! , which means that if there is no keys ready in that given time it will return instantly and we won't have any key.
         * otherwise if there are keys ready it will populate the key set and return the amount of keys that were added to the key set.
         */

        for (SelectionKey key : selector.selectedKeys()) {
            //this loop will go through the selected keys SO, if the selector select some keys that are ready for I/O operation it will put that key in that key set.
            try {
                if (!key.isValid())
                    continue;
                if (key == serverKey) {
                    SocketChannel acceptedChannel = serverChannel.accept(); // accept a new channel
                    if (acceptedChannel == null)
                        continue;

                    acceptedChannel.configureBlocking(false); //non-blocking configure
                    SelectionKey readKey = acceptedChannel.register(selector, SelectionKey.OP_READ);// for read operations
                    clientMap.put(readKey, new ClientSession(readKey, acceptedChannel));// to add the read key to the hashMap (add client to the map)

                    System.out.println("New client ip=" + acceptedChannel.getRemoteAddress() +
                            ", total clients=" + MainServer.clientMap.size());
                }// end isAcceptable condition

                if (key.isReadable()) {
                    ClientSession session = clientMap.get(key);

                    if (session == null)
                        continue;

                    session.read();
                }// end isReadable condition

            } catch (Throwable t) {
                t.printStackTrace();
            }
        }// end foreach loop

        selector.selectedKeys().clear();
    } // end loop() method

    public static void main(String[] args) throws Throwable {
        new MainServer(new InetSocketAddress("localhost", 3333)); // calls the constructor passing new address to listen on.
    }
}

class ClientSession {

    /*
     * here in this class in every time new connection is made
     * the class will be created to hold the information for this client .
     * So, every client will have there own SelectionKey,SocketChannel and ByteBuffer
     */

    SelectionKey selectionKey;
    SocketChannel channel;
    ByteBuffer buffer;

    ClientSession(SelectionKey selectionKey, SocketChannel channel) throws Throwable {
        this.selectionKey = selectionKey;
        this.channel = (SocketChannel) channel.configureBlocking(false); // asynchronous non-blocking
        buffer = ByteBuffer.allocateDirect(64); // 64 byte capacity
    }

    void disconnect() {// this will just cancel the selection key and close the channel .

        MainServer.clientMap.remove(selectionKey); // remove the client from the map
        try {
            if (selectionKey != null)
                selectionKey.cancel();

            if (channel == null)
                return;

            System.out.println("bye bye " + (InetSocketAddress) channel.getRemoteAddress());
            channel.close();
        } catch (Throwable t) { /** quietly ignore  */}
    }

    void read() {

        /* TODO : handle the reading from channel .
         * now in read method when the selector let us know that is a read event pending to call this method
         * then we can handle the read event. So, we can read bytes from the channel and handle it however we need to.
         */

        try {
            int amount_read = -1; // to ignore any exception that channel.read(...) method wants to throw
            try {
                amount_read = channel.read((ByteBuffer) buffer.clear());
            } catch (Throwable t) {
                System.out.println("");
            }

            if (amount_read == -1)
                disconnect();
            //because the read method above will return The number of bytes read, possibly zero, or -1 if the channel has reached end-of-stream

            if (amount_read < 1)
                return; // if zero , we will return and wait until another read event triggers this method

            // if we make it pass then we will handle it whatever we want
            System.out.println("sending back " + buffer.position() + " bytes");
            System.out.println("sending message:\"" + StandardCharsets.UTF_8.decode(buffer.flip()).toString() + "\" back to : " + channel.getRemoteAddress());

            // turn this bus right around and send it back!
            buffer.flip();// to make the buffer ready to read
            channel.write(buffer);
        } catch (Throwable t) {
            disconnect();// to disconnect if something goes wrong
            t.printStackTrace();
        }
    }

}

