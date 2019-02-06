package org.asdfgamer.sunriseClock.network;

import android.content.Context;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.asdfgamer.sunriseClock.R;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;

public class UpnpDiscoveryTask extends AsyncTask<Object, Void, Uri> {

    private String TAG = "UpnpDiscoveryTask";

    private WifiManager wifi;

    public UpnpDiscoveryTask(WifiManager wifiManager) {
        this.wifi = wifiManager;
    }

    @Override
    protected Uri doInBackground(Object[] params) {
        Uri deconzBaseUrl = null;

        Log.i(TAG, "Upnp Discovery started.");

        if (wifi != null) {

            WifiManager.MulticastLock lock = wifi.createMulticastLock(R.string.appNameID + TAG);
            lock.acquire();
            Log.d(TAG, "MulticastLock aquired.");

            DatagramSocket socket = null;

            try {
                InetAddress multicastAddress = InetAddress.getByName("239.255.255.250");
                int multicastPort = 1900;

                String query =
                        "M-SEARCH * HTTP/1.1\r\n" +
                                "HOST: " + multicastAddress.getHostAddress() + ":" + multicastPort + "\r\n" +
                                "MAN: \"ssdp:discover\"\r\n" +
                                "MX: 1\r\n" +
                                "ST: upnp:rootdevice\r\n";

                /* Port for sending out the datagram. Can be different from the required multicastPort.
                 * A random free port (0) is chosen.
                 * */
                socket = new DatagramSocket(0);
                socket.setReuseAddress(true);

                DatagramPacket dataGram = new DatagramPacket(query.getBytes(), query.length(), multicastAddress, multicastPort);
                socket.send(dataGram);
                Log.d(TAG, "Sent out SSDP datagram at " + socket.getLocalSocketAddress());

                long time = System.currentTimeMillis();
                long curTime = System.currentTimeMillis();

                /* For a multicast request, the control point SHOULD wait at least the amount of time specified in the MX header field for responses to arrive from devices. */
                while (curTime - time < 1000 && !isCancelled()) {
                    /* Allowing for up to 512 byte long datagrams. Expected to be enough. */
                    DatagramPacket receivedDatagram = new DatagramPacket(new byte[512], 512);
                    socket.receive(receivedDatagram);
                    Log.d(TAG, "Received SSDP response.");

                    Uri url = parseDatagram(receivedDatagram);
                    if (url != null) {
                        deconzBaseUrl = url;
                    }

                    curTime = System.currentTimeMillis();
                }

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                socket.close();
            }
            lock.release();
        }
        return deconzBaseUrl;
    }

    @Override
    protected void onPostExecute(Uri deconzBaseUrl) {
        //TODO
        Log.d(TAG, "onPostExecute found deconzBaseUrl: " + deconzBaseUrl);
    }


    private Uri parseDatagram(DatagramPacket datagram) {
        Uri deconzBaseUrl = null;
        String httpStatus = new String(datagram.getData(), 0, 15);
        /* Expect HTTP status code at specific position as described in the SSDP specification. */
        if (httpStatus.toUpperCase().equals("HTTP/1.1 200 OK")) {
            InetAddress upnpHostAddress = datagram.getAddress();
            Log.d(TAG, "Parsing SSDP response from " + upnpHostAddress.getHostAddress());

            String ssdpBody = new String(datagram.getData(), 16, datagram.getLength());

            /* Preparing search for relevant lines in SSDP response. */
            String[] ssdpBodyLines = ssdpBody.split("\\r?\\n");
            String serverDescription = null; //Description of the server
            Uri xmlDescription = null; //Path to the XML description
            for (String line : ssdpBodyLines) {
                line = line.toLowerCase();
                if (line.contains("SERVER: ".toLowerCase())) {
                    serverDescription = line;
                    serverDescription = serverDescription.replace("SERVER: ".toLowerCase(), "");
                    Log.d(TAG, "SSDP server description: " + serverDescription);
                } else if (line.contains("LOCATION: ".toLowerCase())) {
                    xmlDescription = Uri.parse(line.replace("LOCATION: ".toLowerCase(), ""));
                    Log.d(TAG, "SSDP XML desc location: " + xmlDescription);
                } else {
                    if (serverDescription != null & xmlDescription != null) {
                        break;
                    }
                }
            }

            /* Multiple UPNP devices could have answered SSDP queries. This tries to find the deconz endpoint by searching for a specific string in the 'Server' field. */
            if (serverDescription.contains("IpBridge".toLowerCase())) {

                Log.i(TAG, "deconz XML description found at: " + xmlDescription.toString());

                //TODO: We should probably get the base url for the deconz endpoint (eg 'deconz.example.org:8080/') from parsing the XML description file.
                deconzBaseUrl = Uri.parse(xmlDescription.getAuthority());
                Log.i(TAG, "deconz base url is: " + deconzBaseUrl);

            }
        }
        return deconzBaseUrl;
    }

}

