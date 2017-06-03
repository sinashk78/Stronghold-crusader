package StrongholdCrusader.Network;

import StrongholdCrusader.Map.Map;
import StrongholdCrusader.Settings;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.LinkedList;

/**
 * Created by Baran on 5/29/2017.
 */
public class Server implements Runnable {
    Thread listenThread;
    Thread sendMapThread;
    LinkedList<Player> players;
    DatagramSocket socket;

    public Server() {
        players = new LinkedList<>();
        try {
            socket = new DatagramSocket(Settings.SERVER_PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        listenThread = new Thread(this);
        listenThread.start();

        sendMapThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    sendMapToAll(new Map());
                    try {
                        Thread.sleep(1000 / Settings.FRAME_RATE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        sendMapThread.start();

    }

    @Override
    public void run() {
        try {
            //buffer to receive incoming data
            byte[] buffer = new byte[Settings.PACKET_MAX_SIZE];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
            //2. Wait for an incoming data
            System.out.println("Server socket created. Waiting for incoming data...");
            //communication loop
            while (true) {
                socket.receive(incoming);
                byte[] data = incoming.getData();
                String packet = new String(data, 0, incoming.getLength());
                analyzePacket(packet, incoming.getAddress(), incoming.getPort());
                Thread.sleep(1000 / Settings.FRAME_RATE);
            }
        } catch (IOException e) {
            System.err.println("IOException " + e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
    }

    private void analyzePacket(String body, InetAddress address, int port) {
        JSONParser jsonParser = new JSONParser();
        GameEvent gameEvent = new GameEvent();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(body);
            gameEvent.type = new Integer(((Long) jsonObject.get("type")).intValue());
            gameEvent.message = (String) (jsonObject.get("message"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        switch (gameEvent.type) {
            case GameEvent.JOIN_TO_GAME: {
                System.out.println(gameEvent.message + ":" + address + ":" + port);
                if (isUsernameAvailable(gameEvent.message)) {
                    players.add(new Player(gameEvent.message, address, port));
                    sendPacket("Player " + gameEvent.message + " created!", address, port);
                } else
                    sendPacket("Player name is already taken...", address, port);
                break;
            }
        }
    }

    private boolean sendPacket(String body, InetAddress address, int port) {
        DatagramPacket dp = new DatagramPacket(body.getBytes(), body.getBytes().length, address, port);
        try {
            socket.send(dp);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void sendPacketForAll(String body) {
        for (Player player : players) {
            sendPacket(body, player.address, player.port);
        }
    }

    public void sendMapToAll(Map map) {
        sendPacketForAll("Map");
    }

    public boolean isUsernameAvailable(String username) {
        for (Player player : players) {
            if (player.playerName.equals(username))
                return false;
        }
        return true;
    }
}